package be.somedi.printen.model.job;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.model.CaregiverTwoPrints;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.LinkedExternalCargiverService;
import be.somedi.printen.util.IOUtil;
import be.somedi.printen.util.TxtUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.file.*;

import static be.somedi.printen.util.TxtUtil.getErrorMessage;
import static be.somedi.printen.util.TxtUtil.isPathWithLetterNotToPrint;

@Component
public class PrintJob {

    @Value("${path-read}")
    private Path PATH_TO_READ;
    @Value("${path-copy}")
    private Path PATH_TO_COPY;
    @Value("${path-error}")
    private Path PATH_TO_ERROR;

    private final ExternalCaregiverService service;
    private final LinkedExternalCargiverService linkedExternalCargiverService;
    private final SendToUmJob sendToUmJob;

    private WatchService watchService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintJob.class);

    @Autowired
    public PrintJob(ExternalCaregiverService service, LinkedExternalCargiverService linkedExternalCargiverService,
                    SendToUmJob sendToUmJob) {
        this.service = service;
        this.linkedExternalCargiverService = linkedExternalCargiverService;
        this.sendToUmJob = sendToUmJob;
    }

    public void startPrintJob() {
        LOGGER.info("Path to read= " + PATH_TO_READ);

        // Directory één keer al volledig nakijken en printjobs uitvoeren
        checkDirectoryAndRunJob();

        // Telkens er een nieuwe file in directory komt printjob uitvoeren
        watchDirectory();
    }

    public void stopPrintJob() {
        try {
            if (watchService != null)
                watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    private void checkDirectoryAndRunJob() {
        try {
            Files.list(PATH_TO_READ)
                    .filter(Files::isRegularFile)
                    .filter(path -> StringUtils.endsWith(path.toString(), ".txt"))
                    .filter(path -> {
                                if (isPrintAndSendJobSucceeded(path)) {
                                    return IOUtil.makeBackUpAndDelete(path, Paths.get(PATH_TO_COPY + "\\" + path.getFileName
                                            ()));
                                } else
                                    return IOUtil.makeBackUpAndDelete(path, Paths.get(PATH_TO_ERROR + "\\" + path.getFileName
                                            ()));
                            }
                    ).forEach(System.out::println);

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void watchDirectory() {
        try {
            watchService = PATH_TO_READ.getFileSystem().newWatchService();
            PATH_TO_READ.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                final WatchKey watchKey = watchService.take();

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    final Path fileName = (Path) event.context();
                    LOGGER.debug("Path changed: " + fileName);

                    final Path path = Paths.get(PATH_TO_READ + "\\" + fileName);

                    if (Files.isRegularFile(path) && StringUtils.endsWith(path.toString(), ".txt")) {
                        LOGGER.debug("Regular file, end with .txt");
                        if (isPrintAndSendJobSucceeded(path))
                            IOUtil.makeBackUpAndDelete(path, Paths.get(PATH_TO_COPY + "\\" + fileName));
                        else
                            IOUtil.makeBackUpAndDelete(path, Paths.get(PATH_TO_ERROR + "\\" + fileName));
                    }
                }

                if (!watchKey.reset()) {
                    LOGGER.debug("Key has been unregistered");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }


    private boolean isPrintAndSendJobSucceeded(Path path) {
        String errorMessage;
        String fileToPrint = FilenameUtils.getBaseName(path.toString()).replace("MSE", "PDF");
        Path pathOfPDF = Paths.get(PATH_TO_READ + "\\" + fileToPrint + ".pdf");

        printAlwaysOneForSpecificCaregevivers(path, pathOfPDF);

        if (isPathWithLetterNotToPrint(path)) {
            LOGGER.debug(path + " write error file");
            IOUtil.writeFileToError(PATH_TO_ERROR, path, getErrorMessage());

            LOGGER.debug("Deze brief hoeft geen print: COPY PDF to error path");
            IOUtil.makeBackUpAndDelete(pathOfPDF, Paths.get(PATH_TO_ERROR + "\\" + fileToPrint + ".pdf"));
            return false;
        }
        ExternalCaregiver caregiverToPrint = service.findByMnemonic(TxtUtil.getMnemnonic(path));
        LOGGER.info("CaregiverToPrint: " + caregiverToPrint);
        if (null != caregiverToPrint) {
            if (caregiverToPrint.getPrintProtocols() == null || caregiverToPrint.getPrintProtocols().toString().equals("")) {
                errorMessage = "De gegevens van deze dokter zijn niet ingevuld (print of niet? formaat? ...): " + caregiverToPrint.getLastName() + " " + caregiverToPrint.getFirstName() + "(" + caregiverToPrint.getExternalID() + ")";
                IOUtil.writeFileToError(PATH_TO_ERROR, path, errorMessage);
                return false;
            }

            // SEND TO UM:
            sendToUM(caregiverToPrint, path);

            if (null != caregiverToPrint.getPrintProtocols() && caregiverToPrint.getPrintProtocols()) {
                if (isPrinted(pathOfPDF)) {
                    IOUtil.makeBackUpAndDelete(pathOfPDF, Paths.get(PATH_TO_COPY + "\\" + fileToPrint + ".pdf"));
                    return true;
                } else {
                    errorMessage = "PDF NOT FOUND: " + fileToPrint + ".pdf";
                    IOUtil.writeFileToError(PATH_TO_ERROR, path, errorMessage);
                    return false;
                }
            } else {
                LOGGER.debug("Deze brief moet niet afgedrukt worden van de dokter --> Copy to result path");
                IOUtil.makeBackUpAndDelete(pathOfPDF, Paths.get(PATH_TO_COPY + "\\" + fileToPrint + ".pdf"));
            }
        } else {
            LOGGER.info("Caregiver is NULL");
            errorMessage = "Caregiver is NULL!";
            IOUtil.writeFileToError(PATH_TO_ERROR, path, errorMessage);
            return false;
        }
        return true;
    }

    private void sendToUM(ExternalCaregiver caregiverToPrint, Path path) {
        if (sendToUmJob.formatAndSend(caregiverToPrint, path)) {
            LOGGER.debug(path + "  verzenden naar UM is gelukt!");

            String externalId = caregiverToPrint.getExternalID();
            String linkedId = linkedExternalCargiverService.findLinkedIdByExternalId(externalId);
            if (linkedId != null) {
                ExternalCaregiver caregiverToSendCopy = service.findByMnemonic(linkedId);
                if (sendToUmJob.formatAndSend(caregiverToSendCopy, path)) {
                    LOGGER.debug(path + " copy verzenden naar UM is gelukt!");
                }
            }
        } else {
            LOGGER.warn(path + " verzenden naar UM is NIET gelukt!");
        }
    }

    private void printAlwaysOneForSpecificCaregevivers(Path pathToTxt, Path pathToPDF){
        ExternalCaregiver externalCaregiver = service.findByMnemonic(TxtUtil.getMnemonicAfterUA(pathToTxt));
        for(CaregiverTwoPrints ct : CaregiverTwoPrints.values()){
            if(StringUtils.right(externalCaregiver.getExternalID(),4).equalsIgnoreCase( StringUtils.right(ct.name(), 4))) {
                LOGGER.debug(externalCaregiver.getLastName() + " wilt altijd een print.");
                isPrinted(pathToPDF);
            }
        }
    }

    private boolean isPrinted(Path pathToPDF) {
        try {
            if (Files.exists(pathToPDF)) {
                LOGGER.debug("Printing: " + pathToPDF.getFileName());

                PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintService(myPrintService);
                PDDocument document = PDDocument.load(pathToPDF.toFile());
                document.silentPrint(job);
                document.close();
                LOGGER.info("Succesfully printed: " + pathToPDF.getFileName());
                return true;
            }
        } catch (IOException | PrinterException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
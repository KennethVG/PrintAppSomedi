package be.somedi.printen.model.job;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.entity.LinkedExternalCaregiver;
import be.somedi.printen.model.CaregiverTwoPrints;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.LinkedExternalCargiverService;
import be.somedi.printen.util.IOUtil;
import be.somedi.printen.util.TxtUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.file.*;

import static be.somedi.printen.util.TxtUtil.isPathWithLetterNotToPrint;
import static be.somedi.printen.util.TxtUtil.letterContainsVulAan;

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


    private static final Logger LOGGER = LogManager.getLogger(PrintJob.class);

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
                    LOGGER.debug("Path is gewijzigd: " + fileName);

                    final Path path = Paths.get(PATH_TO_READ + "\\" + fileName);

                    if (Files.isRegularFile(path) && StringUtils.endsWith(path.toString(), ".txt")) {
                        LOGGER.debug("File eindigt op txt.");
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

        if (isPathWithLetterNotToPrint(path)) {
            LOGGER.debug("Deze brief bevat 'mag weg', 'P.N.' ... --> Verwijder de TXT en PDF direct");
            IOUtil.deleteFile(pathOfPDF);
            IOUtil.deleteFile(path);
            return false;
        } else if (letterContainsVulAan(path)) {
            LOGGER.debug("Deze brief bevat 'vul aan' --> plaatsen in error folder");
            IOUtil.writeFileToError(PATH_TO_ERROR, path, "Deze brief bevat ergens in de tekst 'vul_aan'.");
            IOUtil.makeBackUpAndDelete(pathOfPDF, Paths.get(PATH_TO_ERROR + "\\" + fileToPrint + ".pdf"));
            return false;
        }

        printAlwaysOneForSpecificCaregevivers(path, pathOfPDF);

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
        ExternalCaregiver caregiverOfLetter = service.findByMnemonic(TxtUtil.getMnemonicAfterUA(path));
        if (caregiverOfLetter == null) {
            IOUtil.writeFileToError(PATH_TO_ERROR, path, "Deze brief is niet verzonden naar UM: SPECIALIST van Somedi is null. " +
                    "Geef de specialist een formaat, een nihiiadres en vul in of hij print wilt of niet! De brief is wel goed geprint. " +
                    "Zoek in result terug naar de pdf en txt en plaats deze in NEW als alle gegevens ingevuld zijn!");
        } else if (caregiverOfLetter.geteProtocols() != null && caregiverOfLetter.geteProtocols()) {
            if (sendToUmJob.formatAndSend(caregiverOfLetter, path)) {
                LOGGER.debug(path + "  verzenden naar UM is gelukt voor dokter die de brief geschreven heeft!");
            }
            if (sendToUmJob.formatAndSend(caregiverToPrint, path)) {
                LOGGER.debug(path + "  verzenden naar UM is gelukt voor dokter in AAN/CC!");
                String externalId = caregiverToPrint.getExternalID();
                LinkedExternalCaregiver linkedExternalCaregiver = linkedExternalCargiverService.findLinkedIdByExternalId(externalId);
                if (linkedExternalCaregiver != null) {
                    ExternalCaregiver caregiverToSendCopy = service.findByMnemonic(linkedExternalCaregiver.getLinkedId());
                    if (caregiverToSendCopy != null) {
                        if (sendToUmJob.formatAndSend(caregiverToSendCopy, path)) {
                            LOGGER.debug(path + " copy verzenden naar UM is gelukt!");
                        }
                    }
                }

            } else {
                LOGGER.warn(path + " verzenden naar UM is NIET gelukt!");
            }
        } else {
            LOGGER.info("Deze dokter wil geen elektronische protocols.");
        }
    }

    private void printAlwaysOneForSpecificCaregevivers(Path pathToTxt, Path pathToPDF) {
        ExternalCaregiver externalCaregiver = service.findByMnemonic(TxtUtil.getMnemonicAfterUA(pathToTxt));
        for (CaregiverTwoPrints ct : CaregiverTwoPrints.values()) {
            if (StringUtils.right(externalCaregiver.getExternalID(), 4).equalsIgnoreCase(StringUtils.right(ct.name(), 4))) {
                LOGGER.debug(externalCaregiver.getLastName() + " wilt altijd een print.");
                isPrinted(pathToPDF);
            }
        }
    }

    private boolean isPrinted(Path pathToPDF) {
        try {
            if (Files.exists(pathToPDF)) {
                LOGGER.debug("Aan het printen: " + pathToPDF.getFileName());

                PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintService(myPrintService);
                PDDocument document = PDDocument.load(pathToPDF.toFile());
                document.silentPrint(job);
                document.close();
                LOGGER.info("Geprint: " + pathToPDF.getFileName());
                return true;
            }
        } catch (IOException | PrinterException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
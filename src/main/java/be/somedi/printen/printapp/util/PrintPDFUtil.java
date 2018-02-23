package be.somedi.printen.printapp.util;

import be.somedi.printen.printapp.model.ExternalCaregiver;
import be.somedi.printen.printapp.service.ExternalCaregiverService;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrintPDFUtil {

    @Value("${path-read}")
    private Path PATH_TO_READ;
    @Value("${path-copy}")
    private Path PATH_TO_COPY;
    @Value("${path-error}")
    private Path PATH_TO_ERROR;

    private static final Logger logger = LoggerFactory.getLogger(PrintPDFUtil.class);

    private static final int MNEMONIC_LENGTH = 5;

    private final ExternalCaregiverService service;

    @Autowired
    public PrintPDFUtil(ExternalCaregiverService service) {
        this.service = service;
    }

    public void printAllPDFs() {

        logger.warn(PATH_TO_READ.toString());

        try {
            while (true) {
                WatchService watchService = PATH_TO_READ.getFileSystem().newWatchService();
                PATH_TO_READ.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

                List<Path> paths = Files.list(PATH_TO_READ).filter(Files::isRegularFile).collect(Collectors.toList
                        ());

                for (Path path : paths) {
                    if (StringUtils.endsWith(path.toString(), ".txt")) {
                        if(printIfNeeded(path)){
                            if (makeBackUpAndDelete(path, Paths.get(PATH_TO_COPY + "\\" + path.getFileName()))) {
                                logger.error("Just Backup: Could not delete: " + path + " --> Closing application!");
                                watchService.close();
                                throw new IOException("Close application");
                            }
                        }  else if(makeBackUpAndDelete(path, Paths.get(PATH_TO_ERROR + "\\" + path.getFileName()))) {
                            logger.error("Backup to delete: Could not delete: " + path + " --> Closing application!");
                            watchService.close();
                            throw new IOException("Stop application");
                        }
                    }
                }
            }
        } catch (IOException | PrinterException e) {
            logger.error(e.getMessage());
        }
    }

    private boolean printIfNeeded(Path path) throws IOException, PrinterException {
        String fileName = FilenameUtils.getBaseName(path.toString());
        String mnemonic = StringUtils.right(FilenameUtils.removeExtension(fileName), MNEMONIC_LENGTH);
        ExternalCaregiver caregiverToPrint = service.findByMnemonic(mnemonic);

        Boolean toPrint = null != caregiverToPrint && null != caregiverToPrint.getPrintProtocols();

        if (toPrint && caregiverToPrint.getPrintProtocols()) {
            logger.warn("Caregiver to print: " + caregiverToPrint.toString());
            String fileToPrint = fileName.replace("MSE", "PDF");

            if (!TxtUtil.isPathWithLetterNotToPrint(path)) {
                printPDFFromPath(Paths.get(PATH_TO_READ + "\\" + fileToPrint + ".pdf"));
                logger.warn("Succesfully printed: " + fileToPrint);
                return true;
            } else {
                logger.warn("This letter contains vul_aan/ mag_weg ... " + path.getFileName());
            }
        }
        return false;
    }

    private void printPDFFromPath(Path path) throws IOException, PrinterException {
        if (Files.exists(path)) {
            logger.warn("Printing: " + path.getFileName());
            PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(myPrintService);

            PDDocument document = PDDocument.load(path.toFile());
            document.silentPrint(job);
            document.close();
        } else {
            logger.warn("PATH does not exist: " + path);
        }
    }


    private boolean makeBackUpAndDelete(Path fromPath, Path toPath) throws IOException {
        if (Files.exists(fromPath)) {
            logger.warn("Make backup and delete: " + fromPath.getFileName() + " " + toPath.getFileName());
            Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
            return !fromPath.toFile().delete();
        }
        logger.warn("PATH does not exist: " + fromPath);

        return true;
    }
}



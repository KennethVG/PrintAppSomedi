package be.somedi.printen.printapp.util;

import be.somedi.printen.printapp.model.ExternalCaregiver;
import be.somedi.printen.printapp.service.ExternalCaregiverService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
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

@Component
public class PrintPDFUtil {

    @Value("${path-read}")
    private Path PATH_TO_READ;
    @Value("${path-copy}")
    private Path PATH_TO_COPY;
    @Value("${path-error}")
    private Path PATH_TO_ERROR;

    private static final int MNEMONIC_LENGTH = 5;

    private final ExternalCaregiverService service;

    @Autowired
    public PrintPDFUtil(ExternalCaregiverService service) {
        this.service = service;
    }

    public void printAllPDFs() {
        System.out.println("Path to read= " + PATH_TO_READ);

        // Directory één keer al volledig nakijken en printjobs uitvoeren
       checkDirectoryAndRunJob();

        // Telkens er een nieuwe file in directory komt printjob uitvoeren ********/
        watchDirectory();
    }

    private void checkDirectoryAndRunJob(){
        try {
           Files.list(PATH_TO_READ)
                    .filter(Files::isRegularFile)
                    .filter(path -> StringUtils.endsWith(path.toString(), ".txt"))
                    .filter(path -> {
                        if (isPrintNeeded(path))
                            return makeBackUpAndDelete(path, Paths.get(PATH_TO_COPY + "\\" + path.getFileName()));
                        else
                            return makeBackUpAndDelete(path, Paths.get(PATH_TO_ERROR + "\\" + path.getFileName()));
                    }).count();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void watchDirectory(){
        try (final WatchService watchService = PATH_TO_READ.getFileSystem().newWatchService()) {
            PATH_TO_READ.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                final WatchKey watchKey = watchService.take();

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    final Path fileName = (Path) event.context();
                    System.out.println("Path changed: " + fileName);

                    final Path path = Paths.get(PATH_TO_READ + "\\" + fileName);

                    if (Files.isRegularFile(path) && StringUtils.endsWith(path.toString(), ".txt")) {
                        System.out.println("Regular file, end with .txt");
                        if (isPrintNeeded(path))
                            makeBackUpAndDelete(path, Paths.get(PATH_TO_COPY + "\\" + fileName));
                        else
                            makeBackUpAndDelete(path, Paths.get(PATH_TO_ERROR + "\\" + fileName));
                    }
                }

                if (!watchKey.reset()) {
                    System.out.println("Key has been unregistered");
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private boolean isPrintNeeded(Path path) {
        String fileName = FilenameUtils.getBaseName(path.toString());
        String mnemonic = StringUtils.right(FilenameUtils.removeExtension(fileName), MNEMONIC_LENGTH);
        ExternalCaregiver caregiverToPrint = service.findByMnemonic(mnemonic);

        Boolean toPrint = null != caregiverToPrint && null != caregiverToPrint.getPrintProtocols();

        if (toPrint && caregiverToPrint.getPrintProtocols()) {
            System.out.println("Caregiver to print: " + caregiverToPrint.toString());
            String fileToPrint = fileName.replace("MSE", "PDF");

            if (!TxtUtil.isPathWithLetterNotToPrint(path)) {
                return isPrinted(Paths.get(PATH_TO_READ + "\\" + fileToPrint + ".pdf"));
            } else {
                System.out.println("This letter contains vul_aan/ mag_weg ... " + path.getFileName());
            }
        }
        return false;
    }

    private boolean isPrinted(Path path) {
        try {
            if (Files.exists(path)) {
                System.out.println("Printing: " + path.getFileName());
                PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintService(myPrintService);

                PDDocument document = PDDocument.load(path.toFile());
                document.silentPrint(job);
                document.close();
                System.out.println("Succesfully printed: " + path.getFileName());
                return true;
            }
        } catch (IOException | PrinterException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("PDF NOT FOUND: " + path);
        return false;

    }


    private boolean makeBackUpAndDelete(Path fromPath, Path toPath) {
        if (Files.exists(fromPath)) {
            System.out.println("Make backup: from " + fromPath + " to " + toPath);
            try {
                Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fromPath.toFile().delete()) {
                System.out.println("Deleted: " + fromPath);
                return false;
            }
        }
        System.out.println("PATH does not exist: " + fromPath);

        return true;
    }
}



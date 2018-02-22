package be.somedi.printen.printapp.util;

import be.somedi.printen.printapp.model.ExternalCaregiver;
import be.somedi.printen.printapp.service.ExternalCaregiverService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Path PATH_TO_READ = Paths.get("\\\\hermes\\pemr-batch\\Outgoing\\new\\Test");
    private static final Path PATH_TO_COPY = Paths.get("\\\\hermes\\pemr-batch\\Outgoing\\new\\PDFToPrint");
    private static final int MNEMONIC_LENGTH = 5;

    private final ExternalCaregiverService service;

    @Autowired
    public PrintPDFUtil(ExternalCaregiverService service) {
        this.service = service;
    }

    public void printAllPDFs() {

        try {
            WatchService watchService = PATH_TO_READ.getFileSystem().newWatchService();
            PATH_TO_READ.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while (true) {
                WatchKey key = watchService.take();

                List<Path> paths = Files.list(PATH_TO_READ).filter(Files::isRegularFile).collect(Collectors.toList
                        ());

                for (Path path : paths) {
                    if (StringUtils.endsWith(path.toString(), ".txt")) {
                        printIfNeeded(path);

                        if (makeBackUpAndDelete(path)) {
                            watchService.close();
                            break;
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | PrinterException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void printIfNeeded(Path path) throws IOException, PrinterException {
        String fileName = FilenameUtils.getBaseName(path.toString());
        String mnemonic = StringUtils.right(FilenameUtils.removeExtension(fileName), MNEMONIC_LENGTH);
        ExternalCaregiver caregiverToPrint = service.findByMnemonic(mnemonic);

        Boolean toPrint = null != caregiverToPrint && null != caregiverToPrint.getPrintProtocols();
        if (toPrint && caregiverToPrint.getPrintProtocols()) {
            String fileToPrint = fileName.replace("MSE", "PDF");

            if (!TxtUtil.isPathToPrint(path)) {
                printPDFFromPath(Paths.get(PATH_TO_READ + "\\" + fileToPrint + ".pdf"));
            }
        }
    }

    private void printPDFFromPath(Path path) throws IOException, PrinterException {
        if (Files.exists(path)) {
            System.out.println("PRINTING: " + path.getFileName());
            PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(myPrintService);

            PDDocument document = PDDocument.load(path.toFile());
            document.silentPrint(job);
            document.close();
        } else {
            System.out.println("PATH does not exist: " + path);
        }
    }


    private boolean makeBackUpAndDelete(Path path) throws IOException {
        if (Files.exists(path)) {
            System.out.println("Make backup and delete: " + path.getFileName());
            Files.copy(path, Paths.get(PATH_TO_COPY + "\\" + path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            return !path.toFile().delete();
        }
        System.out.println("PATH does not exist: " + path);

        return true;
    }
}



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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReadPDFUtil {

    private static final Path PATH_TO_READ = Paths.get("\\\\hermes\\pemr-batch\\Outgoing\\new");
    private static final Path PATH_TO_COPY = Paths.get("\\\\hermes\\pemr-batch\\Outgoing\\result\\PDF");
    private static final int MNEMONIC_LENGTH = 5;

    private final ExternalCaregiverService service;

    @Autowired
    public ReadPDFUtil(ExternalCaregiverService service) {
        this.service = service;
    }

    public void printAllPDFs() {





        List<Path> paths;
        try {
            paths = Files.list(PATH_TO_READ).filter(Files::isRegularFile).collect(Collectors.toList
                    ());

            for (Path path : paths) {
                if (StringUtils.endsWith(path.toString(), ".pdf")) {
                    String fileName = FilenameUtils.getBaseName(path.toString());
                    String mnemonic = StringUtils.right(FilenameUtils.removeExtension(fileName), MNEMONIC_LENGTH);
                    ExternalCaregiver caregiverToPrint = service.findByMnemonic(mnemonic);
                    if (null != caregiverToPrint && caregiverToPrint.isPrintProtocols()) {
                        System.out.println("PRINTING: " + path + " is " + caregiverToPrint.getLastName() + " " +
                                caregiverToPrint.getFirstName());
                        printPDFFromPath(path);
                    }

                    makeBackUp(path);
                    if(Files.deleteIfExists(path))
                        System.out.println("PDF DELETED!");
                }
            }
        } catch (IOException | PrinterException e) {
            e.printStackTrace();
        }
    }

    private void printPDFFromPath(Path path) throws IOException, PrinterException {
        PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(myPrintService);

        PDDocument document = PDDocument.load(path.toFile());
        document.silentPrint(job);
        document.close();
    }


    private void makeBackUp(Path path) throws IOException {
        if (path.toFile().exists()){
            System.out.println("BACKUP: " + Paths.get(PATH_TO_COPY + "\\" + path.getFileName()));
            Files.copy(path, Paths.get(PATH_TO_COPY + "\\" +  path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }

    }
}



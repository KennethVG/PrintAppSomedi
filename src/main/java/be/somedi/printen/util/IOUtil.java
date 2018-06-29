package be.somedi.printen.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IOUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);

    public static Path writeFileToError(Path pathToError, Path pathToTxt, String errormessage) {
        Path path = null;
        try {
            path = Files.write(Paths.get(pathToError + "\\" + FilenameUtils.getBaseName(pathToTxt.toString()) + "" +
                    ".err"), errormessage.getBytes());
            LOGGER.debug("Maak error file: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static Path writeFileToUM(Path pathToUM, String mnemonic, String refNr, String ext, String text) {
        Path path = null;
        try {
            path = Paths.get(pathToUM + "/HEC_" + mnemonic + "R_" + refNr + "R." + ext);
            OutputStream out = new FileOutputStream(path.toFile());
            out.write(text.getBytes(Charset.forName("windows-1252")));
            out.close();
            LOGGER.debug("Path " + path + " aangemaakt.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static boolean makeBackUpAndDelete(Path fromPath, Path toPath) {
        if (Files.exists(fromPath)) {
            LOGGER.debug("Maak backup: van " + fromPath + " naar " + toPath);
            try {
                Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (FileUtils.deleteQuietly(fromPath.toFile())) {
                LOGGER.debug("Deleted: " + fromPath);
                return false;
            }
        }
        LOGGER.warn("PATH bestaat niet: " + fromPath);
        return true;
    }

    public static void deleteFile(Path path){
        try {
            Files.delete(path);
        } catch (IOException e) {
           LOGGER.error("Verwijderen van " + path + " is niet gelukt!");
        }
    }
}
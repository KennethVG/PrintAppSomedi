package be.somedi.printen.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IOUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);

    public static Path writeFileToError(Path pathToError, Path pathToTxt, String errormessage) {
        Path path = null;
        try {
            path = Files.write(Paths.get(pathToError + "\\" + FilenameUtils.getBaseName(pathToTxt.toString()) + ".err"), errormessage.getBytes());
            LOGGER.debug("Created error file: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static Path writeFileToUM(Path pathToUM, String mnemonic, String refNr, String ext, String text){
        Path path = null;
        try {
            path = Files.write(Paths.get(pathToUM + "/HEC_" + mnemonic + "R_" + refNr + "R." + ext), text.getBytes());
            LOGGER.debug("Path " + path + " created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static boolean makeBackUpAndDelete(Path fromPath, Path toPath) {
        if (Files.exists(fromPath)) {
            LOGGER.debug("Make backup: from " + fromPath + " to " + toPath);
            try {
                Files.copy(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fromPath.toFile().delete()) {
                LOGGER.debug("Deleted: " + fromPath);
                return false;
            }
        }
        LOGGER.warn("PATH does not exist: " + fromPath);
        return true;
    }
}

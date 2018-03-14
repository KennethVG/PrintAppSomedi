package be.somedi.printen.util;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class IOUtil {

    public static Path writeFileToError(Path pathToError, Path pathToTxt, String errormessage) {
        Path path = null;
        try {
            path = Files.write(Paths.get(pathToError + "\\" + FilenameUtils.getBaseName(pathToTxt.toString()) + ".err"), errormessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static Path writeFileToUM(Path pathToUM, String mnemonic, String refNr, String ext, String text){
        Path path = null;
        try {
            path = Files.write(Paths.get(pathToUM + "/HEC_" + mnemonic + "R_" + refNr + "R." + ext), text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static boolean makeBackUpAndDelete(Path fromPath, Path toPath) {
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

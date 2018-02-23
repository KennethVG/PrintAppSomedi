package be.somedi.printen.printapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TxtUtil {

    private static final Logger logger = LoggerFactory.getLogger(TxtUtil.class);

    public static boolean isPathWithLetterNotToPrint(Path pathToTxt) {

        boolean toPrint = false;

        try (BufferedReader bufferedReader = Files.newBufferedReader(pathToTxt)) {

            String lineWithConsultId = bufferedReader.lines().filter(line -> line.trim().toUpperCase().startsWith("#PR")).findFirst().orElseThrow(RuntimeException::new);
            if(lineWithConsultId.length() > 15){
                return true;
            }

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {

                String finalCurrentLine = currentLine.trim();
                toPrint = Stream.of(DeleteItems.values()).anyMatch(deleteItems -> finalCurrentLine.contains
                        (deleteItems.getText()) || deleteItems.getText().equalsIgnoreCase(finalCurrentLine));
                if(toPrint) return true;
            }
        } catch (IOException e) {
           logger.error(e.getMessage());
        }

        return toPrint;
    }
}

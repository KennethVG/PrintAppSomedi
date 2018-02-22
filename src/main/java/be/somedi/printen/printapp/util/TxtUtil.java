package be.somedi.printen.printapp.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TxtUtil {

    public static boolean isPathToPrint(Path pathToTxt) {

        boolean toPrint = false;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToTxt.toFile()))) {

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                String finalCurrentLine = currentLine.trim();
                toPrint = Stream.of(DeleteItems.values()).anyMatch(deleteItems -> finalCurrentLine.contains
                        (deleteItems.getText()) || deleteItems.getText().equalsIgnoreCase(finalCurrentLine));
                if(toPrint) break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return toPrint;
    }


}

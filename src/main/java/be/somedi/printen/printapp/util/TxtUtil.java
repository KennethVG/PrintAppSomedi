package be.somedi.printen.printapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TxtUtil {

    public static boolean isPathWithLetterNotToPrint(Path pathToTxt) {

        boolean toPrint = false;

        try (BufferedReader bufferedReader = Files.newBufferedReader(pathToTxt)) {

            String lineWithConsultId = bufferedReader.lines().filter(line -> line.trim().toUpperCase().startsWith("#PR")).findFirst().orElseThrow(RuntimeException::new);
            if(lineWithConsultId.length() > 15){
                System.out.println("#PR is groter dan 15 --> NIET PRINTEN!");
                return true;
            }

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {

                String finalCurrentLine = currentLine.trim();
                toPrint = Stream.of(DeleteItems.values()).anyMatch(deleteItems -> finalCurrentLine.contains
                        (deleteItems.getText()) || deleteItems.getText().equalsIgnoreCase(finalCurrentLine));
                if(toPrint){
                    System.out.println("Print bevat vul_aan of andere ... --> NIET PRINTEN!");
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return toPrint;
    }
}

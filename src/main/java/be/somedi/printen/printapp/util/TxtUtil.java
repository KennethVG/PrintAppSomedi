package be.somedi.printen.printapp.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TxtUtil {


    private static final String PR = "#PR";
    private static final String UA = "#UA";
    private static final String BETREFT = "Betreft";
    private static final String MET_VRIENDELIJKE_GROETEN = "Met vriendelijke groeten";
    private static final int MNEMONIC_LENGTH = 5;

    public static boolean isPathWithLetterNotToPrint(Path pathToTxt) {

        boolean toPrint = false;

        try (BufferedReader bufferedReader = Files.newBufferedReader(pathToTxt)) {

            String lineWithConsultId = bufferedReader.lines().filter(line -> line.trim().toUpperCase().startsWith(PR)
            ).findFirst().orElseThrow(RuntimeException::new);
            if (lineWithConsultId.length() > 15) {
                System.out.println(PR + " is groter dan 15 --> NIET PRINTEN!");
                return true;
            }

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {

                String finalCurrentLine = currentLine.trim();
                toPrint = Stream.of(DeleteItems.values()).anyMatch(deleteItems -> finalCurrentLine.contains
                        (deleteItems.getText()) || deleteItems.getText().equalsIgnoreCase(finalCurrentLine));
                if (toPrint) {
                    System.out.println("Print bevat vul_aan of andere ... --> NIET PRINTEN!");
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return toPrint;
    }

    public static String getBodyOfTxt(Path pathToTxt) {

        StringBuilder result = new StringBuilder();
        int startIndex = 0;
        int endIndex = 0;

        try {
            List<String> allLines = Files.readAllLines(pathToTxt);
            for (int i = 0; i < allLines.size(); i++) {
                if (allLines.get(i).trim().startsWith(BETREFT)) {
                    startIndex = i;
                }
                if (allLines.get(i).trim().startsWith(MET_VRIENDELIJKE_GROETEN)) {
                    endIndex = i;
                }
            }

            if (startIndex != 0 && endIndex != 0) {
                for (int i = startIndex; i <= endIndex; i++) {
                    result.append(allLines.get(i)).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString().trim();
    }

    public static String getMnemonicAfterUA(Path pathToTxt) {
        String result = "";

        try {
            String lineWithUA = Files.lines(pathToTxt).filter(line -> line.trim().toUpperCase().startsWith(UA))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            result = StringUtils.right(lineWithUA.trim(), MNEMONIC_LENGTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMnemnonic(Path path) {
        String fileName = FilenameUtils.getBaseName(path.toString());
        return StringUtils.right(FilenameUtils.removeExtension(fileName), MNEMONIC_LENGTH);
    }

}

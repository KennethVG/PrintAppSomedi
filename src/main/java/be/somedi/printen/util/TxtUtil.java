package be.somedi.printen.util;

import be.somedi.printen.model.DeleteItems;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TxtUtil {

    private static final String PR = "#PR";
    private static final String UA = "#UA";
    private static final String PC = "#PC";
    private static final String PN = "#PN";
    private static final String PV = "#PV";
    private static final String PS = "#PS";
    private static final String PP = "#PP";
    private static final String PA = "#PA";
    private static final String PD = "#PD";
    private static final String UD = "#UD";

    private static final String BETREFT = "Betreft";
    private static final String MET_VRIENDELIJKE_GROETEN = "Met vriendelijke groeten";
    private static final String BESLUIT = "BESLUIT";

    private static final int MNEMONIC_LENGTH = 5;
    private static final int LINE_LENGTH = 75;
    private static final int LINE_LENGTH_SUMMARY = 74;
    private static final int CONSULTID_MAX_LENGTH = 15;
    private static final int SUMMARY_MAX_LENGTH = 7;

    private static final Logger LOGGER = LoggerFactory.getLogger(TxtUtil.class);

    public static boolean isPathWithLetterNotToPrint(Path pathToTxt) {
        LOGGER.debug("Inside method: isPathWithLetterNotToPrint: ");

        boolean toPrint = false;

        try (BufferedReader bufferedReader = Files.newBufferedReader(pathToTxt)) {

            String lineWithConsultId = bufferedReader.lines().filter(line -> line.trim().toUpperCase().startsWith(PR)
            ).findFirst().orElseThrow(RuntimeException::new);
            if (lineWithConsultId.length() > CONSULTID_MAX_LENGTH) {
                LOGGER.debug(PR + " is groter dan 15 --> NIET PRINTEN!");
                return true;
            }

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {

                String finalCurrentLine = currentLine.trim();
                toPrint = Stream.of(DeleteItems.values()).anyMatch(deleteItems -> finalCurrentLine.contains
                        (deleteItems.getText()) || deleteItems.getText().equalsIgnoreCase(finalCurrentLine));
                if (toPrint) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return toPrint;
    }

    public static String getBodyOfTxt(Path pathToTxt) {
        LOGGER.debug("Inside method: getBodyOfTxt: ");
        StringBuilder result = new StringBuilder();
        int startIndex = 0;
        int startSummaryIndex = 0;
        int endIndex = 0;

        try {
            List<String> allLines = Files.readAllLines(pathToTxt);
            String oneLine;
            for (int i = 0; i < allLines.size(); i++) {
                oneLine = allLines.get(i).trim();
                if (oneLine.startsWith(BETREFT)) {
                    startIndex = i;
                } else if (oneLine.contains(BESLUIT)) {
                    startSummaryIndex = i;
                } else if (oneLine.startsWith(MET_VRIENDELIJKE_GROETEN)) {
                    endIndex = i;
                }
            }

            if (startIndex != 0 && endIndex != 0) {
                // Er is een besluit. Max. 7 lijnen starten met ]
                if (startSummaryIndex != 0) {
                    result.append(buildBody(startIndex, startSummaryIndex, allLines));
                    for (int j = startSummaryIndex + 1; j <= endIndex && j< startSummaryIndex + SUMMARY_MAX_LENGTH; j++) {
                        oneLine = allLines.get(j).trim();
                        if (oneLine.length() > LINE_LENGTH) {
                            String summary = StringUtils.left(oneLine, LINE_LENGTH_SUMMARY);
                            int summaryIndex = StringUtils.lastIndexOf(summary, " ");
                            result.append("]").append(summary, 0, summaryIndex).append("\n").append("]").append(StringUtils.substring(oneLine, summaryIndex)).append("\n");
                        } else if (oneLine.equals("") || j == endIndex) {
                            result.append(oneLine).append("\n");
                        } else {
                            result.append("]").append(oneLine).append("\n");
                        }
                    }
                    if(endIndex> startSummaryIndex + SUMMARY_MAX_LENGTH){
                        result.append(buildBody(startSummaryIndex + SUMMARY_MAX_LENGTH, endIndex, allLines));
                    }
                }
                else {
                    result.append(buildBody(startIndex, endIndex, allLines));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString().trim();
    }

    public static String getMnemonicAfterUA(Path pathToTxt) {
        return getTextAfterKeyword(UA, pathToTxt);
    }

    public static String getExternalIdAfterPC(Path pathToTxt) {
        return getTextAfterKeyword(PC, pathToTxt);
    }

    public static String getNameAfterPN(Path pathToTxt) {
        return getTextAfterKeyword(PN, pathToTxt);
    }

    public static String getFirstNameAfterPV(Path pathToTxt) {
        return getTextAfterKeyword(PV, pathToTxt);
    }

    public static String getStreetWithNumberAfterPS(Path pathToTxt) {
        return getTextAfterKeyword(PS, pathToTxt);
    }

    public static String getZipCodeAfterPP(Path pathToTxt) {
        return getTextAfterKeyword(PP, pathToTxt);
    }

    public static String getCityAfterPA(Path pathToTxt) {
        return getTextAfterKeyword(PA, pathToTxt);
    }

    public static String getBirthDateAtferPD(Path pathToTxt) {
        return getTextAfterKeyword(PD, pathToTxt);
    }

    public static String getDateOfResearchAfterUD(Path pathToTxt) {
        return getTextAfterKeyword(UD, pathToTxt);
    }

    public static String getRefNrAfterPR(Path pathToTxt) {
        return getTextAfterKeyword(PR, pathToTxt);
    }


    public static String getMnemnonic(Path path) {
        String fileName = FilenameUtils.getBaseName(path.toString());
        return StringUtils.right(FilenameUtils.removeExtension(fileName), MNEMONIC_LENGTH);
    }

    public static long countNumberOfLines(String text) {
        return text.chars().filter(string -> string == '\n').count() + 1;
    }

    private static String buildBody(int startIndex, int endIndex, List<String> allLines) {
        StringBuilder result = new StringBuilder();
        for (int i = startIndex; i <= endIndex; i++) {
            String oneLine = allLines.get(i);
            if (oneLine.length() > LINE_LENGTH) {
                String first75 = StringUtils.left(oneLine, LINE_LENGTH);
                int index = StringUtils.lastIndexOf(first75, " ");
                result.append(StringUtils.substring(first75, 0, index)).append("\n").append(StringUtils.substring(oneLine, index)).append("\n");
            } else {
                result.append(oneLine).append("\n");
            }
        }
        return result.toString();
    }

    private static String getTextAfterKeyword(String keyword, Path pathToTxt) {
        String result = "";
        try {
            String specificLine = Files.lines(pathToTxt).filter(line -> line.trim().toUpperCase().startsWith(keyword))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            result = StringUtils.substring(specificLine, 3).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}

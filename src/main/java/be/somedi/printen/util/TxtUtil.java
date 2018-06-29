package be.somedi.printen.util;

import be.somedi.printen.model.DeleteItems;
import be.somedi.printen.model.UMFormat;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TxtUtil {

    private static final String DR = "#DR";
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
    private static final String MET_COLLEGIALE_GROETEN = "Met collegiale groeten";
    private static final String MET_COLLEGIALE_HOOGACHTNG = "Met collegiale hoogachting";
    private static final String BESLUIT = "BESLUIT";
    private static final String VUL_AAN = "vul_aan";

    private static final int MNEMONIC_LENGTH = 5;
    private static final int LINE_LENGTH = 75;
    private static final int LINE_LENGTH_SUMMARY = 74;
    private static final int EXTERNALID_MAX_LENGTH = 7;
    private static final int SUMMARY_MAX_LENGTH = 7;

    private static final String CHARSET_NAME = "windows-1252";
    private static final Logger LOGGER = LoggerFactory.getLogger(TxtUtil.class);


    public static boolean letterContainsVulAan(Path pathToTxt) {
        long count = 0;
        try {
            count = Files.lines(pathToTxt, Charset.forName(CHARSET_NAME)).filter(line -> line.contains(VUL_AAN)).count();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return count > 0;
    }


    public static boolean isPathWithLetterNotToPrint(Path pathToTxt) {
        boolean notToPrint = false;
        try {
            String externalId = getTextAfterKeyword(DR, pathToTxt);
            if (externalId == null || externalId.equals("")) {
                LOGGER.info("Dit is geen verslag.");
                return true;
            } else if (externalId.length() > EXTERNALID_MAX_LENGTH) {
                LOGGER.info(DR + " of " + pathToTxt.getFileName() + " is groter dan 15 --> NIET PRINTEN!");
                return true;
            }

            long count = Files.lines(pathToTxt, Charset.forName(CHARSET_NAME)).filter(line -> Stream.of(DeleteItems
                    .values()).anyMatch(deleteItems -> line.contains
                    (deleteItems.getText()) || deleteItems.getText().equalsIgnoreCase(line))).count();

            if (count > 0) {
                notToPrint = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return notToPrint;
    }

    public static String getBodyOfTxt(Path pathToTxt, UMFormat format) {
        StringBuilder result = new StringBuilder();
        int startIndex = 0;
        int startSummaryIndex = 0;
        int endIndex = 0;

        try {
            List<String> allLines = Files.readAllLines(pathToTxt, Charset.forName(CHARSET_NAME));
            String oneLine;
            for (int i = 0; i < allLines.size(); i++) {
                oneLine = allLines.get(i).trim();
                if (oneLine.startsWith(BETREFT)) {
                    startIndex = i;
                } else if (oneLine.contains(BESLUIT)) {
                    startSummaryIndex = i;
                } else if (oneLine.startsWith(MET_VRIENDELIJKE_GROETEN) || oneLine.startsWith(MET_COLLEGIALE_GROETEN) || oneLine.startsWith(MET_COLLEGIALE_HOOGACHTNG)) {
                    endIndex = i;
                }
            }

            if (startIndex != 0 && endIndex != 0) {
                // Er is een besluit. Max. 7 lijnen starten met ]
                if (startSummaryIndex != 0 && format == UMFormat.MEDIDOC) {
                    result.append(buildBody(startIndex, startSummaryIndex + 1, allLines));
                    for (int j = startSummaryIndex + 1; j < endIndex && j < startSummaryIndex + SUMMARY_MAX_LENGTH;
                         j++) {
                        oneLine = allLines.get(j).trim();
                        if (oneLine.length() > LINE_LENGTH) {
                            String summary = StringUtils.left(oneLine, LINE_LENGTH_SUMMARY);
                            int summaryIndex = StringUtils.lastIndexOf(summary, " ");
                            result.append("]").append(summary, 0, summaryIndex).append("\n").append("]").append
                                    (StringUtils.substring(oneLine, summaryIndex)).append("\n");
                        } else if (oneLine.equals("")) {
                            result.append(oneLine).append("\n");
                        } else {
                            result.append("]").append(oneLine).append("\n");
                        }
                    }
                    if (endIndex > startSummaryIndex + SUMMARY_MAX_LENGTH) {
                        result.append(buildBody(startSummaryIndex + SUMMARY_MAX_LENGTH, endIndex, allLines));
                    }
                } else if (startSummaryIndex != 0 && format == UMFormat.MEDAR) {
                    result.append(buildBody(startIndex, startSummaryIndex, allLines));
                    result.append("/CONCL\n").append(buildBody(startSummaryIndex, endIndex, allLines));
                } else {
                    result.append(buildBody(startIndex, endIndex, allLines));
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
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
        for (int i = startIndex; i < endIndex; i++) {
            String oneLine = allLines.get(i);
            if (oneLine.length() > LINE_LENGTH) {
                String first75 = StringUtils.left(oneLine, LINE_LENGTH);
                int index = StringUtils.lastIndexOf(first75, " ");
                result.append(StringUtils.substring(first75, 0, index)).append("\n").append(StringUtils.substring
                        (oneLine, index)).append("\n");
            } else {
                result.append(oneLine).append("\n");
            }
        }
        return result.toString();
    }

    private static String getTextAfterKeyword(String keyword, Path pathToTxt) {
        String result = "";
        try {
            String specificLine = Files.lines(pathToTxt, Charset.forName(CHARSET_NAME)).filter(line -> line.trim()
                    .toUpperCase().startsWith(keyword))
                    .findFirst().orElse("");
            result = StringUtils.substring(specificLine, 3).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
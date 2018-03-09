package be.somedi.printen.util;

import be.somedi.printen.model.Gender;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {

    public static String formatRiziv(String riziv) {
        return StringUtils.left(riziv, 1) + "/" + StringUtils.substring(riziv, 1, 6) + "/" +
                StringUtils.substring(riziv, 6, 8) + "/" + StringUtils.right(riziv, 3);
    }

    public static String formatStringWithBlanks(String externalCaregiverField, int numberOfCharacters) {
        StringBuilder result = new StringBuilder(externalCaregiverField);
        for (int i = externalCaregiverField.length(); i < numberOfCharacters; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    public static String formatStringWithMaxChars(String externalCaregiverField, int maxChars) {
        return StringUtils.substring(externalCaregiverField, 0, maxChars);
    }

    public static String formatDateAndTime(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        return formatter.format(ldt);
    }

    public static String formatDate(String date) {
        DateTimeFormatter stringDateFormat = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate ld = LocalDate.parse(date, stringDateFormat);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return formatter.format(ld);
    }

    public static String formatGender(String externalId) {
        if (externalId.startsWith("M")) {
            return Gender.MALE.getAbbreviation();
        } else if (externalId.startsWith("V")) {
            return Gender.FEMALE.getAbbreviation();
        }
        return Gender.UNDEFINED.getAbbreviation();
    }

    public static long countNumberOfLines(String text) {
        return text.chars().filter(string -> string == '\n').count() + 1;
    }

}

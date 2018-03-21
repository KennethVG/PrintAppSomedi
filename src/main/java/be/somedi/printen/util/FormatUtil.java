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

    public static String formatStringWithBlanks(String field, int numberOfCharacters) {
        StringBuilder result = new StringBuilder(field);
        for (int i = field.length(); i < numberOfCharacters; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    public static String formatBlanksBeforeString(int numberOfBlanks, String field){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberOfBlanks; i++) {
            result.append(" ");
        }
        return result.append(field).toString();
    }

    public static String formatStringWithMaxChars(String externalCaregiverField, int maxChars) {
        return StringUtils.substring(externalCaregiverField, 0, maxChars);
    }

    public static String formatDateAndTime(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return formatter.format(ldt);
    }

    public static String formatDate(String date, String pattern) {
        DateTimeFormatter stringDateFormat = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate ld = LocalDate.parse(date, stringDateFormat);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(ld);
    }

    public static String formatDate(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return outputFormat.format(now);
    }

    public static Gender formatGender(String externalId) {
        if (externalId.startsWith("M")) {
            return Gender.M;
        } else if (externalId.startsWith("V")) {
            return Gender.V;
        }
        return Gender.U;
    }
}

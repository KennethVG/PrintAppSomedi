package be.somedi.printen.printapp.format;


import be.somedi.printen.printapp.model.ExternalCaregiver;
import be.somedi.printen.printapp.repository.ExternalCaregiverRepository;
import be.somedi.printen.printapp.util.TxtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Medidoc {

    private final ExternalCaregiverRepository repository;

    @Autowired
    public Medidoc(ExternalCaregiverRepository repository) {
        this.repository = repository;
    }

    public String buildHeading(Path pathToTxt) {

        StringBuilder result = new StringBuilder();

        ExternalCaregiver externalCaregiver = repository.findFirstByExternalID(TxtUtil.getMnemnonic(pathToTxt));
        ExternalCaregiver caregiverToSendLetter = repository.findFirstByExternalID(TxtUtil.getMnemonicAfterUA(pathToTxt));

        // LINE1: rizivNr (Format: C/CCCCC/CC/CCC) :
        result.append(formatRiziv(externalCaregiver.getNihii()));
        result.append("\n");

        // LINE2: naam (24 karakters) en voornaam (Max. 16 karakters)
        result.append(formatStringWithBlanks(externalCaregiver.getLastName(), 24));
        result.append(formatStringWithMaxChars(externalCaregiver.getFirstName(), 16));
        result.append("\n");

        // LINE3: Straat (35 karakters) en nummer (Max. 10 karakters)
        result.append("P.A. Liersesteenweg                ");
        result.append("267");
        result.append("\n");

        // LINE4: postcode en gemeente
        result.append("2220          ");
        result.append("Heist-op-den-Berg");
        result.append("\n");

        //LINE5: Telefoon - fax (Vrij Max. 50 karakters)
        result.append(formatStringWithMaxChars(externalCaregiver.getPhone(), 50));
        result.append("\n");
        result.append("\n");

        // LINE7: Datum (Format: JJMMDDHHMM)
        LocalDateTime ldt = LocalDateTime.now();
        result.append(formatDate(ldt));
        result.append("\n");

        //LINE8: Riziv aanvragende arts (Format: C/CCCCC/CC/CCC)
        result.append(formatRiziv(caregiverToSendLetter.getNihii()));
        result.append("\n");

        //LINE9: naam (24 karakters) en voornaam (Max. 16 karakters) aanvragende arts
        result.append(formatStringWithBlanks(caregiverToSendLetter.getLastName(), 24));
        result.append(formatStringWithMaxChars(caregiverToSendLetter.getFirstName(),16));

        return result.toString();
    }

    public String buildHeadingLetter(){
        StringBuilder result = new StringBuilder();

        //LINE1: Aanduiding (Format: #ArrnPatiënt)
        result.append("#A");

        //LINE2: Naam en voornaam patiënt




        return result.toString();
    }

    private void emptyLine(StringBuilder builder) {
        builder.append("\n");
    }

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

    public static String formatDate(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        return formatter.format(ldt);
    }
}

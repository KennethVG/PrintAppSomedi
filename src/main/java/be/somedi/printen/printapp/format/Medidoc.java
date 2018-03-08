package be.somedi.printen.printapp.format;


import be.somedi.printen.printapp.model.ExternalCaregiver;
import be.somedi.printen.printapp.model.Patient;
import be.somedi.printen.printapp.model.Person;
import be.somedi.printen.printapp.service.ExternalCaregiverService;
import be.somedi.printen.printapp.service.PatientService;
import be.somedi.printen.printapp.service.PersonService;
import be.somedi.printen.printapp.util.TxtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Medidoc {

    private final ExternalCaregiverService externalCaregiverService;
    private final PatientService patientService;
    private final PersonService personService;

    private int count = 0;

    @Autowired
    public Medidoc(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService
            personService) {
        this.externalCaregiverService = externalCaregiverService;
        this.patientService = patientService;
        this.personService = personService;
    }

    public String buildDocument(Path pathToTxt) {
        StringBuilder result = new StringBuilder();
        String heading = buildHeading(pathToTxt);
        String headingLetter = buildHeadingLetter(pathToTxt);
        String body = buildBody(pathToTxt);
        long count = countNumberOfLines(heading) + countNumberOfLines(headingLetter) + countNumberOfLines(body) + 1;

        result.append(heading).append(headingLetter).append(body).append("\n").append("#/").append(count);

        return result.toString();

    }

    public String buildHeading(Path pathToTxt) {

        StringBuilder result = new StringBuilder();
        ExternalCaregiver externalCaregiver = externalCaregiverService.findByMnemonic(TxtUtil.getMnemnonic(pathToTxt));
        ExternalCaregiver caregiverToSendLetter = externalCaregiverService.findByMnemonic(TxtUtil.getMnemonicAfterUA
                (pathToTxt));

        // LINE1: rizivNr (Format: C/CCCCC/CC/CCC) :
        result.append(formatRiziv(externalCaregiver.getNihii())).append("\n");

        // LINE2: naam (24 karakters) en voornaam (Max. 16 karakters)
        result.append(formatStringWithBlanks(externalCaregiver.getLastName(), 24));
        result.append(formatStringWithMaxChars(externalCaregiver.getFirstName(), 16)).append("\n");

        // LINE3: Straat (35 karakters) en nummer (Max. 10 karakters)
        result.append("P.A. Liersesteenweg                ").append("267").append("\n");

        // LINE4: postcode en gemeente
        result.append("2220          ").append("Heist-op-den-Berg").append("\n");

        //LINE5: Telefoon - fax (Vrij Max. 50 karakters)
        result.append(formatStringWithMaxChars(externalCaregiver.getPhone(), 50)).append("\n").append("\n");

        // LINE7: Datum (Format: JJMMDDHHMM)
        result.append(formatDateAndTime(LocalDateTime.now())).append("\n");

        //LINE8: Riziv aanvragende arts (Format: C/CCCCC/CC/CCC)
        result.append(formatRiziv(caregiverToSendLetter.getNihii())).append("\n");

        //LINE9: naam (24 karakters) en voornaam (Max. 16 karakters) aanvragende arts
        result.append(formatStringWithBlanks(caregiverToSendLetter.getLastName(), 24));
        result.append(formatStringWithMaxChars(caregiverToSendLetter.getFirstName(), 16));

        return result.toString();
    }

    public String buildHeadingLetter(Path pathToTxt) {

        StringBuilder result = new StringBuilder();
        String externalId = TxtUtil.getExternalIdAfterPC(pathToTxt);
        Patient patient = patientService.findByExternalId(externalId);
        Person person = personService.findById((long) patient.getPersonId());

        //LINE1: Aanduiding (Format: #ArrnPatiënt)
        result.append("#A").append(person.getRrn()).append("\n");

        //LINE2: Naam (24) en voornaam (max.16) patiënt
        result.append(formatStringWithBlanks(TxtUtil.getNameAfterPN(pathToTxt), 24));
        result.append(formatStringWithMaxChars(TxtUtil.getFirstNameAfterPV(pathToTxt), 16)).append("\n");

        //LINE3: Geboortedatum patiënt (Format: JJMMDD)
        result.append(formatDate(TxtUtil.getBirthDateAtferPD(pathToTxt))).append("\n");

        //LINE4: Geslacht patient (X (vrouwelijk), Y (mannelijk), Z(onbepaald))
        result.append(formatGender(externalId)).append("\n");

        //LINE5: Datum aanvraag onderzoek
        result.append(formatDate(TxtUtil.getDateOfResearchAfterUD(pathToTxt))).append("\n");

        //LINE6: RefNr (14 karakters)
        result.append(formatStringWithBlanks(TxtUtil.getRefNrAfterPR(pathToTxt), 14)).append("\n").append("\n")
                .append("\n").append("\n").append("\n");

        return result.toString();
    }

    public String buildBody(Path pathToTxt) {
        StringBuilder result = new StringBuilder();

        //LINE1: Aanduiding begin resultaat
        result.append("#Rb").append("\n");

        //LINE2: Identificatie van de analyse
        //Todo: Willy vragen

        //LINE3: uitslag (Max. 75 karakters per lijn, ] = BESLUIT)
        //Todo: besluit in orde brengen
        result.append(TxtUtil.getBodyOfTxt(pathToTxt)).append("\n");

        //EINDE:
        result.append("#R/").append("\n").append("#A/");

        return result.toString();
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
            return "Y";
        } else if (externalId.startsWith("V")) {
            return "X";
        }
        return "Z";
    }

    public static long countNumberOfLines(String text) {
        return text.chars().filter(string -> string == '\n').count() + 1;
    }
}

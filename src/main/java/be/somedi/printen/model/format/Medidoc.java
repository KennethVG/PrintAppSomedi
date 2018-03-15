package be.somedi.printen.model.format;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.entity.Patient;
import be.somedi.printen.entity.Person;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.PatientService;
import be.somedi.printen.service.PersonService;
import be.somedi.printen.util.IOUtil;
import be.somedi.printen.util.TxtUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDateTime;

import static be.somedi.printen.util.FormatUtil.*;
import static be.somedi.printen.util.TxtUtil.countNumberOfLines;

@Component
public class Medidoc extends BaseFormat {

    private static final int NUMBER_OF_RIZIV = 8;

    @Value("${path-um}")
    private Path PATH_TO_UM;

    public Medidoc(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService personService) {
        super(externalCaregiverService, patientService, personService);
    }

    public Path makeRepFile() {
        return IOUtil.writeFileToUM(PATH_TO_UM, getMnemonic(), getRefNr(), "REP", buildDocument());
    }

    public Path makeAdrFile() {
        String first8NumbersOfrizivFromCaregiverToSend = StringUtils.left(getCaregiverToSendLetter().getNihii(), NUMBER_OF_RIZIV);
        return IOUtil.writeFileToUM(PATH_TO_UM, getMnemonic(), getRefNr(), "ADR", first8NumbersOfrizivFromCaregiverToSend);
    }

    @Override
    public String buildDocument() {
        StringBuilder result = new StringBuilder();
        long count = countNumberOfLines(buildHeading()) + countNumberOfLines(buildHeadingLetter()) + countNumberOfLines(buildBody());
        result.append(buildHeading()).append("\n").append(buildHeadingLetter()).append(buildBody()).append("\n").append("#/").append(count);
        return result.toString();
    }

    private String buildHeading() {

        StringBuilder result = new StringBuilder();
        ExternalCaregiver externalCaregiver = getExternalCaregiver();
        ExternalCaregiver caregiverToSendLetter = getCaregiverToSendLetter();

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

    private String buildHeadingLetter() {

        StringBuilder result = new StringBuilder();
        String externalId = TxtUtil.getExternalIdAfterPC(getPathToTxt());
        Patient patient = getPatientService().findByExternalId(externalId);
        Person person = getPersonService().findById((long) patient.getPersonId());

        //LINE1: Aanduiding (Format: #ArrnPatiënt)
        result.append("#A").append(person.getInss()).append("\n");

        //LINE2: Naam (24) en voornaam (max.16) patiënt
        result.append(formatStringWithBlanks(TxtUtil.getNameAfterPN(getPathToTxt()), 24));
        result.append(formatStringWithMaxChars(TxtUtil.getFirstNameAfterPV(getPathToTxt()), 16)).append("\n");

        //LINE3: Geboortedatum patiënt (Format: JJMMDD)
        result.append(formatDate(TxtUtil.getBirthDateAtferPD(getPathToTxt()))).append("\n");

        //LINE4: Geslacht patient (X (vrouwelijk), Y (mannelijk), Z(onbepaald))
        result.append(formatGender(externalId)).append("\n");

        //LINE5: Datum aanvraag onderzoek
        result.append(formatDate(TxtUtil.getDateOfResearchAfterUD(getPathToTxt()))).append("\n");

        //LINE6: RefNr (14 karakters)
        result.append(formatStringWithBlanks(getRefNr(), 14)).append("\n").append("\n");

        return result.toString();
    }

    private String buildBody() {
        StringBuilder result = new StringBuilder();

        //LINE1: Aanduiding begin resultaat
        result.append("#Rb").append("\n");

        //LINE2: Identificatie van de analyse
        //Todo: Willy vragen
        result.append("!Onderzoek").append("\n");

        //LINE3: uitslag (Max. 75 karakters per lijn, ] = BESLUIT)
        result.append("Geachte collega,").append("\n\n");
        result.append(TxtUtil.getBodyOfTxt(getPathToTxt())).append("\n")
                .append(getExternalCaregiver().getFirstName()).append(" ")
                .append(getExternalCaregiver().getLastName()).append("\n");

        //EINDE:
        result.append("#R/").append("\n").append("#A/");

        return result.toString();
    }
}

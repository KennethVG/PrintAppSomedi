package be.somedi.printen.model.format;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.PatientService;
import be.somedi.printen.service.PersonService;
import be.somedi.printen.util.TxtUtil;
import org.springframework.stereotype.Component;

import static be.somedi.printen.util.FormatUtil.*;
import static be.somedi.printen.util.TxtUtil.*;

@Component
public class MediCard extends BaseFormat {

    public MediCard(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService
            personService) {
        super(externalCaregiverService, patientService, personService);
    }

    @Override
    public String buildDocument() {

        StringBuilder result = new StringBuilder();
        ExternalCaregiver specialistOfSomedi = getSpecialistOfSomedi();

        // Lijn 1: Proto begin
        result.append("PROTO BEGIN").append("\n\n");

        // Lijn 3 tem 6: Instelling
        result.append(somediHeading()).append("\n\n");

        // Lijn 8: Protocol datum en protocol ref (positie 30)
        result.append(formatStringWithBlanks(formatDate(), 30)).append("R").append(getRefNr()).append("\n");

        // Lijn 9: aanvragende arts (startpositie 10)
        result.append(formatBlanksBeforeString(10, specialistOfSomedi.getLastName() + " " + specialistOfSomedi.getFirstName())).append
                ("\n\n");

        // Lijn 10 tem 15: patiënt info (startpositie 10)
        result.append(formatBlanksBeforeString(10, getNameAfterPN(getPathToTxt()) + "," + getFirstNameAfterPV
                (getPathToTxt()))).append("\n")
                .append(formatBlanksBeforeString(10, getStreetWithNumberAfterPS(getPathToTxt()))).append("\n")
                .append(formatBlanksBeforeString(10, getZipCodeAfterPP(getPathToTxt()) + " " + (getCityAfterPA
                        (getPathToTxt())))).append("\n")
                .append(formatBlanksBeforeString(10, formatGender(getExternalIdAfterPC(getPathToTxt())).name()))
                .append(formatBlanksBeforeString(15, formatDate(TxtUtil.getBirthDateAtferPD(getPathToTxt()), "dd/MM/yyy")))
                .append("\n")
                .append("\n\n"); //TODO: mutualiteit gegevens patiënt

        // Lijn 17 tem 19: Protocol hoofdding
        result.append(line()).append("\n")
                .append(specialistOfSomedi.getTitle()).append(" ").append(specialistOfSomedi.getLastName()).append(" ").append(specialistOfSomedi.getFirstName()).append("\n")
                .append(line()).append("\n\n");

        // Lijn 21 tem eind: protocol
        result.append("Geachte collega,").append("\n\n")
                .append(getBodyOfTxt(getPathToTxt())).append("\n")
                .append(specialistOfSomedi.getTitle()).append(" ").append(specialistOfSomedi.getLastName()).append(" ").append(specialistOfSomedi.getFirstName()).append("\n\n")
                .append("VOLLEDIG PROTOCOL");

        return result.toString();
    }

    private String somediHeading() {
        return "SOMEDI C.V.B.A.\n" +
                "Liersesteenweg 267\n" +
                "2220 Heist-op-den-Berg\n" +
                "Tel.: 015/25.89.11";
    }

    private String line(){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 80; i++) {
            result.append("-");
        }
        return result.toString();
    }
}

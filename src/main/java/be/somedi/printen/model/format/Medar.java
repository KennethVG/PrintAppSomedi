package be.somedi.printen.model.format;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.entity.Person;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.PatientService;
import be.somedi.printen.service.PersonService;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.stereotype.Component;

import static be.somedi.printen.util.FormatUtil.*;
import static be.somedi.printen.util.TxtUtil.*;

@Component
public class Medar extends BaseFormat {

    public Medar(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService
            personService) {
        super(externalCaregiverService, patientService, personService);
    }

    @Override
    public String buildDocument() {
        ExternalCaregiver specialist = getSpecialistOfSomedi();
        ExternalCaregiver toSend = getCaregiverToSendLetter();
        Person patientInfo = getPatientDetails();

        String[] specialistIds = {specialist.getLastName(), specialist.getStreetWithNumber(), specialist.getZip(),
                specialist.getCity(), "", formatRiziv(specialist.getNihii())};
        String[] specialistToSendIds = {toSend.getLastName(), toSend.getStreetWithNumber(), toSend.getZip(), toSend
                .getCity(), "", formatRiziv(toSend.getNihii())};
        String[] patientIds = {patientInfo.getFirstName(), patientInfo.getLastName(), getStreetWithNumberAfterPS(getPathToTxt()),
                getZipCodeAfterPP(getPathToTxt()), getCityAfterPA(getPathToTxt()), formatDate(getBirthDateAtferPD(getPathToTxt()), "yyyyMMdd"),
                formatGender(getExternalIdAfterPC(getPathToTxt())).name()};
        String [] info = {"Created on " + formatDate(), "MDR" + getRefNr()};


        StrBuilder result = new StrBuilder();

        // HEADING
        result.appendFixedWidthPadRight("/FROM", 10, ' ').append(": ")
                .appendWithSeparators(specialistIds, "|").appendNewLine()
                .appendFixedWidthPadRight("/TO", 10, ' ').append(": ")
                .appendWithSeparators(specialistToSendIds, "|").appendNewLine()
                .appendFixedWidthPadRight("/SUBJECT", 10, ' ').append(": ")
                .appendWithSeparators(patientIds, "|").appendNewLine()
                .appendFixedWidthPadRight("/INFO", 10, ' ').append(": ")
                .appendWithSeparators(info, "|").appendNewLine().appendNewLine();

        // BODY
        result.append("/TITLE Raadpleging ").append(specialist.getTitle()).append(" ").append(specialist.getLastName()).appendNewLine()
                .append("/DATE ").append(formatDate()).appendNewLine().append("Geachte collega,\n\n")
                .append("/DESCR").appendNewLine().append(getBodyOfTxt(getPathToTxt())).appendNewLine()
                .append("/END").appendNewLine()
                .append(specialist.getTitle()).append(" ").append(specialist.getLastName()).append(" ").append(specialist.getFirstName()).appendNewLine();


        return result.toString();
    }
}

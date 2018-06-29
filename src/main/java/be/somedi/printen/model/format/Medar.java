package be.somedi.printen.model.format;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.model.UMFormat;
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

        String[] specialistIds = {specialist.getLastName(), specialist.getStreetWithNumber(), specialist.getZip(),
                specialist.getCity(), "", formatRiziv(specialist.getNihii())};
        String[] specialistToSendIds = {toSend.getLastName(), toSend.getStreetWithNumber(), toSend.getZip(), toSend
                .getCity(), "", formatRiziv(toSend.getNihii())};
        String[] patientIds = {getFirstNameAfterPV(getPathToTxt()), getNameAfterPN(getPathToTxt()), getStreetWithNumberAfterPS(getPathToTxt()),
                getZipCodeAfterPP(getPathToTxt()), getCityAfterPA(getPathToTxt()), formatDate(getBirthDateAtferPD(getPathToTxt()), "yyyyMMdd"),
                formatGender(getExternalIdAfterPC(getPathToTxt())).name()};
        String[] info = {"Created on " + formatDate(), "MDR" + getRefNr()};


        StrBuilder result = new StrBuilder();

        // HEADING
        result.appendFixedWidthPadRight("/FROM", 10, ' ').append(": ")
                .appendWithSeparators(specialistIds, "|").append("\n")
                .appendFixedWidthPadRight("/TO", 10, ' ').append(": ")
                .appendWithSeparators(specialistToSendIds, "|").append("\n")
                .appendFixedWidthPadRight("/SUBJECT", 10, ' ').append(": ")
                .appendWithSeparators(patientIds, "|").append("\n")
                .appendFixedWidthPadRight("/INFO", 10, ' ').append(": ")
                .appendWithSeparators(info, "|").append("\n\n");

        // BODY
        result.append("/TITLE Raadpleging ").append(specialist.getTitle()).append(" ").append(specialist.getLastName()).append("\n")
                .append("/DATE ").append(formatDate()).append("\n").append(buildStart())
                .append("/DESCR").append("\n").append(getBodyOfTxt(getPathToTxt(), UMFormat.MEDAR)).append("\n")
                .append("/END").append("\n")
                .append(buildEnd());


        return result.toString();
    }
}

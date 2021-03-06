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
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;

public abstract class BaseFormat {

    private final ExternalCaregiverService externalCaregiverService;
    private final PatientService patientService;
    private final PersonService personService;

    private Path pathToTxt;

    private static final int NUMBER_OF_RIZIV = 8;

    @Autowired
    public BaseFormat(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService personService) {
        this.externalCaregiverService = externalCaregiverService;
        this.patientService = patientService;
        this.personService = personService;
    }

    public String getMnemonic() {
        return TxtUtil.getMnemnonic(pathToTxt);
    }

    public ExternalCaregiver getCaregiverToSendLetter() {
        return externalCaregiverService.findByMnemonic(getMnemonic());
    }

    public ExternalCaregiver getSpecialistOfSomedi() {
        return externalCaregiverService.findByMnemonic(TxtUtil.getMnemonicAfterUA
                (pathToTxt));
    }

    public Patient getPatient() {
        String externalId = TxtUtil.getExternalIdAfterPC(getPathToTxt());
        return getPatientService().findByExternalId(externalId);
    }

    public Person getPatientDetails() {
        Patient patient = getPatient();
        if (patient != null) {
            return getPersonService().findById(getPatient().getPersonId());
        }
        return null;
    }

    public String getRefNr() {
        return TxtUtil.getRefNrAfterPR(getPathToTxt());
    }

    public PatientService getPatientService() {
        return patientService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public Path getPathToTxt() {
        return pathToTxt;
    }

    public void setPathToTxt(Path pathToTxt) {
        this.pathToTxt = pathToTxt;
    }

//    public Path makeRepFile(Path pathToUm, ExternalCaregiver caregiverToSend) {
//        return IOUtil.writeFileToUM(pathToUm, caregiverToSend.getExternalID(), getRefNr(), "REP", buildDocument());
//    }

    public Path makeRepFile(Path pathToUm, ExternalCaregiver caregiverToSend) {
        return IOUtil.writeFileToUM(pathToUm, caregiverToSend.getExternalID(), getRefNr(), "REP", buildDocument(caregiverToSend));
    }


    public Path makeAdrFile(Path pathToUm, ExternalCaregiver caregiverToSend) {
        String first8NumbersOfrizivFromCaregiverToSend = StringUtils.left(caregiverToSend.getNihiiAddress() == null || caregiverToSend.getNihiiAddress().equalsIgnoreCase("NULL") ? caregiverToSend.getNihii() : caregiverToSend.getNihiiAddress(), NUMBER_OF_RIZIV);
        return IOUtil.writeFileToUM(pathToUm, caregiverToSend.getExternalID(), getRefNr(), "ADR", first8NumbersOfrizivFromCaregiverToSend);
    }

    public String buildStart() {
        return "Geachte collega,\n\n";
    }

    public String buildEnd() {
        return "Met vriendelijke groeten,\n" + getSpecialistOfSomedi().getTitle() + " " + getSpecialistOfSomedi()
                .getLastName() + " " + getSpecialistOfSomedi().getFirstName() + "\n";
    }

//    public abstract String buildDocument();
    public abstract String buildDocument(ExternalCaregiver linkedExternalCaregiver);
}

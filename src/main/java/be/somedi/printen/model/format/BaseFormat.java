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

    public Patient getPatient(){
        String externalId = TxtUtil.getExternalIdAfterPC(getPathToTxt());
        return getPatientService().findByExternalId(externalId);
    }

    public Person getPatientDetails(){
        return getPersonService().findById((long) getPatient().getPersonId());
    }

    public String getRefNr(){
        return TxtUtil.getRefNrAfterPR(getPathToTxt());
    }

    public ExternalCaregiverService getExternalCaregiverService() {
        return externalCaregiverService;
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

    public Path makeRepFile(Path pathToUm, ExternalCaregiver caregiverToSend) {
        return IOUtil.writeFileToUM(pathToUm, caregiverToSend.getExternalID(), getRefNr(), "REP", buildDocument());
    }

    public Path makeAdrFile(Path pathToUm, ExternalCaregiver caregiverToSend) {
        String first8NumbersOfrizivFromCaregiverToSend = StringUtils.left(caregiverToSend.getNihiiAddress()!=null?caregiverToSend.getNihiiAddress():caregiverToSend.getNihii(), NUMBER_OF_RIZIV);
        return IOUtil.writeFileToUM(pathToUm, caregiverToSend.getExternalID(), getRefNr(), "ADR", first8NumbersOfrizivFromCaregiverToSend);
    }

    public abstract String buildDocument();
}

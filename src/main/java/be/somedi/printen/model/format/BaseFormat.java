package be.somedi.printen.model.format;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.PatientService;
import be.somedi.printen.service.PersonService;
import be.somedi.printen.util.TxtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;

public abstract class BaseFormat {

    private final ExternalCaregiverService externalCaregiverService;
    private final PatientService patientService;
    private final PersonService personService;

    private Path pathToTxt;

    @Autowired
    public BaseFormat(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService personService) {
        this.externalCaregiverService = externalCaregiverService;
        this.patientService = patientService;
        this.personService = personService;
    }

    public String getMnemonic() {
        return TxtUtil.getMnemnonic(pathToTxt);
    }

    public ExternalCaregiver getExternalCaregiver() {
        return externalCaregiverService.findByMnemonic(getMnemonic());
    }

    public ExternalCaregiver getCaregiverToSendLetter() {
        return externalCaregiverService.findByMnemonic(TxtUtil.getMnemonicAfterUA
                (pathToTxt));
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

    public abstract String buildDocument();
}

package be.somedi.printen.model.format;

import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.service.PatientService;
import be.somedi.printen.service.PersonService;
import org.springframework.stereotype.Component;

@Component
public class HealthOne extends BaseFormat {

    public HealthOne(ExternalCaregiverService externalCaregiverService, PatientService patientService, PersonService personService) {
        super(externalCaregiverService, patientService, personService);
    }

    @Override
    public String buildDocument() {
        return null;
    }
}

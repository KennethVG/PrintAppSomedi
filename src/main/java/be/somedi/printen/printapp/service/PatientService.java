package be.somedi.printen.printapp.service;

import be.somedi.printen.printapp.model.Patient;

public interface PatientService {

    Patient findByExternalId(String externalId);

}

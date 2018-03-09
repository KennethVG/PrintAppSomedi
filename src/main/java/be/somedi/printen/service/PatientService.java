package be.somedi.printen.service;

import be.somedi.printen.entity.Patient;

public interface PatientService {

    Patient findByExternalId(String externalId);

}

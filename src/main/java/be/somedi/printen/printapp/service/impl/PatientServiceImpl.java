package be.somedi.printen.printapp.service.impl;

import be.somedi.printen.printapp.model.Patient;
import be.somedi.printen.printapp.repository.PatientRepository;
import be.somedi.printen.printapp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    @Autowired
    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Patient findByExternalId(String externalId) {
        return repository.findFirstByExternalId(externalId);
    }
}

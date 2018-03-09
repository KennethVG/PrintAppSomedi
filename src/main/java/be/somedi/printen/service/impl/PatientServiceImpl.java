package be.somedi.printen.service.impl;

import be.somedi.printen.entity.Patient;
import be.somedi.printen.repository.PatientRepository;
import be.somedi.printen.service.PatientService;
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

package be.somedi.printen.service.impl;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.repository.ExternalCaregiverRepository;
import be.somedi.printen.service.ExternalCaregiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalCaregiverServiceImpl implements ExternalCaregiverService {

    private final ExternalCaregiverRepository repository;

    @Autowired
    public ExternalCaregiverServiceImpl(ExternalCaregiverRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExternalCaregiver findByMnemonic(String mnemonic) {
        if (mnemonic.length() == 5) {
            return repository.findFirstByExternalID(mnemonic);
        }
        return null;
    }

    @Override
    public int updatePrintProtocols(ExternalCaregiver caregiver) {
        ExternalCaregiver externalCaregiver = repository.saveAndFlush(caregiver);
        if (externalCaregiver != null) {
            return 1;
        }
        return 0;
    }
}

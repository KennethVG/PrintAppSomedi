package be.somedi.printen.printapp.service.impl;

import be.somedi.printen.printapp.model.ExternalCaregiver;
import be.somedi.printen.printapp.repository.ExternalCaregiverRepository;
import be.somedi.printen.printapp.service.ExternalCaregiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalCaregiverServiceImpl implements ExternalCaregiverService {

    private final ExternalCaregiverRepository repository;

    @Autowired
    public ExternalCaregiverServiceImpl(ExternalCaregiverRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ExternalCaregiver> findAll() {
        return repository.findAll();
    }

    @Override
    public ExternalCaregiver findByMnemonic(String mnemonic) {
        if (mnemonic.length() == 5) {
            return repository.findFirstByExternalID(mnemonic);
        }
        return null;
    }

    @Override
    public ExternalCaregiver findById(Long id) {
        return repository.findById(id);
    }
}

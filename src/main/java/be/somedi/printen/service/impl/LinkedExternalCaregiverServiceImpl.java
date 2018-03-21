package be.somedi.printen.service.impl;

import be.somedi.printen.repository.LinkedExternalCargiverRepository;
import be.somedi.printen.service.LinkedExternalCargiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkedExternalCaregiverServiceImpl implements LinkedExternalCargiverService {

    private final LinkedExternalCargiverRepository linkedExternalCargiverRepository;

    @Autowired
    public LinkedExternalCaregiverServiceImpl(LinkedExternalCargiverRepository linkedExternalCargiverRepository) {
        this.linkedExternalCargiverRepository = linkedExternalCargiverRepository;
    }

    @Override
    public String findLinkedIdByExternalId(String externalId) {
        return linkedExternalCargiverRepository.findByExternalId(externalId).getLinkedId();
    }
}

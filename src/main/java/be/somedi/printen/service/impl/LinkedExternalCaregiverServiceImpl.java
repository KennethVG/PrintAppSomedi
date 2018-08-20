package be.somedi.printen.service.impl;

import be.somedi.printen.entity.LinkedExternalCaregiver;
import be.somedi.printen.repository.LinkedExternalCargiverRepository;
import be.somedi.printen.service.LinkedExternalCargiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.rtf.RTFEditorKit;
import javax.transaction.Transactional;

@Service
public class LinkedExternalCaregiverServiceImpl implements LinkedExternalCargiverService {

    private final LinkedExternalCargiverRepository linkedExternalCargiverRepository;

    @Autowired
    public LinkedExternalCaregiverServiceImpl(LinkedExternalCargiverRepository linkedExternalCargiverRepository) {
        this.linkedExternalCargiverRepository = linkedExternalCargiverRepository;
    }

    @Override
    public LinkedExternalCaregiver findLinkedIdByExternalId(String externalId) {
        return linkedExternalCargiverRepository.findByExternalId(externalId);
    }

    @Override
    @Transactional
    public int updateLinkedExternalCaregiver(LinkedExternalCaregiver linkedExternalCaregiver) {
        LinkedExternalCaregiver searchedCaregiver = findLinkedIdByExternalId(linkedExternalCaregiver.getExternalId());
        if (searchedCaregiver != null) {
            linkedExternalCargiverRepository.updateLinkedExternalCaregiver(searchedCaregiver.getExternalId(), linkedExternalCaregiver.getLinkedId());
            return 1;
        }
        LinkedExternalCaregiver caregiver = linkedExternalCargiverRepository.save(linkedExternalCaregiver);
        return caregiver != null ? 1 : 0;
    }

}

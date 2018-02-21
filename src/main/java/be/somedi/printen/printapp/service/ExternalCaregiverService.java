package be.somedi.printen.printapp.service;

import be.somedi.printen.printapp.model.ExternalCaregiver;

import java.util.List;

public interface ExternalCaregiverService {

    List<ExternalCaregiver> findAll();
    ExternalCaregiver findByMnemonic(String mnemonic);
    ExternalCaregiver findById(Long id);
}

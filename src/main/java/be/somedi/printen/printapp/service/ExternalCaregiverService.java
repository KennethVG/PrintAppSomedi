package be.somedi.printen.printapp.service;

import be.somedi.printen.printapp.model.ExternalCaregiver;

public interface ExternalCaregiverService {

    ExternalCaregiver findByMnemonic(String mnemonic);

    int updatePrintProtocols(ExternalCaregiver printProtocols);
}

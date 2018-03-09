package be.somedi.printen.service;

import be.somedi.printen.entity.ExternalCaregiver;

public interface ExternalCaregiverService {

    ExternalCaregiver findByMnemonic(String mnemonic);

    int updatePrintProtocols(ExternalCaregiver printProtocols);
}

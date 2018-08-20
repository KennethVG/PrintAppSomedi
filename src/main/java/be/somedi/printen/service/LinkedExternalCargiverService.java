package be.somedi.printen.service;

import be.somedi.printen.entity.LinkedExternalCaregiver;

public interface LinkedExternalCargiverService {

    LinkedExternalCaregiver findLinkedIdByExternalId(String externalId);

    int updateLinkedExternalCaregiver(LinkedExternalCaregiver linkedExternalCaregiver);
}

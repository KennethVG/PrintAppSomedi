package be.somedi.printen.repository;

import be.somedi.printen.entity.LinkedExternalCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedExternalCargiverRepository extends JpaRepository<LinkedExternalCaregiver, String> {

    LinkedExternalCaregiver findByExternalId(String externalId);
}

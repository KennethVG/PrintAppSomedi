package be.somedi.printen.printapp.repository;

import be.somedi.printen.printapp.model.ExternalCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCaregiverRepository extends JpaRepository<ExternalCaregiver, Long> {

    ExternalCaregiver findFirstByExternalID(String externalId);
}

package be.somedi.printen.repository;

import be.somedi.printen.entity.ExternalCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalCaregiverRepository extends JpaRepository<ExternalCaregiver, Long> {

    ExternalCaregiver findFirstByExternalID(String externalId);

}

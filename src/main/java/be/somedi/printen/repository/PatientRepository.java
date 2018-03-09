package be.somedi.printen.repository;

import be.somedi.printen.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long>{

    Patient findFirstByExternalId(String externalId);

}

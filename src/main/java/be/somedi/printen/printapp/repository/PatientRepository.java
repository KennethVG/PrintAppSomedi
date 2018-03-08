package be.somedi.printen.printapp.repository;

import be.somedi.printen.printapp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long>{

    Patient findFirstByExternalId(String externalId);

}

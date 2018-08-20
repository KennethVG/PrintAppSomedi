package be.somedi.printen.repository;

import be.somedi.printen.entity.LinkedExternalCaregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkedExternalCargiverRepository extends JpaRepository<LinkedExternalCaregiver, String> {

    LinkedExternalCaregiver findByExternalId(String externalId);

    @Modifying
    @Query("UPDATE LinkedExternalCaregiver lc SET lc.linkedId=:linkedId where lc.externalId=:externalId")
    void updateLinkedExternalCaregiver(@Param("externalId") String externalId,@Param("linkedId") String linkedId);


}

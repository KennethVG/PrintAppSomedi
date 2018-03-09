package be.somedi.printen.repository;

import be.somedi.printen.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findPersonById(Long id);

}

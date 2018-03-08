package be.somedi.printen.printapp.repository;

import be.somedi.printen.printapp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findPersonById(Long id);

}

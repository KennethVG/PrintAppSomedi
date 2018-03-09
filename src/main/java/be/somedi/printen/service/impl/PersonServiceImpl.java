package be.somedi.printen.service.impl;

import be.somedi.printen.entity.Person;
import be.somedi.printen.repository.PersonRepository;
import be.somedi.printen.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Person findById(Long id) {
        return repository.findPersonById(id);
    }
}

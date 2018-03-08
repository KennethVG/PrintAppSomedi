package be.somedi.printen.printapp.service.impl;

import be.somedi.printen.printapp.model.Person;
import be.somedi.printen.printapp.repository.PersonRepository;
import be.somedi.printen.printapp.service.PersonService;
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

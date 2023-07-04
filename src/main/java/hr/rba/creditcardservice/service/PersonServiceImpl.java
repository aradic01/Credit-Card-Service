package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.openapi.model.Person;
import hr.rba.creditcardservice.common.PersonMapper;
import hr.rba.creditcardservice.exception.PersonAlreadyExistsException;
import hr.rba.creditcardservice.exception.PersonNotFoundException;
import hr.rba.creditcardservice.jpa.entity.PersonEntity;
import hr.rba.creditcardservice.jpa.repository.FileRepository;
import hr.rba.creditcardservice.jpa.repository.PersonRepository;
import hr.rba.creditcardservice.service.contract.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final FileRepository fileRepository;
    private final PersonRepository personRepository;

    public PersonServiceImpl(FileRepository fileRepository, PersonRepository personRepository) {
        this.fileRepository = fileRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Person createPerson(Person person) {
        if(personRepository.findByOib(person.getOib()).isPresent()) {
            throw new PersonAlreadyExistsException("Person with given OIB already exists!");
        }

        PersonEntity newPerson = PersonMapper.INSTANCE.mapTo(person);
        return PersonMapper.INSTANCE.mapTo(personRepository.save(newPerson));
    }

    @Override
    public Person getPersonByOib(String oib) {
        PersonEntity person = getPersonEntityByOib(oib);
        return PersonMapper.INSTANCE.mapTo(person);
    }

    @Override
    public Person updatePerson(Person person) {
        PersonEntity existingPerson = getPersonEntityByOib(person.getOib());

        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setOib(person.getOib());
        existingPerson.setStatus(PersonMapper.INSTANCE.mapTo(person.getStatus()));

        return PersonMapper.INSTANCE.mapTo(personRepository.save(existingPerson));
    }

    @Override
    @Transactional
    public void deletePersonByOib(String oib) {
        Optional<Long> personId = personRepository.findPersonIdByOib(oib);
        if(personId.isEmpty()) {
            throw new PersonNotFoundException("Person for given OIB doesn't exist!");
        }
        fileRepository.deactivateFilesAndRemovePersonReferences(personId.get());
        personRepository.deleteByOib(oib);
    }

    @Override
    public PersonEntity getPersonEntityByOib(String oib) {
        return personRepository.findByOib(oib)
                .orElseThrow(() -> new PersonNotFoundException("Person for given OIB doesn't exist!"));
    }
}

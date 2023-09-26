package hr.rba.creditcardservice.api.controller;

import hr.rba.creditcardservice.openapi.api.PersonApi;
import hr.rba.creditcardservice.openapi.model.Person;
import hr.rba.creditcardservice.service.contract.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.support.*;

@Controller
@Validated
public class PersonController implements PersonApi {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public ResponseEntity<Person> createPerson(Person person) {

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(person.getOib())
                .toUri();

        return ResponseEntity.created(location)
                .body(personService.createPerson(person));
    }

    @Override
    public ResponseEntity<Void> deletePersonByOib(String personOib) {
        personService.deletePersonByOib(personOib);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Person> getPersonByOib(String personOib) {
        return ResponseEntity.ok()
                .body(personService.getPersonByOib(personOib));
    }

    @Override
    public ResponseEntity<Person> updatePerson(Person person) {
        return ResponseEntity.ok()
                .body(personService.updatePerson(person));
    }
}

package hr.rba.creditcardservice.service.contract;

import hr.rba.creditcardservice.jpa.entity.PersonEntity;
import hr.rba.creditcardservice.openapi.model.Person;

public interface PersonService {
    Person createPerson(Person person);
    Person getPersonByOib(String oib);
    PersonEntity getPersonEntityByOib(String oib);
    Person updatePerson(Person person);
    void deletePersonByOib(String oib);
}

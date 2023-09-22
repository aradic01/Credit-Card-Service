package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.openapi.model.Person;
import hr.rba.creditcardservice.common.mapper.PersonMapper;
import hr.rba.creditcardservice.exception.PersonAlreadyExistsException;
import hr.rba.creditcardservice.exception.PersonNotFoundException;
import hr.rba.creditcardservice.jpa.entity.person.PersonEntity;
import hr.rba.creditcardservice.jpa.repository.PersonRepository;
import hr.rba.creditcardservice.service.contract.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTests {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    private static Person person;
    private static Person personToUpdate;

    @BeforeAll
    static void init() {
        person = new Person(
                "15756478462",
                "John",
                "Doe",
                Person.StatusEnum.CLIENT
        );

        personToUpdate = new Person(
                "15756478462",
                "Jane",
                "Doe",
                Person.StatusEnum.CLIENT
        );
    }

    @Test
    void createPersonShouldCreateANewPersonAndSaveItToDb() {
        when(personRepository.save(any(PersonEntity.class))).thenAnswer(i -> i.getArgument(0));
        personService.createPerson(person);
        verify(personRepository).save(any(PersonEntity.class));
    }

    @Test
    void createPersonShouldThrowAPersonAlreadyExistsException() {
        when(personRepository.findByOib(any()))
                .thenReturn(Optional.of(PersonMapper.INSTANCE.mapTo(person)));
        assertThrows(PersonAlreadyExistsException.class, () -> personService.createPerson(person));
    }

    @Test
    void getPersonByOibShouldReturnPersonObject() {
        when(personRepository.findByOib(any())).thenReturn(Optional.of(PersonMapper.INSTANCE.mapTo(person)));
        assertThat(personService.getPersonByOib("15756478462")).isEqualTo(person);
    }

    @Test
    void getPersonByOibShouldThrowPersonNotFoundException() {
        when(personRepository.findByOib(any())).thenThrow(PersonNotFoundException.class);
        assertThrows(PersonNotFoundException.class, () -> personService.getPersonByOib("15756478462"));
    }

    @Test
    void updatePersonShouldSuccessfullyUpdateAPerson() {
        when(personRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(personRepository.findByOib(any()))
                .thenReturn(Optional.ofNullable(PersonMapper.INSTANCE.mapTo(person)));

        Person updatedPerson = personService.updatePerson(personToUpdate);

        verify(personRepository).save(any());

        assertThat(updatedPerson).isEqualTo(personToUpdate);
    }

    @Test
    void deletePersonShouldRemovePersonFromDb() {
        when(personRepository.findPersonIdByOib(any())).thenReturn(Optional.of(1L));
        personService.deletePersonByOib("15756478462");
        verify(personRepository).deleteByOib("15756478462");
    }

    @Test
    void deletePersonShouldThrowPersonNotFoundException() {
        when(personRepository.findPersonIdByOib(any())).thenReturn(Optional.empty());
        assertThrows(PersonNotFoundException.class, () -> personService.deletePersonByOib("15756478462"));
    }
}

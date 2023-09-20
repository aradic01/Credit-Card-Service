package hr.rba.creditcardservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.rba.creditcardservice.helper.*;
import hr.rba.creditcardservice.openapi.model.Person;
import hr.rba.creditcardservice.exception.PersonAlreadyExistsException;
import hr.rba.creditcardservice.exception.PersonNotFoundException;
import hr.rba.creditcardservice.service.contract.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.*;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    private static SimpleGrantedAuthority userRoleAuthority;
    private static SimpleGrantedAuthority adminRoleAuthority;

    private static Person person;
    private static Person updatedPerson;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void init() {

        adminRoleAuthority = AuthorityFactory.getAuthority("ADMIN");

        userRoleAuthority = AuthorityFactory.getAuthority("USER");

        person = new Person(
                "15756478462",
                "John",
                "Doe",
                Person.StatusEnum.CLIENT
        );

        updatedPerson = new Person(
                "15756478462",
                "Jane",
                "Doe",
                Person.StatusEnum.CLIENT
        );

        objectMapper = new ObjectMapper();
    }

    @Test
    void getPersonByOibShouldReturn401Unauthorized() throws Exception {
        this.mvc.perform(get("/person/15756478462"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPersonByOibShouldReturn403ForbiddenForRetrievingWithRoleUser() throws Exception {
        System.out.println(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")));

        this.mvc.perform(get("/person/15756467893")
                        .with(jwt().authorities(userRoleAuthority)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void createPersonShouldReturnOkWithNewlyCreatedPerson() throws Exception {
        when(personService.createPerson(person)).thenReturn(person);

        this.mvc.perform(put("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person))
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(person)));
    }

    @Test
    void createPersonShouldReturn404() throws Exception {
        when(personService.createPerson(person)).thenThrow(PersonNotFoundException.class);

        this.mvc.perform(put("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person))
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createPersonShouldReturn409() throws Exception {
        when(personService.createPerson(person)).thenThrow(PersonAlreadyExistsException.class);

        this.mvc.perform(put("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person))
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getPersonByOibShouldReturnOkWithRequestedPersonObject() throws Exception {
        when(personService.getPersonByOib(any(String.class))).thenReturn(person);

        this.mvc.perform(get("/person/15756478462")
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void getPersonByOibShouldReturn404() throws Exception {
        when(personService.getPersonByOib(any(String.class))).thenThrow(PersonNotFoundException.class);

        this.mvc.perform(get("/person/15756478462")
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePersonShouldReturnOkWithUpdatedPersonObject() throws Exception {
        when(personService.updatePerson(any(Person.class))).thenReturn(updatedPerson);

        this.mvc.perform(patch("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person))
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(objectMapper.writeValueAsString(updatedPerson)));

    }

    @Test
    void updatePersonShouldReturn404() throws Exception {
        when(personService.updatePerson(any(Person.class))).thenThrow(PersonNotFoundException.class);

        this.mvc.perform(patch("/person")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(person))
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePersonShouldReturnOk() throws Exception {
        this.mvc.perform(delete("/person/15756478462")
                        .with(jwt().authorities(adminRoleAuthority)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

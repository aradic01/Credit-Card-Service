package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.common.FileDescriptor;
import hr.rba.creditcardservice.exception.PersonNotFoundException;
import hr.rba.creditcardservice.jpa.entity.person.PersonEntity;
import hr.rba.creditcardservice.jpa.entity.person.PersonStatus;
import hr.rba.creditcardservice.jpa.repository.FileRepository;
import hr.rba.creditcardservice.service.contract.FileService;
import hr.rba.creditcardservice.service.contract.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FileServiceTests {

    @Autowired
    private FileService fileService;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private PersonService personService;

    private static PersonEntity person;

    @BeforeAll
    static void init() {
        person = new PersonEntity();
        person.setOib("15756478462");
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setStatus(PersonStatus.CLIENT);
        person.setFiles(new HashSet<>());
    }

    @Test
    void generateFileShouldThrowPersonNotFoundException() {
        when(personService.getPersonEntityByOib(any())).thenThrow(PersonNotFoundException.class);
        assertThrows(PersonNotFoundException.class, () -> fileService.generateFile("15756478462"));
    }

    @Test
    void generateFileShouldGenerateNewFileForAPerson() throws Exception {
        when(personService.getPersonEntityByOib(any())).thenReturn(person);
        when(fileRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(fileRepository.saveAll(any()))
                .thenAnswer(i -> Arrays.stream(i.getArguments()).collect(Collectors.toList()));

        FileDescriptor fileDescriptor = fileService.generateFile("15756478462");
        String generatedFilePath = "generatedFiles/" + fileDescriptor.name();
        File generatedFile = new File(generatedFilePath);
        String generatedFileContent = Files.readString(Paths.get(generatedFilePath));

        String expectedGeneratedFileContent = person.getOib() + "," +
                person.getFirstName() + "," +
                person.getLastName() + "," +
                person.getStatus();

        assertThat(fileDescriptor.name()).contains("15756478462");
        assertTrue(generatedFile.exists());
        assertThat(generatedFileContent).isEqualTo(expectedGeneratedFileContent);

        assertTrue(generatedFile.delete());
    }
}

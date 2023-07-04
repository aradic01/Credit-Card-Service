package hr.rba.creditcardservice.service;

import hr.rba.creditcardservice.common.FileDescriptor;
import hr.rba.creditcardservice.jpa.entity.PersonEntity;
import hr.rba.creditcardservice.jpa.entity.FileEntity;
import hr.rba.creditcardservice.jpa.entity.FileStatus;
import hr.rba.creditcardservice.jpa.repository.FileRepository;
import hr.rba.creditcardservice.service.contract.FileService;
import hr.rba.creditcardservice.service.contract.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final PersonService personService;
    private final FileRepository fileRepository;

    public FileServiceImpl(PersonService personService, FileRepository fileRepository) {
        this.personService = personService;
        this.fileRepository = fileRepository;
    }

    @Override
    @Transactional
    public FileDescriptor generateFile(String personOib) throws IOException {
        PersonEntity person = personService.getPersonEntityByOib(personOib);

        updateActiveFilesStatus(person);

        String content = generateFileContent(person);
        String fileName = person.getOib() + "_" + System.currentTimeMillis() + ".txt";

        Path filePath = getFilePath(fileName);
        Files.writeString(filePath, content, StandardOpenOption.CREATE);

        saveFileInfoToDb(person, fileName);

        return new FileDescriptor(fileName, content.getBytes(StandardCharsets.UTF_8));
    }

    private void updateActiveFilesStatus(PersonEntity person) {
        Set<FileEntity> filesToUpdate = person.getFiles()
                .stream()
                .filter(f -> f.getStatus().equals(FileStatus.ACTIVE))
                .map(f -> {
                    f.setStatus(FileStatus.INACTIVE);
                    return f;
                })
                .collect(Collectors.toSet());

        fileRepository.saveAll(filesToUpdate);
    }

    private String generateFileContent(PersonEntity person) {
        StringBuilder line = new StringBuilder();
        line
            .append(person.getOib()).append(",")
            .append(person.getFirstName()).append(",")
            .append(person.getLastName()).append(",")
            .append(person.getStatus());

        return line.toString();
    }

    private Path getFilePath(String fileName) throws IOException {
        Files.createDirectories(Paths.get("generatedFiles"));
        return Paths.get("generatedFiles" + File.separator + fileName);
    }

    private void saveFileInfoToDb(PersonEntity person, String name) {
        FileEntity fileEntity = new FileEntity();

        fileEntity.setFileName(name);
        fileEntity.setStatus(FileStatus.ACTIVE);
        fileEntity.setPerson(person);

        fileRepository.save(fileEntity);
    }
}

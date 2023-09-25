package hr.rba.creditcardservice.api.controller;

import hr.rba.creditcardservice.common.FileDescriptor;
import hr.rba.creditcardservice.openapi.api.FileApi;
import hr.rba.creditcardservice.service.contract.FileService;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
public class FileController implements FileApi {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Resource> generateCardTemplateByOib(String personOib) {

        FileDescriptor file = fileService.generateFile(personOib);
        ByteArrayResource resource = new ByteArrayResource(file.content());

        return ResponseEntity.ok()
                .contentLength(file.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF8''" + file.name())
                .body(resource);
    }
}

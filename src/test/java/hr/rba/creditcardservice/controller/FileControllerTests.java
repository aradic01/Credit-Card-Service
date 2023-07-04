package hr.rba.creditcardservice.controller;

import hr.rba.creditcardservice.common.FileDescriptor;
import hr.rba.creditcardservice.exception.PersonNotFoundException;
import hr.rba.creditcardservice.service.contract.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;

    @Test
    void generateCardTemplateByOibShouldReturn404() throws Exception {
        when(fileService.generateFile(any(String.class))).thenThrow(PersonNotFoundException.class);

        this.mvc.perform(get("/file/15756478462"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void generateCardTemplateByOibShouldReturnOKAndGeneratedFile() throws Exception {
        byte[] file = Files.readAllBytes(Paths.get("src/test/resources/03156791673_1688369282229.txt"));

        when(fileService.generateFile(any(String.class))).thenReturn(new FileDescriptor("03156791673_1688369282229.txt", file));

        this.mvc.perform(get("/file/15756478462"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/octet-stream"))
                .andExpect(content().bytes(file));
    }
}

package hr.rba.creditcardservice.service.contract;

import hr.rba.creditcardservice.common.FileDescriptor;

import java.io.IOException;

public interface FileService {
    FileDescriptor generateFile(String personOib) throws IOException;
}

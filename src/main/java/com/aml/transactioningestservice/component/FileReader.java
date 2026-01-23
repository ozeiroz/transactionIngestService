package com.aml.transactioningestservice.component;

import com.aml.transactioningestservice.dto.TransactionInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class FileReader {

    private final ObjectMapper mapper;

    public FileReader() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public Stream<TransactionInput> read(Path path) throws IOException {
        return Files.lines(path).map(line -> {
            try {
                return mapper.readValue(line, TransactionInput.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse JSON line: " + line, e);
            }
        });
    }
}

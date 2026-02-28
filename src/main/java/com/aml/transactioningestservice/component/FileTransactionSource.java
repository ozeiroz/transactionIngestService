package com.aml.transactioningestservice.component;

import com.aml.transactioningestservice.model.TransactionInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileTransactionSource implements TransactionSource {

    private final ObjectMapper mapper;
    private final Path directoryPath;

    public FileTransactionSource(@Value("${ingest.file.path}") String directoryPath) {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.directoryPath = Path.of(directoryPath);
    }

    @Override
    public Stream<TransactionInput> getTransactions() {
        try {
            log.info("Reading JSON files from directory: {}", directoryPath);

            if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
                log.error("Directory does not exist or is not a directory: {}", directoryPath);
                return Stream.empty();
            }

            return Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .peek(path -> log.debug("Processing file: {}", path))
                    .flatMap(this::readFileSafe);

        } catch (IOException e) {
            log.error("Error reading files from directory: {}", directoryPath, e);
            return Stream.empty();
        }
    }

    private Stream<TransactionInput> readFileSafe(Path filePath) {
        try {
            return Files.lines(filePath)
                    .map(line -> {
                        try {
                            return mapper.readValue(line, TransactionInput.class);
                        } catch (IOException e) {
                            log.error("Failed to parse JSON line in file {}: {}", filePath, line, e);
                            return null;
                        }
                    })
                    .filter(transaction -> transaction != null);
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
            return Stream.empty();
        }
    }
}
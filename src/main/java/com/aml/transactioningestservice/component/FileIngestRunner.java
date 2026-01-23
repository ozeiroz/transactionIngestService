package com.aml.transactioningestservice.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class FileIngestRunner implements CommandLineRunner {
    private final FileReader fileReader; //TODO - вынос в интерфейс
    private final TransactionIngestProcessor transactionIngestProcessor;

    @Value("${ingest.file.path}") //TODO - вынос в интерфейс
    private Path filePath;

    @Override
    public void run(String... args) throws IOException {
        fileReader.read(filePath).forEach(transactionIngestProcessor::ingest);
    }
}

package com.aml.transactioningestservice.component;

import com.aml.transactioningestservice.dto.TransactionInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionWorker {
    private final TransactionIngestProcessor transactionIngestProcessor;
    private final TransactionSource transactionSource;

    @Scheduled(fixedDelayString = "${ingest.schedule.fixedDelay:500}")
    public void process() {
        log.info("Starting transaction processing at {}", Instant.now());

        AtomicInteger processedCount = new AtomicInteger(0);

        try (var transactionStream = transactionSource.getTransactions()) {
            transactionStream.forEach(transaction -> {
                try {
                    transactionIngestProcessor.ingest(transaction);
                    processedCount.incrementAndGet();
                } catch (Exception e) {
                    log.error("Error processing transaction: {}", transaction, e);
                }
            });
        }

        log.info("Transaction processing completed. Processed {} transactions", processedCount.get());
    }
}
package com.aml.transactioningestservice.component;

import com.aml.transactioningestservice.model.TransactionInput;
import com.aml.transactioningestservice.model.AmlTransactionRawEvent;
import com.aml.transactioningestservice.model.EventMetadata;
import com.aml.transactioningestservice.model.IngestSource;
import com.aml.transactioningestservice.model.TransactionPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionIngestProcessor {
    private final KafkaTemplate<String, AmlTransactionRawEvent> kafkaTemplate;

    @Value("${kafka.topics.transactions.raw}")
    private String topic;

    public void ingest(TransactionInput transaction) {
        UUID transactionId = UUID.randomUUID();

        TransactionPayload payload = new TransactionPayload(
                transactionId,
                transaction.getExternalTransactionId(),
                transaction.getUserId(),
                transaction.getFullName(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getMerchantId(),
                transaction.getCountry(),
                transaction.getTimestamp()
        );

        EventMetadata metadata = new EventMetadata(
                IngestSource.FILE_INGEST,
                Instant.now(),
                "v1",
                "local"
        ); //TODO - change hardcoded file_ingest

        AmlTransactionRawEvent rawEvent = new AmlTransactionRawEvent(payload, metadata);

        kafkaTemplate.send(topic, transaction.getExternalTransactionId(), rawEvent);
    }
}

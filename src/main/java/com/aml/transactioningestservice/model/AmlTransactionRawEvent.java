package com.aml.transactioningestservice.model;

public record AmlTransactionRawEvent(
        TransactionPayload payload,
        EventMetadata metadata
) {}

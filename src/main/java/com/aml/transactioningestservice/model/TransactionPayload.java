package com.aml.transactioningestservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionPayload(
    UUID transactionId,
    String externalTransactionId,
    String userId,
    String fullName,
    BigDecimal amount,
    String currency,
    String merchantId,
    String country,
    Instant occuredAt
) {}

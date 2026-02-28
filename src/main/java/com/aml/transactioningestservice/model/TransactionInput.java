package com.aml.transactioningestservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInput {
    private String externalTransactionId;
    private String userId;
    private String fullName;
    private String currency;
    private String merchantId;
    private String country;
    private BigDecimal amount;
    private Instant timestamp;
}
package com.aml.transactioningestservice.component;

import com.aml.transactioningestservice.dto.TransactionInput;

import java.util.stream.Stream;

public interface TransactionSource {
    Stream<TransactionInput> getTransactions();
}

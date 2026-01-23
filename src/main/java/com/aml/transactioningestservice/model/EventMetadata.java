package com.aml.transactioningestservice.model;

import java.time.Instant;

public record EventMetadata(
        IngestSource source,
        Instant ingestedAt,
        String schemaVersion,
        String environment
){}

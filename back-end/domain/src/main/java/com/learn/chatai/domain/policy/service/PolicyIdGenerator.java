package com.learn.chatai.domain.policy.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Formats the human-traceable IDs required by root CLAUDE.md's Policy Code System
 * (e.g. {@code POLICY-DOC-20260710-A1B2C3D4}).
 */
public final class PolicyIdGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private PolicyIdGenerator() {
    }

    public static String newDocumentId() {
        return "POLICY-DOC-%s-%s".formatted(today(), shortId());
    }

    public static String newRevisionId(String documentId) {
        return "REVISION-%s-%s".formatted(documentId, shortId());
    }

    private static String today() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    private static String shortId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

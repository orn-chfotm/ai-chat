-- Generic async job ledger (PRD 08). Reused by every job type
-- (POLICY_UPLOAD, PDF_EXTRACTION, POLICY_VECTORIZATION, CHAT_COMPLETION, PROMPT_UPDATE)
-- instead of a separate table per feature.
CREATE TABLE job (
    job_id          VARCHAR(64) PRIMARY KEY,
    job_type        VARCHAR(50) NOT NULL,
    target_id       VARCHAR(64),
    status          VARCHAR(30) NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    queued_at       TIMESTAMP,
    started_at      TIMESTAMP,
    completed_at    TIMESTAMP,
    duration_ms     BIGINT,
    failure_reason  TEXT,
    created_by      VARCHAR(100),
    metrics         JSONB
);

CREATE INDEX idx_job_target_id ON job (target_id);
CREATE INDEX idx_job_type_status ON job (job_type, status);

-- Uploaded regulation document (PRD 01, PRD 02 ledger).
CREATE TABLE policy_document (
    document_id         VARCHAR(64) PRIMARY KEY,
    policy_code          VARCHAR(100) NOT NULL,
    original_file_name   VARCHAR(255) NOT NULL,
    storage_key           VARCHAR(500) NOT NULL,
    content_type          VARCHAR(100),
    size_bytes             BIGINT,
    checksum               VARCHAR(128),
    status                  VARCHAR(30) NOT NULL,
    created_at              TIMESTAMP NOT NULL
);

CREATE INDEX idx_policy_document_policy_code ON policy_document (policy_code);

-- Applicable version unit (PRD 02). Only one ACTIVE version per policy_code at a time;
-- that invariant is enforced in application code inside a transaction, not by a DB constraint,
-- since the transition also has to flip the previous ACTIVE version to ARCHIVED atomically.
CREATE TABLE policy_version (
    version_id       VARCHAR(64) PRIMARY KEY,
    policy_code       VARCHAR(100) NOT NULL,
    document_id        VARCHAR(64) NOT NULL REFERENCES policy_document (document_id),
    version_number      INT NOT NULL,
    status               VARCHAR(30) NOT NULL,
    effective_from       DATE,
    effective_to         DATE,
    created_at            TIMESTAMP NOT NULL
);

CREATE INDEX idx_policy_version_code_status ON policy_version (policy_code, status);
CREATE INDEX idx_policy_version_document_id ON policy_version (document_id);

-- Extracted / admin-edited text (PRD 03). EXTRACTED rows are the raw conversion output,
-- EDITED rows are the admin-reviewed text that vectorization will use as source-of-truth.
CREATE TABLE policy_text_revision (
    revision_id                 VARCHAR(64) PRIMARY KEY,
    document_id                  VARCHAR(64) NOT NULL REFERENCES policy_document (document_id),
    version_id                    VARCHAR(64) REFERENCES policy_version (version_id),
    revision_type                  VARCHAR(20) NOT NULL,
    content                         TEXT,
    extracted_page_count            INT,
    extracted_character_count       INT,
    created_at                       TIMESTAMP NOT NULL
);

CREATE INDEX idx_policy_text_revision_document_id ON policy_text_revision (document_id);

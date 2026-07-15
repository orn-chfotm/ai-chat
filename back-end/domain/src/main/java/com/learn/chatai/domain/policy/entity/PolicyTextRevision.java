package com.learn.chatai.domain.policy.entity;

import com.learn.chatai.core.BaseTimeEntity;
import com.learn.chatai.domain.policy.enums.PolicyTextRevisionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "policy_text_revision")
public class PolicyTextRevision extends BaseTimeEntity {

    @Id
    @Column(name = "revision_id", length = 64)
    private String revisionId;

    @Column(name = "document_id", nullable = false, length = 64)
    private String documentId;

    @Column(name = "version_id", length = 64)
    private String versionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "revision_type", nullable = false, length = 20)
    private PolicyTextRevisionType revisionType;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "content")
    private String content;

    @Column(name = "extracted_page_count")
    private Integer extractedPageCount;

    @Column(name = "extracted_character_count")
    private Integer extractedCharacterCount;

    private PolicyTextRevision(String revisionId, String documentId, PolicyTextRevisionType revisionType,
                                String content, Integer extractedPageCount, Integer extractedCharacterCount) {
        this.revisionId = revisionId;
        this.documentId = documentId;
        this.revisionType = revisionType;
        this.content = content;
        this.extractedPageCount = extractedPageCount;
        this.extractedCharacterCount = extractedCharacterCount;
    }

    public static PolicyTextRevision extracted(String revisionId, String documentId, String content,
                                                 int extractedPageCount, int extractedCharacterCount) {
        return new PolicyTextRevision(revisionId, documentId, PolicyTextRevisionType.EXTRACTED,
                content, extractedPageCount, extractedCharacterCount);
    }

    public static PolicyTextRevision edited(String revisionId, String documentId, String content) {
        return new PolicyTextRevision(revisionId, documentId, PolicyTextRevisionType.EDITED,
                content, null, null);
    }
}

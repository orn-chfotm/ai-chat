package com.learn.chatai.domain.policy.service;

import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.job.enums.JobType;
import com.learn.chatai.domain.job.service.JobService;
import com.learn.chatai.domain.policy.entity.PolicyDocument;
import com.learn.chatai.domain.policy.entity.PolicyTextRevision;
import com.learn.chatai.domain.policy.exception.PolicyErrorCode;
import com.learn.chatai.domain.policy.exception.PolicyException;
import com.learn.chatai.domain.policy.port.FileStoragePort;
import com.learn.chatai.domain.policy.port.PolicyDocumentRepository;
import com.learn.chatai.domain.policy.port.PolicyTextRevisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Transactional
public class PolicyIngestionService {

    private final PolicyDocumentRepository documentRepository;
    private final PolicyTextRevisionRepository textRevisionRepository;
    private final FileStoragePort fileStoragePort;
    private final JobService jobService;

    /**
     * Saves the file and creates the document + extraction job in one transaction.
     * The caller (controller) triggers the async extraction only after this returns,
     * so the extraction worker never races an uncommitted document/job row.
     */
    public IngestionResult upload(String policyCode, String originalFileName, String contentType,
                                   InputStream content) throws IOException {
        FileStoragePort.StoredFile stored = fileStoragePort.store(originalFileName, content);

        PolicyDocument document = PolicyDocument.uploaded(
                PolicyIdGenerator.newDocumentId(), policyCode, originalFileName,
                stored.storageKey(), contentType, stored.sizeBytes(), stored.checksum());
        documentRepository.save(document);

        Job job = jobService.create(JobType.PDF_EXTRACTION, document.getDocumentId(), null);

        return new IngestionResult(document, job);
    }

    @Transactional(readOnly = true)
    public PolicyDocumentDetail getDocument(String documentId) {
        PolicyDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new PolicyException(PolicyErrorCode.DOCUMENT_NOT_FOUND, "문서를 찾을 수 없습니다: " + documentId));
        PolicyTextRevision latestRevision = textRevisionRepository.findLatestByDocumentId(documentId)
                .orElse(null);
        return new PolicyDocumentDetail(document, latestRevision);
    }

    public record IngestionResult(PolicyDocument document, Job job) {
    }

    public record PolicyDocumentDetail(PolicyDocument document, PolicyTextRevision latestRevision) {
    }
}

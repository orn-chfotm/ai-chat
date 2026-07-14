package com.learn.chatai.api.admin.policy;

import com.learn.chatai.api.admin.job.JobAdminService;
import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.job.enums.JobType;
import com.learn.chatai.domain.policy.entity.PolicyDocument;
import com.learn.chatai.domain.policy.entity.PolicyTextRevision;
import com.learn.chatai.domain.policy.exception.PolicyErrorCode;
import com.learn.chatai.domain.policy.exception.PolicyException;
import com.learn.chatai.domain.policy.port.FileStoragePort;
import com.learn.chatai.domain.policy.port.PdfTextExtractorPort;
import com.learn.chatai.domain.policy.port.PolicyDocumentRepository;
import com.learn.chatai.domain.policy.port.PolicyTextRevisionRepository;
import com.learn.chatai.domain.policy.service.PolicyIdGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

/**
 * API service (03-api-modules.md): combines domain entities/ports and infra adapters for the
 * policy upload + PDF extraction flow. This orchestration used to live in {@code domain} as
 * PolicyIngestionService/PolicyExtractionService; per 04-domain-module.md domain must not DI
 * repository/file/PDF ports itself, so the flow moved here.
 */
@Service
@RequiredArgsConstructor
public class PolicyAdminService {

    private static final Logger log = LoggerFactory.getLogger(PolicyAdminService.class);

    private final PolicyDocumentRepository documentRepository;
    private final PolicyTextRevisionRepository textRevisionRepository;
    private final FileStoragePort fileStoragePort;
    private final PdfTextExtractorPort pdfTextExtractorPort;
    private final JobAdminService jobAdminService;

    /**
     * Saves the file and creates the document + extraction job in one transaction.
     * The caller (controller) triggers the async extraction only after this returns,
     * so the extraction worker never races an uncommitted document/job row.
     */
    @Transactional
    public IngestionResult upload(String policyCode, String originalFileName, String contentType,
                                   InputStream content) throws IOException {
        FileStoragePort.StoredFile stored = fileStoragePort.store(originalFileName, content);

        PolicyDocument document = PolicyDocument.uploaded(
                PolicyIdGenerator.newDocumentId(), policyCode, originalFileName,
                stored.storageKey(), contentType, stored.sizeBytes(), stored.checksum());
        documentRepository.save(document);

        Job job = jobAdminService.create(JobType.PDF_EXTRACTION, document.getDocumentId(), null);

        return new IngestionResult(document, job);
    }

    @Transactional(readOnly = true)
    public Page<PolicyDocument> getDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public PolicyDocumentDetail getDocument(String documentId) {
        PolicyDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new PolicyException(PolicyErrorCode.DOCUMENT_NOT_FOUND, "문서를 찾을 수 없습니다: " + documentId));
        PolicyTextRevision latestRevision = textRevisionRepository.findLatestByDocumentId(documentId)
                .orElse(null);
        return new PolicyDocumentDetail(document, latestRevision);
    }

    /**
     * Saves an admin-edited text revision (PRD 03). Appends a new EDITED revision — the EXTRACTED
     * original and any prior edits are never overwritten — and moves the document to EDITING.
     * {@code markEditing()} guards the status, so editing a document that hasn't finished
     * extraction (or is already applied) fails before anything is written.
     */
    @Transactional
    public PolicyDocumentDetail editText(String documentId, String content) {
        PolicyDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new PolicyException(PolicyErrorCode.DOCUMENT_NOT_FOUND, "문서를 찾을 수 없습니다: " + documentId));
        document.markEditing();
        documentRepository.save(document);

        PolicyTextRevision revision = PolicyTextRevision.edited(
                PolicyIdGenerator.newRevisionId(documentId), documentId, content);
        textRevisionRepository.save(revision);

        return new PolicyDocumentDetail(document, revision);
    }

    /**
     * Async worker for PRD 03 (PDF text extraction). {@code @Async} + {@code @Transactional} live
     * on the same method deliberately: splitting them across two methods in this class would hit
     * Spring's self-invocation limitation (an internal call bypasses the proxy, so @Transactional
     * would silently not apply). Failures are caught inside the transaction so the FAILED status
     * and failure reason are what actually commits, instead of rolling back everything.
     */
    @Async("jobTaskExecutor")
    @Transactional
    public void extractAsync(String documentId, String jobId) {
        jobAdminService.markRunning(jobId);

        PolicyDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalStateException("문서를 찾을 수 없습니다: " + documentId));
        document.markExtracting();
        documentRepository.save(document);

        try (InputStream content = fileStoragePort.read(document.getStorageKey())) {
            PdfTextExtractorPort.ExtractedText extracted = pdfTextExtractorPort.extract(content);

            PolicyTextRevision revision = PolicyTextRevision.extracted(
                    PolicyIdGenerator.newRevisionId(documentId), documentId,
                    extracted.content(), extracted.pageCount(), extracted.characterCount());
            textRevisionRepository.save(revision);

            document.markReviewing();
            documentRepository.save(document);

            jobAdminService.putMetric(jobId, "extractedPageCount", extracted.pageCount());
            jobAdminService.putMetric(jobId, "extractedCharacterCount", extracted.characterCount());
            jobAdminService.markCompleted(jobId);
        } catch (Exception ex) {
            log.error("PDF extraction failed for document {}", documentId, ex);
            document.markFailed();
            documentRepository.save(document);
            jobAdminService.markFailed(jobId, ex.getMessage());
        }
    }

    public record IngestionResult(PolicyDocument document, Job job) {
    }

    public record PolicyDocumentDetail(PolicyDocument document, PolicyTextRevision latestRevision) {
    }
}

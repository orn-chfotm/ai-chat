package com.learn.chatai.domain.policy.service;

import com.learn.chatai.domain.job.service.JobService;
import com.learn.chatai.domain.policy.entity.PolicyDocument;
import com.learn.chatai.domain.policy.entity.PolicyTextRevision;
import com.learn.chatai.domain.policy.port.FileStoragePort;
import com.learn.chatai.domain.policy.port.PdfTextExtractorPort;
import com.learn.chatai.domain.policy.port.PolicyDocumentRepository;
import com.learn.chatai.domain.policy.port.PolicyTextRevisionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

/**
 * Async worker for PRD 03 (PDF text extraction). {@code @Async} + {@code @Transactional} live
 * on the same method deliberately: splitting them across two methods in this class would hit
 * Spring's self-invocation limitation (an internal call bypasses the proxy, so @Transactional
 * would silently not apply). Failures are caught inside the transaction so the FAILED status
 * and failure reason are what actually commits, instead of rolling back everything.
 */
@Service
@RequiredArgsConstructor
public class PolicyExtractionService {

    private static final Logger log = LoggerFactory.getLogger(PolicyExtractionService.class);

    private final PolicyDocumentRepository documentRepository;
    private final PolicyTextRevisionRepository textRevisionRepository;
    private final FileStoragePort fileStoragePort;
    private final PdfTextExtractorPort pdfTextExtractorPort;
    private final JobService jobService;

    @Async("jobTaskExecutor")
    @Transactional
    public void extractAsync(String documentId, String jobId) {
        jobService.markRunning(jobId);

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

            jobService.putMetric(jobId, "extractedPageCount", extracted.pageCount());
            jobService.putMetric(jobId, "extractedCharacterCount", extracted.characterCount());
            jobService.markCompleted(jobId);
        } catch (Exception ex) {
            log.error("PDF extraction failed for document {}", documentId, ex);
            document.markFailed();
            documentRepository.save(document);
            jobService.markFailed(jobId, ex.getMessage());
        }
    }
}

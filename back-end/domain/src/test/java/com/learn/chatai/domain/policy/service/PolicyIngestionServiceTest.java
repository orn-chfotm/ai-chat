package com.learn.chatai.domain.policy.service;

import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.job.enums.JobType;
import com.learn.chatai.domain.job.service.JobService;
import com.learn.chatai.domain.policy.enums.PolicyLifecycleStatus;
import com.learn.chatai.domain.policy.exception.PolicyException;
import com.learn.chatai.domain.policy.port.FileStoragePort;
import com.learn.chatai.domain.policy.port.PolicyDocumentRepository;
import com.learn.chatai.domain.policy.port.PolicyTextRevisionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyIngestionServiceTest {

    @Mock
    private PolicyDocumentRepository documentRepository;
    @Mock
    private PolicyTextRevisionRepository textRevisionRepository;
    @Mock
    private FileStoragePort fileStoragePort;
    @Mock
    private JobService jobService;

    private PolicyIngestionService service;

    @BeforeEach
    void setUp() {
        service = new PolicyIngestionService(documentRepository, textRevisionRepository, fileStoragePort, jobService);
    }

    @Test
    void uploadStoresFileAndCreatesDocumentAndExtractionJob() throws Exception {
        InputStream content = new ByteArrayInputStream("dummy".getBytes());
        when(fileStoragePort.store(eq("policy.pdf"), any()))
                .thenReturn(new FileStoragePort.StoredFile("storage-key", 5L, "checksum"));
        when(documentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Job job = Job.queued(JobType.PDF_EXTRACTION, "doc-id", null);
        when(jobService.create(eq(JobType.PDF_EXTRACTION), anyString(), isNull())).thenReturn(job);

        PolicyIngestionService.IngestionResult result =
                service.upload("INTERNAL_POLICY", "policy.pdf", "application/pdf", content);

        assertThat(result.document().getPolicyCode()).isEqualTo("INTERNAL_POLICY");
        assertThat(result.document().getStatus()).isEqualTo(PolicyLifecycleStatus.UPLOADED);
        assertThat(result.job()).isEqualTo(job);
        verify(documentRepository).save(any());
    }

    @Test
    void getDocumentThrowsWhenMissing() {
        when(documentRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDocument("missing"))
                .isInstanceOf(PolicyException.class);
    }
}

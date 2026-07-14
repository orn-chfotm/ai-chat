package com.learn.chatai.api.admin.policy;

import com.learn.chatai.api.admin.policy.dto.PolicyDocumentResponseDto;
import com.learn.chatai.api.admin.policy.dto.PolicyUploadResponseDto;
import com.learn.chatai.core.response.SuccessResponse;
import com.learn.chatai.domain.policy.service.PolicyExtractionService;
import com.learn.chatai.domain.policy.service.PolicyIngestionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api-admin/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyIngestionService ingestionService;
    private final PolicyExtractionService extractionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<PolicyUploadResponseDto>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("policyCode") @NotBlank String policyCode) throws IOException {
        PolicyIngestionService.IngestionResult result = ingestionService.upload(
                policyCode, file.getOriginalFilename(), file.getContentType(), file.getInputStream());

        // Fired only after the upload transaction above has committed, so the async worker
        // never reads a document/job row that isn't visible to its own transaction yet.
        extractionService.extractAsync(result.document().getDocumentId(), result.job().getJobId());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(SuccessResponse.of(PolicyUploadResponseDto.of(result.document(), result.job())));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<SuccessResponse<PolicyDocumentResponseDto>> getDocument(@PathVariable String documentId) {
        return ResponseEntity.ok(SuccessResponse.of(PolicyDocumentResponseDto.from(ingestionService.getDocument(documentId))));
    }
}

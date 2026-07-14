package com.learn.chatai.api.admin.policy.dto;

import com.learn.chatai.domain.policy.entity.PolicyDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "규정 문서 목록 응답 (페이지네이션)")
public record PolicyDocumentListResponseDto(
        @Schema(description = "문서 목록")
        List<Item> items,

        @Schema(description = "현재 페이지 (0부터 시작)", example = "0")
        int page,

        @Schema(description = "페이지 크기", example = "20")
        int size,

        @Schema(description = "전체 문서 수", example = "42")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "3")
        int totalPages,

        @Schema(description = "다음 페이지 존재 여부")
        boolean hasNext
) {

    public static PolicyDocumentListResponseDto from(Page<PolicyDocument> page) {
        List<Item> items = page.getContent().stream().map(Item::of).toList();
        return new PolicyDocumentListResponseDto(
                items, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.hasNext());
    }

    @Schema(description = "규정 문서 목록 항목 요약")
    public record Item(
            @Schema(description = "문서 ID", example = "POLICY-DOC-20260710-A1B2C3D4")
            String documentId,

            @Schema(description = "관리자가 등록한 규정 코드", example = "INTERNAL_POLICY")
            String policyCode,

            @Schema(description = "업로드된 원본 파일명", example = "취업규칙.pdf")
            String originalFileName,

            @Schema(description = "현재 문서 상태 (Document Lifecycle)", example = "REVIEWING")
            String status,

            @Schema(description = "업로드 시각")
            LocalDateTime createdAt
    ) {

        static Item of(PolicyDocument document) {
            return new Item(
                    document.getDocumentId(),
                    document.getPolicyCode(),
                    document.getOriginalFileName(),
                    document.getStatus().name(),
                    document.getCreatedAt()
            );
        }
    }
}

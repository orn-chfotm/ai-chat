package com.learn.chatai.api.admin.policy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "추출 텍스트 수정 요청 (PRD 03). 관리자가 검수한 전체 텍스트를 저장한다.")
public record PolicyTextEditRequestDto(
        @Schema(description = "관리자가 수정한 규정 텍스트 전문. 저장 시 새 EDITED 이력으로 쌓이며 원문(EXTRACTED)은 보존된다.")
        @NotBlank
        String content
) {
}

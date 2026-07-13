package com.learn.chatai.domain.policy.port;

import java.io.IOException;
import java.io.InputStream;

/**
 * Port implemented by {@code infra.document}. Domain code depends on this interface only,
 * not on the concrete PDF library, per backend-rules "PDF 추출 구현은 infra.file 또는 infra.document에 둔다."
 */
public interface PdfTextExtractorPort {

    ExtractedText extract(InputStream pdfContent) throws IOException;

    record ExtractedText(String content, int pageCount, int characterCount) {
    }
}

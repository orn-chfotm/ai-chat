package com.learn.chatai.infra.document;

import com.learn.chatai.domain.policy.port.PdfTextExtractorPort;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfBoxTextExtractorAdapter implements PdfTextExtractorPort {

    @Override
    public ExtractedText extract(InputStream pdfContent) throws IOException {
        try (PDDocument document = PDDocument.load(pdfContent)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return new ExtractedText(text, document.getNumberOfPages(), text.length());
        }
    }
}

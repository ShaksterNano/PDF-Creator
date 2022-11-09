package io.github.shaksternano.pdfcreator.command;

import io.github.shaksternano.pdfcreator.Counter;
import io.github.shaksternano.pdfcreator.PDFLine;
import io.github.shaksternano.pdfcreator.PDFSettings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.util.List;

public enum ResetFontCommand implements PDFCommand {

    INSTANCE;

    @Override
    public PDPageContentStream execute(List<String> args, PDFLine currentLine, Counter totalHeight, float textAreaWidth, float textAreaHeight, float startX, float startY, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) {
        settings.setBold(false);
        settings.setItalic(false);
        return contentStream;
    }
}

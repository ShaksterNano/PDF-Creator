package io.github.shaksternano.pdfcreator.command;

import io.github.shaksternano.pdfcreator.Counter;
import io.github.shaksternano.pdfcreator.PDFLine;
import io.github.shaksternano.pdfcreator.PDFSettings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.util.List;

public enum SetJustifyCommand implements PDFCommand {

    JUSTIFY(true),
    NO_JUSTIFY(false);

    private final boolean justified;

    SetJustifyCommand(boolean justified) {
        this.justified = justified;
    }

    @Override
    public PDPageContentStream execute(List<String> args, PDFLine currentLine, Counter totalHeight, float textAreaWidth, float textAreaHeight, float startX, float startY, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) {
        settings.setJustified(justified);
        return contentStream;
    }
}

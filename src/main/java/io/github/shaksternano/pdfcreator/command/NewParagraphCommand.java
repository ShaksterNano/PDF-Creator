package io.github.shaksternano.pdfcreator.command;

import io.github.shaksternano.pdfcreator.Counter;
import io.github.shaksternano.pdfcreator.PDFCreator;
import io.github.shaksternano.pdfcreator.PDFLine;
import io.github.shaksternano.pdfcreator.PDFSettings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;

public enum NewParagraphCommand implements PDFCommand {

    INSTANCE;

    @Override
    public PDPageContentStream execute(List<String> args, PDFLine currentLine, Counter totalHeight, float textAreaWidth, float textAreaHeight, float startX, float startY, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) throws IOException {
        contentStream = PDFCreator.tryAddNewPage(currentLine, totalHeight, textAreaHeight, startX, startY, settings, document, contentStream);
        currentLine.write(contentStream, settings, textAreaWidth);
        totalHeight.add(currentLine.getHeight());
        currentLine.clear();
        for (int i = 0; i < 2; i++) {
            contentStream.newLine();
            totalHeight.add(settings.getLeadingRatio() * settings.getFontSize());
        }
        return contentStream;
    }
}

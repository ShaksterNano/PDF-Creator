package io.github.shaksternano.pdfcreator.command;

import io.github.shaksternano.pdfcreator.Counter;
import io.github.shaksternano.pdfcreator.PDFCreator;
import io.github.shaksternano.pdfcreator.PDFLine;
import io.github.shaksternano.pdfcreator.PDFSettings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;

public enum IndentCommand implements PDFCommand {

    INSTANCE;

    @Override
    public PDPageContentStream execute(List<String> args, PDFLine currentLine, Counter totalHeight, float textAreaWidth, float textAreaHeight, float startX, float startY, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) throws IOException {
        contentStream = PDFCreator.tryAddNewPage(currentLine, totalHeight, textAreaHeight, startX, startY, document, contentStream);
        currentLine.write(contentStream, settings, textAreaWidth);
        totalHeight.add(currentLine.getHeight());
        currentLine.clear();
        int indentWidth = 20;
        if (args.size() > 0) {
            try {
                indentWidth *= Integer.parseInt(args.get(0));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Indent width must be a number!");
            }
        }
        contentStream.newLineAtOffset(indentWidth, 0);
        settings.setIndent(indentWidth);
        return contentStream;
    }
}

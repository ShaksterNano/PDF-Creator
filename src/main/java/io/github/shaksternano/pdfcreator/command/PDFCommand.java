package io.github.shaksternano.pdfcreator.command;

import io.github.shaksternano.pdfcreator.Counter;
import io.github.shaksternano.pdfcreator.PDFLine;
import io.github.shaksternano.pdfcreator.PDFSettings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;

/**
 * Represents the command in the input file that
 * modifies the PDF formatting. Commands start with
 * "." in the input file.
 */
@FunctionalInterface
public interface PDFCommand {

    /**
     * Executes the command.
     *
     * @param args           Additional arguments.
     * @param currentLine    The current line being built.
     * @param totalHeight    The total height of the text on the current page.
     * @param textAreaWidth  The width of the text area.
     * @param textAreaHeight The height of the text area.
     * @param startX         The x-coordinate of the start of the text area.
     * @param startY         The y-coordinate of the start of the text area.
     * @param document       The document being written to.
     * @param contentStream  The content stream to write to.
     * @param settings       The settings to use.
     * @return The updated content stream. If a new page was added,
     * a new content stream is returned and the old one is closed.
     * Otherwise, the same content stream is returned.
     * @throws IOException if an I/O error occurs.
     */
    PDPageContentStream execute(List<String> args, PDFLine currentLine, Counter totalHeight, float textAreaWidth, float textAreaHeight, float startX, float startY, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) throws IOException;
}

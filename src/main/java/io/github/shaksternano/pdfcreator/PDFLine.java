package io.github.shaksternano.pdfcreator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents one line of text in a PDF.
 */
public class PDFLine {

    private final List<PDFWord> text = new ArrayList<>();
    private float width = 0;
    private float height = 0;
    private float spaceWidth = 0;

    public void addText(PDFWord text) throws IOException {
        this.text.add(text);
        width += text.getWidth();
        if (height == 0) {
            PDFont font = text.getFont();
            height = text.getFontSize() * font.getBoundingBox().getHeight() / 1000;
        }
        if (spaceWidth == 0) {
            PDFont font = text.getFont();
            spaceWidth = text.getFontSize() * font.getStringWidth(" ") / 1000;
        }
        width += spaceWidth;
    }

    /**
     * Writes the line to the PDF.
     *
     * @param contentStream the content stream to write to.
     * @param settings      The settings to use.
     * @param textAreaWidth The width of the text area.
     * @throws IOException if an I/O error occurs.
     */
    public void write(PDPageContentStream contentStream, PDFSettings settings, float textAreaWidth) throws IOException {
        contentStream.setLeading(settings.getLeadingRatio() * settings.getFontSize());
        float spaceWidth = this.spaceWidth;
        if (settings.isJustified()) {
            float freeSpace = textAreaWidth - settings.getIndent() - width;
            float extraSpace = freeSpace / Math.max(1, text.size() - 1);
            spaceWidth += extraSpace;
        }

        float offset = 0;
        boolean first = true;
        for (PDFWord text : text) {
            contentStream.setFont(text.getFont(), text.getFontSize());
            if (!first && Pattern.matches("\\p{Punct}", Character.toString(text.getContent().charAt(0)))) {
                contentStream.newLineAtOffset(-spaceWidth, 0);
                offset -= spaceWidth;
            }
            contentStream.showText(text.getContent());
            float textWidth = text.getWidth() + spaceWidth;
            contentStream.newLineAtOffset(textWidth, 0);
            offset += textWidth;
            first = false;
        }
        contentStream.newLineAtOffset(-offset, 0);
    }

    public void clear() {
        text.clear();
        width = 0;
        height = 0;
        spaceWidth = 0;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}

package io.github.shaksternano.pdfcreator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PDFLine {

    private final List<PDFText> text = new ArrayList<>();
    private float width = 0;
    private float height = -1;
    private float spaceWidth = -1;

    public void addText(PDFText text) throws IOException {
        this.text.add(text);
        width += text.getWidth();
        if (height == -1) {
            PDFont font = text.getFont();
            height = text.getFontSize() * font.getBoundingBox().getHeight() / 1000;
        }
        if (spaceWidth == -1) {
            PDFont font = text.getFont();
            spaceWidth = text.getFontSize() * font.getStringWidth(" ") / 1000;
        }
        width += spaceWidth;
    }

    public void write(PDPageContentStream contentStream, PDFSettings settings, float textAreaWidth) throws IOException {
        contentStream.setLeading(settings.getLeadingRatio() * settings.getFontSize());
        float spaceWidth = this.spaceWidth;
        if (settings.isJustified()) {
            float freeSpace = textAreaWidth - settings.getIndent() - width;
            float extraSpace = freeSpace / Math.max(1, text.size() - 1);
            spaceWidth += extraSpace;
        }

        float offset = 0;
        for (PDFText text : text) {
            contentStream.setFont(text.getFont(), text.getFontSize());
            if (Pattern.matches("\\p{Punct}", Character.toString(text.getContent().charAt(0)))) {
                contentStream.newLineAtOffset(-spaceWidth, 0);
            }
            contentStream.showText(text.getContent());
            float textWidth = text.getWidth() + spaceWidth;
            contentStream.newLineAtOffset(textWidth, 0);
            offset += textWidth;
        }
        contentStream.newLineAtOffset(-offset, 0);
    }

    public void clear() {
        text.clear();
        width = 0;
        height = -1;
        spaceWidth = -1;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}

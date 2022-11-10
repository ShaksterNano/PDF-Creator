package io.github.shaksternano.pdfcreator;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

/**
 * Represents one word in a PDF.
 */
public class PDFWord {

    private final String content;
    private final PDFont font;
    private final float fontSize;
    private final float width;

    public PDFWord(String content, PDFont font, float fontSize) throws IOException {
        this.content = content.trim();
        this.font = font;
        this.fontSize = fontSize;
        width = fontSize * font.getStringWidth(content) / 1000;
    }

    public String getContent() {
        return content;
    }

    public PDFont getFont() {
        return font;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getWidth() {
        return width;
    }
}

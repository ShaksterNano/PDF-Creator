package io.github.shaksternano.pdfcreator;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public class PDFText {

    private final String content;
    private final Standard14Fonts.FontName fontName;
    private final float fontSize;
    private final float width;

    public PDFText(String content, Standard14Fonts.FontName fontName, float fontSize) throws IOException {
        this.content = content.trim();
        this.fontName = fontName;
        this.fontSize = fontSize;
        PDFont font = new PDType1Font(fontName);
        width = fontSize * font.getStringWidth(content) / 1000;
    }

    public String getContent() {
        return content;
    }

    public Standard14Fonts.FontName getFontName() {
        return fontName;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getWidth() {
        return width;
    }
}

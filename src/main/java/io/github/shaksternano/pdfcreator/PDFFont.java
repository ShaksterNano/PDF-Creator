package io.github.shaksternano.pdfcreator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;

/**
 * Links the regular, bold, italic and bold
 * italic fonts together under one name.
 */
public enum PDFFont {

    HELVETICA(
            "io/github/shaksternano/pdfcreator/font/helvetica.ttf",
            "io/github/shaksternano/pdfcreator/font/helvetica-bold.ttf",
            "io/github/shaksternano/pdfcreator/font/helvetica-oblique.ttf",
            "io/github/shaksternano/pdfcreator/font/helvetica-bold-oblique.ttf"
    );

    private final String regularPath;
    private final String boldPath;
    private final String italicPath;
    private final String boldItalicPath;

    PDFFont(
            String regularPath,
            String boldPath,
            String italicPath,
            String boldItalicPath
    ) {
        this.regularPath = regularPath;
        this.boldPath = boldPath;
        this.italicPath = italicPath;
        this.boldItalicPath = boldItalicPath;
    }

    public PDFont createRegular(PDDocument document) throws IOException {
        return createFont(document, regularPath);
    }

    public PDFont createBold(PDDocument document) throws IOException {
        return createFont(document, boldPath);
    }

    public PDFont createItalic(PDDocument document) throws IOException {
        return createFont(document, italicPath);
    }

    public PDFont createBoldItalic(PDDocument document) throws IOException {
        return createFont(document, boldItalicPath);
    }

    private PDFont createFont(PDDocument document, String fontPath) throws IOException {
        try (InputStream fontStream = PDFFont.class.getClassLoader().getResourceAsStream(fontPath)) {
            return PDType0Font.load(document, fontStream);
        }
    }
}

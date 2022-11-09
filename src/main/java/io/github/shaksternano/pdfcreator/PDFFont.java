package io.github.shaksternano.pdfcreator;

import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public enum PDFFont {

    TIMES_ROMAN(
            Standard14Fonts.FontName.TIMES_ROMAN,
            Standard14Fonts.FontName.TIMES_BOLD,
            Standard14Fonts.FontName.TIMES_ITALIC,
            Standard14Fonts.FontName.TIMES_BOLD_ITALIC
    ),

    HELVETICA(
            Standard14Fonts.FontName.HELVETICA,
            Standard14Fonts.FontName.HELVETICA_BOLD,
            Standard14Fonts.FontName.HELVETICA_OBLIQUE,
            Standard14Fonts.FontName.HELVETICA_BOLD_OBLIQUE
    ),

    COURIER(
            Standard14Fonts.FontName.COURIER,
            Standard14Fonts.FontName.COURIER_BOLD,
            Standard14Fonts.FontName.COURIER_OBLIQUE,
            Standard14Fonts.FontName.COURIER_BOLD_OBLIQUE
    );

    private final Standard14Fonts.FontName regular;
    private final Standard14Fonts.FontName bold;
    private final Standard14Fonts.FontName italic;
    private final Standard14Fonts.FontName boldItalic;

    PDFFont(
            Standard14Fonts.FontName regular,
            Standard14Fonts.FontName bold,
            Standard14Fonts.FontName italic,
            Standard14Fonts.FontName boldItalic
    ) {
        this.regular = regular;
        this.bold = bold;
        this.italic = italic;
        this.boldItalic = boldItalic;
    }

    public Standard14Fonts.FontName getRegular() {
        return regular;
    }

    public Standard14Fonts.FontName getBold() {
        return bold;
    }

    public Standard14Fonts.FontName getItalic() {
        return italic;
    }

    public Standard14Fonts.FontName getBoldItalic() {
        return boldItalic;
    }
}

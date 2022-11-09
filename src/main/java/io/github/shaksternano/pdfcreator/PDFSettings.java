package io.github.shaksternano.pdfcreator;

public class PDFSettings {

    private boolean bold;
    private boolean italic;
    private boolean justified;
    private float indent;
    private float leadingRatio;
    private float fontSize = 12;

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isJustified() {
        return justified;
    }

    public float getIndent() {
        return indent;
    }

    public float getLeadingRatio() {
        return leadingRatio;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public void setJustified(boolean justified) {
        this.justified = justified;
    }

    public void setIndent(float indent) {
        this.indent = indent;
    }

    public void setLeadingRatio(float leadingRatio) {
        this.leadingRatio = leadingRatio;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }
}

package io.github.shaksternano.pdfcreator;

import io.github.shaksternano.pdfcreator.command.PDFCommand;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PDFCreator {

    private final File input;
    private final File output;
    private final PDFFont font;
    private final float startX;
    private final float startY;
    private final float leadingRatio;
    private final float textAreaWidth;
    private final float textAreaHeight;

    private final Map<String, PDFont> cachedFonts = new HashMap<>();

    /**
     * Creates a new PDFCreator instance.
     *
     * @param input        The input file.
     * @param output       The output file.
     * @param font         The font to use in the PDF.
     * @param paddingX     The horizontal text padding.
     * @param paddingY     The vertical text padding.
     * @param leadingRatio The ratio of the leading to the font size.
     */
    public PDFCreator(File input, File output, PDFFont font, float paddingX, float paddingY, float leadingRatio) {
        this.input = input;
        this.output = output;
        this.font = font;
        PDPage page = new PDPage();
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        startX = paddingX;
        startY = pageHeight - paddingY;
        this.leadingRatio = leadingRatio;
        textAreaWidth = pageWidth - paddingX * 2;
        textAreaHeight = pageWidth * 2.9F - paddingY * 2;
    }

    /**
     * Creates the PDF file.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void create() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(input)); PDDocument document = new PDDocument()) {
            PDFSettings settings = new PDFSettings();
            settings.setLeadingRatio(leadingRatio);
            PDPage currentPage = new PDPage();
            document.addPage(currentPage);
            PDPageContentStream contentStream = new PDPageContentStream(document, currentPage);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);

            String line = reader.readLine();
            Counter totalHeight = new Counter();
            PDFLine currentLine = new PDFLine();
            while (line != null) {
                contentStream = processInputLine(line, currentLine, totalHeight, document, contentStream, settings);
                line = reader.readLine();
            }
            contentStream = tryAddNewPage(currentLine, totalHeight, textAreaHeight, startX, startY, settings, document, contentStream);
            currentLine.write(contentStream, settings, textAreaWidth);
            contentStream.endText();
            contentStream.close();
            document.save(output);
        }
    }

    /**
     * Processes one line of the input file.
     *
     * @param input         The input line.
     * @param currentLine   The current line that's being built.
     * @param totalHeight   The total height of the text on the current page.
     * @param document      The PDF document being created.
     * @param contentStream The current content stream.
     * @param settings      The PDF settings.
     * @return The updated content stream. If a new page was added,
     * a new content stream is returned and the old one is closed.
     * Otherwise, the same content stream is returned.
     * @throws IOException If an I/O error occurs.
     */
    protected PDPageContentStream processInputLine(String input, PDFLine currentLine, Counter totalHeight, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) throws IOException {
        if (!input.isBlank()) {
            if (input.startsWith(".")) {
                contentStream = parseAndExecuteCommand(input, currentLine, totalHeight, document, contentStream, settings);
            } else {
                contentStream = writeText(input, currentLine, totalHeight, document, contentStream, settings);
            }
        }
        return contentStream;
    }

    /**
     * Appends text to the current line. If the line is too long,
     * it is written to the PDF and the line is cleared.
     * If the end of the page is reached, a new page is added.
     *
     * @param input         The input line.
     * @param currentLine   The current line that's being built.
     * @param totalHeight   The total height of the text on the current page.
     * @param document      The PDF document being created.
     * @param contentStream The current content stream.
     * @param settings      The PDF settings.
     * @return The updated content stream. If a new page was added,
     * a new content stream is returned and the old one is closed.
     * Otherwise, the same content stream is returned.
     * @throws IOException If an I/O error occurs.
     */
    protected PDPageContentStream writeText(String input, PDFLine currentLine, Counter totalHeight, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) throws IOException {
        String[] words = input.split("\\s+");
        for (String word : words) {
            PDFWord text = new PDFWord(word, getFont(settings, document), settings.getFontSize());
            if (currentLine.getWidth() + text.getWidth() <= textAreaWidth - settings.getIndent()) {
                currentLine.addText(text);
            } else {
                contentStream = tryAddNewPage(currentLine, totalHeight, textAreaHeight, startX, startY, settings, document, contentStream);
                currentLine.write(contentStream, settings, textAreaWidth);
                contentStream.newLine();
                totalHeight.add(settings.getLeadingRatio() * settings.getFontSize());
                currentLine.clear();
                currentLine.addText(text);
            }
        }
        return contentStream;
    }

    /**
     * Gets the font to be used. The font returned varies
     * depending on the {@link PDFSettings}. The font is cached.
     *
     * @param settings The PDF settings.
     * @param document The PDF document being created.
     * @return The font to be used.
     * @throws IOException If an I/O error occurs.
     */
    protected PDFont getFont(PDFSettings settings, PDDocument document) throws IOException {
        boolean bold = settings.isBold();
        boolean italic = settings.isItalic();
        PDFont pdFont;
        if (bold && italic) {
            String fontStyle = "bold-italic";
            pdFont = cachedFonts.get(fontStyle);
            if (pdFont == null) {
                pdFont = font.createBoldItalic(document);
                cachedFonts.put(fontStyle, pdFont);
            }
        } else if (bold) {
            String fontStyle = "bold";
            pdFont = cachedFonts.get(fontStyle);
            if (pdFont == null) {
                pdFont = font.createBold(document);
                cachedFonts.put(fontStyle, pdFont);
            }
        } else if (italic) {
            String fontStyle = "italic";
            pdFont = cachedFonts.get(fontStyle);
            if (pdFont == null) {
                pdFont = font.createItalic(document);
                cachedFonts.put(fontStyle, pdFont);
            }
        } else {
            String fontStyle = "regular";
            pdFont = cachedFonts.get(fontStyle);
            if (pdFont == null) {
                pdFont = font.createRegular(document);
                cachedFonts.put(fontStyle, pdFont);
            }
        }
        return pdFont;
    }

    /**
     * Parses a PDF input command and executes it.
     *
     * @param line          The input line.
     * @param currentLine   The current line that's being built.
     * @param totalHeight   The total height of the text on the current page.
     * @param document      The PDF document being created.
     * @param contentStream The current content stream.
     * @param settings      The PDF settings.
     * @return The updated content stream. If a new page was added,
     * a new content stream is returned and the old one is closed.
     */
    protected PDPageContentStream parseAndExecuteCommand(String line, PDFLine currentLine, Counter totalHeight, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) {
        String[] commandAndArgs = line.substring(1).split("\\s+");
        if (commandAndArgs.length > 0) {
            String commandName = commandAndArgs[0];
            Optional<PDFCommand> command = PDFCommands.getCommand(commandName);
            if (command.isPresent()) {
                try {
                    contentStream = command.orElseThrow().execute(
                            Arrays.asList(commandAndArgs).subList(1, commandAndArgs.length),
                            currentLine,
                            totalHeight,
                            textAreaWidth,
                            textAreaHeight,
                            startX,
                            startY,
                            document,
                            contentStream,
                            settings
                    );
                } catch (IOException e) {
                    System.err.println("Error while executing command " + line);
                    e.printStackTrace();
                }
                return contentStream;
            }
        }
        System.err.println("Unknown command: " + line);
        return contentStream;
    }

    /**
     * Adds a new page if the current page is full.
     *
     * @param currentLine    The current line that's being built.
     * @param totalHeight    The total height of the text on the current page.
     * @param textAreaHeight The height of the text area.
     * @param startX         The x-coordinate of the start of the text area.
     * @param startY         The y-coordinate of the start of the text area.
     * @param settings       The PDF settings.
     * @param document       The PDF document being created.
     * @param contentStream  The current content stream.
     * @return The updated content stream. If a new page was added,
     * a new content stream is returned and the old one is closed.
     * @throws IOException If an I/O error occurs.
     */
    public static PDPageContentStream tryAddNewPage(PDFLine currentLine, Counter totalHeight, float textAreaHeight, float startX, float startY, PDFSettings settings, PDDocument document, PDPageContentStream contentStream) throws IOException {
        totalHeight.add(currentLine.getHeight());
        if (totalHeight.getValue() > textAreaHeight) {
            contentStream.endText();
            contentStream.close();
            PDPage currentPage = new PDPage();
            document.addPage(currentPage);
            contentStream = new PDPageContentStream(document, currentPage);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX + settings.getIndent(), startY);
            totalHeight.set(currentLine.getHeight());
        }
        return contentStream;
    }

    static {
        PDFCommands.registerCommands();
    }
}

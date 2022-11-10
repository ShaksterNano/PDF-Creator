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
        textAreaHeight = pageWidth * 3 - paddingY * 2;
    }

    public void create() throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(input));
                PDDocument document = new PDDocument()
        ) {
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

    protected PDPageContentStream writeText(String input, PDFLine currentLine, Counter totalHeight, PDDocument document, PDPageContentStream contentStream, PDFSettings settings) throws IOException {
        String[] words = input.split("\\s+");
        for (String word : words) {
            PDFText text = new PDFText(word, createFont(settings, document), settings.getFontSize());
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

    protected PDFont createFont(PDFSettings settings, PDDocument document) throws IOException {
        boolean bold = settings.isBold();
        boolean italic = settings.isItalic();
        if (bold && italic) {
            return font.createBoldItalic(document);
        } else if (bold) {
            return font.createBold(document);
        } else if (italic) {
            return font.createItalic(document);
        } else {
            return font.createRegular(document);
        }
    }

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

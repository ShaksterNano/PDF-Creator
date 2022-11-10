package io.github.shaksternano.pdfcreator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        // Get the input file
        Scanner scanner = new Scanner(System.in);
        File input = new File("");
        while (!input.isFile()) {
            System.out.print("Enter the path to the input file: ");
            String inputPath = scanner.nextLine();
            input = new File(inputPath);
            if (!input.isFile()) {
                System.out.println("The file does not exist!");
            }
        }

        // Get the output directory
        File outputDir = new File("");
        while (!outputDir.isDirectory()) {
            System.out.print("Enter the path to the output directory. Leave empty for the input file directory: ");
            String outputPath = scanner.nextLine();
            if (outputPath.isBlank()) {
                outputDir = input.getAbsoluteFile().getParentFile();
            } else {
                outputDir = new File(outputPath);
                if (outputDir.isFile()) {
                    System.out.println("There is already a file with the same name!");
                } else {
                    outputDir.mkdirs();
                }
            }
        }

        // Get the output filename
        System.out.print("Enter the PDF name: ");
        String pdfName = scanner.nextLine();
        if (!pdfName.toLowerCase().endsWith(".pdf")) {
            pdfName += ".pdf";
        }
        File output = new File(outputDir, pdfName);

        // Create the PDF file
        PDFCreator pdfCreator = new PDFCreator(
                input,
                output,
                PDFFont.HELVETICA,
                50,
                50,
                1.25F
        );
        pdfCreator.create();
        System.out.println("PDF created at: " + output.getAbsolutePath());
    }
}

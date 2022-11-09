package io.github.shaksternano.pdfcreator;

import io.github.shaksternano.pdfcreator.command.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PDFCommands {

    private static final Map<String, PDFCommand> commands = new HashMap<>();

    public static void registerCommands() {
        commands.put("paragraph", NewParagraphCommand.INSTANCE);
        commands.put("fill", SetJustifyCommand.JUSTIFY);
        commands.put("nofill", SetJustifyCommand.NO_JUSTIFY);
        commands.put("regular", ResetFontCommand.INSTANCE);
        commands.put("italics", ItalicCommand.INSTANCE);
        commands.put("bold", BoldCommand.INSTANCE);
        commands.put("indent", IndentCommand.INSTANCE);
        commands.put("small", FontSizeCommand.SMALL);
        commands.put("normal", FontSizeCommand.NORMAL);
        commands.put("large", FontSizeCommand.LARGE);
    }

    public static Optional<PDFCommand> getCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }
}

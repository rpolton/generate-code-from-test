package me.shaftesbury.codegenerator.text;

public class LineOfCode implements ILine {
    private final String line;

    public LineOfCode(final String line) {
        this.line = line;
    }

    @Override
    public String getString() {
        return line;
    }
}

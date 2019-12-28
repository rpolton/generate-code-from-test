package me.shaftesbury.codegenerator.tokeniser;

public enum Token implements IToken {
    NEW("new"),
    NOPARAMS("()"),
    TESTANNOTATION("@Test"),
    VOIDRETURNTYPE("void"),
    STARTFUNCTION("{"),
    ENDFUNCTION("}"),
    SEMICOLON(";");

    private final String text;

    Token(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

package me.shaftesbury.codegenerator.tokeniser;

public enum Token implements IToken {
    NEW("new"),
    NOPARAMS("()"),
    TESTANNOTATION("@Test"),
    VOIDRETURNTYPE("void"),
    STARTFUNCTION("{"),
    ENDFUNCTION("}"),
    SEMICOLON(";"),
    FUNCTIONCALL("fn"),
    DOT("."),
    EMPTY(""),
    THIS("this"),
    ASSIGNMENT("="),
    PUBLIC("public"),
    CLASS("class"),
    STARTCLASS("{"),
    STARTFUNCTIONPARAMETERS("("),
    INT("int"),
    ENDFUNCTIONPARAMETERS("}"),
    ENDCLASS("}"),
    PRIVATE("private"),
    FINAL("final"),
    RETURN("return"),
    STARTFIELDS(""),
    ENDFIELDS("");

    private final String text;

    Token(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

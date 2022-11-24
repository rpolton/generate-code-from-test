package me.shaftesbury.codegenerator.model;

import me.shaftesbury.codegenerator.tokeniser.IToken;

public interface IFunctionName extends IToken {
    String asCode();
}

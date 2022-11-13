package me.shaftesbury.codegenerator;

public interface ITransformer<T, R> {
    R transform(T t);
}

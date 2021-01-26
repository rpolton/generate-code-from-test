package me.shaftesbury.codegenerator;

import io.vavr.Tuple2;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.Constructor;
import me.shaftesbury.codegenerator.model.IConstructor;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.IToken;

import static java.util.Objects.requireNonNull;
import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.INT;
import static me.shaftesbury.codegenerator.tokeniser.Token.PUBLIC;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;

public class ClassTransformer implements ITransformer<Traversable<IToken>, ILogicalClass> {

    public ILogicalClass transform(final Traversable<IToken> tokens) {
        requireNonNull(tokens, "tokens must not be null");
//        final Option<IClassName> classNameToken = tokens.find(t -> t instanceof ClassName).map(t -> (ClassName) t);
//        if(tokens.find(t -> t instanceof FunctionName).isDefined()) {
//            final Traversable<IToken> fromStartOfFunction = tokens.dropUntil(t -> t instanceof FunctionName);
//        }
//        tokens.fold ... reduce the tokens to a LogicalClass
//        return classNameToken.map(LogicalClass::of).getOrElse((ILogicalClass) null);
        return reducer(tokens, LogicalClass.builder());
    }

    private ILogicalClass reducer(final Traversable<IToken> tokens, final LogicalClass.Builder builder) {
        if (tokens.isEmpty()) return builder.build();
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (PUBLIC.equals(token)) {
            return reducer(tail, builder);
        } else if (CLASS.equals(token)) {
            return reducer_class(tail, builder);
        } else return reducer(tail, builder);
    }

    private ILogicalClass reducer_class(final Traversable<IToken> tokens, final LogicalClass.Builder builder) {
        if (tokens.isEmpty()) return builder.build();
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (STARTCLASS.equals(token)) {
            return reducer_class(tail, builder);
        } else if (token instanceof IClassName) {
            return reducer_class(tail, builder.withName((IClassName) token));
        } else if (PUBLIC.equals(token)) {
            return reducer_class(tail, builder);
        } else if (token instanceof IFunctionName) {
            final IFunctionName functionName = (IFunctionName) token;
//            if(functionName.getName().equals(builder.getName().toString())) {
            final Tuple2<Traversable<IToken>, IConstructor> t = reducer_constructor(tail, Constructor.builder());
            return reducer_class(t._1, builder.withConstructor(t._2));
//            } else {
//                final Tuple2<Traversable<IToken>, ILogicalFunction> t = reducer_constructor(tail, LogicalFunction.builder());
//                return reducer_class(t._1, builder.withMethod(t._2));
//            }
        } else if (ENDCLASS.equals(token)) {
            return builder.build();
        }
        return builder.build();
    }

    private Tuple2<Traversable<IToken>, IConstructor> reducer_constructor(final Traversable<IToken> tokens, final Constructor.Builder builder) {
        if (tokens.isEmpty()) return new Tuple2<>(tokens, builder.build());
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (STARTFUNCTIONPARAMETERS.equals(token)) {
            final Tuple2<Traversable<IToken>, ParameterList> t = reducer_functionParameters(tail, ParameterList.builder());
            return reducer_constructor(t._1, builder.withParameters(t._2));
        }
        return new Tuple2<>(tokens, builder.build());
    }

    private Tuple2<Traversable<IToken>, ParameterList> reducer_functionParameters(final Traversable<IToken> tokens, final ParameterList.Builder builder) {
        if (tokens.isEmpty()) return new Tuple2<>(tokens, builder.build());
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (INT.equals(token)) {
//            return reducer_functionParameters(tail, builder.with)
            final IToken token2 = tail.head();
            final Traversable<IToken> tail2 = tail.tail();
            if (token2 instanceof Reference) {
                final Reference reference = (Reference) token2;
                final String name = reference.getName();
                return reducer_functionParameters(tail2, builder.withParameter(FunctionParameter.of(TokenToTypeConverter.convert(token), name)));
            } else return null;//reducer_constructor(tail2, builder);
        } else if (ENDFUNCTIONPARAMETERS.equals(token)) {
            return new Tuple2<>(tail, builder.build());
        }
        return null;
    }

    private Tuple2<Traversable<IToken>, LogicalFunction> reducer_function(final Traversable<IToken> tokens, final LogicalFunction.Builder builder) {
        return new Tuple2<>(tokens, builder.build());
    }

}
//List.of(Token.PUBLIC, Token.CLASS, ClassName.of("A"), Token.STARTCLASS,
//                Token.PUBLIC, FunctionName.of("A"), Token.STARTFUNCTIONPARAMETERS, Token.INT, Reference.of("a"), Token.ENDFUNCTIONPARAMETERS,
//                Token.STARTFUNCTION, Token.ENDFUNCTION, Token.ENDCLASS);
package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
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
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFIELDS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.FINAL;
import static me.shaftesbury.codegenerator.tokeniser.Token.INT;
import static me.shaftesbury.codegenerator.tokeniser.Token.PRIVATE;
import static me.shaftesbury.codegenerator.tokeniser.Token.PUBLIC;
import static me.shaftesbury.codegenerator.tokeniser.Token.SEMICOLON;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFIELDS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTION;
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
            final Tuple2<Traversable<IToken>, IConstructor> t = reducer_constructor(tail, Constructor.builder());
            return reducer_class(t._1, builder.withConstructor(t._2));
        } else if (STARTFIELDS.equals(token)) {
            final Tuple2<Traversable<IToken>, Fields> t = reducer_fields(tail, Fields.builder());
            return reducer_class(t._1, builder.withFields(t._2));
        } else if (ENDCLASS.equals(token)) {
            return builder.build();
        }
        return builder.build();
    }

    private Tuple2<Traversable<IToken>, Fields> reducer_fields(final Traversable<IToken> tokens, final Fields.Builder builder) {
        if (tokens.isEmpty()) return Tuple.of(tokens, builder.build());
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (PRIVATE.equals(token)) {
            final IToken token2 = tail.head();
            final Traversable<IToken> tail2 = tail.tail();
            if (FINAL.equals(token2)) {
                final IToken token3 = tail2.head();
                final Traversable<IToken> tail3 = tail2.tail();
                if (INT.equals(token3)) {
                    final IToken token4 = tail3.head();
                    final Traversable<IToken> tail4 = tail3.tail();
                    if (token4 instanceof Reference) {
                        return reducer_fields(tail4, builder.withField(Field.builder().withAccess(TokenToAccessConverter.convert(PRIVATE)).withType(Type.INT).withName(((Reference) token4).getName()).build()));
                    }
                }
            }
        } else if (SEMICOLON.equals(token)) {
            return reducer_fields(tail, builder);
        } else if (ENDFIELDS.equals(token)) {
            return Tuple.of(tail, builder.build());
        }
        return Tuple.of(tail, builder.build());
    }

    private Tuple2<Traversable<IToken>, IConstructor> reducer_constructor(final Traversable<IToken> tokens, final Constructor.Builder builder) {
        if (tokens.isEmpty()) return Tuple.of(tokens, builder.build());
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (STARTFUNCTIONPARAMETERS.equals(token)) {
            final Tuple2<Traversable<IToken>, ParameterList> t = reducer_functionParameters(tail, ParameterList.builder());
            return reducer_constructor(t._1, builder.withParameters(t._2));
        } else if (STARTFUNCTION.equals(token)) {
            final Tuple2<Traversable<IToken>, FunctionBody> t = reducer_function(tail, FunctionBody.builder());
            return reducer_constructor(t._1, builder.withBody(t._2));
        }
        return Tuple.of(tokens, builder.build());
    }

    private Tuple2<Traversable<IToken>, ParameterList> reducer_functionParameters(final Traversable<IToken> tokens, final ParameterList.Builder builder) {
        if (tokens.isEmpty()) return Tuple.of(tokens, builder.build());
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
            return Tuple.of(tail, builder.build());
        }
        return null;
    }

    private Tuple2<Traversable<IToken>, FunctionBody> reducer_function(final Traversable<IToken> tokens, final FunctionBody.Builder builder) {
        if (tokens.isEmpty()) return Tuple.of(tokens, builder.build());
        final IToken token = tokens.head();
        final Traversable<IToken> tail = tokens.tail();
        if (ENDFUNCTION.equals(token)) {
            return Tuple.of(tokens, builder.build());
        } else {
            return reducer_function(tail, builder.withToken(token));
        }
    }

    private class Builder {
    }
}
//List.of(Token.PUBLIC, Token.CLASS, ClassName.of("A"), Token.STARTCLASS,
//                Token.PUBLIC, FunctionName.of("A"), Token.STARTFUNCTIONPARAMETERS, Token.INT, Reference.of("a"), Token.ENDFUNCTIONPARAMETERS,
//                Token.STARTFUNCTION, Token.ENDFUNCTION, Token.ENDCLASS);
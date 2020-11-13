package uk.ac.ucl.jsh.parser;

import uk.ac.ucl.jsh.CallGrammarBaseVisitor;
import uk.ac.ucl.jsh.CallGrammarParser;
import uk.ac.ucl.jsh.call_parts.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CallVisitor extends CallGrammarBaseVisitor<ArrayList<Sub_Call>> {
    @Override public ArrayList<Sub_Call> visitCall(CallGrammarParser.CallContext ctx) {
        ArrayList<Sub_Call> returns = new ArrayList<>();
        if(ctx.redirection()!= null){
            for(CallGrammarParser.RedirectionContext context: ctx.redirection()){
                returns.addAll(visit(context));
            }
        }
        if(ctx.argument() != null){
            returns.addAll(visit(ctx.argument()));
        }
        if(ctx.atom()!= null){
            for(CallGrammarParser.AtomContext context: ctx.atom()){
                returns.addAll(visit(context));
            }
        }
        return returns;
    }

    @Override public ArrayList<Sub_Call> visitAtom(CallGrammarParser.AtomContext ctx) {
        if(ctx.redirection()!=null)
            return visit(ctx.redirection());
        return visit(ctx.argument());
    }

    @Override public ArrayList<Sub_Call> visitLeftRedir(CallGrammarParser.LeftRedirContext ctx) {
        //The arguments in left redirection gives the inputStream.
        return getSubCallArray(new InputRedirection(visit(ctx.argument())));
    }

    @Override public ArrayList<Sub_Call> visitRightRedir(CallGrammarParser.RightRedirContext ctx) {
        //The arguments in right redirection gives the outputStream.
        return getSubCallArray(new OutputRedirection(visit(ctx.argument())));
    }

    @Override public ArrayList<Sub_Call> visitArgument(CallGrammarParser.ArgumentContext ctx) {
        ArrayList<Sub_Call> returns = new ArrayList<>();
        if(ctx.unquoted() != null){
            for(CallGrammarParser.UnquotedContext context: ctx.unquoted()){
                returns.addAll(visit(context));
            }
        }
        if(ctx.quoted() != null){
            for(CallGrammarParser.QuotedContext context: ctx.quoted()){
                returns.addAll(visit(context));
            }
        }
        return returns;
    }

    @Override public ArrayList<Sub_Call> visitSingleQuoted(CallGrammarParser.SingleQuotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText()));
    }

    @Override public ArrayList<Sub_Call> visitDoubleQuoted(CallGrammarParser.DoubleQuotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText()));
    }

    @Override public ArrayList<Sub_Call> visitBackQuoted(CallGrammarParser.BackQuotedContext ctx) {
        return getSubCallArray(new Substitution(ctx.getText()));
    }

    @Override public ArrayList<Sub_Call> visitUnquoted(CallGrammarParser.UnquotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText()));
    }

    private ArrayList<Sub_Call> getSubCallArray(Sub_Call sub_call){
        ArrayList<Sub_Call> returns = new ArrayList<>();
        returns.add(sub_call);
        return returns;
    }

}

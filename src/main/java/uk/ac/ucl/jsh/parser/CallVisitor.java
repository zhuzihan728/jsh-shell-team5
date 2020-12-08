package uk.ac.ucl.jsh.parser;

import uk.ac.ucl.jsh.CallGrammarBaseVisitor;
import uk.ac.ucl.jsh.CallGrammarParser;
import uk.ac.ucl.jsh.call_parts.*;
import java.util.ArrayList;

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
            returns.addAll(visit(ctx.unquoted()));
        }
        if(ctx.quoted() != null){
            returns.addAll(visit(ctx.quoted()));
        }
        if(ctx.argument() != null){
            glue_lists(returns,visit(ctx.argument()));
        }
        return returns;
    }

    @Override public ArrayList<Sub_Call> visitSingleQuoted(CallGrammarParser.SingleQuotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText(),2));
    }

    @Override public ArrayList<Sub_Call> visitDoubleQuoted(CallGrammarParser.DoubleQuotedContext ctx) {
        return visit(ctx.doubleQuotedContent());
    }

    @Override public ArrayList<Sub_Call> visitDoubleQuotedContent(CallGrammarParser.DoubleQuotedContentContext ctx) {
        ArrayList<Sub_Call> returns = new ArrayList<>();
        if(ctx.doubleQuoted_part() != null){
            glue_lists(returns,visit(ctx.doubleQuoted_part()));
        }
        if(ctx.backQuoted() != null){
            glue_lists(returns,visit(ctx.backQuoted()));
        }
        if(ctx.doubleQuotedContent()!=null){
            glue_lists(returns,visit(ctx.doubleQuotedContent()));
        }
        return returns;
    }

    @Override public ArrayList<Sub_Call> visitDoubleQuoted_part(CallGrammarParser.DoubleQuoted_partContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText(),0));
    }

    @Override public ArrayList<Sub_Call> visitBackQuoted(CallGrammarParser.BackQuotedContext ctx) {
        return getSubCallArray(new Substitution(ctx.getText()));
    }

    @Override public ArrayList<Sub_Call> visitUnquoted(CallGrammarParser.UnquotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText(),1));
    }

    private ArrayList<Sub_Call> getSubCallArray(Sub_Call sub_call){
        ArrayList<Sub_Call> returns = new ArrayList<>();
        returns.add(sub_call);
        return returns;
    }

    private void glue_lists(ArrayList<Sub_Call> prev, ArrayList<Sub_Call> appended){
        if(prev.isEmpty()){
            prev.addAll(appended);
            return;
        }

        if(appended.isEmpty()){
            return;
        }
        prev.set(prev.size()-1,
                new BaseCall(prev.get(prev.size()-1).getString()
                + appended.get(0).getString(),0));
        if(appended.size()>1){
            prev.addAll(appended.subList(1,appended.size()-1));
        }
    }
}

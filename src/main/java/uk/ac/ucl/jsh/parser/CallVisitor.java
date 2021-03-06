package uk.ac.ucl.jsh.parser;

import uk.ac.ucl.jsh.CallGrammarBaseVisitor;
import uk.ac.ucl.jsh.CallGrammarParser;
import uk.ac.ucl.jsh.call_parts.*;
import java.util.ArrayList;

/**
 * Visitor class that traverse through the Abstract syntax tree generated by the 
 * ANTLR plugin using the grammar in JshGrammar.g4
 */
public class CallVisitor extends CallGrammarBaseVisitor<ArrayList<Sub_Call>> {

    /**
     * Visit the Call token from the syntax tree. Grammar in JshGrammar.g4
     * This is the token at the root of the syntax tree.
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
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

    /**
     * Visit the Atom token from the syntax tree. Grammar in JshGrammar.g4
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitAtom(CallGrammarParser.AtomContext ctx) {
        if(ctx.redirection()!=null)
            return visit(ctx.redirection());
        return visit(ctx.argument());
    }

    /**
     * Visit the LeftRedir token from the syntax tree. Grammar in JshGrammar.g4
     * This token specify the command that contains a left redirection.
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitLeftRedir(CallGrammarParser.LeftRedirContext ctx) {
        //The arguments in left redirection gives the inputStream.
        return getSubCallArray(new InputRedirection(visit(ctx.argument())));
    }

    /**
     * Visit the RightRedir token from the syntax tree. Grammar in JshGrammar.g4
     * This token specify the command that contains a right redirection.
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitRightRedir(CallGrammarParser.RightRedirContext ctx) {
        //The arguments in right redirection gives the outputStream.
        return getSubCallArray(new OutputRedirection(visit(ctx.argument())));
    }

    /**
     * Visit the Argument token from the syntax tree. Grammar in JshGrammar.g4
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
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

    /**
     * Visit the Single Quoted token from the syntax tree. Grammar in JshGrammar.g4
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitSingleQuoted(CallGrammarParser.SingleQuotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText(),2)); //Type call 2 for single quoted.
    }

    /**
     * Visit the Double Quoted token from the syntax tree. Grammar in JshGrammar.g4
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitDoubleQuoted(CallGrammarParser.DoubleQuotedContext ctx) {
        return visit(ctx.doubleQuotedContent());
    }

    /**
     * Visit the DoubleQuotedContent token from the syntax tree. Grammar in JshGrammar.g4
     * This specify the arguments inside the double quotes.
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
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

    /**
     * Visit the DoubleQuoted_part token from the syntax tree. Grammar in JshGrammar.g4
     * This specify the part of the double quoted content that does not contain backquotes.
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitDoubleQuoted_part(CallGrammarParser.DoubleQuoted_partContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText(),0)); //Call type 0 for call part inside double quoted but not in backquotes.
    }

    /**
     * Visit the BackQuoted token from the syntax tree. Grammar in JshGrammar.g4
     * The backed quoted content indicates that these tokens should be treated as substitution.
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitBackQuoted(CallGrammarParser.BackQuotedContext ctx) {
        return getSubCallArray(new Substitution(ctx.getText()));
    }

    /**
     * Visit the Unquoted token from the syntax tree. Grammar in JshGrammar.g4
     * 
     * @param ctx Current node in the syntax tree that we are visiting.
     */
    @Override public ArrayList<Sub_Call> visitUnquoted(CallGrammarParser.UnquotedContext ctx) {
        return getSubCallArray(new BaseCall(ctx.getText(),1)); // Call type 1 for unquoted.
    }

    /**
     * This is a helper function that when there's only one sub_call, we need to turn it into a 
     * arraylist that matches the return type.
     * 
     * @param sub_call A single sub_call which needs to be turned into a ArrayList.
     */
    private ArrayList<Sub_Call> getSubCallArray(Sub_Call sub_call){
        ArrayList<Sub_Call> returns = new ArrayList<>();
        returns.add(sub_call);
        return returns;
    }

    /**
     * Helper function to concatenate string in 2 arraylists.
     * When there's no space between 2 tokens, we need to concatenate the last and the 
     * first argument in the string, and make sure that there's no space between the two.
     * 
     * @param prev The arraylist at the front.
     * @param appended The arraylist at the end.
     */
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

package uk.ac.ucl.jsh.parser;

import uk.ac.ucl.jsh.JshGrammarBaseVisitor;
import uk.ac.ucl.jsh.JshGrammarParser;
import uk.ac.ucl.jsh.JshMain;
import uk.ac.ucl.jsh.command.Call;
import uk.ac.ucl.jsh.command.Command;
import uk.ac.ucl.jsh.command.Pipe;
import uk.ac.ucl.jsh.command.Sequence;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CommandVisitor extends JshGrammarBaseVisitor<Command> {
    @Override
    public Command visitCmdline(JshGrammarParser.CmdlineContext ctx) {
        if (ctx.seq() != null) {
            return visit(ctx.seq());
        }
        return visit(ctx.command());
    }

    @Override
    public Command visitCommand(JshGrammarParser.CommandContext ctx) {
        if (ctx.pipe() != null) {
            return visit(ctx.pipe());
        }
        return visit(ctx.call());
    }

    @Override
    public Command visitSeqRecur(JshGrammarParser.SeqRecurContext ctx) {
        return new Sequence(visit(ctx.cmd1), visit(ctx.cmd2));
    }

    @Override
    public Command visitSeqBase(JshGrammarParser.SeqBaseContext ctx) {
        return new Sequence(visit(ctx.seq()), visit(ctx.command()));
    }

    @Override
    public Command visitMultiCallPipe(JshGrammarParser.MultiCallPipeContext ctx) {
        return new Pipe(visit(ctx.pipe()), visit(ctx.call()));
    }

    @Override
    public Command visitCallOnlyPipe(JshGrammarParser.CallOnlyPipeContext ctx) {
        return new Pipe(visit(ctx.call1), visit(ctx.call2));
    }

    @Override
    public Command visitCall(JshGrammarParser.CallContext ctx) {
        return new Call(ctx.getText());
    }
}


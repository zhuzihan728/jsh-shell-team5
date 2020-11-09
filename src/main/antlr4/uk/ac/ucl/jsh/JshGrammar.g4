grammar JshGrammar;

/*
 * Parser Rules
 */
cmdline : seq EOF
        | command EOF;

command : pipe
        | call
        ;
seq : seq ';' command #SeqBase
    | cmd1 = command ';' cmd2 = command #SeqRecur
    ;
pipe : call1 = call '|' call2 = call #CallOnlyPipe
     | pipe '|' call #MultiCallPipe
     ;
call : ' '? (redirection ' ')*? argument (' ' atom)*? ' '?
     ;
atom : redirection #AtomRedir
     | argument #AtomArgu
     ;
redirection : '<' ' '? argument #LeftRedir
            | '>' ' '? argument #RightRedir
            ;
argument : (unquoted | quoted)+;
quoted : SINGLEQUOTED #SingleQuoted
       | DOUBLEQUOTED # DoubleQuoted
       | BACKQUOTED #BackQuoted
       ;
unquoted : NONSPECIAL
         ;
/*
 * Lexer Rules
 */

NONSPECIAL : ~['";]+;
DOUBLEQUOTED : '"' (~'"')* '"';
SINGLEQUOTED : '\'' (~'\'')* '\'';
BACKQUOTED : '`' (~'\'')* '`';
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
call : WHITESPACE*? (redirection WHITESPACE+)*? argument (WHITESPACE+ atom)*? WHITESPACE*?;

atom : redirection
     | argument
     ;
redirection : '<' WHITESPACE? argument #LeftRedir
            | '>' WHITESPACE? argument #RightRedir
            ;
argument : unquoted argument?
         | quoted argument?;
quoted : singleQuoted
       | doubleQuoted
       | backQuoted
       ;
unquoted : NONSPECIAL
         ;
doubleQuoted : '"'doubleQuotedContent'"';
doubleQuotedContent : doubleQuoted_part doubleQuotedContent
                    | backQuoted doubleQuotedContent |;
doubleQuoted_part : (NONSPECIAL|'>'|'<'|';'|'|'|WHITESPACE|'\'');
singleQuoted : '\'' singleQuotedContent '\'';
singleQuotedContent : (NONSPECIAL|'>'|'<'|';'|'|'|WHITESPACE|'"'|'`')*;
backQuoted : '`' backQuotedContent '`';
backQuotedContent: (NONSPECIAL|'>'|'<'|';'|'|'|WHITESPACE|'"'|'\'')*;
/*
 * Lexer Rules
 */
WHITESPACE : [ \t];
NONSPECIAL : ~[ \t"'`\n\r;|><]+;
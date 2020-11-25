grammar CallGrammar;

call_cmd : call EOF;
call : WHITESPACE*? (redirection WHITESPACE+)*? argument (WHITESPACE+ atom)*?;

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
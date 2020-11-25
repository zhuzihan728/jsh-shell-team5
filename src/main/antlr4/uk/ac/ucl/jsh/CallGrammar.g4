grammar CallGrammar;

call_cmd : call EOF;
call : ' '? (redirection ' ')*? argument (' ' atom)*? ' '?
     ;
atom : redirection
     | argument
     ;
redirection : '<' ' '? argument #LeftRedir
            | '>' ' '? argument #RightRedir
            ;
argument : unquoted argument?
         | quoted argument?;
quoted : SINGLEQUOTED #SingleQuoted
       | DOUBLEQUOTED # DoubleQuoted
       | BACKQUOTED #BackQuoted
       ;
unquoted : NONSPECIAL
         ;
/*
 * Lexer Rules
 */
WHITESPACE : [ \t];
NONSPECIAL : ~[ \t"'`\n\r;|><]+;
DOUBLEQUOTED : '"' (~'"')* '"';
SINGLEQUOTED : '\'' (~'\'')* '\'';
BACKQUOTED : '`' (~'`')* '`';
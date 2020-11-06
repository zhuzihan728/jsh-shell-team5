grammar JshGrammar;

/*
 * Parser Rules
 */

command : pipe | seq ;
seq :  pipe more_commands | call more_commands;
more_commands : | ';' command more_commands;
pipe : call '|' call| pipe '|' call;
call : ' ' (redirection ' ')* argument (' ' atom)* ' ';
atom : redirection | argument;
redirection : '<' ' ' argument | '>' ' ' argument;
argument : (quoted | UNQUOTED)+;
quoted : SINGLEQUOTED | DOUBLEQUOTED | UNQUOTED;

/*
 * Lexer Rules
 */

NONSPECIAL : ~['";]+;
DOUBLEQUOTED : '"' (~'"')* '"';
SINGLEQUOTED : '\'' (~'\'')* '\'';
BACKQUOTED : '`' (~'\'')* '`';
UNQUOTED : (~'\'')+;
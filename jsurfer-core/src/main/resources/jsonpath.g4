grammar JsonPath;

@header {
package org.jsfr.json.compiler;
}

path: '$' (searchChild|search|index|indexes|slicing|childNode|childrenNode|anyChild|anyIndex|any)* filter? EOF;
searchChild: '..' KEY;
search: '..' ;
anyChild: '.*' ;
anyIndex: '[*]' ;
any: '*' ;
index: '[' NUM ']';
indexes: '[' NUM ( ',' NUM )* ']' ;
slicing: '[' NUM? COLON NUM? ']';
COLON : ':';
childNode: '.' KEY ;
childrenNode: '[' KEY ( ',' KEY )* ']' ;
filter: '[?(' expr ')]';
expr : expr AndOperator expr
           | expr OrOperator expr
           | exprEqualNum
           | exprEqualStr
           | exprGtNum
           | exprLtNum
           | exprExist
           ;
exprExist:  '@.' KEY;
exprGtNum:  '@.' KEY '>' NUM;
exprLtNum:  '@.' KEY '<' NUM;
exprEqualNum: '@.' KEY '==' NUM;
exprEqualStr: '@.' KEY '==\'' KEY '\'';
//exprArrayIdx: '@.length-' NUM;
AndOperator: '&&';
OrOperator: '||';
NUM
    :   '-'? INT '.' [0-9]+ EXP? // 1.35, 1.35E-9, 0.3, -4.5
    |   '-'? INT EXP             // 1e10 -3e4
    |   '-'? INT                 // -3, 45
    ;
fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment EXP :   [Ee] [+\-]? INT ; // \- since - means "range" inside [...]

KEY :  (ESC | ~(["\\] | '.' | '*' | '[' | ']' | '(' | ')' | ',' | ':'| '=' | '@' | '?' | '&' | '|' | '>' | '<' | '\'' | [ \t\n\r]))+  ;
fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

WS  :   [ \t\n\r]+ -> skip ;



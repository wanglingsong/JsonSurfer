grammar JsonPath;

@header {
package org.jsfr.json.compiler;
}

path: '$' relativePath* filter? EOF;
relativePath: searchChild|search|index|indexes|slicing|childNode|childrenNode|anyChild|anyIndex|any;
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
childrenNode: '[' QUOTED_STRING ( ',' QUOTED_STRING )* ']' ;
filter: '[?(' filterExpr ')]';
filterExpr : filterExpr AndOperator filterExpr
           | filterExpr OrOperator filterExpr
           | filterEqualNum
           | filterEqualStr
           | filterGtNum
           | filterLtNum
           | filterExist
           ;
filterExist:  '@' relativePath+;
filterGtNum:  '@' relativePath+ '>' NUM;
filterLtNum:  '@' relativePath+ '<' NUM;
filterEqualNum: '@' relativePath+ '==' NUM;
filterEqualStr: '@' relativePath+ '==' QUOTED_STRING;
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

QUOTED_STRING : '\'' ( ~('\''|'\\') | ('\\' .) )* '\'';

KEY :  (ESC | ~(["\\] | '.' | '*' | '[' | ']' | '(' | ')' | ',' | ':'| '=' | '@' | '?' | '&' | '|' | '>' | '<' | '\'' | [ \t\n\r]))+  ;
fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

WS  :   [ \t\n\r]+ -> skip ;



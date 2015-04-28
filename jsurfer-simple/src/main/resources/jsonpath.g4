grammar JsonPath;

@header {
package org.jsfr.json.compiler;
}

path: '$' (searchChild|search|childNode|childrenNode|index|indexes|slicing|filter|anyChild|anyIndex|any)* EOF;
searchChild: '..' KEY;
search: '..' ;
anyChild: '.*' ;
anyIndex: '[*]' ;
any: '*' ;
childNode: '.' KEY ;
childrenNode: '[' KEY ( ',' KEY )* ']' ;
index: '[' NUM ']';
indexes: '[' NUM ( ',' NUM )* ']' ;
slicing: '[' NUM? COLON NUM? ']';
filter: '[' expr ']';
expr : expr ('&&' expr)+
           | expr ('||' expr)+
           | '*'
           | '@.' KEY
           | '@.' KEY '>' NUM
           | '@.' KEY '<' NUM
           | '@.length-' NUM
           | '@.' KEY '==' NUM
           | '@.' KEY '==\'' KEY '\''
           ;
COLON : ':';
KEY : [a-zA-Z][a-zA-Z0-9_]* ;
NUM : '0' | [1-9][0-9]* ;
WS  :   [ \t\n\r]+ -> skip ;



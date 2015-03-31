grammar JsonPath;

@header {
package org.jsfr.json.compiler;
}

path: '$' (searchChild|search|childNode|childrenNode|index|indexes|filter|anyChild|anyIndex|any)* EOF;
searchChild: '..' KEY;
search: '..' ;
childNode: '.' KEY ;
childrenNode: '[' KEY ( ',' KEY )* ']' ;
index: '[' NUM ']';
indexes: '[' NUM ( ',' NUM )* ']' ;
anyChild: '.*' ;
anyIndex: '[*]' ;
any: '*' ;
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

KEY : [a-zA-Z][a-zA-Z0-9]* ;
NUM : '0' | [1-9][0-9]* ;
WS  :   [ \t\n\r]+ -> skip ;



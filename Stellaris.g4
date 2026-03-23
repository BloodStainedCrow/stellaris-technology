grammar Stellaris;

file
   : (pair|var)* EOF
   ;

map
   : '{' (pair)* '}'
   ;

pair
   : (BAREWORD | NUMBER) SPECIFIER value
   ;

var
   : VARIABLE SPECIFIER NUMBER
   ;

array
   : '{' value+ '}'
   ;

value
   : NUMBER
   | BOOLEAN
   | DATE
   | STRING
   | VARIABLE
   | MATH
   | BAREWORD
   | map
   | array

    /* Sections that look like:
     [[POP_GROUP]
        ...
     ]
     or (yes, no "]" at the end of the word):
     [[HOUSING
        ...
     ]
    */
   | '[[' '!'? BAREWORD ']'? value ']'
   | pair
   ;

BOOLEAN
   : 'yes'
   | 'no'
   | 'true'
   | 'false'
   ;

VARIABLE
   : '@'([A-Za-z][A-Za-z_0-9.%-]*)
   ;

MATH
   : '@' '\\'? '['(~[\]])*']'
   ;

SPECIFIER
   : '=' | '<>' | '>' | '<' | '<=' | '>=' | '!=' ;

NUMBER
   : '-'?[0-9]+'%'
   | '-'?[0-9]+'.'[0-9]+
   | '-'?[0-9]+
   ;

DATE
   : [0-9]+'.'[0-9]+'.'[0-9]+;

BAREWORD
   : [A-Za-z][@A-Za-z_0-9./%|:-]*
   ;

STRING
   : '"'(~["])*'"'
   ;

WS
   : [ \t\n\r]+ -> skip
   ;

LINE_COMMENT
   : '#'~[\r\n]* -> channel(HIDDEN)
   ;
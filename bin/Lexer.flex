import compilerTools.Token;

%%
%class Lexer
%type Token
%line
%column
%{
    private Token token(String lexeme, String lexicalComp, int line, int column){
        return new Token(lexeme, lexicalComp, line+1, column+1);
    }
%}
/* Variables básicas de comentarios y espacios */
TerminadorDeLinea = \r|\n|\r\n
EntradaDeCaracter = [^\r\n]
EspacioEnBlanco = {TerminadorDeLinea} | [ \t\f\r\n]+
ComentarioTradicional = "/*" [^*] ~"*/" | "/*" "*"+ "/" | "#" \#.*
FinDeLineaComentario = "//" {EntradaDeCaracter}* {TerminadorDeLinea}?
ContenidoComentario = ( [^*] | \*+ [^/*] )*
ComentarioDeDocumentacion = "/**" {ContenidoComentario} "*"+ "/"+ "#"

/* Comentario */
Comentario = {ComentarioTradicional} | {FinDeLineaComentario} | {ComentarioDeDocumentacion}

/* Identificador */
Letra = [A-Za-zÑñ_ÁÉÍÓÚáéíóúÜü]
Digito = [0-9]
Identificador = {Letra}({Letra}|{Digito})*

/* Número */
Numero = 0 | [1-9][0-9]*
%%
/* Comentarios o espacios en blanco */
{Comentario}|{EspacioEnBlanco} { /*Ignorar*/ }

/* Identificador PEARL */
"my"{Identificador} |
\${Identificador} |
\@{Identificador} { return token(yytext(), "PEARL_VAR", yyline, yycolumn); }

/* Identificador RUBY */
{Identificador} { return token(yytext(), "RUBY_VAR", yyline, yycolumn); }


/* Tipos de dato */
numero |
color { return token(yytext(), "TIPO_DATO", yyline, yycolumn); }

/* Número */
{Numero} { return token(yytext(), "NUMERO", yyline, yycolumn); }



/* Colores */
#[{Letra}{Digito}]{6} { return token(yytext(), "COLOR", yyline, yycolumn); }

/* Letra */
LETRA { return token(yytext(), "NOMBRE", yyline, yycolumn); }

/* Operadores de agrupación */
"(" { return token(yytext(), "PARENTESIS_A", yyline, yycolumn); }
")" { return token(yytext(), "PARENTESIS_C", yyline, yycolumn); }
"{" { return token(yytext(), "CORCHETE_A", yyline, yycolumn); }
"}" { return token(yytext(), "CORCHETE_B", yyline, yycolumn); }
"[" { return token(yytext(), "LLAVE_A", yyline, yycolumn); }
"]" { return token(yytext(), "LLAVE_C", yyline, yycolumn); }


/* Signos de puntuación */
"," { return token(yytext(), "COMA", yyline, yycolumn); }
";" { return token(yytext(), "PUNTO_COMA", yyline, yycolumn); }
":" { return token(yytext(), "DOS_PUNTOS", yyline, yycolumn); }
"." { return token(yytext(), "PUNTOS", yyline, yycolumn); }


/* Operador de asignación */
= { return token (yytext(), "OP_ASIG", yyline, yycolumn); }


/* Reservadas Ruby */
def |
end |
if |
else |
elsif |
while |
do |
for |
in |
true |
false |
nil |
self |
class |
module |
return |
puts |
to_f { return token(yytext(), "RUBY_Reserv", yyline, yycolumn); }

/* Reservadas Pearl */
my |
List |
def |
use |
end |
if |
else |
elsif |
while |
do |
for |
in |
true |
false |
nil |
self |
class |
module |
return |
puts |
unless |
print |
module { return token(yytext(), "PEARL_Reserv", yyline, yycolumn); }

/* Definir */
def { return token(yytext(), "DEFINIR", yyline, yycolumn); }

/* Repetir */
do |
for |
while { return token(yytext(), "REPETIR", yyline, yycolumn); }

/* Estructura si */
if |
else { return token(yytext(), "SI_SINO", yyline, yycolumn); }

/* Operadores lógicos */
"?" |
"|" |
"&&" |
"||" |
"&" { return token(yytext(), "OP_LOGICO", yyline, yycolumn); }

/* Operadores matematico */
{Numero}"+" |
{Numero}"-" |
{Numero}"*" |
{Numero}"/" |
{Identificador}"+" |
{Identificador}"-" |
{Identificador}"/" |
{Identificador}"*" |
{Identificador}"+"{Identificador} |
{Identificador}"-" {Identificador}|
{Identificador}"/" {Identificador}|
{Identificador}"*" {Identificador} |
{EspacioEnBlanco}"+"{EspacioEnBlanco} |
{EspacioEnBlanco}"-" {EspacioEnBlanco}|
{EspacioEnBlanco}"/" {EspacioEnBlanco}|
{EspacioEnBlanco}"*" {EspacioEnBlanco} |
"/" {EspacioEnBlanco} { return token(yytext(), "OP_ARIT", yyline, yycolumn); }

/* Salida */
{Identificador}"#" |
{EspacioEnBlanco}"#" |
"'"{Letra} |
\"{Letra} |
\'{Identificador} |
\"{Identificador} |
\"{EspacioEnBlanco} |
\'{EspacioEnBlanco} |
{EspacioEnBlanco}\" |
{EspacioEnBlanco}\' |
"/" " " |
\{ \"   { return token(yytext(), "CONCATENAR", yyline, yycolumn); }

/* Errores */
// Número erróneo
0 {Numero}+ { return token(yytext(), "ERROR_1", yyline, yycolumn); }
// Identificador sin $
#{Identificador} { return token(yytext(), "ERROR_2", yyline, yycolumn); }
. { return token(yytext(), "ERROR", yyline, yycolumn); }
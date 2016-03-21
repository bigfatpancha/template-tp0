package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {
    private Integer maxLength;
    private static final Character[] specialChars = {'.', '[', ']', '\\', '*', '+', '?'};

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) {
        List<String> results =  new ArrayList<String>();
        if (numberOfResults > 0) {
            List<Token> tokens = parseRegEx(regEx);
            for (int i = 0; i < numberOfResults; i++) {
                String word = "";
                for (Token token : tokens) {
                    word += generateString(token);
                }
                results.add(word);
            }
        }
        return results;
    }

    /**
     * Recibe un desde y un hasta
     * @param token
     * @return
     */
    private String generateString(Token token) {
        StringBuilder builder = new StringBuilder();

        //Primero calculo cuántas veces corre el for
        int iteraciones = calcularIteraciones(token.getCuantificador());

        for(int i=0;i<iteraciones;i++) {
            int num;
            if (token.getTotalValores() == 1)
                num = 0;
            else
                num = randomIntEntre(0, token.getTotalValores());

            String unCaracter = token.getValor(num).toString();
            builder.append(unCaracter);
        }

        return builder.toString();
    }

    private static int randomIntEntre(Integer desde, Integer hasta) {
        Double D = Math.floor(desde + Math.random() * (1+hasta-desde));
        return D.intValue();
    }

    private int calcularIteraciones(Character cuantificador) {
        if(cuantificador.equals('*')) {
            return randomIntEntre(0,this.maxLength);
        } else if(cuantificador.equals('+')) {
            return randomIntEntre(1,this.maxLength);
        } else if(cuantificador.equals('?')) {
            return randomIntEntre(0,1);
        } else if (cuantificador.equals('n')) {
            return 1;
        } else
            return 0;
    }

    private List<Token> parseRegEx(String regEx) {
        List<Token> tokens = new ArrayList<Token>();
        int totalChars = regEx.length();
        int i = 0;
        while (i < totalChars) {
            int posChar = i;
            int posCuant = i + 1;
            Token token = null;
            if (isLiteral(regEx.charAt(posChar))){
                List<Character> valores = new ArrayList<Character>();
                if ((posCuant < totalChars) && (isCuantificador(regEx.charAt(posCuant)))) {
                    valores.add(regEx.charAt(posChar));
                    token = new Token(regEx.charAt(posCuant), valores);
                    i += 2;
                }
                else {
                    valores.add(regEx.charAt(posChar));
                    token = new Token('n', valores);
                    i += 1;
                }
            } else if (isCorchete(regEx.charAt(posChar))) {
                posChar += 1;
                posCuant += 1;
                List<Character> valores = new ArrayList<Character>();
                while(!isCorcheteCierra(regEx.charAt(posChar))){
                    if (isLiteral(regEx.charAt(posChar))) {
                        valores.add(regEx.charAt(posChar));
                    }
                    else {
                        if (isPunto(regEx.charAt(posChar))){
                            valores.addAll(todosLosChars());
                        }
                        else {
                            if (isContrBarra(regEx.charAt(posChar))) {
                                posChar += 1;
                                valores.add(regEx.charAt(posChar));
                            }
                        }
                    }
                    posChar += 1;
                    posCuant += 1;
                }
                if ((posCuant < totalChars) && (isCuantificador(regEx.charAt(posCuant)))){
                    token = new Token(regEx.charAt(posCuant), valores);
                    i = posChar + 2;
                } else {
                    token = new Token('n', valores);
                    i = posChar + 1;
                }
            } else if (isContrBarra(regEx.charAt(posChar))) {
                posChar += 1;
                posCuant += 1;
                List<Character> valores = new ArrayList<Character>();
                if ((posCuant < totalChars) && (isCuantificador(regEx.charAt(posCuant)))){
                    valores.add(regEx.charAt(posChar));
                    token = new Token(regEx.charAt(posCuant), valores);
                    i = posChar + 2;
                }
                else {
                    valores.add(regEx.charAt(posChar));
                    token = new Token('n', valores);
                    i = posChar + 1;
                }
            } else if (isPunto(regEx.charAt(posChar))) {
                if ((posCuant < totalChars) && (isCuantificador(regEx.charAt(posCuant)))){
                    token = new Token(regEx.charAt(posCuant), todosLosChars());
                    i += posChar + 2;
                }
                else {
                    token = new Token('n', todosLosChars());
                    i = posChar + 1;
                }
            }
            tokens.add(token);
        }
        return tokens;
    }

    private boolean isLiteral(Character caracter) {
        for (int i = 0; i < specialChars.length; i++){
            if (caracter.equals(specialChars[i]))
                return false;
        }
        return true;
    }

    private boolean isCorchete(Character caracter) {
        if (caracter.equals('['))
            return true;
        return false;
    }

    private boolean isCorcheteCierra(Character caracter) {
        if (caracter.equals(']'))
            return true;
        return false;
    }

    private boolean isCuantificador(Character caracter) {
        if (caracter.equals('*') || caracter.equals('+') || caracter.equals('?'))
            return true;
        return false;
    }

    private boolean isContrBarra(Character caracter) {
        if (caracter.equals('\\'))
            return true;
        return false;
    }

    private boolean isPunto(Character caracter) {
        if (caracter.equals('.'))
            return true;
        return false;
    }

    private List<Character> todosLosChars() {
        List<Character> todos = new ArrayList<Character>();
        for (int i = 0; i <= 255; i++){
            todos.add((char)i);
        }
        return todos;
    }
}
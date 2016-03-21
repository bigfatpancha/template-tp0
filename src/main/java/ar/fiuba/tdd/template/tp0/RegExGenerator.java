package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {
    private Integer maxLength;
    private static final String[] specialChars = {".", "[", "]", "\\", "*", "+", "?"};

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
            //Genero un entero que va desde desde hasta hasta.
            int num = randomIntEntre(0, token.getTotalValores());

            String unCaracter = token.getValor(num).toString();
            builder.append(unCaracter);
        }

        return builder.toString();
    }

    private static int randomIntEntre(Integer desde, Integer hasta) {
        Double D = Math.floor(desde + Math.random() * (1+hasta-desde));
        return D.intValue();
    }

    private int calcularIteraciones(String cuantificador) {
        if(cuantificador.equals("*")) {
            return randomIntEntre(0,this.maxLength);
        } else if(cuantificador.equals("+")) {
            return randomIntEntre(1,this.maxLength);
        } else if(cuantificador.equals("?")) {
            return randomIntEntre(0,1);
        } else if (cuantificador.equals("")) {
            return 1;
        } else
            return 0;
    }

    private List<Token> parseRegEx(String regEx) {
        List<Token> tokens = new ArrayList<Token>();
        int totalChars = regEx.length();
        for (int i = 0; i < totalChars; i++) {
            Token token = null;
            if (isLiteral(regEx.substring(i))){
                if (isCuantificador(regEx.substring(i++))) {
                    token = new Token(regEx.substring(i), new ArrayList<Character>(regEx.charAt(i--)));
                    i++;
                }
                else {
                    token = new Token("", new ArrayList<Character>(regEx.charAt(i--)));
                    i--;
                }
            } else if (isCorchete(regEx.substring(i))) {
                List<Character> valores = new ArrayList<Character>();
                while(!"]".equals(regEx.substring(i++))){
                    if (isLiteral(regEx.substring(i)))
                        valores.add(regEx.charAt(i));
                    if (isPunto(regEx.substring(i)))
                        valores.addAll(todosLosChars());
                    if (isContrBarra(regEx.substring(i)))
                        valores.add(regEx.charAt(i++));
                }
                if (isCuantificador(regEx.substring(i++)))
                    token = new Token(regEx.substring(i), valores);
                else {
                    token = new Token("", valores);
                    i--;
                }

            } else if (isContrBarra(regEx.substring(i))) {
                token = new Token(regEx.substring(i+2),  new ArrayList<Character>(regEx.charAt(i--)));
                i++;
            } else if (isPunto(regEx.substring(i))) {
                if (isCuantificador(regEx.substring(i++)))
                    token = new Token(regEx.substring(i), todosLosChars());
                else {
                    token = new Token("", todosLosChars());
                    i--;
                }
            }
            tokens.add(token);
        }
        return tokens;
    }

    private boolean isLiteral(String caracter) {
        for (int i = 0; i < specialChars.length; i++){
            if (caracter.equals(specialChars[i]))
                return false;
        }
        return true;
    }

    private boolean isCorchete(String caracter) {
        if (caracter.equals("["))
            return true;
        return false;
    }

    private boolean isCuantificador(String caracter) {
        if (caracter.equals("*") || caracter.equals("+") || caracter.equals("?"))
            return true;
        return false;
    }

    private boolean isContrBarra(String caracter) {
        if (caracter.equals("\\"))
            return true;
        return false;
    }

    private boolean isPunto(String caracter) {
        if (caracter.equals("."))
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
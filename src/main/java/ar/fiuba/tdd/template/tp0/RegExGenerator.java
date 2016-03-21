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
        List<String> results = new ArrayList<>();
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

    private String generateString(Token token) {
        StringBuilder builder = new StringBuilder();

        //Primero calculo cuántas veces corre el for
        int iterations = calcularIteraciones(token.getQuantificador());

        for (int i = 0; i < iterations; i++) {
            int num;
            if (token.getTotalValues() == 1)
                num = 0;
            else
                num = randomIntBetween(0, token.getTotalValues());

            String aChar = token.getValor(num).toString();
            builder.append(aChar);
        }

        return builder.toString();
    }

    private static int randomIntBetween(Integer desde, Integer hasta) {
        Double D = Math.floor(desde + Math.random() * (1 + hasta - desde));
        return D.intValue();
    }

    private int calcularIteraciones(Character cuantificador) {
        if (cuantificador.equals('*')) {
            return randomIntBetween(0, this.maxLength);
        } else if (cuantificador.equals('+')) {
            return randomIntBetween(1, this.maxLength);
        } else if (cuantificador.equals('?')) {
            return randomIntBetween(0, 1);
        } else if (cuantificador.equals('n')) {
            return 1;
        } else
            return 0;
    }

    private List<Token> parseRegEx(String regEx) {
        List<Token> tokens = new ArrayList<>();
        int totalChars = regEx.length();
        int i = 0;
        while (i < totalChars) {
            int posChar = i;
            int posQuantifier = i + 1;
            Token token = null;
            if (isLiteral(regEx.charAt(posChar))) {
                List<Character> values = new ArrayList<>();
                if ((posQuantifier < totalChars) && (isQuantifier(regEx.charAt(posQuantifier)))) {
                    values.add(regEx.charAt(posChar));
                    token = new Token(regEx.charAt(posQuantifier), values);
                    i += 2;
                } else {
                    values.add(regEx.charAt(posChar));
                    token = new Token('n', values);
                    i += 1;
                }
            } else if (isSquareBracket(regEx.charAt(posChar))) {
                posChar += 1;
                posQuantifier += 1;
                List<Character> values = new ArrayList<>();
                while (!isSquareBracketClose(regEx.charAt(posChar))) {
                    if (isLiteral(regEx.charAt(posChar))) {
                        values.add(regEx.charAt(posChar));
                    } else {
                        if (isDot(regEx.charAt(posChar))) {
                            values.addAll(allTheChars());
                        } else {
                            if (isBackSlash(regEx.charAt(posChar))) {
                                posChar += 1;
                                values.add(regEx.charAt(posChar));
                            }
                        }
                    }
                    posChar += 1;
                    posQuantifier += 1;
                }
                if ((posQuantifier < totalChars) && (isQuantifier(regEx.charAt(posQuantifier)))) {
                    token = new Token(regEx.charAt(posQuantifier), values);
                    i = posChar + 2;
                } else {
                    token = new Token('n', values);
                    i = posChar + 1;
                }
            } else if (isBackSlash(regEx.charAt(posChar))) {
                posChar += 1;
                posQuantifier += 1;
                List<Character> values = new ArrayList<>();
                if ((posQuantifier < totalChars) && (isQuantifier(regEx.charAt(posQuantifier)))) {
                    values.add(regEx.charAt(posChar));
                    token = new Token(regEx.charAt(posQuantifier), values);
                    i = posChar + 2;
                } else {
                    values.add(regEx.charAt(posChar));
                    token = new Token('n', values);
                    i = posChar + 1;
                }
            } else if (isDot(regEx.charAt(posChar))) {
                if ((posQuantifier < totalChars) && (isQuantifier(regEx.charAt(posQuantifier)))) {
                    token = new Token(regEx.charAt(posQuantifier), allTheChars());
                    i += posChar + 2;
                } else {
                    token = new Token('n', allTheChars());
                    i = posChar + 1;
                }
            }
            tokens.add(token);
        }
        return tokens;
    }

    private boolean isLiteral(Character aChar) {
        for (Character specialChar : specialChars) {
            if (aChar.equals(specialChar))
                return false;
        }
        return true;
    }

    private boolean isSquareBracket(Character aChar) {
        return aChar.equals('[');
    }

    private boolean isSquareBracketClose(Character aChar) {
        return (aChar.equals(']'));
    }

    private boolean isQuantifier(Character aChar) {
        return (aChar.equals('*') || aChar.equals('+') || aChar.equals('?'));
    }

    private boolean isBackSlash(Character aChar) {
        return (aChar.equals('\\'));
    }

    private boolean isDot(Character aChar) {
        return (aChar.equals('.'));
    }

    private List<Character> allTheChars() {
        List<Character> all = new ArrayList<>();
        for (int i = 0; i <= 255; i++) {
            all.add((char) i);
        }
        return all;
    }
}
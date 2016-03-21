package ar.fiuba.tdd.template.tp0;

import java.util.List;

/**
 * Created by Lucía on 19/3/2016.
 */
public class Token {
    private Character quantifier;
    private List<Character> values;

    public Token(Character quantifier, List<Character> values) {
        this.quantifier = quantifier;
        this.values = values;
    }

    public Character getQuantifier() {
        return this.quantifier;
    }

    public int getTotalValues() {
        return this.values.size() - 1;
    }

    public Character getValor(int pos) {
        return this.values.get(pos);
    }
}

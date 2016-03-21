package ar.fiuba.tdd.template.tp0;

import java.util.List;

/**
 * Created by Lucía on 19/3/2016.
 */
public class Token {
    private Character cuantificador;
    private List<Character> valores;

    public Token(Character cuantificador, List<Character> valores) {
        this.cuantificador = cuantificador;
        this.valores = valores;
    }

    public Character getQuantificador() {
        return this.cuantificador;
    }

    public int getTotalValues() {
        return this.valores.size() - 1;
    }

    public Character getValor(int pos) {
        return this.valores.get(pos);
    }
}

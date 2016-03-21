package ar.fiuba.tdd.template.tp0;

import java.util.List;

/**
 * Created by Lucía on 19/3/2016.
 */
public class Token {
    private String cuantificador;
    private List<Character> valores;

    public Token(String cuantificador, List<Character> valores) {
        this.cuantificador = cuantificador;
        this.valores = valores;
    }

    public String getCuantificador(){
        return this.cuantificador;
    }

    public int getTotalValores(){
        return this.valores.size();
    }

    public Character getValor(int pos) {
        return this.valores.get(pos);
    }
}

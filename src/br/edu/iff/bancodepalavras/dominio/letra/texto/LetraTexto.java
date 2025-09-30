package br.edu.iff.bancodepalavras.dominio.letra.texto;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;

/**
 * Implementação concreta de Letra para saída textual (console).
 */
public class LetraTexto extends Letra {
    public LetraTexto(char codigo) {
        super(codigo);
    }

    @Override
    public void exibir(Object contexto) {
        System.out.print(this.getCodigo());
    }
}

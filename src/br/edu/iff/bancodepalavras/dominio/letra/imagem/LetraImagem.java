package br.edu.iff.bancodepalavras.dominio.letra.imagem;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;


/**
 * Implementação concreta de Letra para saída baseada em imagem (placeholder).
 */
public class LetraImagem extends Letra {
    public LetraImagem(char codigo) {
        super(codigo);
    }

    @Override
    public void exibir(Object contexto) {
    }
}

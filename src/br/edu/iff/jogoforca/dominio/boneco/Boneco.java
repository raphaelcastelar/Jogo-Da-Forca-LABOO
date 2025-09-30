package br.edu.iff.jogoforca.dominio.boneco;

/**
 * Abstração para exibição do boneco.
 * - Implementações podem ser texto, imagem ou No-Op (Null Object) para testes/console.
 */
public interface Boneco {
    /**
     * Exibe o boneco conforme a quantidade de partes (erros cometidos).
     */
    void exibir(Object contexto, int partes);
}

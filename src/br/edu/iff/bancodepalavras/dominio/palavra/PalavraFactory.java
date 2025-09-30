package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;

/**
 * Fábrica abstrata para criação de Palavra (Factory Method exposto via interface).
 */
public interface PalavraFactory{
    public Palavra getPalavra(String palavra, Tema tema);
}

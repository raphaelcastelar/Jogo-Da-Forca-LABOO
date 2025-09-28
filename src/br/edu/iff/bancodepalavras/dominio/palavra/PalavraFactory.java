package br.edu.iff.bancodepalavras.dominio.palavra;

import bancodepalavras.dominio.tema.Tema;

public interface PalavraFactory{
    public Palavra getPalavra(String palavra, Tema tema);
}
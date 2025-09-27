package br.edu.iff.jogoforca.dominio.jogador;

import br.edu.iff.dominio.ObjetoDominioImpl;

public class Jogador extends ObjetoDominioImpl {

    private String nome;
    private int pontuacao = 0;

    // Construtor privado → só pode ser chamado via fábrica
    private Jogador(long id, String nome, int pontuacao) {
        super(id);
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    private Jogador(long id, String nome) {
        this(id, nome, 0);
    }

    // Fábrica e reconstituição
    public static Jogador reconstituir(long id, String nome, int pontuacao) {
        return new Jogador(id, nome, pontuacao);
    }

    public static Jogador criar(long id, String nome) {
        return new Jogador(id, nome);
    }

    // Métodos de negócio
    public void atualizarPontuacao(int pontos) {
        this.pontuacao += pontos;
    }

    public int getPontuacao() {
        return this.pontuacao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    
}




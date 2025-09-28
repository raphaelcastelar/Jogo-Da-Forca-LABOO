package br.edu.iff.bancodepalavras.dominio.tema;

import br.edu.iff.dominio.ObjetoDominio;

public class Tema implements ObjetoDominio {
    
    private long id;
    private String nome;
    
    public Tema(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
}

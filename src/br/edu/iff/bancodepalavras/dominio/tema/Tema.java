package br.edu.iff.bancodepalavras.dominio.tema;

import br.edu.iff.dominio.ObjetoDominio;

public class Tema implements ObjetoDominio {
    
    private long id;
    private String nome;
    
    public static Tema criar(long id, String nome) {
        return new Tema(id, nome);
    }
    
    public static Tema reconstituir(long id, String nome) {
        return new Tema(id, nome);
    }
    
    private Tema(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
}

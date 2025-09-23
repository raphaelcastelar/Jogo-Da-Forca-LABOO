package br.edu.iff.jogoforca.dominio.rodada;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private int id;
    private Palavra palavra;
    private boolean[] posicoesDescobertas;
    private String palavraArriscada = null;

    private Item(int id, Palavra palavra) {
        this.id = id;
        this.palavra = palavra;
        this.posicoesDescobertas = new boolean[palavra.getTamanho()];
        // Todas as posições começam como falsas (encobertas)
    }

    private Item(int id, Palavra palavra, int[] posicoesDescobertasArray, String palavraArriscada) {
        this.id = id;
        this.palavra = palavra;
        this.posicoesDescobertas = new boolean[palavra.getTamanho()];
        for (int pos : posicoesDescobertasArray) {
            if (pos >= 0 && pos < this.posicoesDescobertas.length) {
                this.posicoesDescobertas[pos] = true;
            }
        }
        this.palavraArriscada = palavraArriscada;
    }

    public static Item reconstituir(int id, Palavra palavra, int[] posicoesDescobertas, String palavraArriscada) {
        return new Item(id, palavra, posicoesDescobertas, palavraArriscada);
    }

    static Item criar(int id, Palavra palavra) {
        return new Item(id, palavra);
    }

    public boolean acertou() {
        if (palavraArriscada == null) {
            return false;
        }
        return palavra.comparar(palavraArriscada);
    }

    public boolean arriscou() {
        return palavraArriscada != null;
    }

    public String getPalavraArriscada() {
        return palavraArriscada;
    }

    void arriscar(String palavra) {
        if (this.palavraArriscada != null) {
            throw new IllegalStateException("Só é possível arriscar uma única vez.");
        }
        this.palavraArriscada = palavra;
    }

    boolean tentar(char codigo) {
        int[] pos = palavra.tentar(codigo);
        boolean encontrado = pos.length > 0;
        if (encontrado) {
            for (int p : pos) {
                if (p >= 0 && p < posicoesDescobertas.length) {
                    posicoesDescobertas[p] = true;
                }
            }
        }
        return encontrado;
    }

    public void exibir(Object contexto) {
        // Implementação assumindo que contexto é algo como PrintStream para exibição simples.
        Letra[] letras = palavra.getLetras();
        for (int i = 0; i < posicoesDescobertas.length; i++) {
            if (posicoesDescobertas[i]) {
                letras[i].exibir(contexto);
            } else {
                // Exibe letra encoberta (usando factory da palavra)
                palavra.getLetraFactory().getLetraEncoberta().exibir(contexto);
            }
        }
    }

    public boolean descobriu() {
        return acertou() || (qtdeLetrasEncobertas() == 0);
    }

    public int calcularPontosLetrasEncobertas(int valorPorLetraEncoberta) {
        return qtdeLetrasEncobertas() * valorPorLetraEncoberta;
    }

    public int qtdeLetrasEncobertas() {
        int count = 0;
        for (boolean descoberto : posicoesDescobertas) {
            if (!descoberto) {
                count++;
            }
        }
        return count;
    }

    public Letra[] getLetrasEncobertas() {
        List<Letra> encobertas = new ArrayList<>();
        Letra[] letras = palavra.getLetras();
        for (int i = 0; i < posicoesDescobertas.length; i++) {
            if (!posicoesDescobertas[i]) {
                encobertas.add(letras[i]);
            }
        }
        return encobertas.toArray(new Letra[0]);
    }

    public Letra[] getLetrasDescobertas() {
        List<Letra> descobertas = new ArrayList<>();
        Letra[] letras = palavra.getLetras();
        for (int i = 0; i < posicoesDescobertas.length; i++) {
            if (posicoesDescobertas[i]) {
                descobertas.add(letras[i]);
            }
        }
        return descobertas.toArray(new Letra[0]);
    }

    public Palavra getPalavra() {
        return palavra;
    }
}

package br.edu.iff.jogoforca.dominio.rodada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rodada extends ObjetoDominioImpl {
    private static int pontosPorLetraEncoberta = 15;
    private static int pontosQuandoDescobreTodasAsPalavras = 100;
    private static int maxErros = 10;
    private static int maxPalavras = 3;

    private static BonecoFactory bonecoFactory;

    private Item[] itens;
    private Letra[] erradas;
    private Jogador jogador;
    private Boneco boneco;

    private Rodada(long id, Item[] itens, Letra[] erradas, Jogador jogador) {
        super(id);
        this.itens = itens;
        this.erradas = erradas;
        this.jogador = jogador;
        this.boneco = getBonecoFactory().getBoneco();
    }

    private Rodada(long id, Palavra[] palavras, Jogador jogador) {
        super(id);
        if (bonecoFactory == null) {
            throw new IllegalStateException("BonecoFactory deve ser setado antes de criar uma Rodada.");
        }
        if (palavras == null || palavras.length == 0 || palavras.length > maxPalavras) {
            throw new IllegalArgumentException("Número de palavras inválido.");
        }
        Tema tema = palavras[0].getTema();
        for (Palavra p : palavras) {
            if (!p.getTema().equals(tema)) {
                throw new IllegalArgumentException("Todas as palavras devem ser do mesmo tema.");
            }
        }
        this.itens = new Item[palavras.length];
        for (int i = 0; i < palavras.length; i++) {
            this.itens[i] = Item.criar(i, palavras[i]);
        }
        this.erradas = new Letra[0];
        this.jogador = jogador;
        this.boneco = bonecoFactory.getBoneco();
    }

    public static Rodada reconstituir(long id, Item[] itens, Letra[] erradas, Jogador jogador) {
        return new Rodada(id, itens, erradas, jogador);
    }

    public static Rodada criar(long id, Palavra[] palavras, Jogador jogador) {
        return new Rodada(id, palavras, jogador);
    }

    public static BonecoFactory getBonecoFactory() {
        return bonecoFactory;
    }

    public static void setBonecoFactory(BonecoFactory factory) {
        bonecoFactory = factory;
    }

    public static void setPontosPorLetraEncoberta(int pontos) {
        pontosPorLetraEncoberta = pontos;
    }

    public static int getPontosPorLetraEncoberta() {
        return pontosPorLetraEncoberta;
    }

    public static void setPontosQuandoDescobreTodasAsPalavras(int pontos) {
        pontosQuandoDescobreTodasAsPalavras = pontos;
    }

    public static int getPontosQuandoDescobreTodasAsPalavras() {
        return pontosQuandoDescobreTodasAsPalavras;
    }

    public static void setMaxErros(int max) {
        maxErros = max;
    }

    public static int getMaxErros() {
        return maxErros;
    }

    public static void setMaxPalavras(int max) {
        maxPalavras = max;
    }

    public static int getMaxPalavras() {
        return maxPalavras;
    }

    public int getQtdeTentativas() {
        return getTentativas().length;
    }

    public int getQtdeAcertos() {
        return getCertas().length;
    }

    public int getQtdeErros() {
        return erradas.length;
    }

    public int getQtdeTentativasRestantes() {
        return maxErros - getQtdeErros();
    }

    public boolean arriscou() {
        for (Item item : itens) {
            if (item.arriscou()) {
                return true;
            }
        }
        return false;
    }

    public boolean descobriu() {
        for (Item item : itens) {
            if (!item.descobriu()) {
                return false;
            }
        }
        return true;
    }

    public boolean encerrou() {
        return arriscou() || descobriu() || (getQtdeTentativasRestantes() == 0);
    }

    public int calcularPontos() {
        if (descobriu()) {
            int pontos = pontosQuandoDescobreTodasAsPalavras;
            for (Item item : itens) {
                pontos += item.calcularPontosLetrasEncobertas(pontosPorLetraEncoberta);
            }
            return pontos;
        }
        return 0;
    }

    public Letra[] getErradas() {
        return Arrays.copyOf(erradas, erradas.length);
    }

    public Letra[] getCertas() {
        Set<Letra> certas = new HashSet<>();
        for (Item item : itens) {
            certas.addAll(Arrays.asList(item.getLetrasDescobertas()));
        }
        return certas.toArray(new Letra[0]);
    }

    public Letra[] getTentativas() {
        Set<Letra> tentativas = new HashSet<>(Arrays.asList(getCertas()));
        tentativas.addAll(Arrays.asList(erradas));
        return tentativas.toArray(new Letra[0]);
    }

    public void exibirLetrasErradas(Object contexto) {
        for (Letra letra : erradas) {
            letra.exibir(contexto);
        }
    }

    public void exibirPalavras(Object contexto) {
        for (Item item : itens) {
            item.exibir(contexto);
        }
    }

    public void exibirBoneco(Object contexto) {
        boneco.exibir(contexto, getQtdeErros());
    }

    public void exibirItens(Object contexto) {
        // Assumindo que exibe palavras, letras erradas e boneco
        exibirPalavras(contexto);
        exibirLetrasErradas(contexto);
        exibirBoneco(contexto);
    }

    public void arriscar(String[] palavras) {
        if (encerrou()) {
            throw new IllegalStateException("Rodada já encerrou.");
        }
        if (arriscou()) {
            throw new IllegalStateException("Só é possível arriscar uma única vez.");
        }
        if (palavras.length != itens.length) {
            throw new IllegalArgumentException("Número de palavras arriscadas deve ser igual ao número de itens.");
        }
        for (int i = 0; i < itens.length; i++) {
            itens[i].arriscar(palavras[i]);
        }
        if (encerrou()) {
            jogador.atualizarPontuacao(calcularPontos());
        }
    }

    public void tentar(char codigo) {
        if (encerrou()) {
            throw new IllegalStateException("Rodada já encerrou.");
        }
        boolean acerto = false;
        for (Item item : itens) {
            if (item.tentar(codigo)) {
                acerto = true;
            }
        }
        if (!acerto) {
            // Usar LetraFactory da primeira palavra (assumindo todas iguais)
            Letra letraErrada = itens[0].getPalavra().getLetraFactory().getLetra(codigo);
            List<Letra> novasErradas = new ArrayList<>(Arrays.asList(erradas));
            novasErradas.add(letraErrada);
            erradas = novasErradas.toArray(new Letra[0]);
        }
        if (encerrou()) {
            jogador.atualizarPontuacao(calcularPontos());
        }
    }

    public int getNumPalavras() {
        return itens.length;
    }

    public Palavra[] getPalavras() {
        Palavra[] palavras = new Palavra[itens.length];
        for (int i = 0; i < itens.length; i++) {
            palavras[i] = itens[i].getPalavra();
        }
        return palavras;
    }

    public Tema getTema() {
        if (itens.length > 0) {
            return itens[0].getPalavra().getTema();
        }
        return null;
    }

    public Jogador getJogador() {
        return jogador;
    }
}

package br.edu.iff.bancodepalavras.dominio.palavra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.LetraFactory;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.dominio.ObjetoDominioImpl;

/**
 * Entidade Palavra
 * Representa uma palavra composta por uma sequência de letras e associada a um tema.
 * Deverá ser adivinhada no jogo.
 * 
 * Padrões:
 * Factory Method: métodos estáticos criar() e reconstituir() para controle da criação do objeto;
 * Flyweight: usa o LetraFactory para reutilizar instâncias de Letra para economizar memória
 * e evitar que letras iguais sejam representadas por objetos distintos (como "a" em "banana").
 * 
 * - Value semantics nas letras: equals/hashCode em Letra permitem comparar corretamente sequências.
 */
public class Palavra extends ObjetoDominioImpl {

    // Fábrica de letras para obter instâncias compartilhadas (Flyweight)
    private static LetraFactory letraFactory;
    // Representação da letra "oculta" (ex.: '*'), fornecida pelo Flyweight
    private Letra letraEncoberta;
    // Array de letras que compõem a palavra
    private Letra[] letras;
    // Tema associado à palavra
    private Tema tema;

    //Getters e Setters
    public static LetraFactory getLetraFactory() {
        return letraFactory;
    }
    public static void setLetraFactory(LetraFactory letraFactory) {
        Palavra.letraFactory = letraFactory;
    }

    //Método construtor privado para respeitar princípios do Factory Method e redirecionar a criação do objeto para os métodos Criar() e Reconstituir()

    private Palavra(long id, String palavra, Tema tema) {
        super(id);

        if (letraFactory != null) {
            this.tema = tema;
            this.letraEncoberta = letraFactory.getLetraEncoberta(); // letra representando posição oculta

            letras = new Letra[palavra.length()];
            for (int i = 0; i < palavra.length(); i++) {
                letras[i] = letraFactory.getLetra(palavra.charAt(i));
            }
        } else {
            throw new IllegalStateException("LetraFactory não foi inicializada. Ela é necessária para instanciar Palavra");
        }
    }

    /**Métodos de fábrica (Factory Method) para controle da criação do objeto Palavra
    *
    * @param id O ID único da palavra
    * @param palavra O texto da palavra
    * @param tema O tema associado à palavra
    * @return A nova instância de Palavra criada
    */
    public static Palavra criar(long id, String palavra, Tema tema) {
        return new Palavra(id, palavra, tema);
    }

    //Reconstroi uma instância de Palavra a partir de dados persistidos (ex.: banco de dados)
    public static Palavra reconstituir (long id, String palavra, Tema tema) {
        return new Palavra(id, palavra, tema);
    }


    /**Retorna a Letra em uma posição específica
    *@param posicao é o índice da letra
    *@return a Letra na posição especificada
    */
    public Letra[] getLetras() {
        return letras;
    }

    /**
     * Exibe a palavra completa.
     * @param contexto O contexto de exibição (ex: um PrintStream para o console).
     */
    public Letra getLetra(int posicao) {
        return letras[posicao];
    }


    /**
     * Exibe a palavra parcialmente, mostrando apenas as letras em posições
     * que são marcadas como verdadeiras.
     * @param contexto O contexto de exibição.
     * @param posicoes Um array booleano onde 'true' indica uma posição de letra descoberta.
     */
    public void exibir(Object contexto) {
        for (Letra letra : letras) {
            letra.exibir(contexto);
        }
    }

     /**
     * Exibe a palavra parcialmente, mostrando apenas as letras em posições
     * que são marcadas como verdadeiras.
     * @param contexto O contexto de exibição.
     * @param posicoes Um array booleano onde 'true' indica uma posição de letra descoberta.
     */
    public void exibir(Object contexto, boolean[] posicoes) {
        for (int i = 0; i < posicoes.length; i++) {
            if (posicoes[i]) {
                letras[i].exibir(contexto);
            } else {
                letraEncoberta.exibir(contexto);
            }
        }
    }

    /**
     * Tenta adivinhar a letra na palavra.
     * Retorna um array com as posições onde a letra foi encontrada.
     * @param codigo
     * @return
     */
    public int[] tentar(char codigo) {
        List<Integer> listaPosicoes = new ArrayList<>();
        for (int i = 0; i < letras.length; i++) {
            if (letras[i].getCodigo() == codigo) {
                listaPosicoes.add(i);
            }
        }
        return listaPosicoes.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Retorna o tema associado à palavra.
     * @return
     */
    public Tema getTema() {
        return tema;
    }

    /**
     * Compara a palavra com a resposta (String) fornecida.
     * @param palavra
     * @return
     */
    public boolean comparar(String palavra) {
        Letra[] vetorLetrasPalavra = new Letra[palavra.length()];
        for (int i = 0; i < palavra.length(); i++) {
            vetorLetrasPalavra[i] = letraFactory.getLetra(palavra.charAt(i));
        }

        return Arrays.equals(letras, vetorLetrasPalavra);
    }

    /**
     * Retorna o tamanho da palavra (número de letras).
     * @return
     */
    public int getTamanho() {
        return letras.length;
    }

    /**
     * Retorna a representação em String da palavra (todas as letras).
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Letra letra : letras) {
            sb.append(letra.getCodigo());
        }
        return sb.toString();
    }





}

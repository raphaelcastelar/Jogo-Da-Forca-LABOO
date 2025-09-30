package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.factory.EntityFactory;
import br.edu.iff.repository.Repository;

/**
 * Fábrica concreta para criar instâncias de Palavra
 * Implementa o padrão Factory Method para a entidade Palavra
 * Obtém o próximo ID do repositório associado (via EntityFactory)
 * e delega a construção do objeto para a própria entidade (padrão Factory Method).
 * 
 * Segue o padrão Singleton para garantir uma única instância global.
 */
public class PalavraFactoryImpl extends EntityFactory implements PalavraFactory{
    private static PalavraFactoryImpl soleInstance;

    /** Construtor de instância 
    @param repository é o repositório de palavras que será injetado
    Esse método deve ser chamado uma única vez para criar a instância única (singleton) da fábrica.
    */
    public static void createSoleInstance(PalavraRepository repository){ 
        soleInstance = new PalavraFactoryImpl(repository);
    }

    //Retorna a instância única
    public static PalavraFactoryImpl getSoleInstance(){
        if (soleInstance == null){
            throw new IllegalStateException("Objeto PalavraFactoryImpl precisa ser criado");
        }
        return soleInstance;
    }

    /**
     * Construtor privado para evitar criação 
    * @param repository é o repositório de palavras que será injetado
    */
    private PalavraFactoryImpl(Repository repository) {
        super(repository);
    }
    /* Retorna o repositório de palavras associado a essa fábrica
     * 
    */
    private PalavraRepository getPalavraRepository(){
        return (PalavraRepository) this.getRepository();
    }

    /** Cria uma nova instância de Palavra
     * É um método de Factory Method que encapsula a lógica de criação de um objeto Palavra
     * @param palavra é o texto da palavra a ser criada
     * @param tema é o tema associado à palavra
     * @return a nova instância de Palavra criada
     */
    @Override
    public Palavra getPalavra(String palavra, Tema tema) {
        return Palavra.criar(this.getProximoId(), palavra, tema);
    }
}

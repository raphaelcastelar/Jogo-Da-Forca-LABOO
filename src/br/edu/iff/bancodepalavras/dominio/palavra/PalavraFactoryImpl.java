package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.factory.EntityFactory;
import br.edu.iff.repository.Repository;

/**
 * Fábrica concreta de Palavra
 * - Obtém o próximo ID do Repository (via EntityFactory) e delega a construção à entidade (Factory Method).
 */
public class PalavraFactoryImpl extends EntityFactory implements PalavraFactory{
    private static PalavraFactoryImpl soleInstance;

    public static void createSoleInstance(PalavraRepository repository){
        soleInstance = new PalavraFactoryImpl(repository);
    }

    public static PalavraFactoryImpl getSoleInstance(){
        if (soleInstance == null){
            throw new IllegalStateException("Objeto PalavraFactoryImpl precisa ser criado");
        }
        return soleInstance;
    }

    private PalavraFactoryImpl(Repository repository) {
        super(repository);
    }

    private PalavraRepository getPalavraRepository(){
        return (PalavraRepository) this.getRepository();
    }

    @Override
    public Palavra getPalavra(String palavra, Tema tema) {
        return Palavra.criar(this.getProximoId(), palavra, tema);
    }
}

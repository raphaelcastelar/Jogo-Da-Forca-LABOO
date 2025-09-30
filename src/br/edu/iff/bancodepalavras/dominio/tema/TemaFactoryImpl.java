package br.edu.iff.bancodepalavras.dominio.tema;

import br.edu.iff.factory.EntityFactory;
import br.edu.iff.repository.Repository;

public class TemaFactoryImpl extends EntityFactory implements TemaFactory {

    private static TemaFactoryImpl soleInstance;

    private TemaFactoryImpl(Repository repository) {
        super(repository);
    }

    public static void createSoleInstance(TemaRepository repository) {
        soleInstance = new TemaFactoryImpl(repository);
    }

    public static TemaFactoryImpl getSoleInstance() {
        if (soleInstance == null) {
            throw new IllegalStateException("Objeto TemaFactoryImpl precisa ser criado");
        }
        return soleInstance;
    }

    private TemaRepository getTemaRepository() {
        return (TemaRepository) this.getRepository();
    }

    @Override
    public Tema getTema(String nome) {
        // Verifica se o tema já existe no repositório
        Tema[] temasExistentes = getTemaRepository().getPorNome(nome);
        if (temasExistentes != null && temasExistentes.length > 0) {
            return temasExistentes[0];
        }
        // Se não existe, cria um novo com ID do repository
        return Tema.criar(this.getProximoId(), nome);
    }
}

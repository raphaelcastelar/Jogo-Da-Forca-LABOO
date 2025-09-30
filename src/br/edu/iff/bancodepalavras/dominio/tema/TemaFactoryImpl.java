package br.edu.iff.bancodepalavras.dominio.tema;

import br.edu.iff.factory.EntityFactory;

public class TemaFactoryImpl extends EntityFactory implements TemaFactory {
    
    private static TemaFactoryImpl soleInstance;
    private TemaRepository repository;
    
    private TemaFactoryImpl(TemaRepository repository) {
        this.repository = repository;
    }
    
    public static void createSoleInstance(TemaRepository repository) {
        soleInstance = new TemaFactoryImpl(repository);
    }
    
    public static TemaFactoryImpl getSoleInstance() {
        return soleInstance;
    }
    
    @Override
    public Tema getTema(String nome) {
        return Tema.criar(0, nome); // ID será definido pelo repository
    }
}

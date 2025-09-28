package br.edu.iff.bancodepalavras.dominio.tema;

public class TemaFactoryImpl implements TemaFactory {
    
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

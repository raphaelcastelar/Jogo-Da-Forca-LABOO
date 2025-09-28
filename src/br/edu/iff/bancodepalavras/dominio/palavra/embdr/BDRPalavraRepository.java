package br.edu.iff.bancodepalavras.dominio.palavra.embdr;

public class BDRPalavraRepository {

import bancodepalavras.dominio.palavra.Palavra;
import bancodepalavras.dominio.palavra.PalavraRepository;
import bancodepalavras.dominio.tema.Tema;
import repository.RepositoryException;

import java.util.List;

public class BDRPalavraRepository implements PalavraRepository {
    private static BDRPalavraRepository soleInstance;

    public static BDRPalavraRepository getSoleInstance(){
        if(soleInstance==null) {
            soleInstance = new BDRPalavraRepository();
        }
        return soleInstance;
    }

    @Override
    public Palavra getPorId(long id) {
        return null;
    }

    @Override
    public List<Palavra> getPorTema(Tema tema) {
        return null;
    }

    @Override
    public List<Palavra> getTodas() {
        return null;
    }

    @Override
    public Palavra getPalavra(String palavra) {
        return null;
    }

    @Override
    public void inserir(Palavra palavra) throws RepositoryException {

    }

    @Override
    public void atualizar(Palavra palavra) throws RepositoryException {

    }

    @Override
    public void remover(Palavra palavra) throws RepositoryException {

    }

    @Override
    public long getProximoId() {
        return 0;
    }
}

}

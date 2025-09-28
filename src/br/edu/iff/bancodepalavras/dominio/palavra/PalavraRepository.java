package br.edu.iff.bancodepalavras.dominio.palavra;

public interface PalavraRepository {

import bancodepalavras.dominio.tema.Tema;
import repository.Repository;
import repository.RepositoryException;

import java.util.List;

public interface PalavraRepository extends Repository {
    public Palavra getPorId(long id);
    public List<Palavra> getPorTema(Tema tema);
    public List<Palavra> getTodas();
    public Palavra getPalavra(String palavra);

    public void inserir(Palavra palavra) throws RepositoryException;
    public void atualizar(Palavra palavra) throws RepositoryException;
    public void remover(Palavra palavra) throws RepositoryException;
}
}

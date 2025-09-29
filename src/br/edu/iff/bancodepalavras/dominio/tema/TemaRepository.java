package br.edu.iff.bancodepalavras.dominio.tema;

import br.edu.iff.repository.RepositoryException;
import java.util.List;

public interface TemaRepository {
    
    Tema getPorId(long id);
    
    List<Tema> getPorNome(String nome);
    
    List<Tema> getTodos();
    
    void inserir(Tema tema) throws RepositoryException;
    
    void atualizar(Tema tema) throws RepositoryException;
    
    void remover(Tema tema) throws RepositoryException;
}

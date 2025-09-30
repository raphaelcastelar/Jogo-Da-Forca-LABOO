package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.repository.Repository;
import br.edu.iff.repository.RepositoryException;

public interface PalavraRepository extends Repository {
    void inserir(Palavra palavra) throws RepositoryException;
    void atualizar(Palavra palavra) throws RepositoryException;
    void remover(Palavra palavra) throws RepositoryException;
    Palavra getPorId(long id);
    Palavra getPalavra(String texto);
    Palavra[] getTodas();
    Palavra[] getPorTema(Tema tema);
}

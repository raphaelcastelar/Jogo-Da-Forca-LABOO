package br.edu.iff.jogoforca.dominio.jogador.embdr;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.repository.RepositoryException;


public class BDRJogadorRepository implements JogadorRepository {

    private static BDRJogadorRepository soleInstance;

    private BDRJogadorRepository() {}

    public static BDRJogadorRepository getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new BDRJogadorRepository();
        }
        return soleInstance;
    }

    @Override
    public void inserir(Jogador jogador) throws RepositoryException {
        // aqui seria implementação com JDBC/JPA
    }

    @Override
    public void atualizar(Jogador jogador) throws RepositoryException {
        // idem
    }

    @Override
    public void remover(Jogador jogador) throws RepositoryException {
        // idem
    }

    @Override
    public Jogador getPorId(long id) {
        // busca no banco
        return null;
    }

    @Override
    public Jogador getPorNome(String nome) {
        // busca no banco
        return null;
    }

    @Override
    public long getProximoId() {
        // no banco seria sequência/auto_increment
        return 0;
    }

    @Override
    public Jogador[] getTodos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodos'");
    }
}

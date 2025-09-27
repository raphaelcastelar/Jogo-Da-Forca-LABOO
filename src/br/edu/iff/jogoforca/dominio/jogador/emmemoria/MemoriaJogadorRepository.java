package br.edu.iff.jogoforca.dominio.jogador.emmemoria;

import java.util.ArrayList;
import java.util.List;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.repository.RepositoryException;

public class MemoriaJogadorRepository implements JogadorRepository {

    private static MemoriaJogadorRepository soleInstance;

    private List<Jogador> pool = new ArrayList<>();
    private long idSequence = 1;

    private MemoriaJogadorRepository() {}

    public static MemoriaJogadorRepository getSoleInstance() {
        if (soleInstance == null) {
            soleInstance = new MemoriaJogadorRepository();
        }
        return soleInstance;
    }

    @Override
    public void inserir(Jogador jogador) throws RepositoryException {
        pool.add(jogador);
    }

    @Override
    public void atualizar(Jogador jogador) throws RepositoryException {
        // Em memória, nada especial
    }

    @Override
    public void remover(Jogador jogador) throws RepositoryException {
        pool.remove(jogador);
    }

    @Override
    public Jogador getPorId(long id) {
        return pool.stream().filter(j -> j.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Jogador getPorNome(String nome) {
        return pool.stream().filter(j -> j.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
    }

    @Override
    public long getProximoId() {
        return idSequence++;
    }
}


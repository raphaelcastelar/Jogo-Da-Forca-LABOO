package br.edu.iff.jogoforca.dominio.jogador.emmemoria;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class MemoriaJogadorRepository implements JogadorRepository {
    private static MemoriaJogadorRepository soleInstance;
    private final Map<Long, Jogador> porId = new ConcurrentHashMap<>();
    private final Map<String, Long> porNome = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    private MemoriaJogadorRepository() {}
    public static MemoriaJogadorRepository getSoleInstance() {
        if (soleInstance == null) soleInstance = new MemoriaJogadorRepository();
        return soleInstance;
    }

    public long getProximoId() { return seq.getAndIncrement(); }

    public void inserir(Jogador j) throws RepositoryException {
        if (porId.containsKey(j.getId())) throw new RepositoryException("Jogador já existe");
        porId.put(j.getId(), j);
        porNome.put(j.getNome().toLowerCase(), j.getId());
    }

    public void atualizar(Jogador j) throws RepositoryException {
        if (!porId.containsKey(j.getId())) throw new RepositoryException("Jogador inexistente");
        porNome.entrySet().removeIf(e -> e.getValue().equals(j.getId()));
        porId.put(j.getId(), j);
        porNome.put(j.getNome().toLowerCase(), j.getId());
    }

    public void remover(Jogador j) throws RepositoryException {
        if (!porId.containsKey(j.getId())) throw new RepositoryException("Jogador inexistente");
        porId.remove(j.getId());
        porNome.entrySet().removeIf(e -> e.getValue().equals(j.getId()));
    }

    public Jogador getPorId(long id) { return porId.get(id); }

    public Jogador getPorNome(String nome) {
        Long id = porNome.get(nome.toLowerCase());
        return id == null ? null : porId.get(id);
    }

    public Jogador[] getTodos() { return porId.values().toArray(new Jogador[0]); }
}

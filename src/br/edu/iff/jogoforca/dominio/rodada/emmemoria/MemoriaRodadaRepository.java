package br.edu.iff.jogoforca.dominio.rodada.emmemoria;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaRepository;
import br.edu.iff.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository em memória para Rodada
 * - Índice por id e por id de jogador para histórico/consulta rápida.
 */
public class MemoriaRodadaRepository implements RodadaRepository {
    private static MemoriaRodadaRepository soleInstance;
    private final Map<Long, Rodada> porId = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> porJogadorId = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    private MemoriaRodadaRepository() {}
    public static MemoriaRodadaRepository getSoleInstance() {
        if (soleInstance == null) soleInstance = new MemoriaRodadaRepository();
        return soleInstance;
    }

    public long getProximoId() { return seq.getAndIncrement(); }

    public void inserir(Rodada r) throws RepositoryException {
        if (porId.containsKey(r.getId())) throw new RepositoryException("Rodada já existe");
        porId.put(r.getId(), r);
        porJogadorId.computeIfAbsent(r.getJogador().getId(), k -> new HashSet<>()).add(r.getId());
    }

    public void atualizar(Rodada r) throws RepositoryException {
        if (!porId.containsKey(r.getId())) throw new RepositoryException("Rodada inexistente");
        porJogadorId.values().forEach(s -> s.remove(r.getId()));
        porId.put(r.getId(), r);
        porJogadorId.computeIfAbsent(r.getJogador().getId(), k -> new HashSet<>()).add(r.getId());
    }

    public void remover(Rodada r) throws RepositoryException {
        if (!porId.containsKey(r.getId())) throw new RepositoryException("Rodada inexistente");
        porId.remove(r.getId());
        porJogadorId.values().forEach(s -> s.remove(r.getId()));
    }

    public Rodada getPorId(long id) { return porId.get(id); }

    public Rodada[] getPorJogador(Jogador jogador) {
        Set<Long> ids = porJogadorId.getOrDefault(jogador.getId(), Collections.emptySet());
        List<Rodada> list = new ArrayList<>();
        for (Long id: ids) list.add(porId.get(id));
        return list.toArray(new Rodada[0]);
    }

    public Rodada[] getTodas() { return porId.values().toArray(new Rodada[0]); }
}

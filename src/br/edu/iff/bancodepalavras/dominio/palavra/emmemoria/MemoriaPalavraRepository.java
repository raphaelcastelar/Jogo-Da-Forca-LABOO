package br.edu.iff.bancodepalavras.dominio.palavra.emmemoria;

import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository em memória para Palavra
 * - Índices: por id, por texto (case-insensitive) e por id de tema.
 * - Thread-safe com ConcurrentHashMap; IDs gerados com AtomicLong.
 */
public class MemoriaPalavraRepository implements PalavraRepository {
    private static MemoriaPalavraRepository soleInstance;
    private final Map<Long, Palavra> porId = new ConcurrentHashMap<>();
    private final Map<String, Long> porTexto = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> porTemaId = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    private MemoriaPalavraRepository() {}
    public static MemoriaPalavraRepository getSoleInstance() {
        if (soleInstance == null) soleInstance = new MemoriaPalavraRepository();
        return soleInstance;
    }

    public long getProximoId() { return seq.getAndIncrement(); }

    public void inserir(Palavra p) throws RepositoryException {
        if (porId.containsKey(p.getId())) throw new RepositoryException("Palavra já existe");
        porId.put(p.getId(), p);
        porTexto.putIfAbsent(p.toString().toLowerCase(), p.getId());
        porTemaId.computeIfAbsent(p.getTema().getId(), k -> new HashSet<>()).add(p.getId());
    }

    public void atualizar(Palavra p) throws RepositoryException {
        if (!porId.containsKey(p.getId())) throw new RepositoryException("Palavra inexistente");
        porTexto.entrySet().removeIf(e -> e.getValue().equals(p.getId()));
        porTemaId.values().forEach(s -> s.remove(p.getId()));
        porId.put(p.getId(), p);
        porTexto.put(p.toString().toLowerCase(), p.getId());
        porTemaId.computeIfAbsent(p.getTema().getId(), k -> new HashSet<>()).add(p.getId());
    }

    public void remover(Palavra p) throws RepositoryException {
        if (!porId.containsKey(p.getId())) throw new RepositoryException("Palavra inexistente");
        porId.remove(p.getId());
        porTexto.entrySet().removeIf(e -> e.getValue().equals(p.getId()));
        porTemaId.values().forEach(s -> s.remove(p.getId()));
    }

    public Palavra getPorId(long id) { return porId.get(id); }

    public Palavra getPalavra(String texto) {
        Long id = porTexto.get(texto.toLowerCase());
        return id == null ? null : porId.get(id);
    }

    public Palavra[] getTodas() { return porId.values().toArray(new Palavra[0]); }

    public Palavra[] getPorTema(Tema tema) {
        Set<Long> ids = porTemaId.getOrDefault(tema.getId(), Collections.emptySet());
        List<Palavra> list = new ArrayList<>();
        for (Long id: ids) list.add(porId.get(id));
        return list.toArray(new Palavra[0]);
    }
}

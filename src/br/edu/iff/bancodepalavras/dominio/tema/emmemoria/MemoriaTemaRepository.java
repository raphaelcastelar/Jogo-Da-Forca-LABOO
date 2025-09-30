package br.edu.iff.bancodepalavras.dominio.tema.emmemoria;

import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoriaTemaRepository implements TemaRepository {
    private static MemoriaTemaRepository soleInstance;
    private final Map<Long, Tema> porId = new ConcurrentHashMap<>();
    private final Map<String, Set<Long>> porNome = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    private MemoriaTemaRepository() {}
    public static MemoriaTemaRepository getSoleInstance() {
        if (soleInstance == null) soleInstance = new MemoriaTemaRepository();
        return soleInstance;
    }

    public long getProximoId() { return seq.getAndIncrement(); }

    public void inserir(Tema tema) throws RepositoryException {
        if (porId.containsKey(tema.getId())) throw new RepositoryException("Tema já existe");
        porId.put(tema.getId(), tema);
        porNome.computeIfAbsent(tema.getNome().toLowerCase(), k -> new HashSet<>()).add(tema.getId());
    }

    public void atualizar(Tema tema) throws RepositoryException {
        if (!porId.containsKey(tema.getId())) throw new RepositoryException("Tema inexistente");
        porNome.values().forEach(s -> s.remove(tema.getId()));
        porNome.computeIfAbsent(tema.getNome().toLowerCase(), k -> new HashSet<>()).add(tema.getId());
        porId.put(tema.getId(), tema);
    }

    public void remover(Tema tema) throws RepositoryException {
        if (!porId.containsKey(tema.getId())) throw new RepositoryException("Tema inexistente");
        porId.remove(tema.getId());
        porNome.values().forEach(s -> s.remove(tema.getId()));
    }

    public Tema getPorId(long id) { return porId.get(id); }

    public Tema[] getTodos() { return porId.values().toArray(new Tema[0]); }

    public Tema[] getPorNome(String nome) {
        Set<Long> ids = porNome.getOrDefault(nome.toLowerCase(), Collections.emptySet());
        List<Tema> list = new ArrayList<>();
        for (Long id: ids) list.add(porId.get(id));
        return list.toArray(new Tema[0]);
    }
}

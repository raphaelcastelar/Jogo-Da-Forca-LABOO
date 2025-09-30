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

    /* Construtor da instância única */
    private MemoriaPalavraRepository() {}
    public static MemoriaPalavraRepository getSoleInstance() { 
        if (soleInstance == null) soleInstance = new MemoriaPalavraRepository();
        return soleInstance;
    }

    /*Retorna o próximo ID sequencial seguro para ser atribuído a uma nova Palavra */
    public long getProximoId() { return seq.getAndIncrement(); } 

    /*Insere uma nova palavra no repositório*/
    /*@param  p é é o objeto Palavra a ser inserido*/
    /*@throws RepositoryException se a palavra com o mesmo ID já existe */
    public void inserir(Palavra p) throws RepositoryException {
        if (porId.containsKey(p.getId())) throw new RepositoryException("Palavra já existe");
        porId.put(p.getId(), p);
        porTexto.putIfAbsent(p.toString().toLowerCase(), p.getId());
        porTemaId.computeIfAbsent(p.getTema().getId(), k -> new HashSet<>()).add(p.getId());
    }

    /*Atualiza uma palavra existente no repositório 
     *@param p é é o objeto Palavra a ser atualizado
     @throws RepositoryException se a palavra com o ID não existe
    */
    public void atualizar(Palavra p) throws RepositoryException {
        if (!porId.containsKey(p.getId())) throw new RepositoryException("Palavra inexistente");
        porTexto.entrySet().removeIf(e -> e.getValue().equals(p.getId()));
        porTemaId.values().forEach(s -> s.remove(p.getId()));
        porId.put(p.getId(), p);
        porTexto.put(p.toString().toLowerCase(), p.getId());
        porTemaId.computeIfAbsent(p.getTema().getId(), k -> new HashSet<>()).add(p.getId());
    }

    /*Remove uma palavra existente no repositório
     * @param p é é o objeto Palavra a ser removido
     * @throws RepositoryException se a palavra com o ID não existe
     */
    public void remover(Palavra p) throws RepositoryException {
        if (!porId.containsKey(p.getId())) throw new RepositoryException("Palavra inexistente");
        porId.remove(p.getId());
        porTexto.entrySet().removeIf(e -> e.getValue().equals(p.getId()));
        porTemaId.values().forEach(s -> s.remove(p.getId()));
    }

    /*Busca uma palavra pelo seu ID único
     * @param id é o ID da palavra a ser buscada
     * @return a palavra com o ID especificado, ou null se não encontrada
     */
    public Palavra getPorId(long id) { return porId.get(id); }


    /*Busca uma palavra pelo seu ID único
     * @param texto é o texto da palavra a ser buscada
     * @return a palavra com o texto especificado, ou null se não encontrada
     */
    public Palavra getPalavra(String texto) {
        Long id = porTexto.get(texto.toLowerCase());
        return id == null ? null : porId.get(id);
    }

    /*Retorna todas as palavras armazenadas no repositório
     * @return um array com todas as palavras
     */
    public Palavra[] getTodas() { return porId.values().toArray(new Palavra[0]); }

    /*Retorna todas as palavras associadas a um tema específico
     * @param tema é o tema cujas palavras serão buscadas
     * @return um array com todas as palavras do tema especificado
     */
    public Palavra[] getPorTema(Tema tema) {
        Set<Long> ids = porTemaId.getOrDefault(tema.getId(), Collections.emptySet());
        List<Palavra> list = new ArrayList<>();
        for (Long id: ids) list.add(porId.get(id));
        return list.toArray(new Palavra[0]);
    }
}

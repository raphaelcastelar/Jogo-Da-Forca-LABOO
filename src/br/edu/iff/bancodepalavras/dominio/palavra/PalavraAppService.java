package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.repository.RepositoryException;

/**
 * Application Service para o agregado Palavra
 * Atua como uma Fachada para as operações relacionadas à entidade Palavra
 * Orquestra validações, a criação de novos objetos Palavra via PalavraFactory
 * e a persistência via PalavraRepository e TemaRepository.
 * 
 * Segue o padrão Singleton Parametrizado
 */
public class PalavraAppService {
    PalavraRepository palavraRepository;
    TemaRepository temaRepository;
    PalavraFactory palavraFactory;
    private static PalavraAppService soleInstance;

     /**
     * Cria a única instância do PalavraAppService.
     * Deve ser chamado apenas uma vez no "Composition Root" da aplicação.
     *
     * @param palavraRepository O repositório de palavras a ser injetado.
     * @param temaRepository O repositório de temas a ser injetado.
     * @param palavraFactory A fábrica de palavras a ser injetada.
     */
    
    public static void createSoleInstance(PalavraRepository palavraRepository, TemaRepository temaRepository, PalavraFactory palavraFactory){
        soleInstance = new PalavraAppService(palavraRepository, temaRepository, palavraFactory);
    }

    /**
     * Retorna a única instância do PalavraAppService.
     *
     * @return A instância única do serviço.
     * @throws IllegalStateException se a instância não foi criada.
     */
    public static PalavraAppService getSoleInstance(){
        if (soleInstance == null){
            throw new IllegalStateException("Objeto PalavraAppService precisa ser criado");
        }
        return soleInstance;
    }

    /**
     * Construtor privado para evitar instanciação direta (padrão Singleton).
     */
    private PalavraAppService(PalavraRepository palavraRepository, TemaRepository temaRepository, PalavraFactory palavraFactory) {
        this.palavraRepository = palavraRepository;
        this.temaRepository = temaRepository;
        this.palavraFactory = palavraFactory;
    }

    /**
     * Orquestra a criação de uma nova palavra no sistema.
     * Realiza validações de pré-condição e delega a criação à fábrica e a
     * persistência ao repositório.
     *
     * @param palavra O texto da nova palavra.
     * @param idTema O ID do tema ao qual a palavra pertence.
     * @return {@code true} se a palavra for inserida com sucesso ou já existir,
     * {@code false} se ocorrer uma RepositoryException.
     * @throws IllegalArgumentException se o tema não for encontrado no repositório.
     */
    public boolean novaPalavra(String palavra, long idTema){
         // Pré-condição: o tema deve existir
        if (temaRepository.getPorId(idTema) == null){
            throw new IllegalArgumentException("Tema não encontrado no repositório");
        }
        
        // Se a palavra já existe, não faz nada e retorna sucesso
        if (palavraRepository.getPalavra(palavra) != null){
            return true;
        }

        // Delega a criação à fábrica e a inserção ao repositório
        try {
            palavraRepository.inserir(palavraFactory.getPalavra(palavra, temaRepository.getPorId(idTema)));
            return true;
        } catch (RepositoryException e) {
            return false;
        }


    }
}

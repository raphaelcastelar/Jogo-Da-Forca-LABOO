package br.edu.iff.bancodepalavras.dominio.palavra;

import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.repository.RepositoryException;

/**
 * Application Service para o agregado Palavra
 * - Orquestra validações de Tema, existência prévia e persistência.
 */
public class PalavraAppService {
    PalavraRepository palavraRepository;
    TemaRepository temaRepository;
    PalavraFactory palavraFactory;
    private static PalavraAppService soleInstance;

    public static void createSoleInstance(PalavraRepository palavraRepository, TemaRepository temaRepository, PalavraFactory palavraFactory){
        soleInstance = new PalavraAppService(palavraRepository, temaRepository, palavraFactory);
    }

    public static PalavraAppService getSoleInstance(){
        if (soleInstance == null){
            throw new IllegalStateException("Objeto PalavraAppService precisa ser criado");
        }
        return soleInstance;
    }

    private PalavraAppService(PalavraRepository palavraRepository, TemaRepository temaRepository, PalavraFactory palavraFactory) {
        this.palavraRepository = palavraRepository;
        this.temaRepository = temaRepository;
        this.palavraFactory = palavraFactory;
    }

    public boolean novaPalavra(String palavra, long idTema){
        if (temaRepository.getPorId(idTema) == null){
            throw new IllegalArgumentException("Tema não encontrado no repositório");
        }

        if (palavraRepository.getPalavra(palavra) != null){
            return true;
        }

        try {
            palavraRepository.inserir(palavraFactory.getPalavra(palavra, temaRepository.getPorId(idTema)));
            return true;
        } catch (RepositoryException e) {
            return false;
        }


    }
}

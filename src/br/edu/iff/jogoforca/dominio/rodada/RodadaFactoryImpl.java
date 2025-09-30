package br.edu.iff.jogoforca.dominio.rodada;

import java.util.concurrent.ThreadLocalRandom;

import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;

/**
 * Base com dependências comuns às fábricas de rodada.
 * Padrões: Template Method (utilitários comuns) + Abstract Factory (subclasses criam Rodada concreta).
 */
public abstract class RodadaFactoryImpl implements RodadaFactory {

    protected final TemaRepository temaRepo;
    protected final PalavraRepository palavraRepo;
    protected final RodadaRepository rodadaRepo;

    protected RodadaFactoryImpl(TemaRepository temaRepo,
                                PalavraRepository palavraRepo,
                                RodadaRepository rodadaRepo) {
        this.temaRepo = temaRepo;
        this.palavraRepo = palavraRepo;
        this.rodadaRepo = rodadaRepo;
    }

    protected long nextRodadaId() {
        long id = rodadaRepo.getProximoId();
        System.out.println("[RodadaFactoryImpl] Próximo id de Rodada: " + id);
        return id;
    }

    protected ThreadLocalRandom rng() { return ThreadLocalRandom.current(); }

    protected int clamp(int v, int min, int max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}

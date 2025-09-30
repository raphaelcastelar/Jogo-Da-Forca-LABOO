package br.edu.iff.jogoforca.dominio.rodada.sorteio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaFactoryImpl;
import br.edu.iff.jogoforca.dominio.rodada.RodadaRepository;

/**
 * Fábrica concreta de Rodada por Sorteio
 * - Escolhe aleatoriamente um Tema existente e 1..N palavras desse tema (sem repetição).
 * - Mantém instância única via AtomicReference (Singleton sem synchronized).
 */
public final class RodadaSorteioFactory extends RodadaFactoryImpl {

    private final int maxPalavras;
    private static final AtomicReference<RodadaSorteioFactory> REF = new AtomicReference<>();

    private RodadaSorteioFactory(TemaRepository temaRepo,
                                 PalavraRepository palavraRepo,
                                 RodadaRepository rodadaRepo,
                                 int maxPalavras) {
        super(temaRepo, palavraRepo, rodadaRepo);
        this.maxPalavras = (maxPalavras <= 0) ? 3 : Math.min(maxPalavras, 3);
    }

    /**
     * Cria a instância única se ainda não existir (idempotente).
     */
    public static RodadaSorteioFactory createIfAbsent(TemaRepository temaRepo,
                                                      PalavraRepository palavraRepo,
                                                      RodadaRepository rodadaRepo,
                                                      int maxPalavras) {
        boolean created = REF.compareAndSet(null, new RodadaSorteioFactory(temaRepo, palavraRepo, rodadaRepo, maxPalavras));
        if (created) {
            System.out.println("[RodadaSorteioFactory] Instância criada (maxPalavras=" + maxPalavras + ")");
        }
        return REF.get();
    }

    /**
     * Obtém a instância única (falha se não criada).
     */
    public static RodadaSorteioFactory getSoleInstance() {
        RodadaSorteioFactory inst = REF.get();
        if (inst == null) throw new IllegalStateException("RodadaSorteioFactory não criada");
        return inst;
    }

    @Override
    public Rodada getRodada(Jogador jogador) {
        if (jogador == null) throw new IllegalArgumentException("Jogador nulo");

        Tema[] temas = temaRepo.getTodos();
        if (temas == null || temas.length == 0) {
            throw new IllegalStateException("Não há temas cadastrados");
        }
        Tema tema = temas[rng().nextInt(temas.length)];
        System.out.println("[RodadaSorteioFactory] Tema sorteado: " + tema.getNome());

        Palavra[] pool = palavraRepo.getPorTema(tema);
        if (pool == null || pool.length == 0) {
            throw new IllegalStateException("Tema sem palavras: " + tema.getNome());
        }

        int qtd = clamp(rng().nextInt(1, 4), 1, Math.min(maxPalavras, pool.length));
        System.out.println("[RodadaSorteioFactory] Quantidade de palavras: " + qtd);
        Palavra[] escolhidas = amostrarSemRepeticao(pool, qtd);

        long id = nextRodadaId();
        System.out.println("[RodadaSorteioFactory] Criando Rodada id=" + id + " para jogador=" + jogador.getNome());
        return Rodada.criar(id, escolhidas, jogador); // ajuste se assinatura diferir
    }

    private Palavra[] amostrarSemRepeticao(Palavra[] pool, int qtd) {
        boolean[] usado = new boolean[pool.length];
        List<Palavra> out = new ArrayList<>(qtd);
        while (out.size() < qtd) {
            int i = rng().nextInt(pool.length);
            if (!usado[i]) {
                usado[i] = true;
                out.add(pool[i]);
            }
        }
        return out.toArray(new Palavra[0]);
    }
}

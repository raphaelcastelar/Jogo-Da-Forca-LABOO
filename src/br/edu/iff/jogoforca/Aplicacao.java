package br.edu.iff.jogoforca;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Scanner;

import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.jogoforca.dominio.rodada.RodadaFactory;
import br.edu.iff.jogoforca.dominio.rodada.RodadaRepository;
import br.edu.iff.jogoforca.emmemoria.MemoriaRepositoryFactory;
import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.texto.LetraTextoFactory;
import br.edu.iff.jogoforca.dominio.boneco.NoOpBonecoFactory;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaAppService;
import br.edu.iff.jogoforca.dominio.rodada.sorteio.RodadaSorteioFactory;
import br.edu.iff.repository.RepositoryException;

/**
 * Padrões:
 * - Singleton (Initialization-on-demand holder): instância única de Aplicacao sem synchronized.
 * - Service Locator/Composition Root: centraliza as factories/repos usados pela aplicação.
 */
public final class Aplicacao {

    private RepositoryFactory repositoryFactory;
    private RodadaFactory rodadaFactory;
    private final AtomicBoolean configurada = new AtomicBoolean(false);

    private Aplicacao() {}

    private static final class Holder {
        private static final Aplicacao INSTANCE = new Aplicacao();
    }

    public static Aplicacao getSoleInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Configura para repositórios em memória. Só efetiva a primeira chamada (idempotente).
     */
    public Aplicacao configurarParaMemoria() {
        if (configurada.compareAndSet(false, true)) {
            System.out.println("[Aplicacao] Configurando para repositórios em memória...");
            this.repositoryFactory = MemoriaRepositoryFactory.getSoleInstance();
            System.out.println("[Aplicacao] Repositórios em memória prontos.");
        } else {
            System.out.println("[Aplicacao] Já estava configurada. Ignorando nova configuração.");
        }
        return this;
    }

    /**
     * Define a fábrica de rodadas. Só efetiva a primeira chamada.
     */
    public Aplicacao definirRodadaFactory(RodadaFactory rodadaFactory) {
        assertConfigurada();
        if (this.rodadaFactory == null && rodadaFactory != null) {
            System.out.println("[Aplicacao] Definindo RodadaFactory: " + rodadaFactory.getClass().getSimpleName());
            this.rodadaFactory = rodadaFactory;
        } else {
            System.out.println("[Aplicacao] RodadaFactory já definida. Mantendo atual.");
        }
        return this;
    }

    public RepositoryFactory getRepositoryFactory() {
        assertConfigurada();
        return repositoryFactory;
    }

    public RodadaFactory getRodadaFactory() {
        assertConfigurada();
        if (rodadaFactory == null) throw new IllegalStateException("RodadaFactory não definida");
        return rodadaFactory;
    }

    public TemaRepository getTemaRepository() { return getRepositoryFactory().getTemaRepository(); }
    public PalavraRepository getPalavraRepository() { return getRepositoryFactory().getPalavraRepository(); }
    public JogadorRepository getJogadorRepository() { return getRepositoryFactory().getJogadorRepository(); }
    public RodadaRepository getRodadaRepository() { return getRepositoryFactory().getRodadaRepository(); }

    private void assertConfigurada() {
        if (!configurada.get()) throw new IllegalStateException("Aplicacao não configurada");
    }

    /**
     * Entry-point CLI simples (sem boneco): permite jogar pelo console.
     */
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        Aplicacao app = Aplicacao.getSoleInstance().configurarParaMemoria();

        // Fábricas necessárias pelo domínio
        br.edu.iff.bancodepalavras.dominio.palavra.Palavra.setLetraFactory(LetraTextoFactory.getSoleInstance());
        Rodada.setBonecoFactory(NoOpBonecoFactory.getSoleInstance());

        // Fábrica concreta de rodadas (sorteio)
        RodadaSorteioFactory.createIfAbsent(
            app.getTemaRepository(),
            app.getPalavraRepository(),
            app.getRodadaRepository(),
            Rodada.getMaxPalavras()
        );
        app.definirRodadaFactory(RodadaSorteioFactory.getSoleInstance());

        // Carga mínima de dados
        try {
            CargaInicial.executar(app);
        } catch (RepositoryException e) {
            System.err.println("[Aplicacao] Aviso: não foi possível carregar dados iniciais: " + e.getMessage());
        }

        System.out.print("Iniciar a partida? (sim/nao): ");
        String iniciar = in.nextLine().trim().toLowerCase();
        if (!"sim".equals(iniciar)) {
            System.out.println("Encerrando o jogo.");
            return;
        }

        System.out.print("Digite o seu nome: ");
        String nomeJogador = in.nextLine().trim();

        RodadaAppService rodadas = new RodadaAppService(app);
        Rodada rodada;
        try {
            rodada = rodadas.novaRodada(nomeJogador);
        } catch (RepositoryException e) {
            System.err.println("Erro ao criar rodada: " + e.getMessage());
            return;
        }

        System.out.println("Tema: " + rodada.getTema().getNome());

        while (!rodada.encerrou()) {
            System.out.println();
            System.out.println("Tentativas restantes: " + rodada.getQtdeTentativasRestantes());
            System.out.print("Tentativas anteriores: ");
            for (Letra tentativa : rodada.getTentativas()) {
                tentativa.exibir(null);
                System.out.print(" | ");
            }
            System.out.println();

            System.out.println("\n--- Palavras ---");
            rodada.exibirPalavras(null);
            System.out.println();

            System.out.println("(1) Tentar letra");
            System.out.println("(2) Arriscar palavra(s)");
            System.out.print("Escolha: ");
            String opcao = in.nextLine().trim();

            switch (opcao) {
                case "1": {
                    System.out.print("Digite a letra: ");
                    String entrada = in.nextLine().trim();
                    if (entrada.isEmpty()) {
                        System.out.println("Nenhuma letra informada.");
                        break;
                    }
                    char c = entrada.charAt(0);
                    try {
                        rodada.tentar(c);
                    } catch (RuntimeException ex) {
                        System.out.println("Erro: " + ex.getMessage());
                    }
                    break;
                }
                case "2": {
                    int n = rodada.getNumPalavras();
                    String[] palpites = new String[n];
                    for (int i = 0; i < n; i++) {
                        System.out.print("Chute a palavra " + (i+1) + ": ");
                        palpites[i] = in.nextLine().trim();
                    }
                    try {
                        rodada.arriscar(palpites);
                    } catch (RuntimeException ex) {
                        System.out.println("Erro: " + ex.getMessage());
                    }
                    break;
                }
                default:
                    System.out.println("Opção inválida.");
            }

            if (rodada.descobriu()) {
                System.out.println("\nParabéns, " + rodada.getJogador().getNome() + "!");
                System.out.println("Pontuação: " + rodada.calcularPontos());
                break;
            }
        }

        if (!rodada.descobriu()) {
            System.out.println("\nVocê não acertou desta vez, " + rodada.getJogador().getNome() + ".");
        }

        System.out.println("Obrigado por jogar!");
    }
}

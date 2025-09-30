package br.edu.iff.jogoforca;

import java.util.Scanner;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.texto.LetraTextoFactory;
import br.edu.iff.bancodepalavras.dominio.palavra.Palavra;
import br.edu.iff.bancodepalavras.dominio.palavra.PalavraRepository;
import br.edu.iff.bancodepalavras.dominio.tema.Tema;
import br.edu.iff.bancodepalavras.dominio.tema.TemaFactoryImpl;
import br.edu.iff.bancodepalavras.dominio.tema.TemaRepository;
import br.edu.iff.jogoforca.dominio.boneco.NoOpBonecoFactory;
import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.jogoforca.dominio.rodada.Rodada;
import br.edu.iff.jogoforca.dominio.rodada.RodadaAppService;
import br.edu.iff.jogoforca.dominio.rodada.RodadaFactory;
import br.edu.iff.jogoforca.dominio.rodada.RodadaRepository;
import br.edu.iff.jogoforca.dominio.rodada.sorteio.RodadaSorteioFactory;
import br.edu.iff.jogoforca.emmemoria.MemoriaRepositoryFactory;
import br.edu.iff.repository.RepositoryException;

/**
 * Composition Root / Launcher
 * - Responsável por montar o grafo de objetos da aplicação (sem framework),
 *   escolhendo as fábricas e repositórios concretos.
 * Padrões em uso neste fluxo:
 * - Repository (MemoriaRepositoryFactory) para isolamento de persistência.
 * - Factory Method nas entidades (ex.: Palavra.criar, Tema.criar, Jogador.criar).
 * - Flyweight para letra (LetraFactory + pool interno em LetraFactoryImpl).
 * - Null Object para boneco (NoOpBonecoFactory) — evita NPE e exibição real.
 * - Facade/Application Service (RodadaAppService) para orquestrar casos de uso.
 */
public final class Aplicacao {

    public static void main(String[] args) throws Exception {
        // fábricas visuais requeridas pelo domínio
        br.edu.iff.bancodepalavras.dominio.palavra.Palavra.setLetraFactory(LetraTextoFactory.getSoleInstance());
        Rodada.setBonecoFactory(NoOpBonecoFactory.getSoleInstance());

        // somente repositórios em memória
        RepositoryFactory repos = MemoriaRepositoryFactory.getSoleInstance();
        TemaRepository temaRepo = repos.getTemaRepository();
        PalavraRepository palavraRepo = repos.getPalavraRepository();
        JogadorRepository jogadorRepo = repos.getJogadorRepository();
        RodadaRepository rodadaRepo = repos.getRodadaRepository();

        // carga inicial mínima para permitir jogar
        cargaInicial(temaRepo, palavraRepo);

        // fábrica concreta de rodada (sorteio)
        RodadaFactory rodadaFactory = RodadaSorteioFactory.createIfAbsent(
            temaRepo, palavraRepo, rodadaRepo, Rodada.getMaxPalavras()
        );

        // jogador padrão (sem prompt de início); usa args[0] se existir
        String nome = (args != null && args.length > 0 && !args[0].isBlank()) ? args[0].trim() : "Jogador";
        Jogador jogador = obterOuCriarJogador(jogadorRepo, nome);

        // cria a rodada na largada
        RodadaAppService app = new RodadaAppService(rodadaFactory, jogadorRepo, rodadaRepo);
        Rodada rodada = app.novaRodada(jogador);

        // UI de console
        cabecalho("Jogo da Forca - memoria");
        System.out.println("Tema selecionado: " + rodada.getTema().getNome());
        System.out.println("Quantidade de palavras: " + rodada.getNumPalavras());
        System.out.println();

        try (Scanner in = new Scanner(System.in)) {
            while (!rodada.encerrou()) {
                imprimirStatus(rodada);

                System.out.println("[1] Tentar uma letra   [2] Arriscar palavra(s)");
                System.out.print(">> ");
                String op = in.nextLine().trim();

                if ("1".equals(op)) {
                    System.out.print("Letra: ");
                    String s = in.nextLine().trim();
                    if (!s.isEmpty()) {
                        rodada.tentar(s.charAt(0));
                    }
                } else if ("2".equals(op)) {
                    String[] palpites = new String[rodada.getNumPalavras()];
                    for (int i = 0; i < palpites.length; i++) {
                        System.out.print("Palpite " + (i + 1) + ": ");
                        palpites[i] = in.nextLine().trim();
                    }
                    rodada.arriscar(palpites);
                } else {
                    System.out.println("Escolha 1 ou 2.");
                }

                if (rodada.descobriu()) {
                    System.out.println();
                    System.out.println("Acertou, " + jogador.getNome() + "!");
                    System.out.println("Pontuacao: " + rodada.calcularPontos());
                    break;
                }
            }
        }

        if (!rodada.descobriu()) {
            System.out.println();
            System.out.println("Nao foi dessa vez, " + jogador.getNome() + ".");
        }
        System.out.println();
        System.out.println("Fim.");
    }

    // helpers

    /**
     * Carga inicial simples: cria 1 tema e 2 palavras se o banco em memória estiver vazio.
     */
    private static void cargaInicial(TemaRepository temaRepo, PalavraRepository palavraRepo) throws RepositoryException {
        if (temaRepo.getTodos().length > 0) return;

        TemaFactoryImpl.createSoleInstance(temaRepo);
        TemaFactoryImpl tf = TemaFactoryImpl.getSoleInstance();

        Tema animais = tf.getTema("Animais");
        temaRepo.inserir(animais);

        palavraRepo.inserir(Palavra.criar(palavraRepo.getProximoId(), "gato", animais));
        palavraRepo.inserir(Palavra.criar(palavraRepo.getProximoId(), "cachorro", animais));
    }

    private static Jogador obterOuCriarJogador(JogadorRepository repo, String nome) throws RepositoryException {
        Jogador j = repo.getPorNome(nome);
        if (j != null) return j;
        j = Jogador.criar(repo.getProximoId(), nome);
        repo.inserir(j);
        return j;
    }

    private static void imprimirStatus(Rodada rodada) {
        System.out.println();
        System.out.println("Tentativas restantes: " + rodada.getQtdeTentativasRestantes());

        System.out.print("Letras tentadas: ");
        Letra[] tentadas = rodada.getTentativas();
        if (tentadas.length == 0) {
            System.out.println("(nenhuma)");
        } else {
            for (int i = 0; i < tentadas.length; i++) {
                tentadas[i].exibir(null);
                if (i < tentadas.length - 1) System.out.print(" ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("--- PALAVRA(S) ---");
        rodada.exibirItens(null);
        System.out.println();
    }

    private static void cabecalho(String titulo) {
        System.out.println("========================================");
        System.out.println(titulo);
        System.out.println("========================================");
    }
}

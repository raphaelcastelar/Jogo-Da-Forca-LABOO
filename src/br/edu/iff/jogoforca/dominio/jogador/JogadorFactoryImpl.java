package br.edu.iff.jogoforca.dominio.jogador;

/**
 * Fábrica concreta para Jogador
 * - Consulta o repositório por nome; se não existir, cria e persiste novo jogador.
 */
public class JogadorFactoryImpl implements JogadorFactory {

    private static JogadorFactoryImpl soleInstance;
    private JogadorRepository repository;

    private JogadorFactoryImpl(JogadorRepository repository) {
        this.repository = repository;
    }

    public static JogadorFactoryImpl getSoleInstance() {
        return soleInstance;
    }

    public static void createSoleInstance(JogadorRepository repository) {
        soleInstance = new JogadorFactoryImpl(repository);
    }

    @Override
    public Jogador getJogador(String nome) {
        Jogador jogador = repository.getPorNome(nome);
        if (jogador == null) {
            jogador = Jogador.criar(repository.getProximoId(), nome);
            try {
                repository.inserir(jogador);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jogador;
    }
}

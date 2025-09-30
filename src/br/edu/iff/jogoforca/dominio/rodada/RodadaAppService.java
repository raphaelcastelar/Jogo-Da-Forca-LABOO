package br.edu.iff.jogoforca.dominio.rodada;

import java.util.Arrays;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;
import br.edu.iff.jogoforca.dominio.jogador.JogadorRepository;
import br.edu.iff.repository.RepositoryException;

/**
 * Facade para a UI: cria rodada, ranking e histórico.
 * Padrão: Facade/Application Service (orquestra casos de uso)
 */
public final class RodadaAppService {

    private final JogadorRepository jogadorRepo;
    private final RodadaRepository rodadaRepo;
    private final RodadaFactory rodadaFactory;

    public RodadaAppService(RodadaFactory rodadaFactory,
                            JogadorRepository jogadorRepo,
                            RodadaRepository rodadaRepo) {
        this.rodadaFactory = rodadaFactory;
        this.jogadorRepo = jogadorRepo;
        this.rodadaRepo = rodadaRepo;
    }

    public Rodada novaRodada(Jogador jogador) throws RepositoryException {
        Rodada rodada = rodadaFactory.getRodada(jogador);
        rodadaRepo.inserir(rodada);
        return rodada;
    }

    public Jogador[] rankingPorPontuacaoDesc() {
        Jogador[] all = jogadorRepo.getTodos();
        Arrays.sort(all, (a,b) -> Integer.compare(b.getPontuacao(), a.getPontuacao()));
        return all;
    }

    public Rodada[] historicoPorJogador(String nomeJogador) {
        Jogador j = jogadorRepo.getPorNome(nomeJogador);
        if (j == null) return new Rodada[0];
        return rodadaRepo.getPorJogador(j);
    }
}


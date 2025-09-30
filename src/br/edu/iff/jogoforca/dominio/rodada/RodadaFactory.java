package br.edu.iff.jogoforca.dominio.rodada;

import br.edu.iff.jogoforca.dominio.jogador.Jogador;

/**
 * Abstract Factory para criação de Rodadas.
 * - Diferentes estratégias/fábricas podem ser plugadas (ex.: sorteio, manual, etc.).
 */
public interface RodadaFactory {
    Rodada getRodada(Jogador jogador);
}

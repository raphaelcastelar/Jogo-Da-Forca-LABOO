package br.edu.iff.repository;

/**
 * Contrato mínimo do padrão Repository
 * - Fornece sequência de IDs; operações CRUD ficam nas interfaces específicas de cada agregado.
 */
public interface Repository {
    long getProximoId();
}

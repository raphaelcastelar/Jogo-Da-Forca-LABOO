package br.edu.iff.repository;

/**
 * Exceção checked para operações de Repository
 * - Sinaliza violações de invariantes de persistência (duplicidade, inexistência etc.).
 */
public class RepositoryException extends Exception {
    public RepositoryException(String message) { super(message); }
    public RepositoryException(String message, Throwable cause) { super(message, cause); }
}

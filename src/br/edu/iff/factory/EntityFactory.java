package br.edu.iff.factory;

import br.edu.iff.repository.Repository;

/**
 * Base de Fábrica de Entidades
 * - Integra Factory + Repository: a factory conhece o Repository para obter IDs e criar entidades coesas.
 * - Mantém baixo acoplamento: subclasses pedem o Repository abstrato e operam no agregado correto.
 */
public abstract class EntityFactory {

    private final Repository repository;

    protected EntityFactory(Repository repository) {
        this.repository = repository;
    }

    protected Repository getRepository() {
        return repository;
    }

    protected long getProximoId() {
        return repository.getProximoId();
    }
}

package br.edu.iff.dominio;

public abstract class ObjetoDominioImpl implements objetoDominio {

    private long id;

    protected ObjetoDominioImpl(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
}
}

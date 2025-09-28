package br.edu.iff.bancodepalavras.dominio.palavra.emmemoria;

public class MemoriaPalavraRepository {

import bancodepalavras.dominio.palavra.Palavra;
import bancodepalavras.dominio.palavra.PalavraRepository;
import bancodepalavras.dominio.tema.Tema;
import repository.BDEmMemoria;
import repository.RepositoryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MemoriaPalavraRepository implements PalavraRepository {
    private static MemoriaPalavraRepository soleInstance;

    public static MemoriaPalavraRepository getSoleInstance(){
        if(soleInstance==null){
            soleInstance = new MemoriaPalavraRepository();
        }
        return soleInstance;
    }
    public MemoriaPalavraRepository() {
    }

    @Override
    public long getProximoId() {
        if (BDEmMemoria.BDPalavra.isEmpty()){
            return 1;
        }

        return Collections.max(BDEmMemoria.BDPalavra.keySet()) + 1;
    }

    @Override
    public Palavra getPorId(long id) {
        return BDEmMemoria.BDPalavra.get(id);
    }

    @Override
    public List<Palavra> getPorTema(Tema tema) {
        return BDEmMemoria.BDPalavra.values().stream().filter(palavra -> palavra.getTema().equals(tema)).collect(Collectors.toList());
    }

    @Override
    public List<Palavra> getTodas() {
        return new ArrayList<>(BDEmMemoria.BDPalavra.values());
    }

    @Override
    public Palavra getPalavra(String palavra) {
        for (Palavra p : BDEmMemoria.BDPalavra.values()) {
            if (p.toString().equals(palavra)){
                return p;
            }
        }

        return null;
    }

    @Override
    public void inserir(Palavra palavra) throws RepositoryException {
        BDEmMemoria.BDPalavra.put(palavra.getId(), palavra);
    }

    @Override
    public void atualizar(Palavra palavra) throws RepositoryException {
        BDEmMemoria.BDPalavra.put(palavra.getId(), palavra);
    }

    @Override
    public void remover(Palavra palavra) throws RepositoryException {
        BDEmMemoria.BDPalavra.remove(palavra.getId());
    }
}

}

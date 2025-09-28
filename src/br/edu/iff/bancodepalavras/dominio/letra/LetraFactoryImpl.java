package br.edu.iff.bancodepalavras.dominio.letra;

import java.util.HashMap;
import java.util.Map;

public abstract class LetraFactoryImpl implements LetraFactory{
    private Map<Character, Letra> pool = new HashMap<>();
    private Letra encoberta = criarLetra('*');

    protected LetraFactoryImpl() {
    }

    public final Letra getLetra(char codigo){
        if (!pool.containsKey(codigo)){
            pool.put(codigo, this.criarLetra(codigo));
        }
        return pool.get(codigo);
    }

    public final Letra getLetraEncoberta(){
        return encoberta;
    }

    protected abstract Letra criarLetra(char codigo);
}
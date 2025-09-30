package br.edu.iff.bancodepalavras.dominio.letra;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementação base do Flyweight
 * - Mantém um pool (Map) de letras únicas por código.
 * - Fornece a letra encoberta compartilhada.
 * Subclasses definem a criação concreta (texto, imagem).
 */
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

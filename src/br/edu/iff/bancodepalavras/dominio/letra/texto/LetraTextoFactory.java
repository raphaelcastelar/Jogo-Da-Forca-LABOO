package br.edu.iff.bancodepalavras.dominio.letra.texto;

import br.edu.iff.bancodepalavras.dominio.letra.Letra;
import br.edu.iff.bancodepalavras.dominio.letra.LetraFactoryImpl;

/**
 * Fábrica concreta de letras (texto) — usa Flyweight via a superclasse.
 */
public class LetraTextoFactory extends LetraFactoryImpl {
    private static LetraTextoFactory soleInstance;

    public static LetraTextoFactory getSoleInstance(){
        if(soleInstance==null){
            soleInstance = new LetraTextoFactory();
        }
        return soleInstance;
    }

    private LetraTextoFactory() {
        super();
    }

    @Override
    protected Letra criarLetra(char codigo) {
        return new LetraTexto(codigo);
    }


}

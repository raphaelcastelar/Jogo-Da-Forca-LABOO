package br.edu.iff.bancodepalavras.dominio.letra.imagem;

import bancodepalavras.dominio.letra.Letra;
import bancodepalavras.dominio.letra.LetraFactoryImpl;

public class LetraImagemFactory extends LetraFactoryImpl {
    private static LetraImagemFactory soleInstance;

    public static LetraImagemFactory getSoleInstance(){
        if(soleInstance==null){
            soleInstance = new LetraImagemFactory();
        }
        return soleInstance;
    }

    private LetraImagemFactory() {
        super();
    }

    @Override
    protected Letra criarLetra(char codigo) {
        return new LetraImagem(codigo);
    }
}
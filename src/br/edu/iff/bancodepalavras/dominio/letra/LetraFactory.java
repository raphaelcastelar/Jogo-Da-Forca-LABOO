package br.edu.iff.bancodepalavras.dominio.letra;

import factory.EntityFactory;

public interface LetraFactory{
    public Letra getLetra(char codigo);
    public Letra getLetraEncoberta();
}
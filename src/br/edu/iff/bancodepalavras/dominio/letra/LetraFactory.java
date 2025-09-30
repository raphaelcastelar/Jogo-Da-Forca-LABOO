package br.edu.iff.bancodepalavras.dominio.letra;



/**
 * Abstração de fábrica para o Flyweight de letras.
 * Implementações (texto, imagem) criam as instâncias específicas.
 */
public interface LetraFactory{
    public Letra getLetra(char codigo);
    public Letra getLetraEncoberta();
}

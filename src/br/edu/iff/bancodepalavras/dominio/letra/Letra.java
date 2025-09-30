package br.edu.iff.bancodepalavras.dominio.letra;

import java.util.Objects;

/**
 * Flyweight (intrínseco = código da letra)
 * - Instâncias de Letra devem ser compartilhadas via LetraFactory para economizar memória.
 * - equals/hashCode baseados no char permitem comparar corretamente em coleções.
 */
public abstract class Letra {
    private char codigo;

    protected Letra(char codigo) {
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }

    public void exibir(Object contexto) {
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Letra letra = (Letra) o;
        return codigo == letra.codigo;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public final String toString() {
        return String.valueOf(codigo);
    }
}

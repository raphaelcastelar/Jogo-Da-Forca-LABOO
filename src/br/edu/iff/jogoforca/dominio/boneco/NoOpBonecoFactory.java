package br.edu.iff.jogoforca.dominio.boneco;

/**
 * Null Object
 * - Fornece um Boneco que não faz nada ao exibir, simplificando a execução
 *   sem dependências de UI real (texto/imagem).
 */
public final class NoOpBonecoFactory implements BonecoFactory {
    private static final Boneco NO_OP = new Boneco() {
        @Override public void exibir(Object contexto, int partes) { /* no-op */ }
    };

    private static final NoOpBonecoFactory INSTANCE = new NoOpBonecoFactory();
    private NoOpBonecoFactory() {}

    public static NoOpBonecoFactory getSoleInstance() { return INSTANCE; }

    @Override
    public Boneco getBoneco() { return NO_OP; }
}

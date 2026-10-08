package padroes.observer;

public class PainelExpedicao implements ObservadorPedido {

    private String ultimaAtualizacao;

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    @Override
    public void atualizar(Pedido pedido) {
        ultimaAtualizacao = "Expedição - pedido " + pedido.getCodigo()
                + ": " + pedido.getStatus();
    }
}

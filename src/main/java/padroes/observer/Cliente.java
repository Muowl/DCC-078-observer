package padroes.observer;

import java.util.Objects;

public class Cliente implements ObservadorPedido {

    private final String nome;
    private String ultimaNotificacao;

    public Cliente(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome obrigatório");
    }

    public String getUltimaNotificacao() {
        return ultimaNotificacao;
    }

    @Override
    public void atualizar(Pedido pedido) {
        ultimaNotificacao = nome + ", seu pedido " + pedido.getCodigo()
                + " está com status: " + pedido.getStatus();
    }
}

package padroes.observer;

import java.util.Objects;

public class CentralPedidos {

    public void atualizarStatus(Pedido pedido, String status) {
        Objects.requireNonNull(pedido, "Pedido obrigatório").atualizarStatus(status);
    }
}

package padroes.observer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Pedido {

    private final String codigo;
    private String status;
    private final Set<ObservadorPedido> observadores;

    public Pedido(String codigo) {
        this.codigo = Objects.requireNonNull(codigo, "Código obrigatório");
        status = "Recebido";
        observadores = new LinkedHashSet<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getStatus() {
        return status;
    }

    public void adicionarObservador(ObservadorPedido observador) {
        observadores.add(Objects.requireNonNull(observador, "Observador obrigatório"));
    }

    public void removerObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    public void atualizarStatus(String status) {
        Objects.requireNonNull(status, "Status obrigatório");
        if (!this.status.equals(status)) {
            this.status = status;
            notificarObservadores();
        }
    }

    private void notificarObservadores() {
        for (ObservadorPedido observador : new ArrayList<>(observadores)) {
            observador.atualizar(this);
        }
    }
}

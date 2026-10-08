package padroes.observer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {

    @Test
    void deveIniciarPedidoRecebido() {
        Pedido pedido = new Pedido("PED-001");

        assertEquals("PED-001", pedido.getCodigo());
        assertEquals("Recebido", pedido.getStatus());
    }

    @Test
    void deveNotificarClienteInscrito() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        pedido.adicionarObservador(cliente);

        new CentralPedidos().atualizarStatus(pedido, "Enviado");

        assertEquals("Ana, seu pedido PED-001 está com status: Enviado",
                cliente.getUltimaNotificacao());
    }

    @Test
    void deveAtualizarPainelExpedicao() {
        Pedido pedido = new Pedido("PED-001");
        PainelExpedicao painel = new PainelExpedicao();
        pedido.adicionarObservador(painel);

        pedido.atualizarStatus("Em separação");

        assertEquals("Expedição - pedido PED-001: Em separação",
                painel.getUltimaAtualizacao());
    }

    @Test
    void deveNotificarTodosOsObservadoresInscritos() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        Cliente outroCliente = new Cliente("Bruno");
        PainelExpedicao painel = new PainelExpedicao();
        pedido.adicionarObservador(cliente);
        pedido.adicionarObservador(outroCliente);
        pedido.adicionarObservador(painel);

        pedido.atualizarStatus("Enviado");

        assertEquals("Ana, seu pedido PED-001 está com status: Enviado",
                cliente.getUltimaNotificacao());
        assertEquals("Bruno, seu pedido PED-001 está com status: Enviado",
                outroCliente.getUltimaNotificacao());
        assertEquals("Expedição - pedido PED-001: Enviado", painel.getUltimaAtualizacao());
    }

    @Test
    void naoDeveNotificarObservadoresNaoInscritos() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        PainelExpedicao painel = new PainelExpedicao();

        pedido.atualizarStatus("Enviado");

        assertNull(cliente.getUltimaNotificacao());
        assertNull(painel.getUltimaAtualizacao());
        assertEquals("Enviado", pedido.getStatus());
    }

    @Test
    void naoDeveNotificarNoMomentoDaInscricao() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");

        pedido.adicionarObservador(cliente);

        assertNull(cliente.getUltimaNotificacao());
    }

    @Test
    void devePararDeNotificarObservadorRemovido() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        PainelExpedicao painel = new PainelExpedicao();
        pedido.adicionarObservador(cliente);
        pedido.adicionarObservador(painel);
        pedido.atualizarStatus("Em separação");

        pedido.removerObservador(cliente);
        pedido.atualizarStatus("Enviado");

        assertEquals("Ana, seu pedido PED-001 está com status: Em separação",
                cliente.getUltimaNotificacao());
        assertEquals("Expedição - pedido PED-001: Enviado", painel.getUltimaAtualizacao());
    }

    @Test
    void devePermitirNovaInscricaoAposRemocao() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        pedido.adicionarObservador(cliente);
        pedido.removerObservador(cliente);
        pedido.atualizarStatus("Enviado");
        assertNull(cliente.getUltimaNotificacao());

        pedido.adicionarObservador(cliente);
        pedido.atualizarStatus("Entregue");

        assertEquals("Ana, seu pedido PED-001 está com status: Entregue",
                cliente.getUltimaNotificacao());
    }

    @Test
    void naoDeveDuplicarNotificacaoParaInscricaoRepetida() {
        Pedido pedido = new Pedido("PED-001");
        List<String> notificacoes = new ArrayList<>();
        ObservadorPedido observador = atualizado -> notificacoes.add(atualizado.getStatus());
        pedido.adicionarObservador(observador);
        pedido.adicionarObservador(observador);

        pedido.atualizarStatus("Enviado");

        assertEquals(List.of("Enviado"), notificacoes);
    }

    @Test
    void naoDeveNotificarQuandoStatusNaoMudar() {
        Pedido pedido = new Pedido("PED-001");
        List<String> notificacoes = new ArrayList<>();
        pedido.adicionarObservador(atualizado -> notificacoes.add(atualizado.getStatus()));

        pedido.atualizarStatus("Recebido");
        pedido.atualizarStatus("Enviado");
        pedido.atualizarStatus("Enviado");

        assertEquals(List.of("Enviado"), notificacoes);
    }

    @Test
    void deveNotificarCadaMudancaComStatusAtualizado() {
        Pedido pedido = new Pedido("PED-001");
        List<String> notificacoes = new ArrayList<>();
        pedido.adicionarObservador(atualizado -> notificacoes.add(
                atualizado.getCodigo() + ": " + atualizado.getStatus()));

        pedido.atualizarStatus("Em separação");
        pedido.atualizarStatus("Enviado");
        pedido.atualizarStatus("Entregue");

        assertEquals(List.of("PED-001: Em separação", "PED-001: Enviado", "PED-001: Entregue"),
                notificacoes);
    }

    @Test
    void deveNotificarSomenteObservadoresDoPedidoAlterado() {
        Pedido pedido = new Pedido("PED-001");
        Pedido outroPedido = new Pedido("PED-002");
        Cliente cliente = new Cliente("Ana");
        Cliente outroCliente = new Cliente("Bruno");
        pedido.adicionarObservador(cliente);
        outroPedido.adicionarObservador(outroCliente);

        pedido.atualizarStatus("Enviado");

        assertEquals("Ana, seu pedido PED-001 está com status: Enviado",
                cliente.getUltimaNotificacao());
        assertNull(outroCliente.getUltimaNotificacao());
        assertEquals("Recebido", outroPedido.getStatus());
    }

    @Test
    void devePermitirMesmoObservadorEmPedidosDiferentes() {
        Pedido pedido = new Pedido("PED-001");
        Pedido outroPedido = new Pedido("PED-002");
        PainelExpedicao painel = new PainelExpedicao();
        pedido.adicionarObservador(painel);
        outroPedido.adicionarObservador(painel);
        pedido.removerObservador(painel);

        outroPedido.atualizarStatus("Em separação");
        pedido.atualizarStatus("Enviado");

        assertEquals("Expedição - pedido PED-002: Em separação", painel.getUltimaAtualizacao());
    }

    @Test
    void devePermitirRemocaoDuranteNotificacao() {
        Pedido pedido = new Pedido("PED-001");
        List<String> notificacoes = new ArrayList<>();
        ObservadorPedido observador = new ObservadorPedido() {
            @Override
            public void atualizar(Pedido atualizado) {
                notificacoes.add("Temporário: " + atualizado.getStatus());
                atualizado.removerObservador(this);
            }
        };
        pedido.adicionarObservador(observador);
        pedido.adicionarObservador(atualizado -> notificacoes.add("Fixo: " + atualizado.getStatus()));

        pedido.atualizarStatus("Enviado");
        pedido.atualizarStatus("Entregue");

        assertEquals(List.of("Temporário: Enviado", "Fixo: Enviado", "Fixo: Entregue"), notificacoes);
    }

    @Test
    void deveIgnorarRemocaoDeObservadorNaoInscrito() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        pedido.adicionarObservador(cliente);

        pedido.removerObservador(new PainelExpedicao());
        pedido.atualizarStatus("Enviado");

        assertEquals("Ana, seu pedido PED-001 está com status: Enviado",
                cliente.getUltimaNotificacao());
    }

    @Test
    void deveRejeitarStatusAusenteSemAlterarPedidoOuNotificar() {
        Pedido pedido = new Pedido("PED-001");
        Cliente cliente = new Cliente("Ana");
        pedido.adicionarObservador(cliente);

        assertThrows(NullPointerException.class, () -> pedido.atualizarStatus(null));

        assertEquals("Recebido", pedido.getStatus());
        assertNull(cliente.getUltimaNotificacao());
    }

    @Test
    void deveRejeitarDadosObrigatoriosAusentes() {
        assertThrows(NullPointerException.class, () -> new Pedido(null));
        assertThrows(NullPointerException.class, () -> new Cliente(null));
        assertThrows(NullPointerException.class, () -> new Pedido("PED-001").adicionarObservador(null));
        assertThrows(NullPointerException.class, () -> new CentralPedidos().atualizarStatus(null, "Enviado"));
    }
}

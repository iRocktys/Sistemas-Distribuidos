/*
Autor: Leandro Tosta (2232510)
Ultima atulização: 26/06/2024
*/

package Servidor;

import Controle_Concorrencia.ControleConcorrencia;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class CorretoraImpl extends UnicastRemoteObject implements InterfaceCorretora {

    private final ControleConcorrencia controleConcorrencia;
    private final Map<String, Map<String, Integer>> carteiraClientes = new HashMap<>();
    private final Map<String, Integer> saldoClientes = new HashMap<>();
    private final Map<String, Integer> precoAcoes = new HashMap<>();

    protected CorretoraImpl(ControleConcorrencia controleConcorrencia) throws RemoteException {
        this.controleConcorrencia = controleConcorrencia;
        inicializarCarteiras();
    }

    private void inicializarCarteiras() {
        // Inicializa alguns precos de acoes
        precoAcoes.put("AAPL", 150);
        precoAcoes.put("GOOGL", 2800);
        precoAcoes.put("AMZN", 3500);

        // Inicializa carteiras para clientes de exemplo
        inicializarCarteiraCliente("cliente1", 5000, Map.of("AAPL", 10, "GOOGL", 5));
        inicializarCarteiraCliente("cliente2", 8000, Map.of("AMZN", 3, "AAPL", 7));
    }

    private void inicializarCarteiraCliente(String cliente, int saldo, Map<String, Integer> acoes) {
        saldoClientes.put(cliente, saldo);
        carteiraClientes.put(cliente, new HashMap<>(acoes));
    }

    @Override
    public synchronized boolean comprarAcao(String nomeAcao, int quantidade, String transacao) throws RemoteException {
        int saldo = saldoClientes.getOrDefault(transacao, 0);

        if (controleConcorrencia.verificarTranca("saldo", transacao)) {
            System.out.println("Operacao bloqueada por saldo insuficiente.");
            return false;
        }

        if (saldo == 0) {
            System.out.println("Cliente " + transacao + " esta com saldo zerado. Compra de acoes esta travada.");
            controleConcorrencia.adquirirTrancaEscrita("saldo", transacao); // Adquire a tranca quando o saldo for zero
            return false;
        }

        if (controleConcorrencia.adquirirTrancaEscrita(nomeAcao, transacao)) {
            try {
                int preco = obterPrecoAcao(nomeAcao) * quantidade;
                if (saldo >= preco) {
                    saldoClientes.put(transacao, saldo - preco);
                    carteiraClientes.putIfAbsent(transacao, new HashMap<>());
                    carteiraClientes.get(transacao).put(nomeAcao, carteiraClientes.get(transacao).getOrDefault(nomeAcao, 0) + quantidade);
                    // Verifica se o cliente agora possui ações e, em caso afirmativo, libera a tranca
                    if (carteiraClientes.get(transacao).size() > 0) {
                        controleConcorrencia.liberarTranca("carteira", transacao);
                    }
                    return true;
                }
            } finally {
                controleConcorrencia.liberarTranca(nomeAcao, transacao);
            }
        }
        return false;
    }

    @Override
    public synchronized boolean venderAcao(String nomeAcao, int quantidade, String transacao) throws RemoteException {
        Map<String, Integer> carteira = carteiraClientes.getOrDefault(transacao, new HashMap<>());

        int quantidadeAtual = carteira.getOrDefault(nomeAcao, 0);
        if (quantidadeAtual < quantidade) {
            System.out.println("Cliente " + transacao + " nao possui a acao " + nomeAcao + ". Venda de acoes esta travada.");
            return false;
        }

        if (carteira.isEmpty()) {
            System.out.println("Cliente " + transacao + " nao possui nenhuma acao. Venda de acoes esta travada.");
            controleConcorrencia.adquirirTrancaEscrita("carteira", transacao);
            return false;
        }

        if (controleConcorrencia.adquirirTrancaEscrita(nomeAcao, transacao)) {
            try {
                carteira.put(nomeAcao, quantidadeAtual - quantidade);
                if (carteira.get(nomeAcao) == 0) {
                    carteira.remove(nomeAcao);
                }
                int saldo = saldoClientes.getOrDefault(transacao, 0);
                int preco = obterPrecoAcao(nomeAcao) * quantidade;
                saldoClientes.put(transacao, saldo + preco);
                return true;
            } finally {
                controleConcorrencia.liberarTranca(nomeAcao, transacao);
            }
        }
        return false;
    }

    @Override
    public synchronized boolean sacar(String transacao, int valor) throws RemoteException {
        int saldo = saldoClientes.getOrDefault(transacao, 0);
        
         if (controleConcorrencia.verificarTranca("saldo", transacao)) {
            System.out.println("Operacao bloqueada por saldo insuficiente.");
            return false;
        }

        if (saldo < valor) {
            System.out.println("Cliente " + transacao + " nao tem saldo suficiente. Saque esta travado.");
            return false;
        }

        if (controleConcorrencia.adquirirTrancaEscrita("saldo", transacao)) {
            try {
                saldoClientes.put(transacao, saldo - valor);
                return true;
            } finally {
                controleConcorrencia.liberarTranca("saldo", transacao);
            }
        }
        return false;
    }

    @Override
    public String listarAcoes(String transacao) throws RemoteException {
        StringBuilder result = new StringBuilder();
        result.append("Saldo: ").append(saldoClientes.getOrDefault(transacao, 0)).append("\n");
        result.append("Acoes na carteira:\n");

        Map<String, Integer> carteira = carteiraClientes.getOrDefault(transacao, new HashMap<>());
        for (Map.Entry<String, Integer> entry : carteira.entrySet()) {
            result.append("Acao: ").append(entry.getKey()).append(", Quantidade: ").append(entry.getValue()).append("\n");
        }

        return result.toString();
    }

    @Override
    public void depositar(String transacao, int valor) throws RemoteException {
        int saldoAtual = saldoClientes.getOrDefault(transacao, 0);
        saldoClientes.put(transacao, saldoAtual + valor);
        System.out.println("Cliente " + transacao + " efetuou um deposito. Compra de acoes esta destravada.");

        // Verifica se o saldo agora é maior que zero e, em caso afirmativo, libera a tranca
        if (saldoAtual + valor > 0) {
            controleConcorrencia.liberarTranca("saldo", transacao);
        }
    }

    @Override
    public void liberarTranca(String recurso, String transacao) throws RemoteException {
        controleConcorrencia.liberarTranca(recurso, transacao);
        System.out.println("Tranca liberada para recurso: " + recurso + " e transacao: " + transacao);
    }

    @Override
    public void conectarCliente(String cliente) throws RemoteException {
        System.out.println("Cliente " + cliente + " conectado ao servidor.");
    }

    @Override
    public void desconectarCliente(String cliente) throws RemoteException {
        System.out.println("Cliente " + cliente + " desconectado do servidor.");
    }

    private int obterPrecoAcao(String nomeAcao) {
        return precoAcoes.getOrDefault(nomeAcao, 0);
    }
}

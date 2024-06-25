package br.corretoralt.Servidor;

import br.corretoralt.Cliente.Compra;
import br.corretoralt.Controle_Concorrencia.ControleConcorrencia;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CorretoraImpl extends UnicastRemoteObject implements InterfaceCorretora {

    private Map<String, Double> saldos; // Ajustado para Double
    private Map<String, List<Compra>> compras;
    private ControleConcorrencia controleConcorrencia;

    protected CorretoraImpl() throws RemoteException {
        saldos = new HashMap<>();
        compras = new HashMap<>();
        controleConcorrencia = new ControleConcorrencia();

        // Inicialização das carteiras dos clientes
        saldos.put("cliente1", 1000.0);
        saldos.put("cliente2", 1000.0);
    }

    @Override
    public synchronized boolean comprarAcao(String cliente, int quantidade, double preco) throws RemoteException {
        double custoTotal = quantidade * preco;
        double saldoAtual = saldos.get(cliente);

        if (saldoAtual >= custoTotal) {
            saldos.put(cliente, saldoAtual - custoTotal);
            compras.putIfAbsent(cliente, new ArrayList<>());
            compras.get(cliente).add(new Compra(cliente, quantidade, preco));
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean venderAcao(String cliente, int quantidade, double preco) throws RemoteException {
        double valorTotal = quantidade * preco;
        double saldoAtual = saldos.get(cliente);

        saldos.put(cliente, saldoAtual + valorTotal);
        compras.putIfAbsent(cliente, new ArrayList<>());
        compras.get(cliente).add(new Compra(cliente, -quantidade, preco));
        return true;
    }

    @Override
    public synchronized boolean liquidar(String cliente) throws RemoteException {
        compras.remove(cliente);
        return true;
    }

    @Override
    public Double getSaldo(String cliente) throws RemoteException {
        return saldos.getOrDefault(cliente, 0.0);
    }

    @Override
    public Compra[] getCompras(String cliente) throws RemoteException {
        List<Compra> comprasCliente = compras.getOrDefault(cliente, new ArrayList<>());
        return comprasCliente.toArray(new Compra[0]);
    }

    @Override
    public void depositar(String cliente, int valor) throws RemoteException {
        saldos.put(cliente, saldos.get(cliente) + valor);
    }

    @Override
    public boolean sacar(String cliente, int valor) throws RemoteException {
        double saldoAtual = saldos.get(cliente);
        if (saldoAtual >= valor) {
            saldos.put(cliente, saldoAtual - valor);
            return true;
        } else {
            return false;
        }
    }
}

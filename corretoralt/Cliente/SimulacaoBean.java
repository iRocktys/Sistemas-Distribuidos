package br.corretoralt.Cliente;

import br.corretoralt.Servidor.InterfaceCorretora;
import jakarta.annotation.PostConstruct;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.RequestScoped;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@ManagedBean(name = "simulacaoBean")
@RequestScoped
public class SimulacaoBean {

    private InterfaceCorretora corretora;
    private String cliente = "cliente1"; // Para simplificação, assumimos um cliente fixo
    private int quantidadeCompra = 10; // Quantidade padrão para cada compra
    private double precoAcao = 50.0; // Preço fixo para simplificação
    private double saldo;
    private Compra[] compras;

    @PostConstruct
    public void init() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            corretora = (InterfaceCorretora) registry.lookup("CorretoraService");
            atualizarDadosCliente();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void comprar() {
        try {
            if (corretora.comprarAcao(cliente, quantidadeCompra, precoAcao)) {
                atualizarDadosCliente();
            } else {
                // Adicione uma mensagem de erro informando que a compra não foi possível
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void vender() {
        try {
            if (corretora.venderAcao(cliente, quantidadeCompra, precoAcao)) {
                atualizarDadosCliente();
            } else {
                // Adicione uma mensagem de erro informando que a venda não foi possível
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liquidar() {
        try {
            if (corretora.liquidar(cliente)) {
                atualizarDadosCliente();
            } else {
                // Adicione uma mensagem de erro informando que a liquidação não foi possível
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizarDadosCliente() {
        try {
            saldo = corretora.getSaldo(cliente);
            compras = corretora.getCompras(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and setters

    public double getSaldo() {
        return saldo;
    }

    public int getQuantidadeCompra() {
        return quantidadeCompra;
    }

    public void setQuantidadeCompra(int quantidadeCompra) {
        this.quantidadeCompra = quantidadeCompra;
    }

    public double getPrecoAcao() {
        return precoAcao;
    }

    public void setPrecoAcao(double precoAcao) {
        this.precoAcao = precoAcao;
    }

    public Compra[] getCompras() {
        return compras;
    }
}

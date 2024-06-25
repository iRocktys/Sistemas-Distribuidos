package br.corretoralt.Cliente;

import java.io.Serializable;

public class Compra implements Serializable {
    private String cliente;
    private int quantidade;
    private double precoCompra;

    public Compra(String cliente, int quantidade, double precoCompra) {
        this.cliente = cliente;
        this.quantidade = quantidade;
        this.precoCompra = precoCompra;
    }

    public String getCliente() {
        return cliente;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoCompra() {
        return precoCompra;
    }
}

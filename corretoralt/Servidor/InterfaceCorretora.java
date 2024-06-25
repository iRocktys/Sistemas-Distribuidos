package br.corretoralt.Servidor;

import br.corretoralt.Cliente.Compra;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCorretora extends Remote {
    void depositar(String cliente, int valor) throws RemoteException;
    boolean sacar(String cliente, int valor) throws RemoteException;
    boolean comprarAcao(String cliente, int quantidade, double preco) throws RemoteException;
    boolean venderAcao(String cliente, int quantidade, double preco) throws RemoteException;
    boolean liquidar(String cliente) throws RemoteException;
    Double getSaldo(String cliente) throws RemoteException;
    Compra[] getCompras(String cliente) throws RemoteException;
}

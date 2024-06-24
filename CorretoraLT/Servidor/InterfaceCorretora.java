/*
Autor: Leandro Tosta (2232510)
Ultima atulização: 24/06/2024
*/

package Servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCorretora extends Remote {
    boolean comprarAcao(String nomeAcao, int quantidade, String transacao) throws RemoteException;
    boolean venderAcao(String nomeAcao, int quantidade, String transacao) throws RemoteException;
    boolean sacar(String transacao, int valor) throws RemoteException;
    String listarAcoes(String transacao) throws RemoteException;
    void depositar(String transacao, int valor) throws RemoteException;
    void liberarTranca(String recurso, String transacao) throws RemoteException;
    void conectarCliente(String cliente) throws RemoteException;
    void desconectarCliente(String cliente) throws RemoteException;
}

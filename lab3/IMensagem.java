/**
 * Laboratorio 3  
 * Autor: Leandro Martins Tosta e Eiti Parruca Adama
 * Ultima atualizacao: 29/04/2024
 */

package lab3;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IMensagem extends Remote {
    
    public Mensagem enviar(Mensagem mensagem) throws RemoteException;
    
}

/*
Autor: Leandro Tosta (2232510)
Ultima atulização: 24/06/2024
*/

package Servidor;

import Controle_Concorrencia.ControleConcorrencia;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    public static void main(String[] args) {
        try {
            ControleConcorrencia controleConcorrencia = new ControleConcorrencia();
            CorretoraImpl corretora = new CorretoraImpl(controleConcorrencia);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("CorretoraService", corretora);
            System.out.println("Servidor RMI esta rodando...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

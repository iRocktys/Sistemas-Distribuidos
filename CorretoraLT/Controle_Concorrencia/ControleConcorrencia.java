/*
Autor: Leandro Tosta (2232510)
Ultima atulização: 26/06/2024
*/

package Controle_Concorrencia;

import java.util.ArrayList;
import java.util.List;

public class ControleConcorrencia {
    private final List<Tranca> trancas;

    public ControleConcorrencia() {
        trancas = new ArrayList<>();
    }

    public synchronized boolean adquirirTrancaLeitura(String recurso, String transacao) {
        for (Tranca t : trancas) {
            if (t.getRecurso().equals(recurso) && t.getTipo().equals("w") && !t.getTransacao().equals(transacao)) {
                return false; 
            }
        }
        trancas.add(new Tranca("r", recurso, transacao));
        return true;
    }

    public synchronized boolean adquirirTrancaEscrita(String recurso, String transacao) {
        for (Tranca t : trancas) {
            if (t.getRecurso().equals(recurso) && !t.getTransacao().equals(transacao)) {
                return false; 
            }
        }
        trancas.add(new Tranca("w", recurso, transacao));
        return true;
    }

    public synchronized void liberarTranca(String recurso, String transacao) {
        trancas.removeIf(t -> t.getRecurso().equals(recurso) && t.getTransacao().equals(transacao));
    }

    public synchronized boolean verificarTranca(String recurso, String transacao) {
        for (Tranca t : trancas) {
            if (t.getRecurso().equals(recurso) && t.getTransacao().equals(transacao)) {
                return true; // Tranca encontrada
            }
        }
        return false; // Nenhuma tranca encontrada
    }

    public synchronized String listarTrancas() {
        StringBuilder trancasStr = new StringBuilder();
        for (Tranca t : trancas) {
            trancasStr.append(t.getTransacao()).append("-[").append(t.getTipo()).append("][").append(t.getRecurso()).append("] ");
        }
        return trancasStr.toString();
    }
}


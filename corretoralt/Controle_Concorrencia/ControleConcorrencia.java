package br.corretoralt.Controle_Concorrencia;

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
                return false; // Outra transacao tem uma tranca de escrita no recurso
            }
        }
        trancas.add(new Tranca("r", recurso, transacao));
        return true;
    }

    public synchronized boolean adquirirTrancaEscrita(String recurso, String transacao) {
        for (Tranca t : trancas) {
            if (t.getRecurso().equals(recurso) && !t.getTransacao().equals(transacao)) {
                return false; // Outra transacao tem uma tranca no recurso
            }
        }
        trancas.add(new Tranca("w", recurso, transacao));
        return true;
    }

    public synchronized void liberarTranca(String recurso, String transacao) {
        trancas.removeIf(t -> t.getRecurso().equals(recurso) && t.getTransacao().equals(transacao));
    }

    public synchronized String listarTrancas() {
        StringBuilder trancasStr = new StringBuilder();
        for (Tranca t : trancas) {
            trancasStr.append("Recurso: ").append(t.getRecurso())
                      .append(", Tipo: ").append(t.getTipo())
                      .append(", Transacao: ").append(t.getTransacao()).append("\n");
        }
        return trancasStr.toString();
    }
}

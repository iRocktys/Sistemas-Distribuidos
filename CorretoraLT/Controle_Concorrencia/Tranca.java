/*
Autor: Leandro Tosta (2232510)
Ultima atulização: 24/06/2024
*/

package Controle_Concorrencia;

public class Tranca {
    private final String tipo; 
    private final String recurso;
    private final String transacao;

    public Tranca(String tipo, String recurso, String transacao) {
        this.tipo = tipo;
        this.recurso = recurso;
        this.transacao = transacao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getTransacao() {
        return transacao;
    }
}

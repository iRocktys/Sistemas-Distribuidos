/**
  * Laboratorio 4  
  * Autor: Leandro Martins Tosta e Eiti Parruca Adama
  * Ultima atualizacao: 29/04/2024
  */
package lab4;
public enum Peer {
    
    PEER1 {
        @Override
        public String getNome() {
            return "PEER1";
        }        
    },
    PEER2 {
        public String getNome() {
            return "PEER2";
        }        
    },
    PEER3 {
        public String getNome() {
            return "PEER3";
        }        
    };
    public String getNome(){
        return "NULO";
    }    
}

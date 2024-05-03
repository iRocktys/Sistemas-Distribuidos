/**
 * Laboratorio 3  
 * Autor: Leandro Martins Tosta e Eiti Parruca Adama
 * Ultima atualizacao: 29/04/2024
 */

package lab3;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteRMI {
	private String opcao="";
    private Scanner leitura = new Scanner(System.in);
    private Scanner leFortuna = new Scanner(System.in);
    
	public static String read(IMensagem stub) throws RemoteException {
		Mensagem mensagem = new Mensagem("", "1"); // O segundo parâmetro é o código para a operação de leitura
		Mensagem resposta = stub.enviar(mensagem); // dentro da mensagem tem o campo 'read'
		return resposta.getMensagem();
	}

	public static String write(IMensagem stub, String escreverFortuna) throws RemoteException {
		Mensagem mensagem = new Mensagem(escreverFortuna, "2");
		Mensagem resposta = stub.enviar(mensagem);
		return resposta.getMensagem();
	}
	
	
    public void iniciar() {
        try {
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);            
            IMensagem stub = (IMensagem) registro.lookup("servidorFortunes");  
            
            do {
                System.out.println("\nMenu:");
                System.out.println("1 - Read");
                System.out.println("2 - Write");
                System.out.println("3 - Sair");
                System.out.println("\nEscolha uma opção:");
                opcao = leitura.next();
                
                switch (opcao) {
					case "1": {
						String resposta = read(stub);
						System.out.println(resposta);
						break;
					}
					case "2": {
						// Monta a mensagem
						System.out.print("Adicionar fortuna: ");
						String fortune = leFortuna.nextLine();
						String resposta = write(stub, fortune);
						System.out.println(resposta);
						break;
					}
                }
            } while(!opcao.equals("3"));
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ClienteRMI().iniciar();        
    }
}

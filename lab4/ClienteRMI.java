/**
  * Laboratorio 4  
  * Autor: Leandro Martins Tosta e Eiti Parruca Adama
  * Ultima atualizacao: 29/04/2024
  */
package lab4;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteRMI {

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

	public static void main(String[] args) {

		// TODO: Obter a Lista de pares disponiveis do arquivo Peer.java
		List<Peer> listaPeers = new ArrayList<>();

		for (Peer peer : Peer.values()) {
			listaPeers.add(peer);
		}

		try {

			Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);

			// Escolhe um peer aleatorio da lista de peers para conectar
			SecureRandom sr = new SecureRandom();

			IMensagem stub = null;
			Peer peer = null;

			boolean conectou = false;
			while (!conectou) {
				peer = listaPeers.get(sr.nextInt(listaPeers.size()));
				try {
					stub = (IMensagem) registro.lookup(peer.getNome());
					conectou = true;
				} catch (java.rmi.ConnectException e) {
					System.out.println(peer.getNome() + " indisponivel. ConnectException. Tentanto o proximo...");
				} catch (java.rmi.NotBoundException e) {
					System.out.println(peer.getNome() + " indisponivel. NotBoundException. Tentanto o proximo...");
				}
			}
			System.out.println("Conectado no peer: " + peer.getNome());

			String opcao = "";
			Scanner leitura = new Scanner(System.in);
			Scanner leFortuna = new Scanner(System.in);
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
						System.out.print("Add fortune: ");
						String fortune = leFortuna.nextLine();
						String resposta = write(stub, fortune);
						System.out.println(resposta);
						break;
					}
				}
			} while (!opcao.equals("x"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

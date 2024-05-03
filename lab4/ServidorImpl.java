/**
  * Laboratorio 4  
  * Autor: Leandro Martins Tosta e Eiti Parruca Adama
  * Ultima atualizacao: 29/04/2024
  */
package lab4;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServidorImpl implements IMensagem {

	ArrayList<Peer> alocados;

	public ServidorImpl() {
		alocados = new ArrayList<>();
	}
	// Cliente: invoca o metodo remoto 'enviar'
	// Servidor: invoca o metodo local 'enviar'
	@Override
	public Mensagem enviar(Mensagem mensagem) throws RemoteException {
		Mensagem resposta;
		try {
			System.out.println("Mensagem recebida: " + mensagem.getMensagem());
			resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
		} catch (Exception e) {
			e.printStackTrace();
			resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
		}
		return resposta;
	}

	public String parserJSON(String json) {
		String mensagem = json;
		String resultado = "";
		String respostaFinal = "";
		Principal principal = new Principal();
		if (mensagem.contains("read")) {
			resultado = principal.read();
			respostaFinal = "{\n" + "\"result:\"" + "\"" + resultado + "\"" + "}";

		} else if (mensagem.contains("write")) {
			int argsStartIndex = mensagem.indexOf("\"args\": [") + 9;
			int argsEndIndex = mensagem.indexOf("]", argsStartIndex);
			if (argsStartIndex != -1 && argsEndIndex != -1) {
				String argsString = mensagem.substring(argsStartIndex, argsEndIndex);
				argsString = argsString.replaceAll("\"", "");
				principal.write(argsString);
				respostaFinal = "{\n" + "\"result:\"" + "\"" + argsString + "\"" + "\n}";

			}

		} else {
			respostaFinal = "MÉTODO NÃO ENCONTRADO!";
		}

		return respostaFinal;
	}

	public void iniciar() {

		try {
			// TODO: Adquire aleatoriamente um 'nome' do arquivo Peer.java
			List<Peer> listaPeers = new ArrayList<>();
			for (Peer peer : Peer.values()) {
				listaPeers.add(peer);
			}

			Registry servidorRegistro;
			try {
				servidorRegistro = LocateRegistry.createRegistry(1099);
			} catch (java.rmi.server.ExportException e) { // Registro jah iniciado
				System.out.print("Registro jah iniciado. Usar o ativo.\n");
			}
			servidorRegistro = LocateRegistry.getRegistry(); // Registro eh unico para todos os peers
			String[] listaAlocados = servidorRegistro.list();
			for (int i = 0; i < listaAlocados.length; i++)
				System.out.println(listaAlocados[i] + " ativo.");

			SecureRandom sr = new SecureRandom();
			Peer peer = listaPeers.get(sr.nextInt(listaPeers.size()));

			int tentativas = 0;
			boolean repetido = true;
			boolean cheio = false;
			while (repetido && !cheio) {
				repetido = false;
				peer = listaPeers.get(sr.nextInt(listaPeers.size()));
				for (int i = 0; i < listaAlocados.length && !repetido; i++) {

					if (listaAlocados[i].equals(peer.getNome())) {
						System.out.println(peer.getNome() + " ativo. Tentando proximo...");
						repetido = true;
						tentativas = i + 1;
					}

				}
				// System.out.println(tentativas+" "+listaAlocados.length);

				// Verifica se o registro estah cheio (todos alocados)
				if (listaAlocados.length > 0 && tentativas == listaPeers.size()) {
					cheio = true;
				}
			}

			if (cheio) {
				System.out.println("Sistema cheio. Tente mais tarde.");
				System.exit(1);
			}

			IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); // 0: sistema operacional indica
																						// a porta (porta anonima)
			servidorRegistro.rebind(peer.getNome(), skeleton);
			System.out.print(peer.getNome() + " Servidor RMI: Aguardando conexoes...");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ServidorImpl servidor = new ServidorImpl();
		servidor.iniciar();

	}
}

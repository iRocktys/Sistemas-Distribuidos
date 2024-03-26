// Leandro Tosta & Eiti Parruca Adama
// 2232510 & 2232472

package lab2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class Servidor {
	public final static Path path = Paths.get("src/fortune-br.txt");
	private static Socket socket;
	private static ServerSocket server;
	private static DataInputStream entrada;
	private static DataOutputStream saida;
	private int porta = 1037;
	
	public class FileReader {
		
		// Faz a leitura do arquivo e salva os tokens em um hashmap
		public void read(HashMap<Integer, String> hm) throws FileNotFoundException {
			InputStream is = new BufferedInputStream(new FileInputStream(path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				int lineCount = 0;
				String line = "";
				while (!(line == null)) {
					// Faz a contagem de Fortunas
					if (line.equals("%")) {
						lineCount++;
					}
					// Faz a leitura da proxima linha
					line = br.readLine();
					StringBuffer fortune = new StringBuffer();
					// Concatenas as linhas enquanto forem diferentes de vazio e %
					while (!(line == null) && !line.equals("%")) {
						fortune.append(line + "\n");
						line = br.readLine();
						// System.out.print(lineCount + ".");
					}
					// Insere no hashmap 
					hm.put(lineCount, fortune.toString());
				}// fim while
				
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}// read()
	} // FileRead
	
	public class Conectar {
		public void conexaoCliente (HashMap<Integer, String> hm) throws IOException{
			System.out.println("Servidor iniciado na porta: " + porta);
			Scanner scanner = new Scanner(System.in);
			Conectar conectar = new Conectar();
			
			try {
				// Criar porta de recepcao
				server = new ServerSocket(porta);
				socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

				// Criar os fluxos de entrada e saida
				entrada = new DataInputStream(socket.getInputStream());
				saida = new DataOutputStream(socket.getOutputStream());

				// Recebimento da mensagem do cliente
				String mensagem = entrada.readUTF();
				
				String mensagemSemChaves = mensagem.replaceAll("[{}]", "");				
				String sub1 = mensagemSemChaves.substring(8, 12);				
				String sub2 = mensagemSemChaves.substring(8, 13);
				String sub3 = mensagemSemChaves.replaceAll("metodo|write|args|:|\n", "");

				if(sub1.equals("read")) {
					String fortunaAleatoria = conectar.print(hm);
					// System.out.println(fortunaAleatoria);
					saida.writeUTF("Result:" + fortunaAleatoria);
					socket.close();
				} else {
					if(sub2.equals("write")) {
						// Adiciona nova fortuna ao arquivo
                        conectar.write(sub3, hm);
                        System.out.println("\nNova fortuna adicionada com sucesso!");
                        
                        // Quando adicionado nova fortuna, faz a leitura do arquivo novamente
                        FileReader fr = new FileReader();
                        fr.read(hm);
                       	
                        saida.writeUTF("Result:" + sub3);
                        socket.close();
					} else {
						saida.writeUTF("Entrada invalida, fora do padrao JSON");
						socket.close();				
					}
				}
				
				// Envio dos dados (resultado)
				socket.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} // conexaoCliente
		
		
		// Escreve nova fortuna ao final do arquivo
		public void write(String novaFortuna, HashMap<Integer, String> hm) throws IOException {
			// Escreve a nova linha no final do arquivo
		    try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(path, java.nio.file.StandardOpenOption.APPEND))) {
		    	out.write(("\n" + novaFortuna + "\n%\n").getBytes());
		    } catch (IOException e) {
		    	System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
		    }
		} // write()
				
		public String print(HashMap<Integer, String> hm) throws IOException {
			Random rand = new Random();
			int numeroAleatorio = rand.nextInt(2500) + 1;
			String entry = hm.get(numeroAleatorio);
	        //System.out.print(entry);
			return entry;
		}// print()
	}
	
	public void iniciar() throws IOException {
		HashMap hm = new HashMap<Integer, String>();
		
		FileReader fr = new FileReader();
		fr.read(hm);	
		
		Conectar conectar = new Conectar();
		conectar.conexaoCliente(hm);
		//conectar.print(hm);
	}
	
	public static void main(String[] args) throws IOException {
		new Servidor().iniciar();
	}

}

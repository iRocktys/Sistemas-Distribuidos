// Leandro Tosta & Eiti Parruca Adama
// 2232510 & 2232472

package lab1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.*;

public class Principal_v0 {
	public final static Path path = Paths.get("src/fortune-br");
	
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
		
		// Escreve nova fortuna ao final do arquivo
		public void write(String novaFortuna, HashMap<Integer, String> hm) throws IOException {
			// Escreve a nova linha no final do arquivo
            try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(path, java.nio.file.StandardOpenOption.APPEND))) {
                out.write(("\n" + novaFortuna + "\n%\n").getBytes());
            } catch (IOException e) {
                System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
            }
        } // write()
		
		// Para exibir as fortunas no console
		public void print(HashMap<Integer, String> hm) throws FileNotFoundException {
			for (Map.Entry<Integer, String> entry : hm.entrySet()) {
				System.out.println("## Fortuna " + entry.getKey() + ":");
				System.out.println(entry.getValue());
			}
		}// print()
	}
	
	public void iniciar() throws IOException {
		FileReader fr = new FileReader();
		Scanner scanner = new Scanner(System.in);
		HashMap hm = new HashMap<Integer, String>();
		
		// Faz a leitura do arquivo inicial 
		fr.read(hm);
		
		// While para exibicao do menu
		while (true) {
	            System.out.println("\nMenu:");
	            System.out.println("1 - Imprimir fortunas");
	            System.out.println("2 - Adicionar nova fortuna");
	            System.out.println("3 - Sair");

	            System.out.println("\nEscolha uma opção:");
	            int opcao = scanner.nextInt();
	            scanner.nextLine(); // Limpar o buffer do scanner

	            try {
	                switch (opcao) {
	                    case 1:
	                        System.out.println("\nFortunas existentes:");
	                        fr.print(hm);
	                        break;
	                    case 2:
	                    	// Adiciona nova fortuna ao arquivo
	                        System.out.println("\nDigite a nova fortuna:");
	                        String novaFortuna = scanner.nextLine();
	                        fr.write(novaFortuna, hm);
	                        System.out.println("\nNova fortuna adicionada com sucesso!");
	                        
	                        // Quando adicionado nova fortuna, faz a leitura do arquivo novamente
	                        fr.read(hm);
	                        break;
	                    case 3:
	                        System.out.println("\nEncerrando o programa...");
	                        return;
	                    default:
	                        System.out.println("\nOpção inválida, tente novamente.");
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	} // iniciar()

	public static void main(String[] args) throws IOException {
		new Principal_v0().iniciar();
	}
}

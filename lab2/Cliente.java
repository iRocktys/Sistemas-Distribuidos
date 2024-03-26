package lab2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    
    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    
    private int porta=1037;
    
    public void iniciar(){
    	System.out.println("Cliente iniciado na porta: "+porta);
    	Scanner scanner = new Scanner(System.in);
    	
    	try {
            
            socket = new Socket("127.0.0.1", porta);
            
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
            
            System.out.println("\nMenu:");
            System.out.println("1 - Write");
            System.out.println("2 - Read");
            System.out.println("3 - Sair");
            System.out.println("\nEscolha uma opção:");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer do scanner

            try {
                switch (opcao) {
                    case 1:
                    	// Recebe do usuário uma string
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        System.out.println("Digite uma mensagem: ");
                        String mensagem = br.readLine();
                        
                    	// A mensagem é enviada ao servidor
                        saida.writeUTF(
                        		"{\n" + 
                        		"metodo:" + "write" + "\n" +
                        		"args:" + mensagem + "\n" +
                        		"}\n"
                        );
                        break;
                    case 2:
                    	// A mensagem é enviada ao servidor
                        saida.writeUTF(
                        		"{\n" + 
                        		"metodo:" + "read" + "\n" +
                        		"args:" + "vazio" + "\n" +
                        		"}\n"
                        );
                        break;
                    case 3:
                        System.out.println("\nEncerrando o programa...");
                        socket.close();
                    default:
                        System.out.println("\nOpção inválida, tente novamente.");
                }
            
                // Recebe o resultado do servidor
                String resultado = entrada.readUTF();
                // Mostra o resultado na tela
                System.out.println(resultado);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Cliente().iniciar();
    }
    
}

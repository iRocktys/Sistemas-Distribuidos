/**
 * Laboratorio 3  
 * Autor: Leandro Martins Tosta e Eiti Parruca Adama
 * Ultima atualizacao: 29/04/2024
 */

package lab3;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class ServidorImpl implements IMensagem{
    private int port = 1096;
    
    public ServidorImpl() {
                
    }
    
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
        	System.out.println("Mensagem recebida: " + mensagem.getMensagem());
			resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
		} catch (Exception e) {
			e.printStackTrace();
			resposta = new Mensagem("{\n" + "\"result\": \"false\"\n" + "}");
		}
        return resposta;
    } // Enviar
    
    public String parserJSON(String json) {
        String mensagem = json;
        String resultado = "";
        String respostaFinal = "";
        Principal principal = new Principal();
        if (mensagem.contains("read")) {
            resultado = principal.read();
            respostaFinal = "{\n" + "\"result\""+ ":" + "\"" + resultado + "\"" + "}";

        } else if (mensagem.contains("write")) {
            int argsStartIndex = mensagem.indexOf("\"args\": [") + 9;
            int argsEndIndex = mensagem.indexOf("]", argsStartIndex);
            if (argsStartIndex != -1 && argsEndIndex != -1) {
                String argsString = mensagem.substring(argsStartIndex, argsEndIndex);
                argsString = argsString.replaceAll("\"", "");
                principal.write(argsString);
                respostaFinal = "{\n" + "\"result\""+":" + "\"" + argsString + "\"" + "\n}";
            }

        } else {
            respostaFinal = "MÉTODO NÃO ENCONTRADO!";
        }

        return respostaFinal;
    } // parserJSON
    
    public void iniciar(){
    	try {
            Registry servidorRegistro = LocateRegistry.createRegistry(port);            
            IMensagem skeleton  = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)
            servidorRegistro.rebind("servidorFortunes", skeleton);
            System.out.print("Servidor RMI: Aguardando conexoes...");
        } catch(Exception e) {
            e.printStackTrace();
        }        

    } // Iniciar
    
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }    
}

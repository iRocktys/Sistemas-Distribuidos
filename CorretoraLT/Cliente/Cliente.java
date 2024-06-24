/*
Autor: Leandro Tosta (2232510)
Ultima atulização: 24/06/2024
*/

package Cliente;

import Servidor.InterfaceCorretora;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    private InterfaceCorretora corretoraService;
    private String clienteId;

    public Cliente(String clienteId) {
        this.clienteId = clienteId;
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            corretoraService = (InterfaceCorretora) registry.lookup("CorretoraService");
            corretoraService.conectarCliente(clienteId);
            System.out.println("Cliente " + clienteId + " conectado ao servidor.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Comprar Acao");
            System.out.println("2. Vender Acao");
            System.out.println("3. Listar Acoes na Carteira");
            System.out.println("4. Fazer Deposito");
            System.out.println("5. Fazer Saque");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opcao: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    // Ações do cliente
                    System.out.print("Digite o nome da acao que deseja comprar: ");
                    String nomeAcaoCompra = scanner.nextLine();
                    System.out.print("Digite a quantidade de acoes que deseja comprar: ");
                    int quantidadeCompra = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    
                    //--
                    try {
                        if (!corretoraService.comprarAcao(nomeAcaoCompra, quantidadeCompra, clienteId)) {
                            System.out.println("Saldo insuficiente. Deseja fazer um deposito? (s/n)");
                            String resposta = scanner.nextLine();
                            
                            // Cliente deseja efetuar o deposito caso não tenha saldo para comprar a ação
                            if (resposta.equalsIgnoreCase("s")) {
                                System.out.print("Digite o valor do deposito: ");
                                int valorDeposito = scanner.nextInt();
                                scanner.nextLine(); 
                                corretoraService.depositar(clienteId, valorDeposito);
                                System.out.println("Deposito realizado com sucesso.");
                                if (corretoraService.comprarAcao(nomeAcaoCompra, quantidadeCompra, clienteId)) {
                                    System.out.println("Compra de acoes bem-sucedida apos deposito!");
                                } 
                            } else {
                                System.out.println("Operacao cancelada.");
                            }
                        } else {
                            System.out.println("Compra de acoes bem-sucedida!");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    
                    // Ações do cliente
                    System.out.print("Digite o nome da acao que deseja vender: ");
                    String nomeAcaoVenda = scanner.nextLine();
                    System.out.print("Digite a quantidade de acoes que deseja vender: ");
                    int quantidadeVenda = scanner.nextInt();
                    scanner.nextLine();
                   
                    //--
                    try {
                        if (!corretoraService.venderAcao(nomeAcaoVenda, quantidadeVenda, clienteId)) {
                            System.out.println("Voce nao possui a acao " + nomeAcaoVenda + ". Deseja continuar com outras operacoes? (s/n)");
                            String resposta = scanner.nextLine();
                            if (resposta.equalsIgnoreCase("s")) {
                                System.out.println("Continue com outras operacoes.");
                            } else {
                                System.out.println("Operacao cancelada.");
                            }
                        } else {
                            System.out.println("Venda de acoes bem-sucedida!");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    listarAcoes();
                    break;
                case 4:
                    System.out.print("Digite o valor do deposito: ");
                    int valorDeposito = scanner.nextInt();
                    scanner.nextLine(); 
                    try {
                        corretoraService.depositar(clienteId, valorDeposito);
                        System.out.println("Deposito realizado com sucesso.");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                   
                    // Ações do cliente
                    System.out.print("Digite o valor do saque: ");
                    int valorSaque = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    //--
                    try {
                        if (!corretoraService.sacar(clienteId, valorSaque)) {
                            System.out.println("Saldo insuficiente. Deseja realizar outra operacao? (s/n)");
                            String resposta = scanner.nextLine();
                            if (resposta.equalsIgnoreCase("s")) {
                                corretoraService.liberarTranca("saldo", clienteId);
                            } else {
                                System.out.println("Operacao cancelada.");
                            }
                        } else {
                            System.out.println("Saque realizado com sucesso!");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        corretoraService.desconectarCliente(clienteId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
    }

    public void listarAcoes() {
        try {
            String acoes = corretoraService.listarAcoes(clienteId);
            System.out.println(acoes);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do cliente (cliente1 ou cliente2): ");
        String clienteId = scanner.nextLine();
        Cliente cliente = new Cliente(clienteId);
        cliente.exibirMenu();
    }
}

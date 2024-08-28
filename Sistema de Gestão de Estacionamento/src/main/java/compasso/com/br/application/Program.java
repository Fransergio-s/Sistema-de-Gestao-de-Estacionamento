package compasso.com.br.application;

import compasso.com.br.model.dao.VehicleDao;
import compasso.com.br.model.dao.DaoFactory;
import compasso.com.br.model.manager.ParkingManager;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        boolean exit2 = false;
        VehicleDao vehicleDao = DaoFactory.createVehicleDao();
        ParkingManager parkingManager = new ParkingManager();


        while (!exit) {
            System.out.println("==== Sistema de Gestão de Estacionamento ====");
            System.out.println("1. Consultar veículo (por placa)");
            System.out.println("2. Registrar entrada de veículo");
            System.out.println("3. Registrar saída de veículo");
            System.out.println("3. Verificar vagas disponíveis");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consumir a quebra de linha
            System.out.println();

            switch (choice) {
                case 1:
                    while (!exit2) {
                        System.out.println("==== Consulta de veículo ====");
                        System.out.println("1. Cadastrar veiculo");
                        System.out.println("2. Verificando veiculo");
                        System.out.println("3. Atualizar veiculo");
                        System.out.println("4. Apagar veiculo");
                        System.out.println("5. Mostrar todos os veiculos cadastrados ");
                        System.out.println("0. Voltar");
                        System.out.print("Escolha uma opção: ");

                        int choice2 = sc.nextInt();

                        sc.nextLine(); // Consumir a quebra de linha
                        System.out.println();

                        switch (choice2) {
                            case 1:
                                parkingManager.registerVehicle(sc);
                                break;
                            case 2:
                                parkingManager.checkVehicle(sc);
                                break;
                            case 3:
                                parkingManager.updateVehicle(sc);
                                break;
                            case 4:
                                parkingManager.deleteVehicle(sc);
                                break;
                            case 5:
                                parkingManager.listAllVehicles();
                                break;
                            case 0:
                                exit2 = true;
                                System.out.println("Voltando ...");
                                break;
                            default:
                                System.out.println("Opção inválida! Tente novamente.");
                        }
                    }
                case 2:
                    parkingManager.registerVehicleEntry(sc);
                    break;
                case 3:
                    parkingManager.registerVehicleExit(sc);
                    break;
                case 4:
                    parkingManager.checkAvailability(sc);
                    break;
                case 0:
                    exit = true;
                    System.out.println("Saindo do sistema...");
                    sc.close();
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                case 5:
                    break;
                case 0:
                    exit = true;
                    System.out.println("Saindo do sistema...");
                    sc.close();
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
}
package compasso.com.br.application;

import compasso.com.br.db.DB;
import compasso.com.br.db.DbException;
import compasso.com.br.model.dao.VehicleDao;
import compasso.com.br.model.entities.Vehicle;
import compasso.com.br.model.dao.DaoFactory;
import compasso.com.br.model.entities.enums.Category;
import compasso.com.br.model.entities.enums.Type;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        boolean exit2 = false;
        VehicleDao vehicleDao = DaoFactory.createVehicleDao();

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
                                System.out.println("Cadastrando veiculo: ");
                                System.out.print("Enter the vehicle license plate: ");
                                String licensePlate = sc.nextLine();
                                System.out.print("Enter the vehicle type \n" +
                                        "(PassengerCars, Motorcycles, Trucks, Ambulances, PoliceCar: )");
                                Type type = Type.valueOf(sc.nextLine());
                                System.out.print("Enter the vehicle category \n" +
                                        "(MonthlyPayers, DeliveryTrucks, PublicService, Single)");
                                Category category = Category.valueOf(sc.nextLine());

                                Vehicle vehicle = new Vehicle(1, licensePlate, type, category);
                                vehicleDao.insert(vehicle);
                                System.out.println("Inserted! New id = " + vehicle.getId());
                                System.out.println();
                                break;
                            case 2:
                                System.out.println("Verificando veiculo (por placa): ");
                                System.out.println("Enter the vehicle license plate");
                                String licensePlate2 = sc.next();
                                Vehicle vehicle2 = vehicleDao.findByPlate(licensePlate2);
                                System.out.println(vehicle2);
                                System.out.println();
                                break;
                            case 3:
                                // Captura a placa antiga
                                System.out.println("Enter the current vehicle license plate:");
                                String oldPlate = sc.next();

                                // Busca o veículo pelo número da placa antiga
                                Vehicle dep = vehicleDao.findByPlate(oldPlate);

                                // Captura a nova placa
                                System.out.println("Enter the new license plate:");
                                String newPlate = sc.next();
                                dep.setLicensePlate(newPlate); // Atualiza a placa do objeto

                                System.out.println("Enter the vehicle type \n"+
                                        "(PassengerCars, Motorcycles, Trucks, Ambulances, PoliceCar: )");
                                String newType = sc.next();
                                dep.setType(Type.valueOf(newType)); // Atualiza a placa do objeto

                                System.out.print("Enter the vehicle category \n " +
                                        "(MonthlyPayers, DeliveryTrucks, PublicService, Single)");
                                String newCategory = sc.next();
                                dep.setCategory(Category.valueOf(newCategory)); // Atualiza a placa do objeto

                                // Passa a placa antiga e o objeto atualizado para o método update
                                vehicleDao.update(dep, oldPlate);

                                System.out.println("Update completed");
                                System.out.println();
                                break;
                            case 4:
                                System.out.println("4. Removendo veiculo");
                                System.out.print("Enter id for delete : ");
                                String plate = sc.next();
                                vehicleDao.deleteByPlate(plate);
                                System.out.println("Delete completed");
                                break;
                            case 5:
                                System.out.println("5. Mostrando todos os veiculos cadastrados ");
                                System.out.println();
                                List<Vehicle> list = vehicleDao.findAll();
                                for (Vehicle d : list) {
                                    System.out.println(d);
                                }
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
                    break;
                case 3:
                    break;
                case 4:
                    break;
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
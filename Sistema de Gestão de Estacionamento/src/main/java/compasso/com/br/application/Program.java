package compasso.com.br.application;

import compasso.com.br.model.dao.MonthlyPayerDao;
import compasso.com.br.model.dao.ParkingSpotDao;
import compasso.com.br.model.dao.VehicleDao;
import compasso.com.br.model.dao.DaoFactory;
import compasso.com.br.model.entities.MonthlyPayer;
import compasso.com.br.model.entities.ParkingSpot;
import compasso.com.br.model.entities.Vehicle;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static compasso.com.br.model.entities.enums.Category.PassengerCars;

public class Program {
    public static void main(String[] args) {

        // Scanner para entrada do usuário
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        // Instanciando os DAOs para interação com o banco de dados
        VehicleDao vehicleDao = DaoFactory.createVehicleDao();
        ParkingSpotDao parkingSpotDao = DaoFactory.createParkingDao();
        MonthlyPayerDao monthlyPayerDao = DaoFactory.createMonthlyPayerDao();

        // Loop principal do menu
        while (!exit) {
            System.out.println("==== Parking Management System ====");
            System.out.println("1. Allow vehicle entry");
            System.out.println("2. Allow vehicle exit");
            System.out.println("3. Check available vacancies");
            System.out.println("0. To go out");
            System.out.print("Choose an option: ");

            // Obtém a escolha do usuário e consome a quebra de linha
            int choice = sc.nextInt();
            sc.nextLine(); // Consumir a quebra de linha
            System.out.println();

            // Executa a lógica com base na escolha do usuário
            switch (choice) {
                case 1:
                    handleVehicleEntry(sc, vehicleDao, parkingSpotDao, monthlyPayerDao);
                    break;

                case 2:
                    // Implementar lógica para saída de veículo (a ser implementado)
                    break;

                case 3:
                    checkAvailableSpots(parkingSpotDao);
                    break;

                case 0:
                    exit = true; // Encerra o loop, saindo do sistema
                    System.out.println("Leaving the system...");
                    sc.close();
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Função para lidar com a entrada de veículos
    private static void handleVehicleEntry(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao) {
        System.out.print("What category is your car? (Passenger Cars (C), Motorcycles (M), Delivery Trucks (T), Public Service Vehicles (P)): ");
        String choiseCategory = sc.next().toUpperCase();

        // Escolha da categoria do veículo
        switch (choiseCategory) {
            case "C":
                handlePassengerCarEntry(sc, vehicleDao, parkingSpotDao, monthlyPayerDao);
                break;

            case "M":
                handleMotorcycleEntry(sc, parkingSpotDao);
                break;

            case "T":
                handleTruckEntry(sc, parkingSpotDao);
                break;

            case "P":
                System.out.println("Have a good trip!"); // Veículos de serviço público têm entrada livre
                break;

            default:
                System.out.println("Invalid category! Please try again.");
        }
    }

    // Função para lidar com a entrada de carros de passeio
    private static void handlePassengerCarEntry(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        // Verifica se há vagas suficientes
        if (availableSpots.size() < 2) {
            System.out.println("No vacancies available, come back later!");
            return;
        }

        System.out.print("What type of car is yours? (Monthly Payer (M), Casual (C)): ");
        String carType = sc.next().toUpperCase();
        // Validação da entrada do usuário
        while (!carType.equals("M") && !carType.equals("C")) {
            System.out.println("Invalid type! Please choose Monthly Payer (M) or Casual (C).");
            carType = sc.next().toUpperCase();
        }

        // Lógica para pagadores mensais ou casuais
        if (carType.equals("M")) {
            handleMonthlyPayer(sc, vehicleDao, parkingSpotDao, monthlyPayerDao);
        } else {
            handleCasualEntry(sc, parkingSpotDao);
        }
    }

    // Lida com a entrada de pagadores mensais
    private static void handleMonthlyPayer(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao) {
        System.out.print("Enter your license plate: ");
        String licensePlate = sc.next().toUpperCase();
        sc.nextLine(); // Consumir a quebra de linha

        // Busca o pagador mensal pela placa
        MonthlyPayer monthlyPayer = monthlyPayerDao.findByPlate(licensePlate);
        if (monthlyPayer != null) {
            allocateSpots(sc, parkingSpotDao);
        } else {
            // Caso a placa não seja válida, oferece opção para se tornar mensalista
            System.out.println("Invalid license plate! Do you want to become a monthly member? Yes (Y) No (N)");
            String newMember = sc.next().toUpperCase();
            if (newMember.equals("Y")) {
                registerMonthlyPayer(sc, vehicleDao, monthlyPayerDao, licensePlate);
                allocateSpots(sc, parkingSpotDao);
            }
        }
    }

    // Registra um novo pagador mensal
    private static void registerMonthlyPayer(Scanner sc, VehicleDao vehicleDao, MonthlyPayerDao monthlyPayerDao, String licensePlate) {
        System.out.println("The monthly fee is: US$ 50.00");
        System.out.print("Did you make the payment? Yes (Y) No (N): ");
        String paid = sc.next().toUpperCase();
        sc.nextLine();

        // Verifica o pagamento e registra o veículo e o pagador mensal
        if (paid.equals("Y")) {
            System.out.print("Enter your car model: ");
            String carModel = sc.next();

            Vehicle newVehicle = new Vehicle(licensePlate, carModel, PassengerCars);
            vehicleDao.insert(newVehicle);

            MonthlyPayer newMonthlyPayer = new MonthlyPayer(LocalDate.now(), licensePlate);
            monthlyPayerDao.insert(newMonthlyPayer);

            System.out.println("New monthly member added successfully!");
        } else {
            System.out.println("Payment not completed. Operation canceled.");
        }
    }

    // Lida com a entrada de veículos casuais
    private static void handleCasualEntry(Scanner sc, ParkingSpotDao parkingSpotDao) {
        allocateSpots(sc, parkingSpotDao);
    }

    // Aloca vagas para um veículo
    private static void allocateSpots(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        // Seleção das vagas pelo usuário
        System.out.println("Select the vacancies (select two vacancies)");
        System.out.print("Vacancy 1: ");
        int vacancy1 = sc.nextInt();
        System.out.print("Vacancy 2: ");
        int vacancy2 = sc.nextInt();
        sc.nextLine();

        // Verifica se as vagas selecionadas estão disponíveis
        if (isSpotAvailable(vacancy1, vacancy2, availableSpots)) {
            updateSpotAsOccupied(vacancy1, parkingSpotDao);
            updateSpotAsOccupied(vacancy2, parkingSpotDao);

            System.out.println("Vehicle registered in vacancies " + vacancy1 + " and " + vacancy2 + ". Have a good trip!");
        } else {
            System.out.println("Invalid vacancies! Please choose others.");
        }
    }

    // Verifica se as vagas selecionadas estão disponíveis
    private static boolean isSpotAvailable(int vacancy1, int vacancy2, List<ParkingSpot> availableSpots) {
        boolean isAvailable = false;
        for (ParkingSpot spot : availableSpots) {
            if (spot.getNumber() == vacancy1 || spot.getNumber() == vacancy2) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    // Atualiza o status da vaga para ocupado
    private static void updateSpotAsOccupied(int vacancy, ParkingSpotDao parkingSpotDao) {
        ParkingSpot selectedSpot = new ParkingSpot();
        selectedSpot.setNumber(vacancy);
        selectedSpot.setOccupied(true);
        parkingSpotDao.update(selectedSpot);
    }

    // Lida com a entrada de motocicletas
    private static void handleMotorcycleEntry(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();
        if (!availableSpots.isEmpty()) {
            System.out.println("Registering motorcycle entry...");
            allocateSpotForSingleVehicle(sc, parkingSpotDao);
        } else {
            System.out.println("No vacancies available, come back later!");
        }
    }

    // Lida com a entrada de caminhões
    private static void handleTruckEntry(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();
        if (availableSpots.size() >= 4) {
            System.out.println("Registering truck entry...");
            allocateSpots(sc, parkingSpotDao); // Aloca 4 vagas
        } else {
            System.out.println("No vacancies available, come back later!");
        }
    }

    // Aloca uma vaga para veículos que precisam de uma única vaga (moto)
    private static void allocateSpotForSingleVehicle(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        System.out.println("Select the vacancy:");
        int vacancy = sc.nextInt();
        sc.nextLine();

        // Verifica se a vaga está disponível e a atualiza
        if (isSpotAvailable(vacancy, -1, availableSpots)) {
            updateSpotAsOccupied(vacancy, parkingSpotDao);
            System.out.println("Motorcycle registered in vacancy " + vacancy + ". Have a good trip!");
        } else {
            System.out.println("Invalid vacancy! Please choose another.");
        }
    }

    // Verifica as vagas disponíveis
    private static void checkAvailableSpots(ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();
        System.out.println("Vacancies available: " + availableSpots.size() + "\n");
    }
}

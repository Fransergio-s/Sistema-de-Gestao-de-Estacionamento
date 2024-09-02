package compasso.com.br.application;


import compasso.com.br.model.dao.*;
import compasso.com.br.model.entities.MonthlyPayer;
import compasso.com.br.model.entities.ParkingSpot;
import compasso.com.br.model.entities.Ticket;
import compasso.com.br.model.entities.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static compasso.com.br.model.entities.enums.Category.DeliveryTrucks;
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
        TicketDao ticketDao = DaoFactory.createTicketDao();

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
                    handleVehicleEntry(sc, vehicleDao, parkingSpotDao, monthlyPayerDao, ticketDao);
                    break;

                case 2:
                    handleVehicleExit(sc, vehicleDao, parkingSpotDao, monthlyPayerDao, ticketDao);
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
    private static void handleVehicleEntry(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao, TicketDao ticketDao) {
        System.out.print("What category is your car? (Passenger Cars (C), Motorcycles (M), Delivery Trucks (T), Public Service Vehicles (P)): ");
        String choiseCategory = sc.next().toUpperCase();
        int entryGate = checkEntryGate(sc, choiseCategory);

        // Escolha da categoria do veículo
        switch (choiseCategory) {
            case "C":
                handlePassengerCarEntry(sc, vehicleDao, parkingSpotDao, monthlyPayerDao, ticketDao, entryGate);
                break;

            case "M":
                handleMotorcycleEntry(sc, vehicleDao, parkingSpotDao, monthlyPayerDao, ticketDao, entryGate);
                break;

            case "T":
                handleTruckEntry(sc, vehicleDao, parkingSpotDao);
                break;

            case "P":
                System.out.println("Have a good trip!"); // Veículos de serviço público têm entrada livre
                break;

            default:
                System.out.println("Invalid category! Please try again.");
        }
    }

    // Função para lidar com a entrada de veículos
    private static void handleVehicleExit(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao, TicketDao ticketDao) {
        System.out.print("What category is your car? (Passenger Cars (C), Motorcycles (M), Delivery Trucks (T), Public Service Vehicles (P)): ");
        String choiseCategory = sc.next().toUpperCase();
        int exitGate = checkExitGate(sc, choiseCategory);

        // Escolha da categoria do veículo
        switch (choiseCategory) {
            case "C":
                handlePassengerCarExit(sc, parkingSpotDao, monthlyPayerDao, exitGate, ticketDao);
                break;

            case "M":
                handleMotorcycleExit(sc, parkingSpotDao, monthlyPayerDao,ticketDao, exitGate);
                break;

            case "T":
                handleTruckExit(sc, vehicleDao, parkingSpotDao);
                break;

            case "P":
                System.out.println("Have a good trip!"); // Veículos de serviço público têm entrada livre
                break;

            default:
                System.out.println("Invalid category! Please try again.");
        }
    }

    // Função para lidar com a entrada de carros de passeio
    private static void handlePassengerCarEntry(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao, TicketDao ticketDao, Integer entryGate) {

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
            handleCasualEntry(sc, parkingSpotDao, ticketDao, entryGate);
        }
    }


    // Lida com a entrada de motocicletas
    private static void handleMotorcycleEntry(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao, TicketDao ticketDao, Integer entryGate) {

        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        // Verifica se há vagas suficientes
        if (availableSpots.isEmpty()) {
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
            handleMonthlyPayerMotorcycle(sc, vehicleDao, parkingSpotDao, monthlyPayerDao);
        } else {
            handleCasualEntryMotorcycle(sc, parkingSpotDao, ticketDao,  entryGate);
        }
    }

    // Lida com a entrada de caminhões
    private static void handleTruckEntry(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao) {

        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        registerDeliveredTrucks(sc, vehicleDao);
        // Mostra ao usuário as vagas disponíveis em quartetos
        System.out.println("Available spots:");
        for (int i = 0; i < availableSpots.size(); i++) {
            System.out.print(availableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada quatro números para mostrar quartetos de vagas
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println(); // Quebra de linha após a lista de vagas

        System.out.println("Select the first vacancy: ");
        int selectedSpot = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se as quatro vagas necessárias estão disponíveis
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);
        ParkingSpot secondSpot = parkingSpotDao.findByNumber(selectedSpot + 1);
        ParkingSpot thirdSpot = parkingSpotDao.findByNumber(selectedSpot + 2);
        ParkingSpot fourthSpot = parkingSpotDao.findByNumber(selectedSpot + 3);

        if (firstSpot == null || secondSpot == null || thirdSpot == null || fourthSpot == null ||
                firstSpot.isOccupied() || secondSpot.isOccupied() || thirdSpot.isOccupied() || fourthSpot.isOccupied()) {
            System.out.println("One or more selected spots in the quartet are not available. Please try again.");
            return;
        }

        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(true);
        secondSpot.setOccupied(true);
        thirdSpot.setOccupied(true);
        fourthSpot.setOccupied(true);
        parkingSpotDao.update(firstSpot);
        parkingSpotDao.update(secondSpot);
        parkingSpotDao.update(thirdSpot);
        parkingSpotDao.update(fourthSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = firstSpot.getNumber() + "/" + secondSpot.getNumber() + "/" + thirdSpot.getNumber() + "/" + fourthSpot.getNumber();
        System.out.println("Entry registered successfully. Spots " + occupiedSpots + " have been allocated.");

    }

    // Função para lidar com a saida de carros de passeio
    private static void handlePassengerCarExit(Scanner sc, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao, Integer exitGate, TicketDao ticketDao) {

        System.out.print("What type of car is yours? (Monthly Payer (M), Casual (C)): ");
        String carType = sc.next().toUpperCase();
        // Validação da entrada do usuário
        while (!carType.equals("M") && !carType.equals("C")) {
            System.out.println("Invalid type! Please choose Monthly Payer (M) or Casual (C).");
            carType = sc.next().toUpperCase();
        }

        // Lógica para pagadores mensais ou casuais
        if (carType.equals("M")) {
            handleMonthlyPayerExit(sc, parkingSpotDao, monthlyPayerDao);
        } else {
            handleCasualExit(sc, parkingSpotDao, exitGate, ticketDao);
        }
    }


    // Lida com a saida de motocicletas
    private static void handleMotorcycleExit(Scanner sc, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao,TicketDao ticketDao, Integer exitGate) {

        System.out.print("What type of car is yours? (Monthly Payer (M), Casual (C)): ");
        String carType = sc.next().toUpperCase();
        // Validação da entrada do usuário
        while (!carType.equals("M") && !carType.equals("C")) {
            System.out.println("Invalid type! Please choose Monthly Payer (M) or Casual (C).");
            carType = sc.next().toUpperCase();
        }

        // Lógica para pagadores mensais ou casuais
        if (carType.equals("M")) {
            handleMonthlyPayerMotorcycleExit(sc, parkingSpotDao, monthlyPayerDao);
        } else {
            handleCasualExitMotorcycle(sc, parkingSpotDao, ticketDao, exitGate);
        }
    }

    // Lida com a entrada de caminhões
    private static void handleTruckExit(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> unavailableSpots = parkingSpotDao.findUnavailableSpots();

        registerDeliveredTrucks(sc, vehicleDao);
        // Mostra ao usuário as vagas disponíveis em quartetos
        System.out.println("Unavailable spots:");
        for (int i = 0; i < unavailableSpots.size(); i++) {
            System.out.print(unavailableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada quatro números para mostrar quartetos de vagas
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println(); // Quebra de linha após a lista de vagas

        System.out.println("Select the first vacancy: ");
        int selectedSpot = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se as quatro vagas necessárias estão disponíveis
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);
        ParkingSpot secondSpot = parkingSpotDao.findByNumber(selectedSpot + 1);
        ParkingSpot thirdSpot = parkingSpotDao.findByNumber(selectedSpot + 2);
        ParkingSpot fourthSpot = parkingSpotDao.findByNumber(selectedSpot + 3);

        if (firstSpot == null || secondSpot == null || thirdSpot == null || fourthSpot == null ||
                !firstSpot.isOccupied() || !secondSpot.isOccupied() || !thirdSpot.isOccupied() || !fourthSpot.isOccupied()) {
            System.out.println("One or more selected spots in the quartet are not available. Please try again.");
            return;
        }

        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(false);
        secondSpot.setOccupied(false);
        thirdSpot.setOccupied(false);
        fourthSpot.setOccupied(false);
        parkingSpotDao.update(firstSpot);
        parkingSpotDao.update(secondSpot);
        parkingSpotDao.update(thirdSpot);
        parkingSpotDao.update(fourthSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = firstSpot.getNumber() + "/" + secondSpot.getNumber() + "/" + thirdSpot.getNumber() + "/" + fourthSpot.getNumber();
        System.out.println("Exit registered successfully. Spots " + occupiedSpots + " have been deallocated.");
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

    // Lida com a saida de pagadores mensais
    private static void handleMonthlyPayerExit(Scanner sc, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao) {
        System.out.print("Enter your license plate: ");
        String licensePlate = sc.next().toUpperCase();
        sc.nextLine(); // Consumir a quebra de linha

        // Busca o pagador mensal pela placa
        MonthlyPayer monthlyPayer = monthlyPayerDao.findByPlate(licensePlate);
        if (monthlyPayer != null) {
            deallocateSpots(sc, parkingSpotDao);
        } else {
            // Caso a placa não seja válida, oferece opção para se tornar mensalista
            System.out.println("Invalid license plate! Plate not registered. ");
        }
    }

    // Aloca vagas para um veículo
    private static void deallocateSpots(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> unavailableSpots = parkingSpotDao.findUnavailableSpots();

        // Mostra ao usuário as vagas disponíveis
        System.out.println("Unavailable spots:");
        for (int i = 0; i < unavailableSpots.size(); i++) {
            System.out.print(unavailableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada dois números para mostrar pares de vagas
            if ((i + 1) % 2 == 0) {
                System.out.println();
            }
        }

        System.out.println("\nSelect the first vacancy: ");
        int selectedSpot = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se a segunda vaga necessária está disponível
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);
        ParkingSpot secondSpot = parkingSpotDao.findByNumber(selectedSpot + 1);

        if (firstSpot == null || secondSpot == null || !firstSpot.isOccupied() || !secondSpot.isOccupied()) {
            System.out.println("The selected spot or its pair is not available. Please try again.");
            return;
        }

        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(false);
        secondSpot.setOccupied(false);
        parkingSpotDao.update(firstSpot);
        parkingSpotDao.update(secondSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = firstSpot.getNumber() + "/" + secondSpot.getNumber();
        System.out.println("Exit registered successfully. Spots " + occupiedSpots + " have been deallocated.");
    }

    // Lida com a saida de pagadores mensais
    private static void handleMonthlyPayerMotorcycleExit(Scanner sc, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao) {
        System.out.print("Enter your license plate: ");
        String licensePlate = sc.next().toUpperCase();
        sc.nextLine(); // Consumir a quebra de linha

        // Busca o pagador mensal pela placa
        MonthlyPayer monthlyPayer = monthlyPayerDao.findByPlate(licensePlate);
        if (monthlyPayer != null) {
            deallSpotForSingleVehicle(sc, parkingSpotDao);
        } else {
            // Caso a placa não seja válida, oferece opção para se tornar mensalista
            System.out.println("Invalid license plate! Plate not registered. ");
        }
    }


    // Lida com a entrada de pagadores mensais
    private static void handleMonthlyPayerMotorcycle(Scanner sc, VehicleDao vehicleDao, ParkingSpotDao parkingSpotDao, MonthlyPayerDao monthlyPayerDao) {
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
                allocateSpotForSingleVehicle(sc, parkingSpotDao);
            }
        }
    }

    // Aloca uma vaga para veículos que precisam de uma única vaga (moto)
    private static void allocateSpotForSingleVehicle(Scanner sc, ParkingSpotDao parkingSpotDao) {

        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        // Mostra ao usuário as vagas disponíveis
        System.out.println("Available spots:");
        for (int i = 0; i < availableSpots.size(); i++) {
            System.out.print(availableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada dois números para mostrar pares de vagas
            if ((i + 1) % 2 == 0) {
                System.out.println();
            }
        }
        System.out.println("Select the vacancy:");
        int vacancy = sc.nextInt();
        sc.nextLine();

        // Verifica se a vaga está disponível e a atualiza
        if (isSpotAvailable(vacancy, availableSpots)) {
            updateSpotAsOccupied(vacancy, parkingSpotDao);
            System.out.println("Motorcycle registered in vacancy " + vacancy + ". Have a good trip!");
        } else {
            System.out.println("Invalid vacancy! Please choose another.");
        }
    }

    // Registra um novo pagador mensal
    private static void registerMonthlyPayer(Scanner sc, VehicleDao vehicleDao, MonthlyPayerDao monthlyPayerDao, String licensePlate) {
        System.out.println("The monthly fee is: US$ 250.00");
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
    // Registra um novo pagador mensal
    private static void registerDeliveredTrucks(Scanner sc, VehicleDao vehicleDao) {

        System.out.println("Insert your license plate");
        String licensePlate = sc.next();

        Vehicle vehicle = vehicleDao.findByPlate(licensePlate);
        if (vehicle != null) {
            System.out.println("Delivery truck already registered");

        }
        else {
            System.out.print("Enter your car model: ");
            String carModel = sc.next();
            Vehicle newVehicle = new Vehicle(licensePlate, carModel, DeliveryTrucks);
            vehicleDao.insert(newVehicle);

            System.out.println("New delivered truck  added successfully!");
        }
    }


    // Lida com a entrada de veículos que usam ticket
    private static void handleCasualEntry(Scanner sc, ParkingSpotDao parkingSpotDao, TicketDao ticketDao,Integer entryGate) {
        // Busca vagas disponíveis
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();


        // Mostra ao usuário as vagas disponíveis
        System.out.println("Available spots:");
        for (int i = 0; i < availableSpots.size(); i++) {
            System.out.print(availableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada dois números para mostrar pares de vagas
            if ((i + 1) % 2 == 0) {
                System.out.println();
            }
        }

        System.out.println("\nSelect the first vacancy: ");
        int selectedSpot = sc.nextInt();

        System.out.println("Insert your license plate");
        String licensePlate = sc.next();

        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se a segunda vaga necessária está disponível
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);
        ParkingSpot secondSpot = parkingSpotDao.findByNumber(selectedSpot + 1);

        if (firstSpot == null || secondSpot == null || firstSpot.isOccupied() || secondSpot.isOccupied()) {
            System.out.println("The selected spot or its pair is not available. Please try again.");
            return;
        }

        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(true);
        secondSpot.setOccupied(true);
        parkingSpotDao.update(firstSpot);
        parkingSpotDao.update(secondSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = firstSpot.getNumber() + "/" + secondSpot.getNumber();
        Ticket ticket = new Ticket(licensePlate, LocalDateTime.now(), LocalDateTime.now(), entryGate, 0, occupiedSpots, 0.0);
        ticketDao.insert(ticket);

        System.out.println("Entry registered successfully. Spots " + occupiedSpots + " have been allocated.");
    }

    // Lida com a saída de veículos casuais
    private static void handleCasualExit(Scanner sc, ParkingSpotDao parkingSpotDao, Integer exitGate, TicketDao ticketDao) {

        // Solicita o ID do ticket
        System.out.println("\nSelect the ticket id: ");
        int id = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se o ticket existe
        Ticket ticket = ticketDao.findById(id);
        if (ticket == null) {
            System.out.println("Ticket not found. Please try again.");
            return;
        }

        // Pega as vagas ocupadas do ticket e separa-as
        String[] occupiedSpots = ticket.getParkingSpot().split("/");
        int numberOfSpots = occupiedSpots.length;

        // Marca as vagas como disponíveis novamente
        for (String spotNumber : occupiedSpots) {
            ParkingSpot spot = parkingSpotDao.findByNumber(Integer.parseInt(spotNumber));
            if (spot != null) {
                spot.setOccupied(false);
                parkingSpotDao.update(spot);
            }
        }

        // Calcula o tempo de permanência e o valor a pagar
        LocalDateTime entryTime = ticket.getEntryHour();
        LocalDateTime exitTime = LocalDateTime.now(); // Hora de saída atual
        long minutesParked = java.time.Duration.between(entryTime, exitTime).toMinutes();

        // Calcula o valor a pagar
        double amount = minutesParked * 0.10;
        if (amount < 5.0) {
            amount = 5.0;
        } else {
            amount = amount * numberOfSpots; // Multiplica pelo número de vagas ocupadas
        }

        // Atualiza o ticket com a hora de saída e o valor a pagar
        ticket.setExitHour(exitTime);
        ticket.setAmount(amount);
        ticket.setExitGate(exitGate);
        ticketDao.update(ticket);

        // Informa o valor a ser pago
        System.out.printf("Exit registered successfully. The total amount to pay is: $%.2f%n", amount);
    }

    // Lida com a entrada de veículos casuais
    private static void handleCasualEntryMotorcycle(Scanner sc, ParkingSpotDao parkingSpotDao,TicketDao ticketDao, Integer entryGate) {
        // Busca vagas disponíveis
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        // Verifica se há vagas suficientes para um carro de passeio
        if (availableSpots.isEmpty()) {
            System.out.println("No vacancies available, come back later!");
            return;
        }

        // Mostra ao usuário as vagas disponíveis em pares
        System.out.println("Available spots:");
        for (int i = 0; i < availableSpots.size(); i += 2) {
            if (i + 1 < availableSpots.size()) {
                System.out.println(availableSpots.get(i).getNumber() + " / " + availableSpots.get(i + 1).getNumber());
            } else {
                // Se houver um número ímpar de vagas, mostra a última vaga sem par
                System.out.println(availableSpots.get(i).getNumber());
            }
        }

        System.out.println("\nSelect the first vacancy: ");
        int selectedSpot = sc.nextInt();

        System.out.println("Insert your license plate");
        String licensePlate = sc.next();

        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se a segunda vaga necessária está disponível
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);


        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(true);
        parkingSpotDao.update(firstSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = String.valueOf(firstSpot.getNumber());
        Ticket ticket = new Ticket(licensePlate, LocalDateTime.now(), LocalDateTime.now(), entryGate, 0, occupiedSpots, 0.0);
        ticketDao.insert(ticket);

        System.out.println("Entry registered successfully. Spots " + occupiedSpots + " have been allocated.");

    }

    // Lida com a entrada de veículos casuais
    private static void handleCasualExitMotorcycle(Scanner sc, ParkingSpotDao parkingSpotDao, TicketDao ticketDao, Integer exitGate) {
        // Busca todas as vagas ocupadas

        // Solicita o ID do ticket
        System.out.println("\nSelect the ticket id: ");
        int id = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se o ticket existe
        Ticket ticket = ticketDao.findById(id);
        if (ticket == null) {
            System.out.println("Ticket not found. Please try again.");
            return;
        }

        // Pega as vagas ocupadas do ticket e separa-as
        String[] occupiedSpots = ticket.getParkingSpot().split("/");
        int numberOfSpots = occupiedSpots.length;

        // Marca as vagas como disponíveis novamente
        for (String spotNumber : occupiedSpots) {
            ParkingSpot spot = parkingSpotDao.findByNumber(Integer.parseInt(spotNumber));
            if (spot != null) {
                spot.setOccupied(false);
                parkingSpotDao.update(spot);
            }
        }

        // Calcula o tempo de permanência e o valor a pagar
        LocalDateTime entryTime = ticket.getEntryHour();
        LocalDateTime exitTime = LocalDateTime.now(); // Hora de saída atual
        long minutesParked = java.time.Duration.between(entryTime, exitTime).toMinutes();

        // Calcula o valor a pagar
        double amount = minutesParked * 0.10;
        if (amount < 5.0) {
            amount = 5.0;
        } else {
            amount = amount * numberOfSpots; // Multiplica pelo número de vagas ocupadas
        }

        // Atualiza o ticket com a hora de saída e o valor a pagar
        ticket.setExitHour(exitTime);
        ticket.setAmount(amount);
        ticket.setExitGate(exitGate);
        ticketDao.update(ticket);

        // Informa o valor a ser pago
        System.out.printf("Exit registered successfully. The total amount to pay is: $%.2f%n", amount);
    }

    // Aloca vagas para um veículo
    private static void allocateSpots(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();

        // Mostra ao usuário as vagas disponíveis
        System.out.println("Available spots:");
        for (int i = 0; i < availableSpots.size(); i++) {
            System.out.print(availableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada dois números para mostrar pares de vagas
            if ((i + 1) % 2 == 0) {
                System.out.println();
            }
        }

        System.out.println("\nSelect the first vacancy: ");
        int selectedSpot = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se a segunda vaga necessária está disponível
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);
        ParkingSpot secondSpot = parkingSpotDao.findByNumber(selectedSpot + 1);

        if (firstSpot == null || secondSpot == null || firstSpot.isOccupied() || secondSpot.isOccupied()) {
            System.out.println("The selected spot or its pair is not available. Please try again.");
            return;
        }

        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(true);
        secondSpot.setOccupied(true);
        parkingSpotDao.update(firstSpot);
        parkingSpotDao.update(secondSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = firstSpot.getNumber() + "/" + secondSpot.getNumber();
        System.out.println("Entry registered successfully. Spots " + occupiedSpots + " have been allocated.");
    }


    // Verifica se as vagas selecionadas estão disponíveis
    private static boolean isSpotAvailable(int vacancy1, List<ParkingSpot> availableSpots) {
        boolean isAvailable = false;
        for (ParkingSpot spot : availableSpots) {
            if (spot.getNumber() == vacancy1) {
                isAvailable = true;
                break;
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


    // Aloca uma vaga para veículos que precisam de uma única vaga (moto)
    private static void deallSpotForSingleVehicle(Scanner sc, ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> unavailableSpots = parkingSpotDao.findUnavailableSpots();

        // Mostra ao usuário as vagas disponíveis
        System.out.println("Unavailable spots:");
        for (int i = 0; i < unavailableSpots.size(); i++) {
            System.out.print(unavailableSpots.get(i).getNumber() + " ");
            // Coloca uma quebra de linha a cada dois números para mostrar pares de vagas
            if ((i + 1) % 2 == 0) {
                System.out.println();
            }
        }

        System.out.println("\nSelect the first vacancy: ");
        int selectedSpot = sc.nextInt();
        sc.nextLine(); // Consumir a quebra de linha

        // Verifica se a segunda vaga necessária está disponível
        ParkingSpot firstSpot = parkingSpotDao.findByNumber(selectedSpot);

        if (firstSpot == null || !firstSpot.isOccupied()){
            System.out.println("The selected spot is not occupied. Please try again.");
            return;
        }

        // Atualiza o estado das vagas como ocupadas
        firstSpot.setOccupied(false);

        parkingSpotDao.update(firstSpot);

        // Registra a entrada do veículo com as vagas ocupadas
        String occupiedSpots = firstSpot.getNumber() + "";
        System.out.println("Exit registered successfully. Spots " + occupiedSpots + " have been deallocated.");
    }

    // Verifica as vagas disponíveis
    private static void checkAvailableSpots(ParkingSpotDao parkingSpotDao) {
        List<ParkingSpot> availableSpots = parkingSpotDao.findAvailableSpots();
        System.out.println("Vacancies available: " + availableSpots.size() + "\n");
    }


    // Lógica para verificar a cancela de entrada
    private static int checkEntryGate(Scanner sc, String category) {
        while (true) {
            System.out.println("Enter the entry gate (1 to 5):");
            int gate = sc.nextInt();

            switch (category) {
                case "C" -> {
                    if (gate >= 1 && gate <= 5) {
                        return gate; // Cancela válida
                    }
                }
                case "M" -> {
                    if (gate == 5) {
                        return gate;
                    }
                }
                case "T" -> {
                    if (gate == 1) {
                        return gate;
                    }
                }
                case "P" -> {
                    return gate;
                }
                case null, default -> System.out.println("Invalid entry gate. Please try in other.");
            }
        }
    }

    // Lógica para verificar a cancela de saída
    private static int checkExitGate(Scanner sc, String category) {
        while (true) {
            System.out.println("Enter the entry gate (6 at 10):");
            int gate = sc.nextInt();

            switch (category) {
                case "C" -> {
                    if (gate >= 6 && gate <= 10) {
                        return gate; // Cancela válida
                    }
                }
                case "M" -> {
                    if (gate == 10) {
                        return gate;
                    }
                }
                case "T" -> {
                    if (gate >= 6 && gate <= 10) {
                        return gate;
                    }
                }
                case "P" -> {
                    return gate;
                }
                case null, default -> System.out.println("Invalid exit gate. Please try in other.");
            }
        }


    }



}

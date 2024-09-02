# Challenge 1 - Compass Uol: Sistema-de-Gestão-de-Estacionamento


## The project:

This project aims to simulate the registration, entry and exit of vehicles in a shopping mall parking lot. Without using the Spring MVC framework. The goal is to follow good architectural practices, clean code principles and adherence to SOLID principles.

The project aimed to experiment and apply the main OOP concepts, such as inheritance, polymorphism, encapsulation, abstraction and dependency injection, in addition to using Java resources such as Stream, Enum and Generics.

The program connects to a SQL database using JDBC and has 5 tables: monthly_payers, parking_spot, ticket and vehicles. The project offers CRUD functionality for ticket, vehicles and monthly_payers. Allowing you to register new mothly payers and tickets in the system.

The program also includes gate management to allow only permitted vehicles to enter the gates. It is also possible to create parking entrance tickets based on the vehicle's license plate, or if you prefer, register the vehicle as a monthly ticket holder.

## Project Structure:


The project has several folders for better code organization. I will briefly describe each one:

- *db (src/main/java/db):* Contains the class responsible for configuring and connecting to the database.

- *dao (src/main/java/dao):* Contains service interfaces that perform QUERY(s) in the database.

- *entities (src/main/java/entities):* Contains the system classes.

- *enums (src/main/java/enums):* Contains enums that help classify attributes with predefined values.

- *application (src/main/java/view/application):* Contains the class responsible for starting the application, creating menus for the user and performing system operations.

##

## Running the Project:

### Importing PostgreSQL database server:
- Install MYSQL 8.4.2 on your machine.
- After installation, open MYSQL and install the full version and configure a user as admin and define your password.
- Now you will access the ‘resources’ folder in the project root directory and insert your database data into the ‘db.properties’ file.
- Go to the ‘InitializeDatabase’ class and execute the function. The system must create the tables in your database.
- Excellent! You have synchronized the 'parking_management_system' database. 
- You can run the application in the ‘program’ class

*If the tables show an error when starting the tables, just go to your database and perform the step below*

#### Creating the database tables:
<code>
-- Criação das tabelas

-- Tabela parking_spot
CREATE TABLE IF NOT EXISTS parking_spot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    number INT NOT NULL,
    occupied BOOLEAN NOT NULL DEFAULT FALSE,
    reserved BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (number)
);

-- Tabela vehicle
CREATE TABLE IF NOT EXISTS vehicle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plate VARCHAR(10) NOT NULL,
    model VARCHAR(50) NOT NULL,
    category VARCHAR(20) NOT NULL,
    UNIQUE (plate)
);

-- Tabela monthly_payers
CREATE TABLE IF NOT EXISTS monthly_payers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_month DATE NOT NULL,
    plate VARCHAR(10),
    FOREIGN KEY (plate) REFERENCES vehicle(plate)
);

-- Tabela ticket
CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plate VARCHAR(10) NOT NULL,
    entry_hour DATETIME NOT NULL,
    exit_hour DATETIME,
    entry_gate INT NOT NULL,
    exit_gate INT,
    amount DECIMAL(10,2),
    parking_spot VARCHAR(100),
    FOREIGN KEY (plate) REFERENCES vehicle(plate)
);

-- Inserção de 500 vagas na tabela parking_spot
-- Definindo as 200 primeiras vagas como reserved = true
INSERT INTO parking_spot (number, reserved) 
SELECT number, 
       CASE 
           WHEN number <= 200 THEN TRUE 
           ELSE FALSE 
       END AS reserved
FROM (
    SELECT @rownum := @rownum + 1 AS number
    FROM (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS t1,
         (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) AS t2,
         (SELECT @rownum := 0) AS t0
) AS numbers
WHERE number <= 500;

</code>

### Running Java application:

- Install Java
- Install Java 18 or higher on your machine for the application to work.
- Install an IDE capable of running Java code in version 18 or higher, such as IntelliJ or Eclipse.
- Open the project in your preferred IDE and navigate to the src directory. Inside the src directory, go to the main directory and open the java directory.
- Here you will be able to see the project structure mentioned above.
- Expand the application package and click on the class inside it called Application.
- Inside this class, there is a main method that can run the entire application in the console.
- Click "Run".
- Now we will run the console application.

### Console:
- On the console, you will be asked to choose between the 3 options available on the system. They are: 1. Allow vehicle entry, 2. Allow vehicle exit, 3. Check available vacancies and 0. To go out. Just click between 0,1,2 or 3 and the system will direct you to the desired option.


### Allow vehicle entry:
- In this window the system will ask you what category your vehicle is, and depending on the answer the system will only allow you to enter a certain gate.
- The system will allow vehicles from the "Passenger Cars" and "Public Service Vehicles" categories to enter any gate from 1 to 5, "Delivery Trucks" can only enter through gate 1, and "Motorcycles" only through gate 5
- If the vehicle is "Passenger Cars" or "Motorcycles", the system will ask whether the vehicle is a monthly vehicle or not. 
- If you select that you are a monthly member, the system will ask for your license plate number. If the sign is listed as a monthly member, the system will only ask the user to select the vacancy they will occupy. If the card is not registered, the system will ask if the user wants to become a monthly member. If desired, the system will register the vehicle as a monthly member and reserve the space that the user will use. If you choose not to, the system will return to the previous menu.
- If the "Casual" option is selected, the system will request license plate data, occupied spaces and will generate a ticket for the user.
- If the vehicle was in the "Delivery Trucks" category, the system will request the license plate data. If they appear as registered, the system will reserve spaces for the vehicle. If not, the system will register the vehicle and then reserve the spaces that will be used (4 in total).
- If the vehicle is a public service vehicle, the system will only allow entry without requesting any data.


### Allow vehicle exit:
- In this window the system will ask you what category your vehicle is, and depending on the answer the system will only allow you to exit at a certain gate.
- The system will allow vehicles in the "Passenger Cars", "Delivery Trucks" and "Public Service Vehicles" categories to exit at any gate from 6 to 10, and "Motorcycles" only at gate 10
- If the vehicle is "Passenger Cars" or "Motorcycles", the system will ask whether the vehicle is a monthly vehicle or not. 
- If you select that you are a monthly member, the system will ask for your license plate number. If the sign is listed as a monthly ticket holder, the system will only ask the user to select the vacancy they will vacate. If the vacancy is not listed as occupied or the vehicle is not a monthly ticket holder, the system will return to the previous screen.
- If the "Casual" option is selected, the system will request the ticket ID. If the ticket exists, the system will return the amount to be paid by the user. The amount to be paid is equal to the time the user spent * 0.10, multiplied by the number of vacancies. If the amount to be paid is less than 5, the system will set the amount as 5
- If the vehicle was in the "Delivery Trucks" category, the system will request the license plate data. If they appear as registered, the system will ask for the vacancies occupied and will vacate them. If not, you will return to the previous screen.
- If the vehicle is a public service vehicle, the system will only release the exit without requesting any data. 


### Check available vacancies:
- In this window the system will show the number of available vacancies.

### Em caso de alguma duvida, contate-me pelo e-mail: fransergio.morato.pb@compasso.com.br


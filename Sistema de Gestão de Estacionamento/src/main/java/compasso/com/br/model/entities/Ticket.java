package compasso.com.br.model.entities;

import java.util.Date;

public class Ticket {

    private Integer id;
    private Vehicle vehicle;
    private Date entryTime;
    private Date exitTime;
    private Double ammount;
    private int entryGate; // Número da cancela de entrada.
    private int exitGate; // Número da cancela de saída.
    private int parkingSpace; // Número da vaga ocupada.


}

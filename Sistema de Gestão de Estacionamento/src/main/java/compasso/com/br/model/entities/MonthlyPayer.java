package compasso.com.br.model.entities;

import java.time.LocalDate;

public class MonthlyPayer {

    private int id;
    private LocalDate paymentMonth;
    private int idVehicle;

    public MonthlyPayer() {
    }

    public MonthlyPayer(int id, LocalDate paymentMonth, int idVehicle) {
        this.id = id;
        this.paymentMonth = paymentMonth;
        this.idVehicle = idVehicle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(LocalDate paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public Integer getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(int idVehicle) {
        this.idVehicle = idVehicle;
    }

}

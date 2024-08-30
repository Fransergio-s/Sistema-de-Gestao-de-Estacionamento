package compasso.com.br.model.entities;

import java.time.LocalDate;

public class MonthlyPayer {

    private int id;
    private LocalDate paymentMonth;
    private String plate;

    public MonthlyPayer() {
    }

    public MonthlyPayer(LocalDate paymentMonth, String plate) {
        this.paymentMonth = paymentMonth;
        this.plate = plate;
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


    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}

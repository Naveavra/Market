package PresentationLayer.Suppliers;

public class Discount {
    private int amount;
    private Double discount;

    public Discount(int amount,double discount) {
        this.amount = amount;
        this.discount = discount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

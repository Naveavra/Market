package DomainLayer.Suppliers;

public class Discount {
    private int amount;
    private double discount;

    public Discount(int amount,double discount) {
        this.amount = amount;
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

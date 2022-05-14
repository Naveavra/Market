package PresentationLayer.Supplier;

import DomainLayer.Supplier.OrderFromSupplier;

import java.time.LocalDateTime;

public class PastOrder extends Order {
    private double totalPrice;
    private String finishDate;//the day the order arrived to destination

    public PastOrder(Order order, double totalPrice) {
        super(order);
        this.totalPrice=totalPrice;
        this.finishDate= LocalDateTime.now().toString();
    }

    public PastOrder(Order order, double totalPrice, String finishDate) {
        super(order);
        this.totalPrice=totalPrice;
        this.finishDate= finishDate;
    }

    public int getOrderId(){
        return super.getOrderId();
    }
    public String toString(){
        return "total price: "+totalPrice+", finish date: "+finishDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

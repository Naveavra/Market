package DomainLayer;

import java.time.LocalDateTime;

public class PastOrder extends Order {
    private double totalPrice;
    private String finishDate;

    public PastOrder(Order order, double totalPrice) {
        super(order);
        this.totalPrice=totalPrice;
        this.finishDate=LocalDateTime.now().toString();
    }
    public int getOrderId(){
        return super.getOrderId();
    }
    public String toString(){
        return "total price: "+totalPrice+", finish date"+finishDate;
    }

}

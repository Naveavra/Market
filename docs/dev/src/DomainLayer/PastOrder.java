package DomainLayer;

import java.time.LocalDateTime;

public class PastOrder extends OrderFromSupplier {
    private double totalPrice;
    private String finishDate;

    public PastOrder(OrderFromSupplier order, double totalPrice) {
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

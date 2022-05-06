package DomainLayer;

import java.time.LocalDateTime;

public class PastOrderSupplier extends OrderFromSupplier {
    private double totalPrice;
    private String finishDate;

    public PastOrderSupplier(OrderFromSupplier order, double totalPrice) {
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

package DomainLayer.Suppliers;

import java.time.LocalDateTime;

public class PastOrderSupplier extends OrderFromSupplier {
    private double totalPrice;
    private String finishDate;//the day the order arrived to destination

    public PastOrderSupplier(OrderFromSupplier order, double totalPrice) {
        super(order);
        this.totalPrice=totalPrice;
        this.finishDate=LocalDateTime.now().toString();
    }

    public PastOrderSupplier(OrderFromSupplier order, double totalPrice, String finishDate) {
        super(order);
        this.totalPrice=totalPrice;
        this.finishDate= finishDate;
    }


    public int getOrderId(){
        return super.getOrderId();
    }
    public String toString(){
        return "total price: "+totalPrice+", finish date"+finishDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

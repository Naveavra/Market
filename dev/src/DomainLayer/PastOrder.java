package DomainLayer;

import java.time.LocalDateTime;

public class PastOrder extends Order {
    private double totalPrice;
    private LocalDateTime finishDate;

    public PastOrder(Order order, double totalPrice) {
        super(order);
        this.totalPrice=totalPrice;
        this.finishDate=LocalDateTime.now();
    }

}

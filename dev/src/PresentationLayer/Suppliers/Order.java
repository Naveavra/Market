package PresentationLayer.Suppliers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private int orderId;
    private String date;
    private DeliveryTerm daysToDeliver;
    private int supplierNumber;


    public Order(int orderId,DeliveryTerm daysToDeliver,int supplierNumber){
        this.orderId = orderId;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
        this.daysToDeliver=daysToDeliver;
        this.supplierNumber=supplierNumber;
    }
    public Order(Order order){
        this.date=order.date;
        this.orderId=order.orderId;
        this.supplierNumber=order.supplierNumber;
        this.daysToDeliver= order.daysToDeliver;
//
    }

    public String toString(){
        String ans = "Order number: "+ orderId;
        ans += ", date: " + date ;
        return ans;
    }
    public int getOrderId(){
        return orderId;
    }


}

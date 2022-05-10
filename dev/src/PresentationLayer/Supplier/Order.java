package PresentationLayer.Supplier;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Order {
    private int orderId;
    private String date;
    //  private Map<Product,Integer> products;//product and count

    public Order(int orderId){
        this.orderId = orderId;
        //products = new HashMap<>();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
    }
    public Order(Order order){
        this.date=order.date;
        this.orderId=order.orderId;
//        this.products = new HashMap<>();
//        for(Product p:order.products.keySet()){
//            this.products.put(new Product(p),order.products.get(p));
//        }
    }

    public String toString(){
        String ans = "Order number: "+ orderId;
        ans += ", date: " + date;
        return ans;
    }
    public int getOrderId(){
        return orderId;
    }


}

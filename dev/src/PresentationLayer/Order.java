package PresentationLayer;

import DomainLayer.Product;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderId;
    private LocalDateTime date;
    private Map<Product,Integer> products;//product and count

    public Order(int orderId){
        this.orderId = orderId;
        products = new HashMap<>();
        date = LocalDateTime.now();
    }
    public Order(Order order){
        this.date=order.date;
        this.orderId=order.orderId;
        this.products = new HashMap<>();
        for(Product p:order.products.keySet()){
            this.products.put(new Product(p),order.products.get(p));
        }
    }

    public String toString(){
        String ans = "Order number: "+ orderId;
        ans += ", date" + date;
        return ans;
    }

}

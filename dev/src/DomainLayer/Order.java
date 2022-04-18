package DomainLayer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Order {
    private int orderId;
    //private LocalDateTime date;
    private Map<Product,Integer> products;//product and count

    public Order(int orderId){
        this.orderId = orderId;
        products = new HashMap<>();
//        date = LocalDateTime.now();
    }
    public Order(Order order){
//        this.date=order.date;
        this.orderId=order.orderId;
        this.products = new HashMap<>();
        for(Product p:order.products.keySet()){
            this.products.put(new Product(p),order.products.get(p));
        }
    }

    public boolean addProductToOrder(Product p, int count){
        products.put(p, products.getOrDefault(p,0)+count);
        return true;

    }



    public double getTotalIncludeDiscounts() {
        double total = 0;
        for (Product p: products.keySet()){
            total += p.getPriceAfterDiscount(products.get(p));
        }
        return total;
    }


    public int getCountProducts() {
        int sum=0;
        for (Product p: products.keySet()){
           sum +=products.get(p);
        }
        return sum;
    }
}

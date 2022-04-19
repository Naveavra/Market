package DomainLayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Order {
    private int orderId;
    private String date;
    private Map<Product,Integer> products;//product and count

    public Order(int orderId){
        this.orderId = orderId;
        products = new HashMap<>();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
    }
    public Order(Order order){
        this.date=order.date;
        this.orderId=order.orderId;
        this.products = new HashMap<>();
        for(Product p:order.products.keySet()){
            this.products.put(new Product(p),order.products.get(p));
        }
    }

    public boolean updateProductToOrder(Product p, int count){
        if(count<=0){
            return false;
        }
        if(p==null){
            return false;
        }
        products.put(p, products.getOrDefault(p,0)+count);
        return true;

    }
public int getOrderId(){
        return orderId;
}
public String getDate(){
        return date;
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

    public boolean removeProductFromOrder(Product p) {
        if(products.containsKey(p)) {
            products.remove(p);
            return true;
        }
        return false;
    }

    public Map<Product,Integer> getProducts() {
        return products;
    }
}

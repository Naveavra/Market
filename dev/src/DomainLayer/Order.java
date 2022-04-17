package DomainLayer;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class Order {
    private int orderId;
    private LocalDateTime date;
    private Map<Product,Integer> products;//product and count
    private double totalToPay;

    public Order(int orderId){
        this.orderId = orderId;
        products = new TreeMap<>();
        totalToPay = 0;
        date = LocalDateTime.now();
    }

    public boolean addProductToOrder(Product p, int count){
        totalToPay+= p.getPrice()*count;
        products.put(p, products.getOrDefault(p,0)+count);
        return true;

    }

    public Order finishOrder(){
        updateTotalIncludeDiscounts();
    }

    private double updateTotalIncludeDiscounts() {
        double total = 0;
        for (Product p: products.keySet()){
            total += p.getPriceAfterDiscount(products.get(p));
        }
        totalToPay = total;
        return totalToPay;
    }


}

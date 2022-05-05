package DomainLayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderFromSupplier {
    private int orderId;
    private String date;
    private Map<ProductSupplier,Integer> products;//product and count
    private DeliveryTerm daysToDeliver;

    public OrderFromSupplier(int orderId){
        this.orderId = orderId;
        products = new HashMap<>();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
        DeliveryTerm.DaysInWeek[] daysInWeek = {};
        daysToDeliver=new DeliveryTerm(daysInWeek);
    }
    public OrderFromSupplier(OrderFromSupplier order){
        this.date=order.date;
        this.orderId=order.orderId;
        this.products = new HashMap<>();
        for(ProductSupplier p:order.products.keySet()){
            this.products.put(new ProductSupplier(p),order.products.get(p));
        }
        this.daysToDeliver=order.daysToDeliver;
    }

    public boolean updateProductToOrder(ProductSupplier p, int count){
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
        for (ProductSupplier p: products.keySet()){
            total += p.getPriceAfterDiscount(products.get(p));
        }
        return total;
    }


    public int getCountProducts() {
        int sum=0;
        for (ProductSupplier p: products.keySet()){
           sum +=products.get(p);
        }
        return sum;
    }

    public boolean removeProductFromOrder(ProductSupplier p) {
        if(products.containsKey(p)) {
            products.remove(p);
            return true;
        }
        return false;
    }

    public Map<ProductSupplier,Integer> getProducts() {
        return products;
    }
    public DeliveryTerm getDaysToDeliver(){
        return daysToDeliver;
    }
    public boolean addDeliveryDays(String[] daysInWeeks){
       this.daysToDeliver.updateFixedDeliveryDays(daysInWeeks);
       return true;
    }

    public boolean updateDeliveryDays(String[] daysInWeek) {
        this.daysToDeliver.updateFixedDeliveryDays(daysInWeek);
        return true;
    }
}

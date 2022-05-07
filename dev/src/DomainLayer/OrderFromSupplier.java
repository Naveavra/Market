package DomainLayer;

import DAL.OrdersFromSupplierDAO;
import DAL.ProductSupplierDAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFromSupplier {
    private int orderId;
    private String date;
    private DeliveryTerm daysToDeliver;
    //DAO
    private ProductSupplierDAO productsDAO;
    private OrdersFromSupplierDAO ordersDAO;
    //private Map<ProductSupplier,Integer> products;//product and count


    public OrderFromSupplier(int orderId){
        this.orderId = orderId;
        //products = new HashMap<>();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
        DeliveryTerm.DaysInWeek[] daysInWeek = {};
        daysToDeliver=new DeliveryTerm(daysInWeek);
        productsDAO = new ProductSupplierDAO();
    }


    public OrderFromSupplier(int orderId, String date, DeliveryTerm deliveryTerm){
        this.orderId = orderId;
        //products = new HashMap<>();
        this.date = date;
        daysToDeliver = deliveryTerm;
    }

    public OrderFromSupplier(OrderFromSupplier order){
        this.date=order.date;
        this.orderId=order.orderId;
//        this.products = new HashMap<>();
//        for(ProductSupplier p:order.products.keySet()){
//            this.products.put(new ProductSupplier(p),order.products.get(p));
//        }
        this.daysToDeliver=order.daysToDeliver;
    }

    public boolean updateProductToOrder(ProductSupplier p, int count){
        if(count<=0){
            return false;
        }
        if(p==null){
            return false;
        }
        try {
            ordersDAO.updateCountProductToOrder(p.getProductId(),orderId, count);
        } catch (SQLException e) {
            return false;
        }
        //products.put(p, products.getOrDefault(p,0)+count);
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
        try {
            Map<ProductSupplier,Integer> productsInOrder = ordersDAO.getAllProductsOfOrder(orderId);
            for (ProductSupplier p: productsInOrder.keySet()){
                total += p.getPriceAfterDiscount(productsInOrder.get(p));
            }
            return total;
        } catch (Exception e) {
            return -1;
        }

    }


    public int getCountProducts() {
        int sum=0;
        try {
            Map<ProductSupplier,Integer> productsInOrder = ordersDAO.getAllProductsOfOrder(orderId);
            for (ProductSupplier p: productsInOrder.keySet()){
                sum +=productsInOrder.get(p);
            }
            return sum;
        } catch (Exception e) {
            return -1;
        }

    }

    public boolean removeProductFromOrder(ProductSupplier p) {
        try {
            ordersDAO.removeProductFromOrder(p.getProductId(),orderId);
            return true;
        } catch (Exception e) {
            return false;
        }
//        if(products.containsKey(p)) {
//            products.remove(p);
//            return true;
//        }
//        return false;
    }

    public Map<ProductSupplier,Integer> getProducts() {
        try {
            return ordersDAO.getAllProductsOfOrder(orderId);
        } catch (SQLException e) {
            return null;
        }
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

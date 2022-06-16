package DomainLayer.Suppliers;

import DAL.OrdersFromSupplierDAO;
import DAL.ProductsSupplierDAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class OrderFromSupplier {
    private int orderId;
    private String date;
    private DeliveryTerm daysToDeliver;
    private int supplierNumber;
    //DAO
    private transient ProductsSupplierDAO productsDAO;
    private transient OrdersFromSupplierDAO ordersDAO;

      public OrderFromSupplier(int supplierNumber){
       this.supplierNumber=supplierNumber;
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
        DeliveryTerm.DaysInWeek[] daysInWeek = {};
        daysToDeliver=new DeliveryTerm("");
        productsDAO = new ProductsSupplierDAO();
        ordersDAO=new OrdersFromSupplierDAO();
    }


    public OrderFromSupplier(int supplierNumber,int orderId, String date, DeliveryTerm deliveryTerm){
        this.supplierNumber=supplierNumber;
          this.orderId = orderId;
        this.date = date;
        if(deliveryTerm==null){
            this.daysToDeliver=null;
        }
        else { this.daysToDeliver = new DeliveryTerm(deliveryTerm.getDaysInWeeks());
        }
        ordersDAO=new OrdersFromSupplierDAO();

    }

    public OrderFromSupplier(OrderFromSupplier order){
        this.date=order.date;
        this.orderId=order.orderId;
        this.daysToDeliver=new DeliveryTerm(order.daysToDeliver.getDaysInWeeks());
        ordersDAO=new OrdersFromSupplierDAO();
        this.supplierNumber=order.getSupplierNumber();
    }

    public OrderFromSupplier() {
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        date = simpleDateFormat.format(new Date());
        DeliveryTerm.DaysInWeek[] daysInWeek = {};
        daysToDeliver=new DeliveryTerm(daysInWeek);
        productsDAO = new ProductsSupplierDAO();
        ordersDAO =new OrdersFromSupplierDAO();
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
    public boolean addProductToOrder(ProductSupplier p,int count){
        if(count<=0){
            return false;
        }
        if(p==null){
            return false;
        }
        try {
            Map<ProductSupplier,Integer> products=ordersDAO.getAllProductsOfOrder(orderId);
            if(products.containsKey(p)){
                count=count+products.get(p);
                ordersDAO.updateCountProductToOrder(p.getProductId(), orderId, count);
            }
            else {
                ordersDAO.addProductToOrder(p, orderId, count);
            }
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
    public boolean addDeliveryDays(String[] daysInWeek) {
        this.daysToDeliver.updateFixedDeliveryDays(daysInWeek);
        try {
            ordersDAO.addDeliveryTermsToOrder(orderId,daysInWeek);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean updateDeliveryDays(String[] daysInWeek) {
        this.daysToDeliver.updateFixedDeliveryDays(daysInWeek);
        try {
            ordersDAO.updateDeliveryTermsInOrder(orderId,daysInWeek);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}

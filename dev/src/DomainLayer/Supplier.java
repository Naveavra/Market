package DomainLayer;


import java.security.PrivateKey;
import java.util.*;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private Map<String,String> contacts =new HashMap<>();//<name,email>
    private Map<Integer,Product> products =new HashMap<>();
    private Map<Integer,Double> discountByAmount;//sum of products in order
    private Map<Integer,Order> orders;
    private Map<Integer,Order> pastOrders;
    public Supplier(int supplierNumber, String name,int bankAccount,Map<String,String> contacts){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        contacts.replaceAll((n,v) -> v);
        discountByAmount=new TreeMap<>();
        orders = new TreeMap<>();
        pastOrders = new HashMap<>();
    }
    public boolean updateAccount(String supplierName,int bankAccount,Map<String,String>contacts){
        this.name=supplierName;
        this.bankAccount=bankAccount;
        contacts.replaceAll((n,v) -> v);
        return true;
    }
    public boolean addContact(String name,String email){
        if(contacts.containsKey(name)){
            return false;
        }
        contacts.put(name, email);
        return true;
    }
    public boolean addDiscount( int count,double discount){
        if(this.discountByAmount.containsKey(count)){
            return false;
        }
        this.discountByAmount.put(count, discount);
        return true;
    }

    public boolean removeDiscountOnAmount(int count){
        if(!this.discountByAmount.containsKey(count)){
            return false;
        }
        this.discountByAmount.remove(count);
        return true;
    }
    public boolean addProduct(int catalogNumber,String name, double price){
        Product product =new Product(catalogNumber, name, price);
        products.put(catalogNumber, product);
        return true;
    }
    public boolean removeProduct(int catalogNumber){
        if(!products.containsKey(catalogNumber)){
            return false;
        }
        products.remove(catalogNumber);
        return true;
    }
//    public Order createOrder(){
//        return true;
//    }
    public Product getProduct(int catalogNumber){
        return products.get(catalogNumber);
    }
    public boolean addProductToOrder(int orderId,int catalogNumber){
        return true;
    }
    public double updateTotalIncludeDiscounts(int orderId){
        Order order = orders.get(orderId);
        int count = order.getCountProducts();
        double price = order.getTotalIncludeDiscounts();
        return price*findMaxUnder(count);
    }
    private double findMaxUnder(int count){
        int out=0;
        for(int s:discountByAmount.keySet()){
            if(s<=count){
                out=s;
            }
            else{
                return discountByAmount.get(out);
            }
        }
        return discountByAmount.get(out);
    }
    public PastOrder finishOrder(int orderId){
        double totalPrice =updateTotalIncludeDiscounts(orderId);
        return new PastOrder(orders.get(orderId),totalPrice);
    }

    public void createOrder() {
    }
}

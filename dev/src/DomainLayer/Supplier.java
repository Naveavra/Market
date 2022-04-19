package DomainLayer;


import java.security.PrivateKey;
import java.util.*;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private Map<String,String> contacts;//<name,email>
    private Map<Integer,Product> products =new HashMap<>();
    private Map<Integer,Double> discountByAmount;//sum of products in order
    private Map<Integer,Order> orders;
    private boolean active;
    private List<PastOrder> finalOrders;
    private static int orderNum=0;

    public Supplier(int supplierNumber, String name,int bankAccount,Map<String,String> contacts){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new HashMap<>();
        for(String n: contacts.keySet()){
            this.contacts.put(n,contacts.get(n));
        }
        discountByAmount=new TreeMap<>();
        orders = new HashMap<>();
        active =true;
        finalOrders=new ArrayList<>();
    }
    public boolean updateAccount(String supplierName,int bankAccount,Map<String,String>contacts){
        this.name=supplierName;
        this.bankAccount=bankAccount;
        this.contacts=new HashMap<>();
        for(String n: contacts.keySet()){
            this.contacts.put(n,contacts.get(n));
        }
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
        if(products.containsKey(catalogNumber)){
            return false;
        }
        if(price<=0){
            return false;
        }
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
    public Order createOrder(){
        Order order = new Order(orderNum);
        orders.put(orderNum,order);
        orderNum++;
        return order;
    }
    public Product getProduct(int catalogNumber){
        return products.get(catalogNumber);
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
    public boolean finishOrder(int orderId){
        if(orders.get(orderId)==null){
            return false;
        }
        double totalPrice =updateTotalIncludeDiscounts(orderId);
        this.addToFinish(new PastOrder(orders.get(orderId),totalPrice));
        return true;
    }


    public String getName() {
        return name;
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public Map<Integer,Order> getActiveOrders() {
        return orders;
    }
    public boolean isProductExist(int catalogNumber){
        return products.containsKey(catalogNumber);
    }

    public void closeAccount() {
        active=false;
    }
    public boolean isActive(){
        return active;
    }


    public Map<Integer,Product> getProducts() {
        return products;
    }

    public String getProductsNames() {
        StringBuilder list = new StringBuilder();
        for(Product p: products.values()){
            list.append(" ").append(p.getName());
        }
        return list.toString();
    }

    public void addToFinish(PastOrder pastOrder) {
        this.finalOrders.add(pastOrder);
    }
    public int getBankAccount(){
        return bankAccount;
    }
    public Map<String,String> getContacts(){
        return contacts;
    }
}

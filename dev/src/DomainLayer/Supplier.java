package DomainLayer;


import java.util.*;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private Map<String,String> contacts;//<name,email>
    private Map<Integer,Product> products =new HashMap<>();
    private Map<Integer,Double> discountByAmount;//sum of products in order
    private Map<Integer, OrderFromSupplier> orders;
    private boolean active;
    private List<PastOrder> finalOrders;
    private static int orderNum=0;
    private boolean isDeliver;

    public Supplier(int supplierNumber, String name,int bankAccount,Map<String,String> contacts,boolean isDeliver){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new HashMap<>();
        for(String n: contacts.keySet()){
            this.contacts.put(n,contacts.get(n));
        }
        discountByAmount=new TreeMap<>();
        discountByAmount.put(0,1.0);
        orders = new HashMap<>();
        active =true;
        finalOrders=new ArrayList<>();
        this.isDeliver=isDeliver;
    }
    public boolean updateAccount(String supplierName,int bankAccount,Map<String,String>contacts){
        this.name=supplierName;
        this.bankAccount=bankAccount;
       // this.isDeliver=isDeliver;
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
        if(count<0|discount<0|discount>1){
            return false;
        }
        if(this.discountByAmount.containsKey(count)){
            return false;
        }
        this.discountByAmount.put(count, discount);
        return true;
    }

    public boolean removeDiscountOnAmount(int count){
        if(count<=0){
            return false;
        }
        if(!this.discountByAmount.containsKey(count)){
            return false;
        }
        this.discountByAmount.remove(count);
        return true;
    }
    public boolean addProduct(int catalogNumber,String name, double price){
        if(catalogNumber<0){
            return false;
        }
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
    public OrderFromSupplier createOrder(){
        OrderFromSupplier order = new OrderFromSupplier(orderNum);
        orders.put(orderNum,order);
        orderNum++;
        return order;
    }
    public Product getProduct(int catalogNumber){
        return products.get(catalogNumber);
    }
    public double updateTotalIncludeDiscounts(int orderId){
        OrderFromSupplier order = orders.get(orderId);
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
        if(orderId<0){
            return false;
        }
        if(orders.get(orderId)==null){
            return false;
        }
        //do something with isDeliver
        double totalPrice = updateTotalIncludeDiscounts(orderId);
        finalOrders.add(new PastOrder(orders.get(orderId),totalPrice));
        orders.remove(orderId);
        return true;
    }


    public String getName() {
        return name;
    }

    public OrderFromSupplier getOrder(int orderId) {
        return orders.get(orderId);
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders() {
        return orders;
    }
    public boolean isProductExist(int catalogNumber){
        if(catalogNumber<0){
            return false;
        }
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


    public int getBankAccount(){
        return bankAccount;
    }
    public Map<String,String> getContacts(){
        return contacts;
    }

    public List<PastOrder> getFinalOrders() {
        return finalOrders;
    }

    public boolean updateDeliveration(boolean deliver) {
        if(isDeliver==deliver){
            return false;
        }
        isDeliver=deliver;
        return true;
    }
}

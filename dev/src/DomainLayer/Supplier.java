package DomainLayer;


import DAL.OrdersFromSupplierDAO;
import DAL.PastOrdersSupplierDAO;
import DAL.ProductSupplierDAO;
import DAL.SuppliersDAO;

import java.sql.SQLException;
import java.util.*;

public class Supplier {
    private static int orderNum=0;

    private int supplierNumber;
    private String name;
    private int bankAccount;
    private boolean isDeliver;
    private boolean active;
    private Map<String,String> contacts;//<name,email>
    private Map<Integer,Double> discountByAmount;//sum of products in order

    //DAO to db
    private ProductSupplierDAO productsDAO;
    private OrdersFromSupplierDAO ordersDAO;
    private PastOrdersSupplierDAO pastOrdersDAO;
//
//    private Map<Integer, ProductSupplier> products;//remove
//    private Map<Integer, OrderFromSupplier> orders;//remove
//    private List<PastOrderSupplier> finalOrders;//remove


    public Supplier(int supplierNumber, String name,int bankAccount,Map<String,String> contacts,boolean isDeliver,boolean active){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new HashMap<>();
        for(String n: contacts.keySet()){
            this.contacts.put(n,contacts.get(n));
        }
        discountByAmount=new TreeMap<>();
        discountByAmount.put(0,1.0);
        this.active =active;
        this.isDeliver=isDeliver;
        productsDAO = new ProductSupplierDAO();
        ordersDAO = new OrdersFromSupplierDAO();
//        finalOrders=new ArrayList<>();
//        orders = new HashMap<>();
//        products =new HashMap<>();
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
    public boolean addProduct(int catalogNumber, double price, int productId, int supplierNumber){
        if(catalogNumber<0){
            return false;
        }
        if(price<=0){
            return false;
        }
        try {
            if (isProductExist(productId)) {
                return false;
            }

            ProductSupplier productSupplier = new ProductSupplier(catalogNumber, price, productId);
            productsDAO.insert(productSupplier);
            //products.put(catalogNumber, productSupplier);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean removeProduct(int catalogNumber){
//        if(!products.containsKey(catalogNumber)){
//            return false;
//        }
        try {
            productsDAO.removeProduct(catalogNumber, supplierNumber);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    public OrderFromSupplier createOrder(){
        OrderFromSupplier order = new OrderFromSupplier(orderNum);
        try {
            ordersDAO.createOrderFromSupplier(order,supplierNumber);
        } catch (SQLException e) {
            return null;
        }
        orderNum++;
        return order;
    }
    public ProductSupplier getProduct(int catalogNumber){
        try{
            return productsDAO.getProductByCatalogNumber(supplierNumber,catalogNumber);
        }
        catch (Exception e){
            return null;
        }
    }

    public double updateTotalIncludeDiscounts(int orderId) throws SQLException {
        OrderFromSupplier order = getOrder(orderId);
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
        OrderFromSupplier o = getOrder(orderId);
        if(o == null){
            return false;
        }
        //do something with isDeliver
        double totalPrice = 0;
        try {
            totalPrice = updateTotalIncludeDiscounts(orderId);
            pastOrdersDAO.insertPastOrder(new PastOrderSupplier(o,totalPrice));
            ordersDAO.removeOrder(orderId);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }


    public String getName() {
        return name;
    }

    public OrderFromSupplier getOrder(int orderId) {
        try {
            return ordersDAO.getOrderWithAllTheProducts(orderId);
        } catch (SQLException e) {
            return null;
        }
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders() {
        try {
            return ordersDAO.getActiveOrders(supplierNumber);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean isProductExist(int productId){
        try{
            return (productsDAO.getProduct(supplierNumber, productId) != null);
        }
        catch (Exception e){
            return false;
        }
    }

    public void closeAccount() {
        active=false;
    }
    public boolean isActive(){
        return active;
    }


    public Map<Integer, ProductSupplier> getProducts() {
        try{
            return productsDAO.getAllProductsOfSupplier(supplierNumber);
        }
        catch (Exception e){
            return null;
        }
    }

//    public String getProductsNames() {
//        StringBuilder list = new StringBuilder();
//        for(Product p: products.values()){
//            list.append(" ").append(p.getName());
//        }
//        return list.toString();
//    }


    public int getBankAccount(){
        return bankAccount;
    }
    public Map<String,String> getContacts(){
        return contacts;
    }

    public List<PastOrderSupplier> getFinalOrders() {
        return pastOrdersDAO.getAllPastOrders();
    }

    public boolean updateDeliveration(boolean deliver) {
        if(isDeliver==deliver){
            return false;
        }
        isDeliver=deliver;
        return true;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public boolean getActive() {
        return active;
    }

    public boolean getIsDeliver() {
        return isDeliver;
    }
    public Map<Integer,Double> getDiscounts(){
        return discountByAmount;
    }

    public void addDiscounts(Map<Integer, Double> newDiscountsSupplier) {
        discountByAmount.putAll(newDiscountsSupplier);
    }
}

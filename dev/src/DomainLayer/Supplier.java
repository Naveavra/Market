package DomainLayer;


import DAL.ProductsSupplierDAO;
import DAL.SuppliersDAO;
import DomainLayer.Storage.Contact;
import PresentationLayer.Supplier.Product;

import java.sql.SQLException;
import java.util.*;

public class Supplier {
    private static int orderNum=0;

    private int supplierNumber;
    private String name;
    private int bankAccount;
    private boolean isDeliver;
    private boolean active;
    private LinkedList<Contact> contacts;
    //private Map<String,String> contacts;//<name,email>
    private Map<Integer,Double> discountByAmount;//sum of products in order
    //DAO to db
    private ProductsSupplierDAO productsDAO;
    private SuppliersDAO suppliersDAO;
//    private Map<Integer, ProductSupplier> products;//remove
//    private Map<Integer, OrderFromSupplier> orders;//remove
//    private List<PastOrderSupplier> finalOrders;//remove


    public Supplier(int supplierNumber, String name,int bankAccount,LinkedList<Contact> contacts,boolean isDeliver,boolean active){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new LinkedList<>();
        this.contacts.addAll(contacts);
        discountByAmount=new TreeMap<>();
        discountByAmount.put(0,1.0);
        this.active =active;
        this.isDeliver=isDeliver;
        productsDAO = new ProductsSupplierDAO();
        suppliersDAO = new SuppliersDAO();
        //ordersDAO = new OrdersFromSupplierDAO();
//        finalOrders=new ArrayList<>();
//        orders = new HashMap<>();
//        products =new HashMap<>();
    }

    public boolean updateAccount(String supplierName,int bankAccount){
        this.name=supplierName;
        this.bankAccount=bankAccount;
        // this.isDeliver=isDeliver;
        this.contacts=new LinkedList<>();
        try {
            suppliersDAO.updateSupplier(this);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean addContact(String name,String email,String telephone){
        if(contains(contacts,name)){
            return false;
        }
        contacts.add(new Contact(name, email,telephone));
        try {
            suppliersDAO.addContact(this,name,email,telephone);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean contains(LinkedList<Contact> contacts, String name) {
        for(Contact c:contacts){
            if(c.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean addDiscount( int count,double discount){
        if(count<0|discount<0|discount>1){
            return false;
        }
        if(this.discountByAmount.containsKey(count)){
            return false;
        }
        this.discountByAmount.put(count, discount);
        try {
            suppliersDAO.insertDiscountOnAmount(this,count,discount);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeDiscountOnAmount(int count){
        if(count<=0){
            return false;
        }
        if(!this.discountByAmount.containsKey(count)){
            return false;
        }
        this.discountByAmount.remove(count);
        try {
            suppliersDAO.removeDiscountOnAmount(this,count);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
    public boolean addProduct(int catalogNumber, double price, int productId){
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
            if(isProductExist(catalogNumber, supplierNumber)){
                return false;
            }
            ProductSupplier productSupplier = new ProductSupplier(supplierNumber,catalogNumber, price, productId,new HashMap<>());
            productsDAO.insert(productSupplier);
            //products.put(catalogNumber, productSupplier);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean removeProduct(int catalogNumber){
        if (!isProductExist(catalogNumber,supplierNumber)) {
            return false;
        }
        try {
            productsDAO.removeProduct(catalogNumber, supplierNumber);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean isProductExist(int catalogNumber, int supplierNumber) {
        try{
            return (productsDAO.getProductByCatalogNumber(supplierNumber, catalogNumber) != null);
        }
        catch (Exception e){
            return false;
        }
    }

    public ProductSupplier getProduct(int catalogNumber){
        try{
            return productsDAO.getProductByCatalogNumber(supplierNumber,catalogNumber);
        }
        catch (Exception e){
            return null;
        }
    }

    public String getName() {
        return name;
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

    public int getBankAccount(){
        return bankAccount;
    }
    public LinkedList<Contact> getContacts(){
        return contacts;
    }

    public boolean updateDeliveration(boolean deliver) {
        if(isDeliver==deliver){
            return false;
        }
        isDeliver=deliver;
        return true;
    }


    public void addDiscounts(Map<Integer, Double> newDiscountsSupplier) {//by DAO
        discountByAmount.putAll(newDiscountsSupplier);


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

    public void updateProductPrice(int catalogNumber, int price) {
        getProduct(catalogNumber).setPrice(price);
        try {
            productsDAO.updateProduct(getProduct(catalogNumber),price);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateContact(String name, String email, String telephone) {
       for(Contact c:contacts){
           if(c.getName().equals(name)){
               c.setEmail(email);
               c.setTel(telephone);
           }
       }
        try {
            suppliersDAO.updateSupplierContacts(this);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return false;
    }


//****to delete
//    public double updateTotalIncludeDiscounts(int orderId) throws SQLException {
//        OrderFromSupplier order = getOrder(orderId);
//        int count = order.getCountProducts();
//        double price = order.getTotalIncludeDiscounts();
//        return price*findMaxUnder(count);
//    }
//    private double findMaxUnder(int count){
//        int out=0;
//
//        for(int s:discountByAmount.keySet()){
//            if(s<=count){
//                out=s;
//            }
//            else{
//                return discountByAmount.get(out);
//            }
//        }
//        return discountByAmount.get(out);
//    }
//    public boolean finishOrder(int orderId){
//        if(orderId<0){
//            return false;
//        }
//        OrderFromSupplier o = getOrder(orderId);
//        if(o == null){
//            return false;
//        }
//        //do something with isDeliver
//        double totalPrice = 0;
//        try {
//            totalPrice = updateTotalIncludeDiscounts(orderId);
//            pastOrdersDAO.insertPastOrder(new PastOrderSupplier(o,totalPrice));
//            ordersDAO.removeOrder(orderId);
//            return true;
//        } catch (SQLException e) {
//            return false;
//        }
//
//    }
//    public OrderFromSupplier getOrder(int orderId) {
//        try {
//            return ordersDAO.getOrderWithAllTheProducts(orderId);
//        } catch (SQLException e) {
//            return null;
//        }
//    }
//
//    public Map<Integer, OrderFromSupplier> getActiveOrders() {
//        try {
//            return ordersDAO.getActiveOrders(supplierNumber);
//        } catch (SQLException e) {
//            return null;
//        }
//    }

//    public String getProductsNames() {
//        StringBuilder list = new StringBuilder();
//        for(Product p: products.values()){
//            list.append(" ").append(p.getName());
//        }
//        return list.toString();
//    }

}

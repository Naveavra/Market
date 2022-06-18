package DomainLayer.Suppliers;


import DAL.ProductsSupplierDAO;
import DAL.SuppliersDAO;

import java.sql.SQLException;
import java.util.*;

public class Supplier {
    private static int orderNum=0;

    private int supplierNumber;
    private String name;
    private int bankAccount;
    private boolean active;
    private int area;
    private String[] deliveryDays;
    private LinkedList<Contact> contacts;
    private LinkedList<Discount> discountByAmount;//sum of products in order
    //DAO to db
    private transient ProductsSupplierDAO productsDAO;
    private transient SuppliersDAO suppliersDAO;


    public Supplier(int supplierNumber, String name,int bankAccount,LinkedList<Contact> contacts, String[] days,int area,boolean active){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new LinkedList<>();
        this.contacts.addAll(contacts);
        discountByAmount=new LinkedList<Discount>();
        discountByAmount.add(new Discount(0,1.0));
        this.active =active;
        this.area = area;
        this.deliveryDays=sort(days);
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
        if(contains(discountByAmount,count)){
            return false;
        }
        this.discountByAmount.add(new Discount(count,discount));
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
        if(!contains(discountByAmount, count)){
            return false;
        }
        remove(discountByAmount,count);
        try {
            suppliersDAO.removeDiscountOnAmount(this,count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void remove(LinkedList<Discount> discountByAmount, int count) {
        discountByAmount.removeIf(d -> d.getAmount() == count);
    }

    public boolean addProduct(int catalogNumber, int daysUntilExpiration, double price, int productId){
        if(catalogNumber<0){
            return false;
        }
        if(price<=0){
            return false;
        }
        if(productId<=0)
            return false;
        try {
            if (isProductExist(productId)) {
                return false;
            }
            if(isProductExist(catalogNumber, supplierNumber)){
                return false;
            }
            ProductSupplier productSupplier = new ProductSupplier(supplierNumber,catalogNumber, daysUntilExpiration, price, productId,new LinkedList<>());
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



    public void addDiscounts(LinkedList<Discount> discountsToAdd) {//by DAO
        this.discountByAmount.addAll(discountsToAdd);


    }
    public int getSupplierNumber() {
        return supplierNumber;
    }

    public boolean getActive() {
        return active;
    }

    public LinkedList<Discount> getDiscounts(){
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
        boolean exist=false;
       for(Contact c:contacts){
           if(c.getName().equals(name)){
               c.setEmail(email);
               c.setTel(telephone);
               exist=true;
           }
       }
       if(!exist){
           contacts.add(new Contact(name,email,telephone));
       }
        try {
            suppliersDAO.updateSupplierContacts(this);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return false;
    }
    public boolean contains(LinkedList<Discount> discounts,int count){
        for(Discount d:discounts){
           if(d.getAmount()==count){
               return true;
           }
        }
        return false;
    }
    public String[] getDeliveryDays() {
    return deliveryDays;
    }
    public int getArea(){
        return area;
    }
    public int getDeliverDaysInt(){
        String out ="";
        for(String s:deliveryDays){
            out+=s;
        }
        return Integer.parseInt(out);
    }

    public boolean updateDeliveration(String[] days) {
        this.deliveryDays=days;
        try {
            suppliersDAO.updateSupplier(this);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
    private String[] sort(String[] deliveryDays) {
        int[] arr = new int[deliveryDays.length];
        int i=0;
        for(String s:deliveryDays){
            arr[i]=Integer.parseInt(s);
            i++;
        }
        i=0;
        quicksort(arr,0,arr.length-1);
        String[] out =new String[arr.length];
        for(Integer s:arr){
            out[i]=String.valueOf(s);
            i++;
        }
        return out;
    }
    static void quicksort(int[] arr, int low, int high){
        if(low < high){
            int p = partition(arr, low, high);
            quicksort(arr, low, p-1);
            quicksort(arr, p+1, high);
        }
    }
    static int partition(int[] arr, int low, int high){
        int p = low, j;
        for(j=low+1; j <= high; j++)
            if(arr[j] < arr[low])
                swap(arr, ++p, j);

        swap(arr, low, p);
        return p;
    }
    static void swap(int[] arr, int low, int pivot){
        int tmp = arr[low];
        arr[low] = arr[pivot];
        arr[pivot] = tmp;
    }
}

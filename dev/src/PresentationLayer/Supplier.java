package PresentationLayer;


import DomainLayer.Order;
import DomainLayer.PastOrder;
import DomainLayer.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private Map<String,String> contacts =new HashMap<>();//<name,email>
//    private Map<Integer, Product> products =new HashMap<>();
//    private Map<Integer,Double> discountByAmount;//sum of products in order
//    private Map<Integer, Order> orders;
//    private Map<Integer,Order> pastOrders;

    public Supplier(int supplierNumber, String name, int bankAccount, Map<String,String> contacts){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        contacts.replaceAll((n,v) -> v);
//        discountByAmount=new TreeMap<>();
//        orders = new TreeMap<>();
//        pastOrders = new HashMap<>();
    }

    public String getSupplierName() {
        return name;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public String toString(){
        String ans = "Supplier name:" + name;
        ans+= "\nSupplier number: " + supplierNumber;
        ans+= "\nBank Account: "+ bankAccount;
        ans+= "\nContacts:";
        for( Map.Entry<String,String> e: contacts.entrySet()){
            ans+= "\n\t* "+ e.getKey() + ": " + e.getValue();
        }
        return ans;
    }

    public int getBankNumber() {
        return bankAccount;
    }



    public Map<String, String> getContacts() {
        return contacts;
    }
}

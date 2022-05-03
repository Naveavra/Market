package main.java.PresentationLayer.Supplier;



import java.util.*;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private Map<String,String> contacts =new HashMap<>();//<name,email>
    private List<String> namesOfContacts;
    private boolean active;
//    private Map<Integer, Product> products =new HashMap<>();
//    private Map<Integer,Double> discountByAmount;//sum of products in order
//    private Map<Integer, Order> orders;
//    private Map<Integer,Order> pastOrders;

    public Supplier(int supplierNumber, String name, int bankAccount, Map<String,String> contacts){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        contacts.replaceAll((n,v) -> v);
        namesOfContacts=new ArrayList<>();
        namesOfContacts.addAll(this.contacts.keySet());
        active =true;
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
        int i=1;
        for( Map.Entry<String,String> e: contacts.entrySet()){
            ans+= "\n\t "+i+". "+ e.getKey() + ": " + e.getValue();
            i++;
        }
        return ans;
    }

    public int getBankNumber() {
        return bankAccount;
    }



    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setSupplierName(String newSupplierName) {
        this.name=newSupplierName;
    }

    public void setBankAccount(int bankNumber) {
        this.bankAccount=bankNumber;
    }
    public int numOfContacts(){
        return namesOfContacts.size();
    }
    public boolean isActive(){
        return active;
    }

}

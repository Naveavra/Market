package PresentationLayer.Supplier;

import java.util.*;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private LinkedList<Contact> contacts;//<name,email>
    private boolean active;
//    private Map<Integer, Product> products =new HashMap<>();
//    private Map<Integer,Double> discountByAmount;//sum of products in order
//    private Map<Integer, Order> orders;
//    private Map<Integer,Order> pastOrders;

    public Supplier(int supplierNumber, String name, int bankAccount,LinkedList<Contact> contacts){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new LinkedList<>();
        this.contacts.addAll(contacts);
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
        for( Contact c: contacts){
            ans+= "\n\t "+i+". "+ c.getName() + ": " + c.getEmail() +" , "+ c.getTelephone();
            i++;
        }
        return ans;
    }

    public int getBankNumber() {
        return bankAccount;
    }



    public LinkedList<Contact> getContacts() {
        return contacts;
    }

    public void setSupplierName(String newSupplierName) {
        this.name=newSupplierName;
    }

    public void setBankAccount(int bankNumber) {
        this.bankAccount=bankNumber;
    }
    public int numOfContacts(){
        return contacts.size();
    }
    public boolean isActive(){
        return active;
    }

    public void setContact(Contact c) {
        boolean exist=false;
        for(Contact contact:contacts){
                 if(contact.getName().equals(c.getName())){
                     contact.setEmail(c.getEmail());
                     contact.setTel(c.getEmail());
                     exist=true;
                 }
        }
        if(!exist){
            contacts.add(new Contact(c.getName(),c.getEmail(),c.getTelephone()));
        }
    }
}

package PresentationLayer.Suppliers;

import java.util.*;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private int area;
    private String[] deliveryDays;
    private LinkedList<Contact> contacts;//<name,email>
    private boolean active;
//    private Map<Integer, Product> products =new HashMap<>();
//    private Map<Integer,Double> discountByAmount;//sum of products in order
//    private Map<Integer, Order> orders;
//    private Map<Integer,Order> pastOrders;

    public Supplier(int supplierNumber, String name, int bankAccount,LinkedList<Contact> contacts,int area,String[] deliveryDays){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        this.contacts=new LinkedList<>();
        this.contacts.addAll(contacts);
        active =true;
        this.area=area;
        this.deliveryDays=sort(deliveryDays);
//        discountByAmount=new TreeMap<>();
//        orders = new TreeMap<>();
//        pastOrders = new HashMap<>();
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
        ans += "\nArea shipment: " +area;
        int x=0;
        ans += "\ndelivery days : " ;

        for(String s : deliveryDays){
            ans += s +" , ";

        }
        return ans.substring(0,ans.length()-2);
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

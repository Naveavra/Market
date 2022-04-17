package DomainLayer;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;

public class Supplier {
    private int supplierNumber;
    private String name;
    private int bankAccount;
    private Map<String,String> contacts =new HashMap<>();

    public Supplier(int supplierNumber, String name,int bankAccount,Map<String,String> contacts){
        this.supplierNumber=supplierNumber;
        this.name=name;
        this.bankAccount=bankAccount;
        contacts.replaceAll((n,v) -> v);
    }
    public boolean addDiscount(int supplierNumber, int count,double discount){
        return true;
    }
    public boolean addDiscount(int supplierNumber, int productId,int count,double discount){
        return true;
    }
    public boolean removeDiscount(int supplierNumber, int productId){
        return true;
    }
    public boolean addProduct(int catalogNumber,String name, double price){
        return true;
    }
    public boolean removeProduct(int catalogNumber){
        return true;
    }
    public boolean createOrder(int supplierNumber){
        return true;
    }
}

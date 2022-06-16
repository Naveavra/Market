package DomainLayer.Transport;

import java.util.concurrent.ConcurrentHashMap;

public class DriverDocument {

    private Driver driver;
    private int id;
    private Store store;
    private ConcurrentHashMap<String, Integer> supplies;
    public String orderDocID;

    public DriverDocument(Driver driver, int id, ConcurrentHashMap<String, Integer> supplies,
                          Store store, String orderDocID){
        this.driver = driver;
        this.id = id;
        this.supplies = supplies;
        this.store = store;
        this.orderDocID =orderDocID;
    }
    public Store getStore(){
        return store;
    }
    public String getOrderDocID(){
        return this.orderDocID;
    }
    public String getDriverID(){
        return this.driver.getId();
    }
    public void setSupplies(ConcurrentHashMap<String,Integer> supplies){
        this.supplies=supplies;
    }
    public String toString(){
        String res = "";
        String docID = String.valueOf(id);
        String driver = this.driver.getName();
        String store = this.store.getId();

        res = "The doc id is: " + docID + "\nThe driver is: " + driver +
                "\nDestination: " + store + "\nSupplies: \n";
        for(String supp: this.supplies.keySet()){
            res+= "Product ID: " +supp+" ,Quantity: "+this.supplies.get(supp)+"\n";
        }
        return res;

    }

    public Integer getID() {
        return this.id;
    }
}

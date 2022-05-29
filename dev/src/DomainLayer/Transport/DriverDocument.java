package DomainLayer.Transport;

import java.util.concurrent.ConcurrentHashMap;

public class DriverDocument {

    private Driver driver;
    private int id;
    private Site store;
    private ConcurrentHashMap<Supply, Integer> supplies;
    public String orderDocID;

    public DriverDocument(Driver driver, int id, ConcurrentHashMap<Supply, Integer> supplies,
                          Site store, String orderDocID){
        this.driver = driver;
        this.id = id;
        this.supplies = supplies;
        this.store = store;
        this.orderDocID =orderDocID;
    }
    public Site getStore(){
        return store;
    }
    public String getOrderDocID(){
        return this.orderDocID;
    }
    public String getDriverID(){
        return this.driver.getId();
    }
    public void setSupplies(ConcurrentHashMap<Supply,Integer> supplies){
        this.supplies=supplies;
    }
    public String toString(){
        String res = "";
        String docID = String.valueOf(id);
        String driver = this.driver.getName();
        String store = this.store.getId();

        res = "The doc id is: " + docID + "\nThe driver is: " + driver +
                "\nDestination: " + store + "\nSupplies: \n";
        for(Supply supp: this.supplies.keySet()){
            res+= "Name: " +supp.name+" ,Quantity: "+this.supplies.get(supp)+"\n";
        }
        return res;

    }

    public Integer getID() {
        return this.id;
    }
}

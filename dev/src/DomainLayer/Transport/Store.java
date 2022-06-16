package DomainLayer.Transport;

public class Store {

    public enum ShippingArea {
        North,
        Center,
        South
    }

    private Contact contactInfo;
    private ShippingArea areaCode;
    private String id;
    private int type;

    public Store(String id, Contact contact, ShippingArea shippingArea, int type){
        this.contactInfo = contact;
        this.areaCode = shippingArea;
        this.id = id;
        this.type = type; // 0 for supplier 1 for store
    }
    public String toString(){
        return this.id;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public ShippingArea getAreaCode() {
        return areaCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}

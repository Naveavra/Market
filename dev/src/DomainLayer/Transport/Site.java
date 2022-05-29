package DomainLayer.Transport;

public class Site {

    public enum ShippingArea {
        North,
        Center,
        South
    }

    Contact contactinfo;
    ShippingArea areacode;
    String id;
    int type;

    public Site(String id,Contact contact, ShippingArea shippingArea, int type){
        this.contactinfo = contact;
        this.areacode = shippingArea;
        this.id = id;
        this.type = type; // 0 for supplier 1 for store
    }
    public String toString(){
        return this.id;
    }
    public Contact getContactinfo() {
        return contactinfo;
    }

    public ShippingArea getAreacode() {
        return areacode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

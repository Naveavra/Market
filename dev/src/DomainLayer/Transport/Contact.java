package DomainLayer.Transport;

public class Contact {

    String address;
    String name;
    String phonenumber;

    public Contact(String address, String name, String phonenumber) {

        this.address = address;
        this.name = name;
        this.phonenumber = phonenumber;

    }

    public void setName(String name){ this.name = name;}

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
}



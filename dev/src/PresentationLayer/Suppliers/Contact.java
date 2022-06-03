package PresentationLayer.Suppliers;

public class Contact {
    private String name;
    private String email;
    private String telephone;

    public Contact(String name, String email, String telephone) {
        this.telephone = telephone;
        this.name=name;
        this.email=email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }
    public void setEmail(String e) {
        this.email=e;
    }

    public void setTel(String tel) {
        this.telephone=tel;
    }
}

package DomainLayer.Storage;

public class Category {
    private String name;
    private double discount;

    public Category(String name) {
        this.name=name;
        discount=0;
    }

    public void setDiscount(double discount){
        this.discount=discount;
    }


    public double getDiscount(){
        return discount;
    }
    public String getName(){return name;}
}

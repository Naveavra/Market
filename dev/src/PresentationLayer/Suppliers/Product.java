package PresentationLayer.Suppliers;
import java.util.LinkedList;

public class Product {
    private int catalogNumber;
    private int productId;
    private double price;
    private int daysUntilExpiration;
    private LinkedList<Discount> discounts;//sum of specific product


    public Product(int catalogNumber,int productId,double price, int daysUntilExpiration){
        this.catalogNumber =catalogNumber;
        this.productId=productId;
        this.price=price;
        this.daysUntilExpiration = daysUntilExpiration;
        discounts = new LinkedList<>();// keep it sorted
    }
    public Product(Product product){
        this.catalogNumber = product.catalogNumber;
        this.productId= product.productId;
        this.price=product.price;
        this.daysUntilExpiration = product.daysUntilExpiration;
        discounts=new LinkedList<>();
        this.discounts.addAll(product.discounts);
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public boolean addDiscount(int count,double discount){
        for(Discount d:discounts){
            if(d.getAmount()==count){
                return false;
            }
        }
        this.discounts.add(new Discount(count, discount));
        return true;
    }

    public double getPrice() {
        return price;
    }
    public int getDaysUntilExpiration(){
        return daysUntilExpiration;
    }

    public String toString(){
        String ans = "catalog Number: " + catalogNumber;
        ans+= ", product Id: " +productId;
        ans+= ", price: "+ price;
        ans+=", days until expiration: "+daysUntilExpiration;
        return ans;
    }
}

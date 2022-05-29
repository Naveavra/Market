package PresentationLayer.Supplier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Product {
    private int catalogNumber;
    private int productId;
    private double price;
    private LinkedList<Discount> discounts;//sum of specific product


    public Product(int catalogNumber,int productId,double price){
        this.catalogNumber =catalogNumber;
        this.productId=productId;
        this.price=price;
        discounts = new LinkedList<>();// keep it sorted
    }
    public Product(Product product){
        this.catalogNumber = product.catalogNumber;
        this.productId= product.productId;
        this.price=product.price;
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

    public String toString(){
        String ans = "catalog Number: " + catalogNumber;
        ans+= ", product Id: " +productId;
        ans+= ", price: "+ price;
        return ans;
    }
}

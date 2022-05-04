package DomainLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Product {
    private int catalogNumber;//unique number in supplier
    private int productId;//unique number to product in the system
    private String name;//remove
    private double price;
    private Map<Integer,Double> discount;//sum of specific product


    public Product (int catalogNumber,String name,double price){
        this.catalogNumber =catalogNumber;
        this.name=name;
        this.price=price;
        discount =new HashMap<>();
        discount.put(0, 1.0);// keep it sorted
    }
    public Product (Product product){
        this.catalogNumber = product.catalogNumber;
        this.name= product.name;
        this.price=product.price;
        discount=new HashMap<>();
        discount.put(0, 1.0);// keep it sorted
        for(int x: product.discount.keySet()){
             discount.put(x,product.discount.get(x));
        }
    }
    public void setName(String name) {
        this.name = name;

    }
    public String getName(){
        return name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public boolean addDiscount(int count,double discount){
        if(this.discount.containsKey(count)){
            return false;
        }
        this.discount.put(count,discount);
        return true;
    }
    public boolean removeDiscountOnProduct( int count){
        if(!this.discount.containsKey(count)){
            return false;
        }
        this.discount.remove(count);
        return true;
    }

    public double getPriceAfterDiscount(int count) {
        return this.price*count*findMaxUnder(count);
    }
    private double findMaxUnder(int count){
        int out=0;
        for(int s:discount.keySet()){
            if(s<=count){
                out=s;
            }
            else{
                return discount.get(out);
            }
        }
        return discount.get(out);
    }

    public double getPrice() {
        return price;
    }
    public Map<Integer,Double> getDiscount(){
        return discount;
    }
    public int getCatalogNumber(){
        return catalogNumber;
    }
}

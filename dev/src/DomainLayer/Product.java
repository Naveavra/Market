package DomainLayer;

import java.util.Map;
import java.util.TreeMap;

public class Product {
    private int catalogNumber;
    private String name;
    private double price;
    private Map<Integer,Double> discount;//sum of specific product
    public Product (int catalogNumber,String name,double price){
        this.catalogNumber =catalogNumber;
        this.name=name;
        this.price=price;
        discount =new TreeMap<>();// keep it sorted
    }
    public void setName(String name) {
        this.name = name;

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
}

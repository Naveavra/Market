package PresentationLayer.Supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Product {
    private int catalogNumber;
    private String name;
    private double price;
    private Map<Integer,Double> discount;//sum of specific product


    public Product(int catalogNumber, String name, double price){
        this.catalogNumber =catalogNumber;
        this.name=name;
        this.price=price;
        discount =new HashMap<>();// keep it sorted
    }
    public Product(Product product){
        this.catalogNumber = product.catalogNumber;
        this.name= product.name;
        this.price=product.price;
        discount=new HashMap<>();
        for(int x: product.discount.keySet()){
             discount.put(x,product.discount.get(x));
        }
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

    public double getPrice() {
        return price;
    }

    public String toString(){
        String ans = "catalog Number: " + catalogNumber;
        ans += ", name: "+ name;
        ans+= ", price: "+ price;
        return ans;
    }
}

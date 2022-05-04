package DomainLayer.Storage;

import java.util.LinkedList;
import java.util.List;

public class SubSubCategory {
    private String name;
    private List<Product> products;//products

    public SubSubCategory(String name){
        this.name=name;
        products=new LinkedList<>();
    }
    public SubSubCategory(String name, List<Product> addProducts){
        this.name=name;
        products=new LinkedList<>();
        products.addAll(addProducts);
    }

    public List<Product> getAllProducts(){
        return products;
    }

    public String getName(){
        return name;
    }
    public void addProduct(Product p){
        if(!products.contains(p))
            products.add(p);
    }


    public boolean productBelongsTo(int id){
        boolean ans=false;
        for(Product p : getAllProducts())
            ans=ans || (p.getId()==id);
        return ans;
    }

    public void removeProduct(int id){
        for(Product p : getAllProducts())
            if(p.getId()==id)
                products.remove(p);

    }
}

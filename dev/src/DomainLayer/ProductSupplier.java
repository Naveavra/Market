package DomainLayer;

import DAL.ProductsSupplierDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProductSupplier {
    private int catalogNumber;//unique number in supplier
    private int productId;//unique number to product in the system
    private int supplierNumber;
    private double price;
    private Map<Integer,Double> discount;//sum of specific product
    private ProductsSupplierDAO productsSupplierDAO = new ProductsSupplierDAO();



    public ProductSupplier(int catalogNumber, double price, int productId){
        this.catalogNumber =catalogNumber;
        this.price=price;
        this.productId = productId;
        discount =new HashMap<>();
        discount.put(0, 1.0);// keep it sorted
    }
    public ProductSupplier(ProductSupplier productSupplier){
        this.catalogNumber = productSupplier.catalogNumber;
        this.price= productSupplier.price;
        discount=new HashMap<>();
        discount.put(0, 1.0);// keep it sorted
        for(int x: productSupplier.discount.keySet()){
             discount.put(x, productSupplier.discount.get(x));
        }
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public boolean addDiscount(int count,double discount){
        if(this.discount.containsKey(count)){
            return false;
        }
        this.discount.put(count,discount);
        try {
            productsSupplierDAO.insertDiscountOnProduct(this,count,discount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
    public boolean removeDiscountOnProduct( int count){
        if(!this.discount.containsKey(count)){
            return false;
        }
        this.discount.remove(count);
        try {
            productsSupplierDAO.removeDiscountOnProduct(this,count);
        } catch (SQLException e) {
           e.printStackTrace();
        }
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

//    public String getName() {
//        return "";
//    }

    public int getProductId() {
        return productId;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }
}

package DomainLayer.Supplier;

import DAL.ProductsSupplierDAO;
import com.oracle.webservices.internal.api.message.DistributedPropertySet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ProductSupplier {
    private int catalogNumber;//unique number in supplier
    private int productId;//unique number to product in the system
    private int supplierNumber;
    private double price;
    private LinkedList<Discount> discounts;//sum of specific product
    private transient ProductsSupplierDAO productsSupplierDAO = new ProductsSupplierDAO();



    public ProductSupplier(int supplierNumber,int catalogNumber, double price, int productId,LinkedList<Discount> discount){
        this.supplierNumber=supplierNumber;
        this.catalogNumber =catalogNumber;
        this.price=price;
        this.productId = productId;
        this.discounts=discount;
        discounts.add(new Discount(0,1));// keep it sorted
    }
    public ProductSupplier(ProductSupplier productSupplier){
        this.catalogNumber = productSupplier.catalogNumber;
        this.productId=productSupplier.productId;
        this.supplierNumber=productSupplier.supplierNumber;
        this.price= productSupplier.price;
        discounts=new LinkedList<>();
        discounts.add(new Discount(0,1));// keep it sorted
        discounts.addAll(productSupplier.discounts);
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public boolean addDiscount(int count,double discount){
        if(contains(discounts,count )){
            return false;
        }
        this.discounts.add(new Discount(count,discount));
        try {
            productsSupplierDAO.insertDiscountOnProduct(this,count,discount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean removeDiscountOnProduct( int count){
        remove(this.discounts,count);
        try {
            if(productsSupplierDAO.getDiscountOnProduct(this,count)!=null){
                productsSupplierDAO.removeDiscountOnProduct(this,count);
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public double getPriceAfterDiscount(int count) {
        return this.price*count*findMaxUnder(this,count);
    }
    private double findMaxUnder(ProductSupplier p,int count){
        double discount=findMaxUnder(discounts,count);
        if(discount==-1) {
            try {
                return productsSupplierDAO.getDiscountOnProduct(p, count);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return discount;
    }
    public double findMaxUnder(LinkedList<Discount> d,int amount){
        double out=-1;
        for(Discount discount:d){
            if(discount.getAmount()<=amount){
                out=discount.getDiscount();
            }
        }
        return out;
    }

    public double getPrice() {
        return price;
    }
    public LinkedList<Discount> getDiscount(){
        return discounts;
    }
    public int getCatalogNumber(){
        return catalogNumber;
    }
    public int getProductId() {
        return productId;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }
    public boolean contains(LinkedList<Discount> discounts,int amount){
        for(Discount discount:discounts){
            if(discount.getAmount()==amount){
                return true;
            }
        }
        return false;
    }
    public void remove(LinkedList<Discount> discounts,int amount){
        discounts.removeIf(d -> d.getAmount() == amount);
    }
}
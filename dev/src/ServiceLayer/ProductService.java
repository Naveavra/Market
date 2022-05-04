package ServiceLayer;

import DomainLayer.Storage.ProductController;
import DomainLayer.Storage.*;

import java.io.IOException;
import java.util.List;

public class ProductService {

    private ProductController productCon;

    public ProductService(){
        this.productCon=new ProductController();
    }

    public String getNameWithId(int id)
    {
        return productCon.getProductWithId(id).getName();
    }

    public void addNewProduct(int pId, String pName, String desc, int daysForResupply, double priceSupplier,
                              double price, String maker){
        productCon.addNewProduct(pId, pName, desc, daysForResupply, priceSupplier, price, maker);
    }

    public void changeDaysForResupply(int id, int days){
        productCon.changeDaysForResupply(id, days);
    }

    public void changeDaysPassed(int id, int days){
        productCon.changeDaysPassed(id, days);
    }

    public void addItemToProduct(int id, Location.Place  loc, int shelf, String ed){//location ?? -> string
        productCon.addItemToProduct(id, loc, shelf, ed);
    }

    public void removeProductFromManu(int id){
        productCon.removeProductFromManu(id);
    }

    public double getPriceOfProduct(int id){
        return productCon.getPriceOfProduct(id);
    }

    public int getAmountOfProduct(int id){
        return productCon.getAmountOfProduct(id);
    }

    public boolean productInShop(int id) {
        return productCon.productInShop(id);
    }

    public void removeItem(Location.Place place, int shelf, String ed, int pId){//location
        productCon.removeItem(place, shelf, ed, pId);
    }

    public boolean hasItem(Location.Place place, int shelf, String ed, int pId){//location
        return productCon.hasItem(place, shelf, ed, pId);
    }

    public void setDiscountToOneItem(int id, double discount){
        productCon.setDiscountToOneItem(id, discount);
    }

    public Item defineAsDamaged(int id, String description, Location.Place place, int shelf, String ed)//location??
    {
        return productCon.defineAsDamaged(id, description, place, shelf, ed);
    }

    public boolean canBuyItems(int id, int amount){
        return productCon.canBuyItems(id, amount);
    }

    public double buyItems(int id, int amount){
        return productCon.buyItems(id, amount);
    }

    public boolean needsRefill(int id){
        return productCon.needsRefill(id);
    }

    public void transferItem(int id, String ed, Location.Place curePlace, int curShelf, Location.Place toPlace, int toShelf){//location???
        productCon.transferItem(id, ed, curePlace, curShelf, toPlace, toShelf);
    }

    public void addAllItems(int id, int amount, String ed, int shelf){
        productCon.addAllItems(id, amount, ed, shelf);
    }

    public int getProductIdWithName(String name){
        return productCon.getProductIdWithName(name);
    }

    public void moveItemsToStore(int id, int amount){
        productCon.moveItemsToStore(id, amount);
    }

    public boolean hasProduct(int pId){
        return productCon.hasProduct(pId);
    }

    public CategoryService createCategoryService(){
        return new CategoryService(new CategoryController(productCon));
    }

}

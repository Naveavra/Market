package main.java.DomainLayer.Storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductController
{
    private HashMap<Integer, Product> allProducts;
    private List<Product> refill;

    public ProductController(){
        allProducts=new HashMap<>();
        refill=new LinkedList<>();
    }

    public Product getProductWithId(int id)
    {
        if(allProducts.containsKey(id))
            return this.allProducts.get(id);
        return null;
    }

    public Product addNewProduct(int pId, String pName, String desc, int daysForResupply, double priceSupplier,
                                 double price, String maker){
        Product p=new Product(pId, pName, desc, daysForResupply, priceSupplier, price, maker);
        allProducts.put(pId, p);
        return p;
    }

    public void changeDaysForResupply(int id, int days){
        if(allProducts.containsKey(id))
            allProducts.get(id).setDaysForResupply(days);
        allProducts.get(id).toRefill();
        if(allProducts.get(id).getNeedsRefill())
            refill.add(allProducts.get(id));
    }
    public void changeDaysPassed(int id, int days){
        if(allProducts.containsKey(id))
            allProducts.get(id).setDaysPassed(days);
    }

    public void addItemToProduct(int id, Location.Place  loc, int shelf, String ed){
        if(productInShop(id))
            allProducts.get(id).addItem(loc, shelf, ed);
    }

    public void removeProductFromManu(int id){
        allProducts.remove(id);
    }

    public double getPriceOfProduct(int id){
        if(allProducts.containsKey(id))
            return allProducts.get(id).getPrice();
        return -1;
    }

    public int getAmountOfProduct(int id){
        if(allProducts.containsKey(id))
            return allProducts.get(id).getCurAmount();
        return 0;
    }
    public boolean productInShop(int id){
        return allProducts.containsKey(id);
    }

    public void removeItem(Location.Place place, int shelf, String ed, int pId){
        if(allProducts.containsKey(pId))
            if(allProducts.get(pId).hasItem(place, shelf, ed)) {
                Item remove = allProducts.get(pId).removeItem(place, shelf, ed, false);
            }
    }
    public boolean hasItem(Location.Place place, int shelf, String ed, int pId){
        return allProducts.get(pId).hasItem(place, shelf, ed);
    }

    public void setDiscountToOneItem(int prodectId, double discount)
    {
        allProducts.get(prodectId).setDiscount(discount);
    }

    public Item defineAsDamaged(int id, String description, Location.Place place, int shelf, String ed)
    {
        Item damaged = allProducts.get(id).removeItem(place, shelf, ed, false);
        if(damaged!=null) {
            damaged.setDefectiveDescription(description);
            allProducts.get(id).getDamagedItems().add(damaged);
        }
        return damaged;
    }

    public List<Item> getDamagedItems() throws IOException {
        List<Item> damagedItems=new LinkedList<>();
        for(int id : allProducts.keySet()){
            damagedItems.addAll(allProducts.get(id).getDamagedItems());
        }
        return damagedItems;
    }

    public List<Product> makeRefillReport(){
        return refill;
    }

    public boolean canBuyItems(int id, int amount){
        boolean ans=false;
        if(productInShop(id))
            ans=allProducts.get(id).canBuy(amount);
        return ans;
    }
    public double buyItems(int id, int amount){
        if(productInShop(id)) {
            if(canBuyItems(id, amount)) {
                double ans=allProducts.get(id).buyAmount(amount);
                return ans;
            }
        }
        return -1;
    }

    public boolean needsRefill(int id){
        if (allProducts.get(id).getRefill() > 0 && !allProducts.get(id).getNeedsRefill()) {
            refill.add(allProducts.get(id));
            allProducts.get(id).setNeedsRefill(true);
            return true;
        }
        else if(allProducts.get(id).getRefill()==0) {
                allProducts.get(id).setNeedsRefill(false);
                refill.remove(allProducts.get(id));
        }
        return false;
    }

    public void transferItem(int id, String ed, Location.Place curePlace, int curShelf, Location.Place toPlace, int toShelf){
        if(productInShop(id))
            allProducts.get(id).transferItem(ed, curePlace, curShelf, toPlace, toShelf);
    }

    public void addAllItems(int id, int amount, String ed, int shelf){
        if(allProducts.containsKey(id))
            allProducts.get(id).addAllItems(amount, ed, shelf);
    }

    public int getProductIdWithName(String name){
        for(Product p : allProducts.values())
            if(p.getName().equals(name))
                return p.getId();
        return -1;
    }

    public void moveItemsToStore(int id, int amount){
        if(allProducts.containsKey(id))
            allProducts.get(id).moveToStore(amount);
    }


    public boolean hasProduct(int pId) {
        return this.allProducts.keySet().contains(pId);
    }




}

package ServiceLayer;

import DomainLayer.FacadeSupplier_Storage;

import java.util.HashMap;

public class CategoryService {

    private FacadeSupplier_Storage facadeSupplier;

    public CategoryService() {
        this.facadeSupplier = new FacadeSupplier_Storage();
    }//const empty

    public void addCategory(String cName) {
        facadeSupplier.addCategory(cName);
    }

    public void addNewProduct(int pId, String pName, String desc, double price,
                              String maker, String cat, String sub, String subSub) {
        facadeSupplier.addNewProduct(pId, pName, desc, price, maker, cat, sub, subSub);
    }

    public void addSubCat(String cName, String subName) {
        facadeSupplier.addSubCat(cName, subName);
    }

    public void addSubSubCat(String cName, String subName, String subsub) {
        facadeSupplier.addSubSubCat(cName, subName, subsub);
    }


    public void setDiscount(String cName, double discount) {
        facadeSupplier.setDiscount(cName, discount);
    }

    public void transferProduct(int id, String catAdd, String subAdd, String subSubAdd) {
        facadeSupplier.transferProduct(id, catAdd, subAdd, subSubAdd);
    }


    public void removeFromCatalog(int id) {
        facadeSupplier.removeFromCatalog(id);
    }


    public void removeCat(String catName){
        facadeSupplier.removeCat(catName);
    }


    public String getNameWithId(int id)
    {
        return facadeSupplier.getNameWithId(id);
    }
    public boolean needsRefill(int productId){
        return facadeSupplier.needsRefill(productId);

    }

    public void setDiscountToOneItem(int id, double discount){
        facadeSupplier.setDiscountToOneItem(id, discount);
    }

    public void defineAsDamaged(int id, String description,String place, int shelf, String ed)
    {
        facadeSupplier.defineAsDamaged(id, description, place, shelf, ed);
    }


    public double buyItems(int id, int amount){
        return facadeSupplier.buyItems(id, amount);
    }

    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        facadeSupplier.transferItem(id, ed, curePlace, curShelf, toPlace, toShelf);
    }

    public int getProductIdWithName(String name){
        return facadeSupplier.getProductIdWithName(name);
    }

    public void moveItemsToStore(int id, int amount){
        facadeSupplier.moveItemsToStore(id, amount);
    }

    public String printAllProducts(){
        return facadeSupplier.printAllProducts();
    }

    public HashMap<Integer, Integer> getItemsFromTransport(int id) {
        return facadeSupplier.getItemsFromTransport(id);
    }
    public void addAllItems(int productId, int quantity, String ed, int shelf){
        facadeSupplier.addAllItems(productId, quantity, ed, shelf);
    }

    public void updateOrders(){
        facadeSupplier.updateOrders();
    }

    public String getAllOrderDocIDs() {
        return facadeSupplier.getAllOrderDocIDs();
    }
}
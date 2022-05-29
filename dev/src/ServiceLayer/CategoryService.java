package ServiceLayer;

import DomainLayer.Facade;

public class CategoryService {

    private Facade facade;

    public CategoryService() {
        this.facade = new Facade();
    }//const empty

    public void addCategory(String cName) {
        facade.addCategory(cName);
    }

    public void addNewProduct(int pId, String pName, String desc, double price,
                              String maker, String cat, String sub, String subSub) {
        facade.addNewProduct(pId, pName, desc, price, maker, cat, sub, subSub);
    }

    public void addSubCat(String cName, String subName) {
        facade.addSubCat(cName, subName);
    }

    public void addSubSubCat(String cName, String subName, String subsub) {
        facade.addSubSubCat(cName, subName, subsub);
    }


    public void setDiscount(String cName, double discount) {
        facade.setDiscount(cName, discount);
    }

    public void transferProduct(int id, String catAdd, String subAdd, String subSubAdd) {
        facade.transferProduct(id, catAdd, subAdd, subSubAdd);
    }


    public void removeFromCatalog(int id) {
        facade.removeFromCatalog(id);
    }


    public void removeCat(String catName){
        facade.removeCat(catName);
    }


    public String getNameWithId(int id)
    {
        return facade.getNameWithId(id);
    }
    public boolean needsRefill(int productId){
        return facade.needsRefill(productId);

    }

    public void setDiscountToOneItem(int id, double discount){
        facade.setDiscountToOneItem(id, discount);
    }

    public void defineAsDamaged(int id, String description,String place, int shelf, String ed)
    {
        facade.defineAsDamaged(id, description, place, shelf, ed);
    }


    public double buyItems(int id, int amount){
        return facade.buyItems(id, amount);
    }

    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        facade.transferItem(id, ed, curePlace, curShelf, toPlace, toShelf);
    }

    public void addAllItems(int id, int amount, String ed, int shelf){
        facade.addAllItems(id, amount, ed, shelf);
    }

    public int getProductIdWithName(String name){
        return facade.getProductIdWithName(name);
    }

    public void moveItemsToStore(int id, int amount){
        facade.moveItemsToStore(id, amount);
    }

    public String printAllProducts(){
        return facade.printAllProducts();
    }
}
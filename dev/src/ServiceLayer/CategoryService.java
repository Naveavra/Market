package ServiceLayer;

import DomainLayer.Storage.Category;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Storage.ReportController;

import java.util.List;

public class CategoryService {

    private CategoryController categoryCon;

    public CategoryService() {
        this.categoryCon = new CategoryController();
    }//const empty

    public void addCategory(String cName) {
        categoryCon.addCategory(cName);
    }

    public void addProductToCat(int id, String cat, String sub, String subSub) {
        categoryCon.addProductToCat(id, cat, sub, subSub);
    }

    public void addNewProduct(int pId, String pName, String desc, int daysForResupply, double priceSupplier, double price,
                              String maker, String cat, String sub, String subSub) {
        categoryCon.addNewProduct(pId, pName, desc, daysForResupply, priceSupplier, price, maker, cat, sub, subSub);
    }

    public void addSubCat(String cName, String subName) {
        categoryCon.addSubCat(cName, subName);
    }

    public void addSubSubCat(String cName, String subName, String subsub) {
        categoryCon.addSubSubCat(cName, subName, subsub);
    }

    public Category findCat(String cat) {
        return categoryCon.findCat(cat);
    }//return json

    public void setDiscount(String cName, double discount) {
        categoryCon.setDiscount(cName, discount);
    }

    public List<Category> makeReport(List<String> catNames) {
        return categoryCon.makeReport(catNames);
    }//json

    public void transferProduct(int id, String catRemove, String catAdd, String subAdd, String subSubAdd) {
        categoryCon.transferProduct(id, catRemove, catAdd, subAdd, subSubAdd);
    }


    public void removeFromCatalog(int id) {
        categoryCon.removeFromCatalog(id);
    }

    public boolean canRemoveCat(String catName) {
        return categoryCon.canRemoveCat(catName);
    }

    public void removeCat(String catName){
        categoryCon.removeCat(catName);
    }

    public boolean hasCategory(String name){
        return categoryCon.hasCategory(name);
    }





    //==============================================================================

    public String getNameWithId(int id)
    {
        return categoryCon.getProductWithId(id).getName();
    }

    public void changeDaysForResupply(int id, int days){
        categoryCon.changeDaysForResupply(id, days);
    }

    public void changeDaysPassed(int id, int days){
        categoryCon.changeDaysPassed(id, days);
    }

    public void addItemToProduct(int id, String loc, int shelf, String ed){//location ?? -> string
        categoryCon.addItemToProduct(id, loc, shelf, ed);
    }

    public void removeProductFromManu(int id){
        categoryCon.removeProductFromManu(id);
    }

    public double getPriceOfProduct(int id){
        return categoryCon.getPriceOfProduct(id);
    }

    public int getAmountOfProduct(int id){
        return categoryCon.getAmountOfProduct(id);
    }

    public boolean productInShop(int id) {
        return categoryCon.productInShop(id);
    }

    public void removeItem(String place, int shelf, String ed, int pId){
        categoryCon.removeItem(place, shelf, ed, pId);
    }

    public boolean hasItem(String place, int shelf, String ed, int pId){
        return categoryCon.hasItem(place, shelf, ed, pId);
    }

    public void setDiscountToOneItem(int id, double discount){
        categoryCon.setDiscountToOneItem(id, discount);
    }

    public void defineAsDamaged(int id, String description,String place, int shelf, String ed)
    {
        categoryCon.defineAsDamaged(id, description, place, shelf, ed);
    }

    public boolean canBuyItems(int id, int amount){
        return categoryCon.canBuyItems(id, amount);
    }

    public double buyItems(int id, int amount){
        return categoryCon.buyItems(id, amount);
    }

    public boolean needsRefill(int id){
        return categoryCon.needsRefill(id);
    }

    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        categoryCon.transferItem(id, ed, curePlace, curShelf, toPlace, toShelf);
    }

    public void addAllItems(int id, int amount, String ed, int shelf){
        categoryCon.addAllItems(id, amount, ed, shelf);
    }

    public int getProductIdWithName(String name){
        return categoryCon.getProductIdWithName(name);
    }

    public void moveItemsToStore(int id, int amount){
        categoryCon.moveItemsToStore(id, amount);
    }

    public boolean hasProduct(int pId){
        return categoryCon.productInShop(pId);
    }

    public ReportService createReportService(){
        return new ReportService(new ReportController(categoryCon));
    }
}
package ServiceLayer;

import main.java.DomainLayer.Storage.Category;
import main.java.DomainLayer.Storage.CategoryController;

import java.util.List;

public class CategoryService {

    private CategoryController categoryCon;

    public CategoryService(CategoryController categoryCon) {
        this.categoryCon = categoryCon;
    }

    public void addCategory(String cName) {
        categoryCon.addCategory(cName);
    }

    public void addProductToCat(int id, String cat, String sub, String subSub) {
        categoryCon.addProductToCat(id, cat, sub, subSub);
    }

    public void addNewProductToCat(int pId, String pName, String desc, int daysForResupply, double priceSupplier, double price,
                                   String maker, String cat, String sub, String subSub) {
        categoryCon.addNewProductToCat(pId, pName, desc, daysForResupply, priceSupplier, price, maker, cat, sub, subSub);
    }

    public void addSubCat(String cName, String subName) {
        categoryCon.addSubCat(cName, subName);
    }

    public void addSubSubCat(String cName, String subName, String subsub) {
        categoryCon.addSubSubCat(cName, subName, subsub);
    }

    public Category findCat(String cat) {
        return categoryCon.findCat(cat);
    }

    public void setDiscount(String cName, double discount) {
        categoryCon.setDiscount(cName, discount);
    }

    public List<Category> makeReport(List<String> catNames) {
        return categoryCon.makeReport(catNames);
    }

    public void transferProduct(int id, String catRemove, String catAdd, String subAdd, String subSubAdd) {
        categoryCon.transferProduct(id, catRemove, catAdd, subAdd, subSubAdd);
    }

    public boolean canRemoveProduct(int id) {
        return categoryCon.canRemoveProduct(id);
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

    public CategoryController getCategoryCon(){
        return categoryCon;
    }
}
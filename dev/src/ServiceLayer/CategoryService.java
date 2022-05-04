package ServiceLayer;

import DomainLayer.Storage.Category;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Storage.ReportController;

import java.util.List;

public class CategoryService {

    private CategoryController categoryCon;

    public CategoryService(CategoryController categoryCon) {
        this.categoryCon = categoryCon;
    }//const empty

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

    public ReportService createReportService(){
        return new ReportService(new ReportController(categoryCon, categoryCon.getProductCon()));
    }
}
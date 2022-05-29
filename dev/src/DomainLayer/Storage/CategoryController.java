package DomainLayer.Storage;


import DAL.CategoryToProductDAO;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CategoryController
{
    private CategoryToProductDAO categoriesDAO;


    public CategoryController(){
        categoriesDAO =new CategoryToProductDAO();
    }

    public void addCategory(String categoryName, double discount){
        categoriesDAO.insertIntoDiscount(categoryName, discount);
    }

    public void addProductToCategory(int id, String cat, String sub, String subSub){
        categoriesDAO.insertIntoProduct(getProductWithId(id), cat, sub, subSub);
    }

    public void addSubCategory(String cName, String subName){
        categoriesDAO.insertIntoSubCategory(cName, subName);
    }

    public void addSubSubCategory(String cName, String subName, String subSub){

        categoriesDAO.insertIntoSubSubCategory(cName, subName, subSub);
    }

    public void setDiscount(String categoryName, double discount){
        categoriesDAO.updateDiscount(categoryName, discount);
        List<Product> products= categoriesDAO.getProductsOfCategory(categoryName);
        for(Product p : products) {
            p.setDiscount(discount);
            categoriesDAO.updateProduct(p);
        }
    }

    public List<HashMap<String, HashMap<String, HashMap<String, List<Product>>>>> makeReport(List<String> catNames){
        List<HashMap<String, HashMap<String, HashMap<String, List<Product>>>>> cats=new LinkedList<>();
        HashMap<String, HashMap<String, List<Product>>> sub;
        HashMap<String, List<Product>> productsInCat;
        List<Category> categories=categoriesDAO.getCategories();
        for(String name : catNames){
            sub=null;
            productsInCat=null;
            Category c=null;
            for(Category category :categories)
                if(category.getName().equals(name))
                    c=category;
            if(c!=null) {
                List<Category> subCategories = categoriesDAO.getSubCategories(name);
                for (Category subName : subCategories) {
                    List<Category> subSubCategories = categoriesDAO.getSubSubCategories(name, subName.getName());
                    for (Category subSubName : subSubCategories) {
                        List<Product> products = categoriesDAO.getProductsOfSubSubCategory(name, subName.getName(), subSubName.getName());
                        productsInCat=new HashMap<>();
                        productsInCat.put(subSubName.getName(), products);
                    }
                    sub=new HashMap<>();
                    sub.put(subName.getName(), productsInCat);
                }
                HashMap<String, HashMap<String, HashMap<String, List<Product>>>> addCats=new HashMap<>();
                addCats.put(c.getName(), sub);
                cats.add(addCats);
            }
        }
        return cats;
    }

    public List<Category> getDiscounts(List<String> catNames) {
        return categoriesDAO.getCategories();
    }

    public void transferProduct(int productId, String newCategory, String newSubCategory, String newSubSubCategory){
        categoriesDAO.updateCategoriesForProduct(productId, newCategory, newSubCategory, newSubSubCategory);
    }

    public void removeFromCatalog(int id){
        Product p=getProductWithId(id);
        p.removeAllItems();
        categoriesDAO.removeProduct(id);
    }

    public boolean removeCat(String catName){
        return categoriesDAO.removeCategory(catName);
    }


    public Product getProductWithId(int productId)
    {
        return categoriesDAO.getProduct(productId);
    }

    public void addNewProduct(int productId, String productName, String desc,
                              double price, String maker, String catName, String subCatName, String subSubName){
        if(validId(productId) && categoriesDAO.hasSubSubCategory(catName, subCatName, subSubName)) {
            categoriesDAO.insertIntoProduct(new Product(productId, productName, desc, price, maker), catName, subCatName, subSubName);
        }
    }
    public void addItemToProduct(int id, String loc, int shelf, String ed){
        Product p = getProductWithId(id);
        if(p != null) {
            p.addItem(loc, shelf, ed);
            p.getRefill();
            categoriesDAO.updateProduct(p);
        }
    }


    public double getPriceOfProduct(int id){
        Product p = getProductWithId(id);
        if(p!=null)
            return p.getPrice();
        return -1;
    }

    public int getAmountOfProduct(int id){
        Product p = getProductWithId(id);
        if(p!=null)
            return p.getCurAmount();
        return -1;
    }
    public void setDiscountToOneItem(int productId, double discount)
    {
        Product p = getProductWithId(productId);
        if(p != null) {
            p.setDiscount(discount);
            categoriesDAO.updateProduct(p);
        }
    }

    public void defineAsDamaged(int id, String description, String place, int shelf, String ed){
        Product p=getProductWithId(id);
        if(p!=null) {
            p.setItemDamaged(id, ed, place, shelf, description);
            p.getRefill();
            categoriesDAO.updateProduct(p);
        }
    }

    public List<Item> getDamagedItems(){
        List<Item> damagedItems=new LinkedList<>();
        for(Product p: getAllProducts()){
            damagedItems.addAll(p.getDamagedItems());
        }
        return damagedItems;
    }

    public List<Item> getExpiredItems(){
        List<Item> expiredItems=new LinkedList<>();
        for(Product p: getAllProducts()){
            expiredItems.addAll(p.getExpiredItems());
        }
        return expiredItems;
    }



    public List<Product> makeRefillReport(){
        List<Product> refill=new LinkedList<>();
        for(Product p : getAllProducts())
            if(p.getNeedsRefill())
                refill.add(p);
        return refill;
    }

    public boolean needsRefill(int productId){
        Product p=getProductWithId(productId);
        if(p!=null)
            return p.getNeedsRefill();
        return false;
    }
    public double buyItems(int id, int amount){
        Product p=getProductWithId(id);
        double ans=-1;
        if(p!=null) {
            ans=p.buyAmount(amount);
            p.getRefill();
            categoriesDAO.updateProduct(p);
        }
        return ans;
    }
    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        Product p=getProductWithId(id);
        if(p!=null) {
            p.transferItem(ed, curePlace, curShelf, toPlace, toShelf);
            p.getRefill();
            categoriesDAO.updateProduct(p);
        }
    }

    public void addAllItems(int id, int amount, String ed, int shelf){
        Product p=getProductWithId(id);
        if(p!=null) {
            p.addAllItems(amount, ed, shelf);
            categoriesDAO.updateProduct(p);
        }
    }

    public int getProductIdWithName(String name){
        return categoriesDAO.getProduct(name).getId();
    }

    public void moveItemsToStore(int id, int amount){
        Product p=getProductWithId(id);
        if(p!=null) {
            p.moveToStore(amount);
            categoriesDAO.updateProduct(p);
        }
    }



    public List<Product> getAllProducts(){
        return categoriesDAO.getAllProducts();
    }

    private boolean validId(int id){
        for(Product p : getAllProducts())
            if(p.getId()==id)
                return false;
        return true;
    }

    public String printAllProducts() {
        List<Product> list = getAllProducts();
        String ans="";
        for(Product p : list) {
            ans+="Product id is: " +p.getId()+"\tProduct name is: "+p.getName()+"\tthe current amount is:"+p.getCurAmount()+'\n';
        }
        return ans;
    }

    public Map<Integer, Integer> getProductIds(){
        return categoriesDAO.getProductIds();
    }

    public boolean hasCategory(String name){
        return categoriesDAO.hasCategory(name);
    }
    public List<Product> getProductsOfCategory(String name){
        return categoriesDAO.getProductsOfCategory(name);
    }

}

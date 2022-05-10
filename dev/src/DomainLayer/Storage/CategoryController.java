package DomainLayer.Storage;


import DAL.CategoryToProductDAO;

import java.util.LinkedList;
import java.util.List;

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

    public List<Category> makeReport(List<String> catNames){
        List<Category> cats=new LinkedList<>();
        for(String name : catNames){
            if(categoriesDAO.hasCategory(name)) {
                List<String> subCategories = categoriesDAO.getSubCategories(name);
                List<SubCategory> add = new LinkedList<>();
                for (String subName : subCategories) {
                    List<String> subSubCategories = categoriesDAO.getSubSubCategories(name, subName);
                    List<SubSubCategory> addSub = new LinkedList<>();
                    for (String subSubName : subSubCategories) {
                        List<Product> products = categoriesDAO.getProductsOfSubSubCategory(name, subName, subSubName);
                        SubSubCategory subSubCategory = new SubSubCategory(subSubName, products);
                        addSub.add(subSubCategory);
                    }
                    SubCategory subCategory = new SubCategory(subName, addSub);
                    add.add(subCategory);
                }
                Category c = new Category(name, add);
                cats.add(c);
            }
        }
        return cats;
    }

    public void transferProduct(int productId, String newCategory, String newSubCategory, String newSubSubCategory){
        categoriesDAO.updateCategoriesForProduct(productId, newCategory, newSubCategory, newSubSubCategory);
    }

    public void removeFromCatalog(int id){
        categoriesDAO.removeProduct(id);
    }

    public void removeCat(String catName){
        categoriesDAO.removeCategory(catName);
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

    public void removeItem(String place, int shelf, String ed, int pId){
        Product p = getProductWithId(pId);
        if(p!=null)
            if(p.hasItem(place, shelf, ed))
                p.removeItem(place, shelf, ed, false);
    }
    public boolean hasItem(String place, int shelf, String ed, int pId){
        if(getProductWithId(pId)!=null)
            return getProductWithId(pId).hasItem(place, shelf, ed);
        return false;
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
        }
    }

    public List<Item> getDamagedItems(){
        List<Item> damagedItems=new LinkedList<>();
        for(Product p: getAllProducts()){
            damagedItems.addAll(p.getDamagedItems());
        }
        return damagedItems;
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

    public boolean canBuyItems(int id, int amount){
        boolean ans=false;
        Product p=getProductWithId(id);
        if(p!=null)
            ans=p.canBuy(amount);
        return ans;
    }
    public double buyItems(int id, int amount){
        Product p=getProductWithId(id);
        if(p!=null) {
            if(p.canBuy(amount)) {
                double ans=p.buyAmount(amount);
                categoriesDAO.updateProduct(p);
                return ans;
            }
        }
        return -1;
    }
    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        Product p=getProductWithId(id);
        if(p!=null) {
            p.transferItem(ed, curePlace, curShelf, toPlace, toShelf);
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

}

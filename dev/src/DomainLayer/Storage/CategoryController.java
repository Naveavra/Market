package DomainLayer.Storage;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CategoryController
{
    List<Category> categories;//all products
    ProductController productCon;
    public CategoryController(ProductController pro){
        categories=new LinkedList<>();
        productCon=pro;
    }

    public void addCategory(String cName){
        Category add=new Category(cName);
        boolean toAdd=true;
        for(Category c: categories)
            if(c.getName().equals(cName))
                toAdd=false;
        if(toAdd)
            categories.add(add);
    }

    public void addProductToCat(int id, String cat, String sub, String subSub){
        if(findCat(cat)!=null && productCon.getProductWithId(id)!=null)
            findCat(cat).addProduct(productCon.getProductWithId(id), sub, subSub);

    }

    public void addNewProductToCat(int pId, String pName, String desc, int daysForResupply, double priceSupplier, double price,
                                   String maker, String cat, String sub, String subSub){
        findCat(cat).addProduct(productCon.addNewProduct(pId, pName, desc, daysForResupply,
                priceSupplier, price, maker), sub, subSub);
    }

    public void addSubCat(String cName, String subName){
        Category cat=findCat(cName);
        if(cat!=null)
            cat.addSubCat(subName);
    }
    public void addSubSubCat(String cName, String subName, String subsub){
        int place=0;
        while(place<categories.size() && !cName.equals(categories.get(place).getName()))
            place++;
        if(cName.equals(categories.get(place).getName()))
            categories.get(place).addSubSubCat(subName, subsub);
    }

    public Category findCat(String cat){
        Category ans=null;
        for(Category category : categories)
            if(category.getName().equals(cat))
                ans=category;
        return ans;
    }
    public void setDiscount(String cName, double discount){
        if(findCat(cName)!=null)
            findCat(cName).setDiscount(discount);
    }
    public List<Category> makeReport(List<String> catNames){
        List<Category> cats=new LinkedList<>();
        for( String name : catNames){
            Category c=findCat(name);
            if(c!=null)
                cats.add(c);
        }
        return cats;
    }

    public void transferProduct(int id, String catRemove, String catAdd, String subAdd, String subSubAdd){
        findCat(catRemove).removeFromCat(id);
        findCat(catAdd).addProduct(productCon.getProductWithId(id), subAdd, subSubAdd);
    }

    public boolean canRemoveProduct(int id){
        boolean ans=false;
        for(Category c : categories)
            ans=ans || c.productBelongsTo(id);

        return !ans;
    }

    public void removeFromCatalog(int id){
        if(canRemoveProduct(id))
            productCon.removeProductFromManu(id);
    }

    public boolean canRemoveCat(String catName){
        if(findCat(catName).getAllProducts().size()>0)
            return false;
        return true;
    }

    public void removeCat(String catName){
        if(findCat(catName)!=null && canRemoveCat(catName))
            categories.remove(findCat(catName));
    }


    public boolean hasCategory(String name){
        for(Category c : this.categories)
            if(c.getName() == name)
                return true;
        return false;
    }

    //for products==================================================================================================

    public Product getProductWithId(int productId)
    {
        Product p = null;
        for(Category c : this.categories) {
            for(SubCategory subC : c.getSubCats()) {
                for(SubSubCategory subSubC : subC.getAllSubSub()) {
                   p = subSubC.findProductWithId(productId);
                   if(p != null)
                       return p;
                }
            }
        }
        return null;
    }

    public void addNewProduct(int productId, String productName, String desc, int daysForResupply, double priceSupplier,
                                 double price, String maker, String catname, String subCatName, String subSubName){
        Product p=new Product(productId, productName, desc, daysForResupply, priceSupplier, price, maker);
        Category cat = findCat(catname);
        if(cat != null)
            cat.addProduct(p, subCatName, subSubName);
    }

//    public void changeDaysForResupply(int id, int days){
//        Product p = getProductWithId(id);
//        if(p != null) {
//            p.setDaysForResupply(days);
//            p.toRefill();
//            if (p.getNeedsRefill())
//                refill.add(allProducts.get(id));
//        }
//    }
//    public void changeDaysPassed(int id, int days){
//        if(allProducts.containsKey(id))
//            allProducts.get(id).setDaysPassed(days);
//    }
//
//    public void addItemToProduct(int id, Location.Place  loc, int shelf, String ed){
//        if(productInShop(id))
//            allProducts.get(id).addItem(loc, shelf, ed);
//    }
//
//    public void removeProductFromManu(int id){
//        allProducts.remove(id);
//    }
//
//    public double getPriceOfProduct(int id){
//        if(allProducts.containsKey(id))
//            return allProducts.get(id).getPrice();
//        return -1;
//    }
//
//    public int getAmountOfProduct(int id){
//        if(allProducts.containsKey(id))
//            return allProducts.get(id).getCurAmount();
//        return 0;
//    }
//    public boolean productInShop(int id){
//        return allProducts.containsKey(id);
//    }
//
//    public void removeItem(Location.Place place, int shelf, String ed, int pId){
//        if(allProducts.containsKey(pId))
//            if(allProducts.get(pId).hasItem(place, shelf, ed)) {
//                Item remove = allProducts.get(pId).removeItem(place, shelf, ed, false);
//            }
//    }
//    public boolean hasItem(Location.Place place, int shelf, String ed, int pId){
//        return allProducts.get(pId).hasItem(place, shelf, ed);
//    }
//
//    public void setDiscountToOneItem(int prodectId, double discount)
//    {
//        allProducts.get(prodectId).setDiscount(discount);
//    }
//
//    public Item defineAsDamaged(int id, String description, Location.Place place, int shelf, String ed)
//    {
//        Item damaged = allProducts.get(id).removeItem(place, shelf, ed, false);
//        if(damaged!=null) {
//            damaged.setDefectiveDescription(description);
//            allProducts.get(id).getDamagedItems().add(damaged);
//        }
//        return damaged;
//    }
//
//    public List<Item> getDamagedItems() throws IOException {
//        List<Item> damagedItems=new LinkedList<>();
//        for(int id : allProducts.keySet()){
//            damagedItems.addAll(allProducts.get(id).getDamagedItems());
//        }
//        return damagedItems;
//    }
//
//    public List<Product> makeRefillReport(){
//        return refill;
//    }
//
//    public boolean canBuyItems(int id, int amount){
//        boolean ans=false;
//        if(productInShop(id))
//            ans=allProducts.get(id).canBuy(amount);
//        return ans;
//    }
//    public double buyItems(int id, int amount){
//        if(productInShop(id)) {
//            if(canBuyItems(id, amount)) {
//                double ans=allProducts.get(id).buyAmount(amount);
//                return ans;
//            }
//        }
//        return -1;
//    }
//
//    public boolean needsRefill(int id){
//        if (allProducts.get(id).getRefill() > 0 && !allProducts.get(id).getNeedsRefill()) {
//            refill.add(allProducts.get(id));
//            allProducts.get(id).setNeedsRefill(true);
//            return true;
//        }
//        else if(allProducts.get(id).getRefill()==0) {
//            allProducts.get(id).setNeedsRefill(false);
//            refill.remove(allProducts.get(id));
//        }
//        return false;
//    }
//
//    public void transferItem(int id, String ed, Location.Place curePlace, int curShelf, Location.Place toPlace, int toShelf){
//        if(productInShop(id))
//            allProducts.get(id).transferItem(ed, curePlace, curShelf, toPlace, toShelf);
//    }
//
//    public void addAllItems(int id, int amount, String ed, int shelf){
//        if(allProducts.containsKey(id))
//            allProducts.get(id).addAllItems(amount, ed, shelf);
//    }
//
//    public int getProductIdWithName(String name){
//        for(Product p : allProducts.values())
//            if(p.getName().equals(name))
//                return p.getId();
//        return -1;
//    }
//
//    public void moveItemsToStore(int id, int amount){
//        if(allProducts.containsKey(id))
//            allProducts.get(id).moveToStore(amount);
//    }
//
//
//    public boolean hasProduct(int pId) {
//        return this.allProducts.keySet().contains(pId);
//    }
//
//
//    public ProductController getProductCon(){
//        return productCon;
//    }


}

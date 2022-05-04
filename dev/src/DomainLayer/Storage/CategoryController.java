package DomainLayer.Storage;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CategoryController
{
    List<Category> categories;//all products
    public CategoryController(){
        categories=new LinkedList<>();
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
        if(findCat(cat)!=null && getProductWithId(id)!=null)
            findCat(cat).addProduct(getProductWithId(id), sub, subSub);

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
        findCat(catAdd).addProduct(getProductWithId(id), subAdd, subSubAdd);
    }

    public boolean canRemoveProduct(int id){
        boolean ans=false;
        for(Category c : categories)
            ans=ans || c.productBelongsTo(id);

        return !ans;
    }

    public void removeFromCatalog(int id){
        if(canRemoveProduct(id))
            removeProductFromManu(id);
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
        if(validId(productId)) {
            Product p = new Product(productId, productName, desc, daysForResupply, priceSupplier, price, maker);
            Category cat = findCat(catname);
            if (cat != null)
                cat.addProduct(p, subCatName, subSubName);
        }
    }

    public void changeDaysForResupply(int id, int days){
        Product p = getProductWithId(id);
        if(p != null) {
            p.setDaysForResupply(days);
            p.toRefill();
        }
    }
    public void changeDaysPassed(int id, int days){
        Product p = getProductWithId(id);
        if(p != null) {
            p.setDaysPassed(days);
            p.toRefill();
        }
    }

    public void addItemToProduct(int id, String loc, int shelf, String ed){
        Product p = getProductWithId(id);
        if(p != null) {
            p.addItem(loc, shelf, ed);
        }
    }

    public void removeProductFromManu(int id){
        for(Category c : categories)
            c.getAllProducts().remove(getProductWithId(id));
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
    public boolean productInShop(int id){
        Product p = getProductWithId(id);
        if(p!=null)
            return true;
        return false;
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
        if(getProductWithId(productId)!=null)
            getProductWithId(productId).setDiscount(discount);
    }

    public void defineAsDamaged(int id, String description, String place, int shelf, String ed)
    {
        Product p=getProductWithId(id);
        if(p!=null) {
            Item damaged = p.removeItem(place, shelf, ed, false);
            System.out.println(place+" "+shelf+" "+ed);
            if (damaged != null) {
                System.out.println("yes");
                damaged.setDefectiveDescription(description);
                p.getDamagedItems().add(damaged);
            }
        }
    }

    public List<Object> getDamagedItems() throws IOException {
        List<Object> damagedItems=new LinkedList<>();
        for(Product p: getAllProducts()){
            damagedItems.addAll(p.getDamagedItems());
        }
        return damagedItems;
    }

    public List<Object> makeRefillReport(){
        List<Object> refill=new LinkedList<>();
        for(Product p : getAllProducts())
            if(p.getNeedsRefill())
                refill.add(p);
        return refill;
    }

    public boolean canBuyItems(int id, int amount){
        boolean ans=false;
        if(productInShop(id))
            ans=getProductWithId(id).canBuy(amount);
        return ans;
    }
    public double buyItems(int id, int amount){
        if(productInShop(id)) {
            if(canBuyItems(id, amount)) {
                double ans=getProductWithId(id).buyAmount(amount);
                return ans;
            }
        }
        return -1;
    }

    public boolean needsRefill(int id){
        if(getProductWithId(id)!=null) {
            if (getProductWithId(id).getRefill() > 0 && !getProductWithId(id).getNeedsRefill()) {
                getProductWithId(id).setNeedsRefill(true);
                return true;
            } else if (getProductWithId(id).getRefill() == 0) {
                getProductWithId(id).setNeedsRefill(false);
            }
        }
        return false;
    }

    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        if(productInShop(id))
            getProductWithId(id).transferItem(ed, curePlace, curShelf, toPlace, toShelf);
    }

    public void addAllItems(int id, int amount, String ed, int shelf){
        if(productInShop(id))
            getProductWithId(id).addAllItems(amount, ed, shelf);
    }

    public int getProductIdWithName(String name){
        for(Product p : getAllProducts())
            if(p.getName().equals(name))
                return p.getId();
        return -1;
    }

    public void moveItemsToStore(int id, int amount){
        if(productInShop(id))
            getProductWithId(id).moveToStore(amount);
    }



    public List<Product> getAllProducts(){
        List<Product> ans=new LinkedList<>();
        for(Category c : categories)
            ans.addAll(c.getAllProducts());
        return ans;
    }

    public boolean validId(int id){
        for(Product p : getAllProducts())
            if(p.getId()==id)
                return false;
        return true;
    }

}

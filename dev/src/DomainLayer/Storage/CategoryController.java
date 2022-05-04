package DomainLayer.Storage;

import java.util.LinkedList;
import java.util.List;

public class CategoryController
{
    List<Category> categories;
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

    
}

package DomainLayer.Storage;
import java.util.LinkedList;
import java.util.List;

public class Category {
    private String name;
    private double discount;
    private List<SubCategory> subCats;

    public Category(String name) {
        this.name=name;
        discount=0;
        subCats=new LinkedList<>();

    }
    public Category(String name, List<SubCategory> addSub) {
        this.name=name;
        discount=0;
        subCats=new LinkedList<>();
        subCats.addAll(addSub);
    }

    public void setDiscount(double discount){
        this.discount=discount;
        for(Product p : getAllProducts())
            p.setDiscount(discount);
    }

    public void addSubCat(String subCat){
        SubCategory add=new SubCategory(subCat);
        boolean toAdd=true;
        for(SubCategory s: subCats)
            if(s.getName().equals(subCat))
                toAdd=false;
        if(toAdd)
            subCats.add(add);
    }
    public void addSubSubCat(String subCat, String subSubCat){
        addSubCat(subCat);
        SubCategory sub=findSub(subCat);
        if(sub!=null)
            sub.addSubSub(subSubCat);
    }
    public List<Product> getProductsOfSub(String sub){
        if(subCats.contains(findSub(sub)))
            return findSub(sub).getAllProducts();
        else
            return null;
    }

    public double getDiscount(){
        return discount;
    }

    public SubCategory findSub(String sName){
        int place=0;
        SubCategory ans=null;
        for(SubCategory sub : subCats)
            if(sub.getName().equals(sName))
                ans=sub;
        return ans;
    }

    public void addProduct(Product p, String sub, String subSub){
        if(p!=null && !subCats.contains(findSub(sub)))
            subCats.add(new SubCategory(sub));
        findSub(sub).addProduct(p, subSub);
    }

    public List<Product> getAllProducts(){
        List<Product> ans=new LinkedList<>();
        for(SubCategory sub : subCats)
            ans.addAll(sub.getAllProducts());
        return ans;
    }

    public String getName() {
        return name;
    }

    public boolean productBelongsTo(int id){
        boolean ans=false;
        for(Product p : getAllProducts())
            ans=ans || (p.getId()==id);
        return ans;
    }

    public void removeFromCat(int id){
        for(SubCategory sub : subCats)
            sub.removeProduct(id);

    }



    public List<SubCategory> getSubCats(){
        return this.subCats;
    }

    public boolean hasSubCat(String name){
        for(SubCategory sub : this.subCats)
            if(sub.getName() == name)
                return true;
        return false;
    }

    public SubCategory getSubCatByName(String name) {
        for(SubCategory sub : this.subCats)
        {
            if(sub.getName() == name)
                return sub;
        }
        return null;
    }



}

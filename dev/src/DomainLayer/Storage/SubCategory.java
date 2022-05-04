package DomainLayer.Storage;

import java.util.LinkedList;
import java.util.List;

public class SubCategory{
    private String name;
    private List<SubSubCategory> subSubCategories;


    public SubCategory(String name){
        this.name=name;
        subSubCategories=new LinkedList<>();
    }
    public SubCategory(String name, List<SubSubCategory> addSubSub){
        this.name=name;
        this.subSubCategories=new LinkedList<>();
        subSubCategories.addAll(addSubSub);
    }
    public void addProduct(Product p, String subSub){
        if(!subSubCategories.contains(findSubSub(subSub)))
            subSubCategories.add(new SubSubCategory(subSub));
        for(SubSubCategory c: subSubCategories)
            if(c.getName().equals(subSub))
                c.addProduct(p);
    }

    public SubSubCategory findSubSub(String sName){
        int place=0;
        SubSubCategory ans=null;
        for(SubSubCategory sub : subSubCategories)
            if(sub.getName().equals(sName))
                ans=sub;
        return ans;
    }

    public List<SubSubCategory> getAllSubSub(){
        return subSubCategories;
    }

    public void addSubSub(String subSub){
        SubSubCategory add=new SubSubCategory(subSub);
        boolean toAdd=true;
        for(SubSubCategory s: subSubCategories)
            if(s.getName().equals(subSub))
                toAdd=false;
        if(toAdd)
            subSubCategories.add(add);
    }
    public List<Product> getAllProducts(){
        List<Product> ans=new LinkedList<>();
        for(SubSubCategory c : subSubCategories)
            ans.addAll(c.getAllProducts());
        return ans;
    }

    public String getName() {
        return name;
    }

    public boolean productBelongsTo(int id){
        boolean ans=false;
        for(SubSubCategory c : subSubCategories)
            ans=ans || c.productBelongsTo(id);
        return ans;
    }

    public void removeProduct(int id){
        for(SubSubCategory sub : subSubCategories)
            sub.removeProduct(id);
    }

    public boolean hasSubSubCategory(String name){
        return findSubSub(name) != null;
    }

}

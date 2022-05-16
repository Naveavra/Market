package DAL;

import DomainLayer.Storage.Category;
import DomainLayer.Storage.Product;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CategoryToProductDAO {
    private static CategoryDAO categoryDAO;
    private static ProductDAO productDAO;

    public CategoryToProductDAO(){
        categoryDAO=new CategoryDAO();
        productDAO=new ProductDAO();
    }

    //discount table
    public boolean insertIntoDiscount(String category, double discount){
        try {
            return categoryDAO.insertIntoDiscount(category, discount);
        } catch (SQLException ignored) {
            return false;
        }
    }
    public boolean updateDiscount(String category, double discount){
        try {
            categoryDAO.updateDiscount(category, discount);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean hasCategory(String category){
        try {
            return categoryDAO.hasCategory(category);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeCategory(String category){
        if(canRemoveCategory(category)) {
            try {
                categoryDAO.removeCategory(category);
                return true;
            } catch (SQLException ignored) {
            }
        }
        return false;
    }

    //subCategory table
    public boolean insertIntoSubCategory(String category, String subCategory){
        try {
            return categoryDAO.insertIntoSubCategory(category, subCategory);
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Category> getCategories(){
        try {
            return categoryDAO.getCategories();
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public List<Category> getSubCategories(String category){
        try {
            return categoryDAO.getSubCategories(category);
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public void removeSubCategory(String category, String subCategory){
        if (canRemoveSubCategory(category, subCategory)) {
            try {
                categoryDAO.removeSubCategory(category, subCategory);
            } catch (SQLException ignored) {
            }
        }
    }

    public boolean hasSubCategory(String category, String subCategory){
        try {
            return categoryDAO.hasSubCategory(category, subCategory);
        } catch (SQLException e) {
            return false;
        }
    }

    //subSubCategory table
    public boolean insertIntoSubSubCategory(String  category, String subCategory, String subSubCategory){
        try {
            return categoryDAO.insertIntoSubSubCategory(category, subCategory, subSubCategory);
        } catch (SQLException e) {
            return false;
        }
    }
    public List<Category> getSubSubCategories(String category, String subCategory){
        try {
            return categoryDAO.getSubSubCategories(category, subCategory);
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public void removeSubSubCategory(String category, String subCategory, String subSubCategory){
        if(canRemoveSubSubCategory(category, subCategory, subSubCategory)) {
            try {
                categoryDAO.removeSubSubCategory(category, subCategory, subSubCategory);
            } catch (SQLException ignored) {
            }
        }
    }


    public boolean hasSubSubCategory(String category, String subCategory, String subSubcategory){
        try {
            return categoryDAO.hasSubSubCategory(category, subCategory, subSubcategory);
        } catch (SQLException e) {
            return false;
        }
    }



    //product table
    public boolean insertIntoProduct(Product p, String category, String subCategory, String subSubCategory){
        try {
            if(productDAO.insert(p, category, subCategory, subSubCategory)) {
                return true;
                }
        } catch (SQLException e) {
            return false;
        }

        return false;
    }

    public Product getProduct(int productId){
        try {
            return productDAO.get(productId);
        } catch (SQLException e) {
            return null;
        }
    }

    public Product getProduct(String productName){
        try {
            return productDAO.get(productName);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Product> getAllProducts(){
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public Map<Integer, Integer> getProductIds(){
        try {
            return productDAO.getProductIds();
        } catch (SQLException e) {
            return new HashMap<>();
        }
    }
    //functions to get products of categories
    public List<Product> getProductsOfCategory(String category){
        try {
            return productDAO.getAllByCategory(category);
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public List<Product> getProductsOfSubCategory(String category, String subCategory){
        try {
            return productDAO.getAllBySubCategory(category, subCategory);
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public List<Product> getProductsOfSubSubCategory(String category, String subCategory, String subSubCategory){
        try {
            return productDAO.getAllBySubSubCategory(category, subCategory, subSubCategory);
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    public void updateCategoriesForProduct(int productId, String catName, String subCatName, String subSubCatName){
        try {
            productDAO.updateCategoriesForProduct(productId, catName, subCatName, subSubCatName);
        } catch (SQLException ignored) {
        }
    }

    public void removeProduct(int productId){
        try {
            productDAO.removeProduct(productId);
        } catch (SQLException ignored) {

        }
    }

    public void updateProduct(Product p){
        try {
            productDAO.updateProduct(p);
        } catch (SQLException ignored) {
        }
    }


    //more functions
    private boolean canRemoveCategory(String category){
        if(getProductsOfCategory(category).size()==0)
            return true;
        return false;
    }

    private boolean canRemoveSubCategory(String category, String subCategory){
        if(getProductsOfSubCategory(category, subCategory).size()==0)
            return true;
        return false;
    }

    private boolean canRemoveSubSubCategory(String category, String subCategory, String subSubCategory){
        if(getProductsOfSubSubCategory(category, subCategory, subSubCategory).size()==0)
            return true;
        return false;
    }
}
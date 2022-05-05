package ServiceLayer;

import DomainLayer.Product;
import DomainLayer.SupplierController;
import com.google.gson.Gson;

import java.util.Map;

public class ProductSupplierService {

    private SupplierController supplierController;
    private Gson gson;

    public ProductSupplierService(){
        supplierController = new SupplierController();
        gson = new Gson();
    }

    /**
     * the function adds a new product to a supplier to supply
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param name the name of the new product
     * @param price the price of the product
     * @param productId unique number to product in the system
     * @return true if succeed, false if failed
     */
    public boolean addProduct(int supplierNumber, int catalogNumber, String name, int price, int productId){
        boolean ans = supplierController.getSupplier(supplierNumber).addProduct(catalogNumber, price, productId);
        return ans;
    }

    /**
     * the function return the products which the supplier supply
     * @param supplierNumber the id of the supplier
     * @return gson which wrappers the dictionary<catalog number,product>
     */
    public String getProductsOfSupplier(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        Map<Integer,Product> products = supplierController.getSupplier(supplierNumber).getProducts();
        return gson.toJson(products);
    }

    /**
     * the function update the fields of the product
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param name the new name (if u want to change)
     * @param price the new price (>0) (if u want to change)
     * @return true if succeed, false if failed
     */
    public boolean updateProduct(int supplierNumber, int catalogNumber, String name, int price){
        if(price<=0){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isProductExist(catalogNumber)){
           return false;
        }
        if(name.equals("")){
            return false;
        }
        supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).setPrice(price);
        return true;
    }

    /**
     * the function removes a product from supplier to supply
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @return true if succeed, false if failed
     */
    public boolean removeProduct(int supplierNumber, int catalogNumber){
        boolean ans = supplierController.getSupplier(supplierNumber).removeProduct(catalogNumber);
        return ans;
    }
}

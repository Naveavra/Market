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


    public boolean addProduct(int supplierNumber, int catalogNumber, String name, int price, int productId){
        boolean ans = supplierController.getSupplier(supplierNumber).addProduct(catalogNumber, price, productId);
        return ans;
        //JSONObject json = new JSONObject(demo);
    }

    public String getProductsOfSupplier(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            //System.out.println("supplier didnt found");//not printing in service
            return "fail";//change form ""
        }
        Map<Integer,Product> products = supplierController.getSupplier(supplierNumber).getProducts();
        return gson.toJson(products);
    }

    public boolean updateProduct(int supplierNumber, int catalogNumber, String name, int price){
        if(price<=0){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isProductExist(catalogNumber)){
           return false;
        }
        //supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).setName(name);
        supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).setPrice(price);
        return true;

    }


    public boolean removeProduct(int supplierNumber, int catalogNumber){
        boolean ans = supplierController.getSupplier(supplierNumber).removeProduct(catalogNumber);
        return ans;
    }

}

package ServiceLayer;

import DomainLayer.SupplierController;
import com.google.gson.Gson;
import org.json.JSONObject;

public class ProductService {

    private SupplierController supplierControler;
    private Gson gson;

    public ProductService(){
        supplierControler = new SupplierController();
        gson = new Gson();
    }


    public boolean addProduct(int supplierNumber, int catalogNumber, String name, int price){
        boolean ans = supplierControler.getSupplier(supplierNumber).addProduct(catalogNumber, name, price);
        return ans;
        //JSONObject json = new JSONObject(demo);
    }

    public String getProductsOfSupplier(int supplierNumber){

        return null;
    }

    public boolean updateProuduct(int supplierNumber, int catalogNumber, String name, int price){
        supplierControler.getSupplier(supplierNumber).getProduct(catalogNumber).setName(name);
        supplierControler.getSupplier(supplierNumber).getProduct(catalogNumber).setPrice(price);
        return true;

    }


    public boolean removeProduct(int supplierNumber, int catalogNumber){
        boolean ans = supplierControler.getSupplier(supplierNumber).removeProduct(catalogNumber);
        return ans;
    }

}

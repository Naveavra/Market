package ServiceLayer;

import DomainLayer.SupplierControler;
import org.json.JSONObject;

public class ProductService {

    private SupplierControler supplierControler;


    public JSONObject addProduct(int supplierNumber, int catalogNamber, String name, int price){
        boolean ans = supplierControler.getSupplier(supplierNumber).addProduct(catalogNamber, name, price);
        return ToJson(ans);
        //JSONObject json = new JSONObject(demo);
    }

    public void updateProuduct(int supplierNumber, int catalogNumber, String name, int price){
        supplierControler.getSupplier(supplierNumber).getProduct(catalogNumber).setName(name);
        supplierControler.getSupplier(supplierNumber).getProduct(catalogNumber).setPrice(price);

    }


    public JSONObject removeProduct(int supplierNumber, int catalogNumber){
        boolean ans = supplierControler.getSupplier(supplierNumber).removeProduct(catalogNumber);
        return ToJson(ans);
    }

    private JSONObject ToJson(Object obj)
    {
        return new JSONObject(obj);
    }
}

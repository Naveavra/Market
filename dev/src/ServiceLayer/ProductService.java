package ServiceLayer;
//import org.Json.simple.JSONObject;
//import org.json.JSONArray;

import DomainLayer.SupplierControler;

public class ProductService {

    private SupplierControler supplierControler;


    public String addProduct(int supplierNumber, int catalogNamber, String name, int price){
        boolean ans = supplierControler.getSupplier(supplierNumber).addProduct(catalogNamber, name, price);
        return ToJson(ans);
        //JSONObject json = new JSONObject(demo);
    }

    public void updateProuduct(int supplierNumber, int catalogNumber, String name, int price){
        supplierControler.getSupplier(supplierNumber).getProduct(catalogNumber).setName(name);
        supplierControler.getSupplier(supplierNumber).getProduct(catalogNumber).setPrice(price);

    }


    public String removeProduct(int supplierNumber, int catalogNumber){
        boolean ans = supplierControler.getSupplier(supplierNumber).removeProduct(catalogNumber);
        return ToJson(ans);
    }

    private String ToJson(Object obj)
    {
        return null;
        //return JsonSerializer.Serialize(obj, obj.GetType());
    }
}

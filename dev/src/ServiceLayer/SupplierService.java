package ServiceLayer;


import DomainLayer.Supplier;
import DomainLayer.SupplierController;

import java.util.Map;
import com.google.gson.Gson;
import org.json.JSONObject;

public class SupplierService {
    private SupplierController supplierController =new SupplierController();
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        return supplierController.openAccount(supplierNumber, supplierName,bankAccount,contacts);
    }
    public boolean closeAccount(int supplierNumber){
        return supplierController.closeAccount(supplierNumber);
    }
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        return supplierController.getSupplier(supplierNumber).updateAccount(supplierName,bankAccount,contacts);
    }
    public boolean addDiscount(int supplierNumber,int productNumber,int count,double discount){
        return supplierController.getSupplier(supplierNumber).getProduct(productNumber).addDiscount(count, discount);
    }
    public boolean addDiscount(int supplierNumber,int count,double discount){
        return supplierController.getSupplier(supplierNumber).addDiscount(count, discount);
    }
    public boolean removeDiscountOnProduct(int supplierNumber,int catalogNumber,int count){
        return supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).removeDiscountOnProduct(count);
    }
    public boolean removeDiscountOnAmount(int supplierNumber,int count){
        return supplierController.getSupplier(supplierNumber).removeDiscountOnAmount(count);
    }

    public String getSupplier(int supplierNumber) {
        Gson gson = new Gson();
        Supplier s = supplierController.getSupplier(supplierNumber);
        String json = gson.toJson(s);
        return  json;

    }
}

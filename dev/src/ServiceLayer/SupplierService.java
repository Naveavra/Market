package ServiceLayer;


import DomainLayer.PastOrder;
import DomainLayer.Supplier;
import DomainLayer.SupplierController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import org.json.JSONObject;

public class SupplierService {
    private SupplierController supplierController =new SupplierController();
    private Gson gson=new Gson();
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts,boolean isDeliver){
        if(supplierNumber<=0){
            return false;
        }
        return supplierController.openAccount(supplierNumber, supplierName,bankAccount,contacts,isDeliver);
    }
    public boolean closeAccount(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        return supplierController.closeAccount(supplierNumber);
    }
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(bankAccount<0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).updateAccount(supplierName,bankAccount,contacts);
    }
    public boolean addDiscount(int supplierNumber,int catalogNumber,int count,double discount){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }

        if(catalogNumber<0){
            return false;
        }
        if(supplierController.getSupplier(supplierNumber).getProduct(catalogNumber)==null){
            return false;
        }
        if(count<=0){
            return false;
        }
        if(discount<=0|discount>=1){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).addDiscount(count, discount);
    }
    public boolean addDiscount(int supplierNumber,int count,double discount){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }

        if(count<=0|discount<=0|discount>=1){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).addDiscount(count, discount);
    }
    public boolean removeDiscountOnProduct(int supplierNumber,int catalogNumber,int count){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }

        if(supplierController.getSupplier(supplierNumber).getProduct(catalogNumber)==null){
            return false;
        }
        if(count<=0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).removeDiscountOnProduct(count);
    }
    public boolean removeDiscountOnAmount(int supplierNumber,int count){
        if( supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }

        if(count<=0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).removeDiscountOnAmount(count);
    }

    public String getSupplier(int supplierNumber) {
        Gson gson = new Gson();
        Supplier s = supplierController.getSupplier(supplierNumber);
//        String json = "{ \"supplierNumber\": " +supplierNumber + ", \"name\": "+ s.getName();
        String json = gson.toJson(s);
        return  json;

    }

    public boolean updateDeliveration(PresentationLayer.Supplier s,boolean isDeliver) {
        if(supplierController.getSupplier(s.getSupplierNumber())==null){
            return false;
        }
        if(!supplierController.getSupplier(s.getSupplierNumber()).isActive()){
            return false;
        }
        return supplierController.getSupplier(s.getSupplierNumber()).updateDeliveration(isDeliver);
    }

    public String  watchPastOrders(int supplierNumber) {
     List<PastOrder> pastOrderList=new ArrayList<>();
     supplierController.getSupplier(supplierNumber).getFinalOrders();
     return gson.toJson(pastOrderList);
    }
//    public void addContact(int supplierNumber,String name,String email){
//        supplierController.getSupplier(supplierNumber).
//    }
}

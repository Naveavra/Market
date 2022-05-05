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

    /**
     *the function open an account to a new supplier
     *@param supplierNumber  the id of the supplier
     *@param supplierName  the name of the supplier
     *@param bankAccount  the bunk number of the supplier
     *@param contacts  dictionary between name of contact and email
     *@param isDeliver  boolean var which specify who is responsible for delivery the SUPERLI or the supplier
     *@return true if succeed, false if failed
     */

    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts,boolean isDeliver){
        if(supplierNumber<=0){
            return false;
        }
        return supplierController.openAccount(supplierNumber, supplierName,bankAccount,contacts,isDeliver);
    }

    /**
     * the function close an existing supplier account
     * @param supplierNumber the id of the supplier
     * @return true if succeed, false if failed
     */
    public boolean closeAccount(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        return supplierController.closeAccount(supplierNumber);
    }

    /**
     * the function update the supplier details
     * @param supplierNumber the id of the supplier
     * @param supplierName the name of the supplier
     * @param bankAccount the bank account number of the supplier
     * @param contacts dictionary between name of contact and his email
     * @return
     */
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

    /**
     * add discount by amount on a specific product to a specific supplier
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param count number of specific product
     * @param discount amount of discount -> (0,1) // like 0.2,0.5,0.66,...
     * @return true if succeed, false if failed
     */
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

    /**
     * add discount to a specific supplier by amount on sum of product in order
     * @param supplierNumber the id of the supplier
     * @param count number of products in order
     * @param discount amount of discount -> (0,1) // like 0.2,0.5,0.66,...
     * @return true if succeed, false if failed
     */
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

    /**
     * the function removes existing discount on amount of specific product to specific supplier
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param count number of specific product which there is a discount on
     * @return true is succeed, false is failed
     */
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

    /**
     * the function removes existing discount on sum of products in order to a specific supplier
     * @param supplierNumber the id of the supplier
     * @param count quantity of products on order which have a discount
     * @return true if succeed,false if failed
     */
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
        return gson.toJson(s);

    }

    /**
     * the function updates the responsibility of the transports to a particular supplier
     * @param supplierNumber the id of the supplier
     * @param isDeliver boolean var which
     * @return A Boolean variable that defines who is responsible for transports, true -the supplier,false - SUPERLI
     */
    public boolean updateDeliveration(int supplierNumber,boolean isDeliver) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).updateDeliveration(isDeliver);
    }

    /**
     * the function gets the past orders (Those sent) of a supplier
     * @param supplierNumber the id of the supplier
     * @return Json string which wrappers the list of past order
     */
    public String  watchPastOrders(int supplierNumber) {
        List<PastOrder> pastOrderList=new ArrayList<>();
        pastOrderList=supplierController.getSupplier(supplierNumber).getFinalOrders();
        return gson.toJson(pastOrderList);
    }
//    public void addContact(int supplierNumber,String name,String email){
//        supplierController.getSupplier(supplierNumber).
//    }
}

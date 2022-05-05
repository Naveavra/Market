package DomainLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierController {
    private static Map<Integer,Supplier> suppliers = new HashMap<>();

    /**
     * the function gets a supplier number and return the supplier with the same supplier number
     * @param supplierNumber the id of the supplier
     * @return supplier
     */
    public Supplier getSupplier(Integer supplierNumber) {
        return suppliers.get(supplierNumber);
    }

    /**
     * the function open an account to a new supplier
     * @param supplierNumber  the id of the supplier
     * @param supplierName  the name of the supplier
     * @param bankAccount  the bunk number of the supplier
     * @param contacts  dictionary between name of contact and email
     * @param isDeliver  boolean var which specify who is responsible for delivery the SUPERLI or the supplier
     * @return true if succeed, false if failed
     */
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts,boolean isDeliver){
        if(supplierNumber<0){
            return false;
        }
        if(bankAccount<0){
            return false;
        }
        if(suppliers.containsKey(supplierNumber)){
            return false;
        }
        Supplier s = new Supplier(supplierNumber,supplierName, bankAccount, contacts,isDeliver);
        suppliers.put(supplierNumber, s);
        return true;
    }

    /**
     * the function close the account to an existing supplier account
     * @param supplierNumber the id of the supplier
     * @return true if succeed, false if failed
     */
    public boolean closeAccount(int supplierNumber){
        if(supplierNumber<0){
            return false;
        }
        if(!suppliers.containsKey(supplierNumber)){
            return false;
        }
        if(!suppliers.get(supplierNumber).isActive()){
            return false;
        }
        suppliers.get(supplierNumber).closeAccount();
        return true;
    }

}

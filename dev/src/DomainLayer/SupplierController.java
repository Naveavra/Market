package DomainLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierController {
    private static Map<Integer,Supplier> suppliers = new HashMap<>();

    public Supplier getSupplier(Integer supplierNumber){
        return suppliers.get(supplierNumber);
    }
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        if(supplierNumber<0){
            return false;
        }
        if(bankAccount<0){
            return false;
        }
        if(suppliers.containsKey(supplierNumber)){
            return false;
        }
        Supplier s = new Supplier(supplierNumber,supplierName, bankAccount, contacts);
        suppliers.put(supplierNumber, s);
        return true;
    }
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

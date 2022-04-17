package DomainLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierController {
    private Map<Integer,Supplier> suppliers = new HashMap<>();

    public Supplier getSupplier(Integer supplierNumber){
        return suppliers.get(supplierNumber);
    }
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        return true;
    }
    public boolean closeAccount(int supplierNumber){
        return true;
    }
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        return true;
    }

}

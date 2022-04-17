package ServiceLayer;

import DomainLayer.Supplier;
import DomainLayer.SupplierController;

import java.util.Map;

public class SupplierService {
    private SupplierController supplierController =new SupplierController();
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        return supplierController.openAccount(supplierNumber, supplierName,bankAccount,contacts);
    }
    public boolean closeAccount(int supplierNumber){
        return supplierController.closeAccount(supplierNumber);
    }
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        return supplierController.updateAccount(supplierNumber,supplierName,bankAccount,contacts );
    }
    public boolean addDiscount(int supplierNumber,int productId,int count,double discount){
        return
    }
}

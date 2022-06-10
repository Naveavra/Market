package DomainLayer.Suppliers;

import DAL.SuppliersDAO;

import java.util.*;

public class SupplierController {
    //private static Map<Integer,Supplier> suppliers = new HashMap<>();
    private SuppliersDAO suppliersDAO = new SuppliersDAO();

    /**
     * the function gets a supplier number and return the supplier with the same supplier number
     * @param supplierNumber the id of the supplier
     * @return supplier
     */
    public Supplier getSupplier(Integer supplierNumber) {
        try {
            return suppliersDAO.getSupplier(supplierNumber);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * the function open an account to a new supplier
     * @param supplierNumber  the id of the supplier
     * @param supplierName  the name of the supplier
     * @param bankAccount  the bunk number of the supplier
     *@param days  string array which specify on which days the supplier regularly supplies products
     * @return true if succeed, false if failed
     */
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount,  String[] days,int area){
        if(supplierNumber<0){
            return false;
        }
        if(bankAccount<0){
            return false;
        }
        try {
            if(suppliersDAO.getSupplier(supplierNumber) != null){
                return false;
            }
            Supplier s = new Supplier(supplierNumber,supplierName, bankAccount,new LinkedList<Contact>(),days,area,true);
            suppliersDAO.insertSupplier(s);
        }
        catch (Exception e){
            return false;
        }
        //suppliers.put(supplierNumber, s);
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
        try {
            Supplier s = suppliersDAO.getSupplier(supplierNumber);
            if (s == null) {
                return false;
            }
            if (!s.isActive()) {
                return false;
            }
            s.closeAccount();
            //suppliersDAO.updateSupplier(s);
            suppliersDAO.closeAccount(s);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}

package ServiceLayer;


import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Suppliers.SupplierController;
import com.google.gson.Gson;

public class SupplierService {
    private SupplierController supplierController =new SupplierController();
    private Gson gson=new Gson();
    private FacadeSupplier_Storage facadeSupplier;

    public SupplierService(){
        facadeSupplier = new FacadeSupplier_Storage();
    }

    /**
     *the function open an account to a new supplier
     *@param supplierNumber  the id of the supplier
     *@param supplierName  the name of the supplier
     *@param bankAccount  the bunk number of the supplier
     *@param days  string array which specify on which days the supplier regularly supplies products
     *@return true if succeed, false if failed
     */
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, String[] days,int area){
        return facadeSupplier.openAccount(supplierNumber, supplierName, bankAccount,days,area);
    }

    /**
     * the function close an existing supplier account
     * @param supplierNumber the id of the supplier
     * @return true if succeed, false if failed
     */
    public boolean closeAccount(int supplierNumber){
        return facadeSupplier.closeAccount(supplierNumber);
    }

    /**
     * the function update the supplier details
     * @param supplierNumber the id of the supplier
     * @param supplierName the name of the supplier
     * @param bankAccount the bank account number of the supplier
     */
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount){
        return facadeSupplier.updateAccount(supplierNumber, supplierName, bankAccount);
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
        return facadeSupplier.addDiscount(supplierNumber, catalogNumber, count, discount);
    }

    /**
     * add discount to a specific supplier by amount on sum of product in order
     * @param supplierNumber the id of the supplier
     * @param count number of products in order
     * @param discount amount of discount -> (0,1) // like 0.2,0.5,0.66,...
     * @return true if succeed, false if failed
     */
    public boolean addDiscount(int supplierNumber,int count,double discount){
        return facadeSupplier.addDiscount(supplierNumber, count, discount);
    }

    /**
     * the function removes existing discount on amount of specific product to specific supplier
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param count number of specific product which there is a discount on
     * @return true is succeed, false is failed
     */
    public boolean removeDiscountOnProduct(int supplierNumber,int catalogNumber,int count){
        return facadeSupplier.removeDiscountOnProduct(supplierNumber, catalogNumber, count);
    }

    /**
     * the function removes existing discount on sum of products in order to a specific supplier
     * @param supplierNumber the id of the supplier
     * @param count quantity of products on order which have a discount
     * @return true if succeed,false if failed
     */
    public boolean removeDiscountOnAmount(int supplierNumber,int count){
        return facadeSupplier.removeDiscountOnAmount(supplierNumber, count);
    }

    public String getSupplier(int supplierNumber) {
        return facadeSupplier.getSupplier(supplierNumber);
    }

    /**
     * the function updates the responsibility of the transports to a particular supplier
     * @param supplierNumber the id of the supplier
     *@param days  string array which specify on which days the supplier regularly supplies products
     * @return A Boolean variable that defines who is responsible for transports, true -the supplier,false - SUPERLI
     */
    public boolean updateDeliveration(int supplierNumber,String[] days) {
        return facadeSupplier.updateDeliveration(supplierNumber, days);
    }

    public boolean addContact(int supplierNumber,String name, String email, String telephone) {
        return facadeSupplier.addContact(supplierNumber,name,email,telephone);
    }

    public boolean updateContact(int supplierNumber, String name, String email, String telephone) {
        return facadeSupplier.updateContact(supplierNumber, name, email, telephone);
    }
//    public void addContact(int supplierNumber,String name,String email){
//        supplierController.getSupplier(supplierNumber).
//    }
}

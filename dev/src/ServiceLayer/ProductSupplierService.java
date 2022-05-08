package ServiceLayer;

import DomainLayer.Facade;

public class ProductSupplierService {
    private Facade facade;

    public ProductSupplierService(){
        facade = new Facade();
    }

    /**
     * the function adds a new product to a supplier to supply
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param name the name of the new product
     * @param price the price of the product
     * @param productId unique number to product in the system
     * @return true if succeed, false if failed
     */
    public boolean addProduct(int supplierNumber, int catalogNumber, String name, int price, int productId){
       return facade.addProduct(supplierNumber, catalogNumber, name, price, productId);
    }

    /**
     * the function return the products which the supplier supply
     * @param supplierNumber the id of the supplier
     * @return gson which wrappers the dictionary<catalog number,product>
     */
    public String getProductsOfSupplier(int supplierNumber){
        return facade.getProductsOfSupplier(supplierNumber);
    }

    /**
     * the function update the fields of the product
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param name the new name (if u want to change)
     * @param price the new price (>0) (if u want to change)
     * @return true if succeed, false if failed
     */
    public boolean updateProduct(int supplierNumber, int catalogNumber, String name, int price){
     return facade.updateProduct(supplierNumber, catalogNumber, name, price);
    }

    /**
     * the function removes a product from supplier to supply
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @return true if succeed, false if failed
     */
    public boolean removeProduct(int supplierNumber, int catalogNumber){
        return facade.removeProduct(supplierNumber, catalogNumber);
    }
}

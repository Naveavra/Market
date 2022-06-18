package ServiceLayer;

import DomainLayer.FacadeSupplier_Storage;

public class ProductSupplierService {
    private FacadeSupplier_Storage facadeSupplier;

    public ProductSupplierService(){
        facadeSupplier = new FacadeSupplier_Storage();
    }

    /**
     * the function adds a new product to a supplier to supply
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param price the price of the product
     * @param productId unique number to product in the system
     * @return true if succeed, false if failed
     */
    public boolean addProduct(int supplierNumber, int catalogNumber, int daysUntilExpiration, int price, int productId){
       return facadeSupplier.addProductToSupplier(supplierNumber, catalogNumber, daysUntilExpiration, price, productId);
    }

    /**
     * the function return the products which the supplier supply
     * @param supplierNumber the id of the supplier
     * @return gson which wrappers the dictionary<catalog number,product>
     */
    public String getProductsOfSupplier(int supplierNumber){
        return facadeSupplier.getProductsOfSupplier(supplierNumber);
    }

    /**
     * the function update the fields of the product
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @param price the new price (>0) (if u want to change)
     * @return true if succeed, false if failed
     */
    public boolean updateProduct(int supplierNumber, int catalogNumber,int productId, int price){
        return facadeSupplier.updateProduct(supplierNumber, catalogNumber,productId, price);
    }

    /**
     * the function removes a product from supplier to supply
     * @param supplierNumber the id of the supplier
     * @param catalogNumber unique number of the product in specific supplier
     * @return true if succeed, false if failed
     */
    public boolean removeProduct(int supplierNumber, int catalogNumber){
        return facadeSupplier.removeProduct(supplierNumber, catalogNumber);
    }
}

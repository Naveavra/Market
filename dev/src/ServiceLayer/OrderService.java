package ServiceLayer;

import DomainLayer.*;


public class OrderService {

    private Facade facade;

    public OrderService(){
        facade=new Facade();
    }
    public String createOrder(int supplierNumber){
        return facade.createOrder(supplierNumber);
    }

    /**
     * the function adds product to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param catalogNUmber unique number of the product in specific supplier
     * @param count how much to add
     * @return true if succeed, false if failed
     */
    public boolean addProductToOrder(int supplierNumber,int orderId , int catalogNUmber, int count){
        return facade.addProductToOrder(supplierNumber,orderId,catalogNUmber,count);
    }

    /**
     * the function update a product to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param catalogNUmber unique number of the product in specific supplier
     * @param newCount the new number of product in the order
     * @return true if succeed, false if failed
     */
    public boolean updateProductInOrder(int supplierNumber,int orderId, int catalogNUmber, int newCount){
        return facade.updateProductInOrder(supplierNumber,orderId,catalogNUmber,newCount);
    }
    /**
     * the function remove a product to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param catalogNUmber unique number of the product in specific supplier
     * @return true if succeed, false if failed
     */
    public boolean deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){
        return facade.deleteProductFromOrder(supplierNumber, orderId, catalogNUmber);
    }

    /**
     * the function return the active orders(which has not benn sent) of a supplier
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the list of the active order
     */
    public String getActiveOrders(int supplierNumber){
      return facade.getActiveOrders(supplierNumber);
    }

    /**
     * the function gets the delivery days for each active order
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the dictionary<order id,days[]> of the delivery days of each active order
     */
    public String getFixedDaysOrders(int supplierNumber){
       return facade.getFixedDaysOrders(supplierNumber);
    }

    /**
     * the function send the order to the supplier system( hasn't been Implemented yet)
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @return true is succeed, false if failed
     */
    public boolean sendOrder(int supplierNumber, int orderId){
      return facade.sendOrder(supplierNumber, orderId);
    }

    /**
     * the function get the order from the supplier
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @return gson string which wrappers the order
     */
    public String getOrder(int supplierNumber, int orderId) {
      return facade.getOrder(supplierNumber, orderId);
    }

    /**
     * the function return the products in the order of the supplier
     * @param supplierNumber the id of the supplier
     * @param orderID the id of the supplier
     * @return gson which wrappers the dictionary<product,amount>
     */
    public String getProductsInOrder(int supplierNumber, int orderID) {
        return facade.getProductsInOrder(supplierNumber, orderID);
    }
}

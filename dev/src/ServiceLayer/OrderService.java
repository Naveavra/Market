package ServiceLayer;

import DomainLayer.FacadeSupplier;


public class OrderService {

    private FacadeSupplier facadeSupplier;

    public OrderService(){
        facadeSupplier =new FacadeSupplier();
    }
    public String createOrder(int supplierNumber){
        return facadeSupplier.createOrder(supplierNumber);
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
        return facadeSupplier.addProductToOrder(supplierNumber,orderId,catalogNUmber,count);
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
        return facadeSupplier.updateProductInOrder(supplierNumber,orderId,catalogNUmber,newCount);
    }
    /**
     * the function remove a product to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param catalogNUmber unique number of the product in specific supplier
     * @return true if succeed, false if failed
     */
    public boolean deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){
        return facadeSupplier.deleteProductFromOrder(supplierNumber, orderId, catalogNUmber);
    }

    /**
     * the function return the active orders(which has not benn sent) of a supplier
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the list of the active order
     */
    public String getActiveOrders(int supplierNumber){
      return facadeSupplier.getActiveOrders(supplierNumber);
    }

    /**
     * the function gets the delivery days for each active order
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the dictionary<order id,days[]> of the delivery days of each active order
     */
    public String getFixedDaysOrders(int supplierNumber){
       return facadeSupplier.getFixedDaysOrders(supplierNumber);
    }

    /**
     * the function send the order to the supplier system( hasn't been Implemented yet)
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @return true is succeed, false if failed
     */
    public boolean sendOrder(int supplierNumber, int orderId){
      return facadeSupplier.sendOrder(supplierNumber, orderId);
    }
    public String getOrderDetails(int orderId) {
        return facadeSupplier.getOrderTotalDetails(orderId);
    }

    /**
     * the function get the order from the supplier
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @return gson string which wrappers the order
     */
    public String getOrder(int supplierNumber, int orderId) {
      return facadeSupplier.getOrder(supplierNumber, orderId);
    }

    /**
     * the function return the products in the order of the supplier
     * @param supplierNumber the id of the supplier
     * @param orderID the id of the supplier
     * @return gson which wrappers the dictionary<product,amount>
     */
    public String getProductsInOrder(int supplierNumber, int orderID) {
        return facadeSupplier.getProductsInOrder(supplierNumber, orderID);
    }

    /**
     * the function add delivery days to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param daysInWeek which days to deliver (friday,monday..)
     * @return true if succeed, false if failed
     */
    public boolean addFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        return facadeSupplier.addFixedDeliveryDaysForOrder(supplierNumber, orderId, daysInWeek);
    }

    /**
     * the function update delivery days to an existing order
     *  @param supplierNumber  the id of the supplier
     *  @param orderId the id of the order
     *  @param daysInWeek which days to deliver (friday,monday..)
     * @return true if succeed, false if failed
     */
    public boolean updateFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        return facadeSupplier.updateFixedDeliveryDaysForOrder(supplierNumber, orderId, daysInWeek);
    }

    /**
     * the function gets the past orders (Those sent) of a supplier
     * @param supplierNumber the id of the supplier
     * @return Json string which wrappers the list of past order
     */
    public String getPastOrders(int supplierNumber) {
        return facadeSupplier.watchPastOrders(supplierNumber);
    }
}

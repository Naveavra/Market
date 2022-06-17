package ServiceLayer;

import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.PastOrderSupplier;
import PresentationLayer.Suppliers.DeliveryTerm;
import PresentationLayer.Suppliers.Order;
import PresentationLayer.Suppliers.PastOrder;

import java.util.HashMap;
import java.util.Map;


public class OrderService {

    private FacadeSupplier_Storage facadeSupplier;

    public OrderService(){
        facadeSupplier =new FacadeSupplier_Storage();
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
    public Map<Integer, Order> getActiveOrders(int supplierNumber){
        Map<Integer, Order> out =new HashMap<>();
        Map<Integer, OrderFromSupplier> from =facadeSupplier.getActiveOrders(supplierNumber);
        for(Integer x: from.keySet()){
            DeliveryTerm d =new DeliveryTerm(from.get(x).getDaysToDeliver().getDaysInWeeks());
            out.put(x,new Order(from.get(x).getOrderId(),d,from.get(x).getSupplierNumber()));
        }
      return out;
    }
    public Map<Integer, Order> getActiveOrders(){
        Map<Integer, Order> out =new HashMap<>();
        Map<Integer, OrderFromSupplier> from =facadeSupplier.getActiveOrders();
        for(Integer x: from.keySet()){
            DeliveryTerm d =new DeliveryTerm(from.get(x).getDaysToDeliver().getDaysInWeeks());
            out.put(x,new Order(from.get(x).getOrderId(),d,from.get(x).getSupplierNumber()));
        }
        return out;
    }

    /**
     * the function gets the delivery days for each active order
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the dictionary<order id,days[]> of the delivery days of each active order
     */
    public Map<Integer,DeliveryTerm> getFixedDaysOrders(int supplierNumber){
        Map<Integer,DeliveryTerm> out =new HashMap<>();
        Map<Integer, DomainLayer.Suppliers.DeliveryTerm> from = facadeSupplier.getFixedDaysOrders(supplierNumber);
        for(Integer x :from.keySet()){
            String days =getDays(from.get(x).getDaysInWeeks());
            DeliveryTerm d =new DeliveryTerm(days);
            out.put(x,d);
        }
       return out ;
    }

    private String getDays(DomainLayer.Suppliers.DeliveryTerm.DaysInWeek[] daysInWeeks) {
        String out="";
        for(DomainLayer.Suppliers.DeliveryTerm.DaysInWeek d:daysInWeeks){
            out+= DomainLayer.Suppliers.DeliveryTerm.getDayAsString(d);
        }
        return out;
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
    public Map<Integer, PastOrder> getPastOrders(int supplierNumber) {
        Map<Integer, PastOrder> out =new HashMap<>();
        Map<Integer,PastOrderSupplier> from =facadeSupplier.watchPastOrders(supplierNumber);
        for(Integer x:from.keySet()){
            DeliveryTerm d =new DeliveryTerm(from.get(x).getDaysToDeliver().getDaysInWeeks());
            Order o =new Order(from.get(x).getOrderId(), d,from.get(x).getSupplierNumber());
            PastOrder p =new PastOrder(o,from.get(x).getTotalPrice());
            out.put(x,p);
        }
        return out;
    }

    public Boolean cancelOrder(int orderId) {
        return facadeSupplier.cancelOrder(orderId);
    }
}

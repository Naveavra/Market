package ServiceLayer;

import DomainLayer.DeliveryTerm;
import DomainLayer.OrderFromSupplier;
import DomainLayer.Product;
import DomainLayer.SupplierController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private SupplierController supplierController;
    private Gson gson;

    public OrderService(){
        supplierController = new SupplierController();
        gson = new Gson();
    }
    public String createOrder(int supplierNumber){
        return gson.toJson(supplierController.getSupplier(supplierNumber).createOrder());
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
        Product p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        if(p==null){
            return false;
        }
        boolean ans = supplierController.getSupplier(supplierNumber).getOrder(orderId).updateProductToOrder(p, count);
        return ans;
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
        Product p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        boolean ans = supplierController.getSupplier(supplierNumber).getOrder(orderId).updateProductToOrder(p, newCount);
        return ans;
    }
    /**
     * the function remove a product to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param catalogNUmber unique number of the product in specific supplier
     * @return true if succeed, false if failed
     */
    public boolean deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){
        Product p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        return supplierController.getSupplier(supplierNumber).getOrder(orderId).removeProductFromOrder(p);
    }

    /**
     * the function return the active orders(which has not benn sent) of a supplier
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the list of the active order
     */
    public String getActiveOrders(int supplierNumber){
        Map<Integer, OrderFromSupplier> orders = supplierController.getSupplier(supplierNumber).getActiveOrders();
        List<OrderFromSupplier> orders1=new ArrayList<>(orders.values());
        return gson.toJson(orders);
    }

    /**
     * the function gets the delivery days for each active order
     * @param supplierNumber the id of the supplier
     * @return gson string which wrappers the dictionary<order id,days[]> of the delivery days of each active order
     */
    public String getFixedDaysOrders(int supplierNumber){
        Map<Integer, OrderFromSupplier> orders = supplierController.getSupplier(supplierNumber).getActiveOrders();
        Map<Integer, DeliveryTerm> deliveryDays=new HashMap<>();
        for(Integer o: orders.keySet()){
            if(!orders.get(o).getDaysToDeliver().isEmpty()){
                deliveryDays.put(orders.get(o).getOrderId(),orders.get(o).getDaysToDeliver());
            }
        }
        return gson.toJson(deliveryDays);
    }

    /**
     * the function send the order to the supplier system( hasn't been Implemented yet)
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @return true is succeed, false if failed
     */
    public boolean sendOrder(int supplierNumber, int orderId){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(supplierController.getSupplier(supplierNumber).getOrder(orderId)==null){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).finishOrder(orderId);
    }

    /**
     * the function get the order from the supplier
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @return gson string which wrappers the order
     */
    public String getOrder(int supplierNumber, int orderId) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        if(supplierController.getSupplier(supplierNumber).getOrder(orderId)==null){
            return "fail";
        }
        OrderFromSupplier o = supplierController.getSupplier(supplierNumber).getOrder(orderId);
        return gson.toJson(o);
    }

    /**
     * the function return the products in the order of the supplier
     * @param supplierNumber the id of the supplier
     * @param orderID the id of the supplier
     * @return gson which wrappers the dictionary<product,amount>
     */
    public String getProductsInOrder(int supplierNumber, int orderID) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        if(supplierController.getSupplier(supplierNumber).getOrder(orderID)==null){
            return "fail";
        }
        Map<Product,Integer> products = supplierController.getSupplier(supplierNumber).getOrder(orderID).getProducts();
        Map<String,Integer> prouductsJson = new HashMap<>();
        for (Map.Entry<Product,Integer> e: products.entrySet()){
            Product p = e.getKey();
            prouductsJson.put(gson.toJson(p),e.getValue());
        }
        return gson.toJson(prouductsJson);
    }
}

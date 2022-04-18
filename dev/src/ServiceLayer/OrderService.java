package ServiceLayer;

import DomainLayer.Order;
import DomainLayer.Product;
import DomainLayer.SupplierController;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.Map;

public class OrderService {

    private SupplierController supplierControler;
    private Gson gson;

    public OrderService(){
        supplierControler = new SupplierController();
        gson = new Gson();
    }
    public String createOrder(int supplierNumber){
        return gson.toJson(supplierControler.getSupplier(supplierNumber).createOrder());
    }

    public boolean addProductToOrder(int supplierNumber,int orderId , int catalogNUmber, int count){
        Product p = supplierControler.getSupplier(supplierNumber).getProduct(catalogNUmber);
        boolean ans = supplierControler.getSupplier(supplierNumber).getOrder(orderId).updateProductToOrder(p, count);
        return ans;
    }

    public boolean updateProductInOrder(int supplierNumber,int orderId, int catalogNUmber, int newCount){
        Product p = supplierControler.getSupplier(supplierNumber).getProduct(catalogNUmber);
        boolean ans = supplierControler.getSupplier(supplierNumber).getOrder(orderId).updateProductToOrder(p, newCount);
        return ans;
    }

    public boolean deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){
        Product p = supplierControler.getSupplier(supplierNumber).getProduct(catalogNUmber);
        boolean ans = supplierControler.getSupplier(supplierNumber).getOrder(orderId).removeProductFromOrder(p);
        return ans;
    }

    public String getActiveOrders(int supplierNumber){
        Map<Integer,Order> orders = supplierControler.getSupplier(supplierNumber).getActiveOrders();
        return gson.toJson(orders);
    }

    public String getFixedDaysOrders(int supplierNumber){

        return null;
    }

    public String sendOrder(int supplierNumber, int orderId, boolean isSupplierDeliver){
        return null;
    }

    public String getOrder(int supplierNumber, int orderId) {
        Order o = supplierControler.getSupplier(supplierNumber).getOrder(orderId);
        return gson.toJson(o);
    }

    public String getProductsInOrder(int supplierNumber, int orderID) {

        return gson.toJson(supplierControler.getSupplier(supplierNumber).getOrder(orderID).getProducts());
    }
}

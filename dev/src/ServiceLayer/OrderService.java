package ServiceLayer;

import DomainLayer.Order;
import DomainLayer.PastOrder;
import DomainLayer.Product;
import DomainLayer.SupplierController;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public boolean addProductToOrder(int supplierNumber,int orderId , int catalogNUmber, int count){
        Product p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        if(p==null){
            return false;
        }
        boolean ans = supplierController.getSupplier(supplierNumber).getOrder(orderId).updateProductToOrder(p, count);
        return ans;
    }

    public boolean updateProductInOrder(int supplierNumber,int orderId, int catalogNUmber, int newCount){
        Product p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        boolean ans = supplierController.getSupplier(supplierNumber).getOrder(orderId).updateProductToOrder(p, newCount);
        return ans;
    }

    public boolean deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){
        Product p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        return supplierController.getSupplier(supplierNumber).getOrder(orderId).removeProductFromOrder(p);
    }

    public String getActiveOrders(int supplierNumber){
        Map<Integer,Order> orders = supplierController.getSupplier(supplierNumber).getActiveOrders();
//        String[] arr=new String[orders.size()];
//        StringBuilder out= new StringBuilder();
//        int i=0;
//        for(Order o: orders.values()){
//            StringBuilder tmp= new StringBuilder();
//            tmp = new StringBuilder(String.valueOf(o.getOrderId()) + "\n");
//            tmp.append(o.getDate());
//            for(Product x:o.getProducts().keySet()){
//                tmp.append("\n\t product: ").append(x.getName()).append(" count: ").append(o.getProducts().get(x).toString());
//            }
//        }
//        for (String s : arr) {
//            out.append(s);
//        }
        List<Order> orders1=new ArrayList<>(orders.values());

        return gson.toJson(orders1);
        //return gson.toJson(out.toString());
    }

    public String getFixedDaysOrders(int supplierNumber){

        return null;
    }

    public boolean sendOrder(int supplierNumber, int orderId, boolean isSupplierDeliver){
        return supplierController.getSupplier(supplierNumber).finishOrder(orderId);

    }

    public String getOrder(int supplierNumber, int orderId) {
        Order o = supplierController.getSupplier(supplierNumber).getOrder(orderId);
        return gson.toJson(o);
    }

    public String getProductsInOrder(int supplierNumber, int orderID) {

        return gson.toJson(supplierController.getSupplier(supplierNumber).getOrder(orderID).getProducts());
    }
}

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
        Map<Integer, OrderFromSupplier> orders = supplierController.getSupplier(supplierNumber).getActiveOrders();
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
        List<OrderFromSupplier> orders1=new ArrayList<>(orders.values());

        return gson.toJson(orders);
        //return gson.toJson(out.toString());
    }

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

    public boolean sendOrder(int supplierNumber, int orderId){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(supplierController.getSupplier(supplierNumber).getOrder(orderId)==null){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).finishOrder(orderId);
    }

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

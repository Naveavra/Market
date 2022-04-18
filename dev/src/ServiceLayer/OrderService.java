package ServiceLayer;

import DomainLayer.Product;
import DomainLayer.SupplierController;
import PresentationLayer.Order;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.Map;

public class OrderService {

    private SupplierController supplierControler;

    public String createOrder(int supplierNumber){
        Gson gson = new Gson();
        //return gson.toJson(supplierControler.getSupplier(supplierNumber).createOrder());
        return "";
    }

    public String addProductToOrder(int supplierNumber, int catalogNUmber, int count){

        return null;
    }

    public String updateProductInOrder(int supplierNumber,int orderId, int catalogNUmber, int newCount){

        return null;
    }

    public String deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){

        return null;
    }

    public String getActiveOrders(int supplierNumber){

        return null;
    }

    public String getFixedDaysOrders(int supplierNumber){

        return null;
    }

    public JSONObject sendOrder(int orderId, boolean isSupplierDeliver){

        return null;
    }

    public Order getOrder(int orderID) {
        return null;
    }

    public String getProductsInOrder(int orderID) {
        return null;
    }
}

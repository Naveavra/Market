package ServiceLayer;

import DomainLayer.SupplierController;
import org.json.JSONObject;

public class OrderService {

    private SupplierController supplierControler;

    public boolean createOrder(int supplierNumber){
        supplierControler.getSupplier(supplierNumber).createOrder();
        return true;
    }

    public JSONObject addProductToOrder(int supplierNumber, int catalogNUmber){

        return null;
    }

    public JSONObject sendOrder(int orderId, boolean isSupplierDeliver){

        return null;
    }
}

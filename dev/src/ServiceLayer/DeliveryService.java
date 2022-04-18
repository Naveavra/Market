package ServiceLayer;

import DomainLayer.SupplierController;
import org.json.JSONObject;


public class DeliveryService {
    private SupplierController supplierControler;

    public DeliveryService(){
        supplierControler = new SupplierController();
    }

    public boolean addFixedDeliveryDaysForOrder(int orderId, String[] daysInWeek){


        return false;
    }

    public boolean updateFixedDeliveryDaysForOrder(int orderId, String[] daysInWeek){

        return false;
    }

    public boolean stopFixedDeliveryDaysForOrder(int orderId){

        return false;
    }
}

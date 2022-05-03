package ServiceLayer;

import DomainLayer.SupplierController;
import org.json.JSONObject;


public class DeliveryService {
    private SupplierController supplierController;

    public DeliveryService(){
        supplierController = new SupplierController();
    }

    public boolean addFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
     if(supplierController.getSupplier(supplierNumber)==null){
         return false;
     }
     if(!supplierController.getSupplier(supplierNumber).isActive()){
         return false;
     }
     if(supplierController.getSupplier(supplierNumber).getOrder(orderId)==null){
         return false;
     }
        return supplierController.getSupplier(supplierNumber).getOrder(orderId).addDeliveryDays(daysInWeek);
    }

    public boolean updateFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(supplierController.getSupplier(supplierNumber).getOrder(orderId)==null){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).getOrder(orderId).updateDeliveryDays(daysInWeek);

    }
//
//    public boolean stopFixedDeliveryDaysForOrder(int orderId){
//
//        return false;
//    }
}

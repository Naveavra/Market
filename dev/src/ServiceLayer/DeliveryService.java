package ServiceLayer;

import DomainLayer.SupplierController;
import org.json.JSONObject;


public class DeliveryService {
    private SupplierController supplierController;

    public DeliveryService(){
        supplierController = new SupplierController();
    }

    /**
     * the function add delivery days to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param daysInWeek which days to deliver (friday,monday..)
     * @return true if succeed, false if failed
     */
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

    /**
     * the function update delivery days to an existing order
     *  @param supplierNumber  the id of the supplier
     *  @param orderId the id of the order
     *  @param daysInWeek which days to deliver (friday,monday..)
     * @return true if succeed, false if failed
     */
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

package ServiceLayer;

import DomainLayer.Facade;


public class DeliveryService {
    private Facade facade;

    public DeliveryService(){
        facade=new Facade();
    }

    /**
     * the function add delivery days to an existing order
     * @param supplierNumber the id of the supplier
     * @param orderId the id of the order
     * @param daysInWeek which days to deliver (friday,monday..)
     * @return true if succeed, false if failed
     */
    public boolean addFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        return facade.addFixedDeliveryDaysForOrder(supplierNumber, orderId, daysInWeek);
    }

    /**
     * the function update delivery days to an existing order
     *  @param supplierNumber  the id of the supplier
     *  @param orderId the id of the order
     *  @param daysInWeek which days to deliver (friday,monday..)
     * @return true if succeed, false if failed
     */
    public boolean updateFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        return facade.updateFixedDeliveryDaysForOrder(supplierNumber, orderId, daysInWeek);
    }
}

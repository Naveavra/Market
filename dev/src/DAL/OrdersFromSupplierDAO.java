package DAL;

import DomainLayer.OrderFromSupplier;


import java.util.HashMap;

public class OrdersFromSupplierDAO {
    private Connect connect;
    private static HashMap<Integer, OrderFromSupplier> IMOrdersFromSupplier;//key: orderId

    /**
     * constructor
     */
    public OrdersFromSupplierDAO(){
        connect=Connect.getInstance();
        IMOrdersFromSupplier = new HashMap<>();
    }
}

package DAL;

import DomainLayer.ProductSupplier;
import DomainLayer.Supplier;
import javafx.util.Pair;

import java.util.HashMap;

public class OrdersFromSupplierDAO {
    private Connect connect;
    private static HashMap<Pair<Integer,Integer>, Supplier> IMProductSupplier;

    /**
     * constructor
     */
    public OrdersFromSupplierDAO(){
        connect=Connect.getInstance();
        IMProductSupplier=new HashMap<>();
    }
}

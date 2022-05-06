package DAL;

import DomainLayer.OrderFromSupplier;
import javafx.util.Pair;

import java.util.HashMap;

public class PastOrdersSupplierDAO {
    private Connect connect;
    private static HashMap<Pair<Integer,String>, OrderFromSupplier> IMPastOrdersFromSupplier;//key: orderId and finish date

    /**
     * constructor
     */
    public PastOrdersSupplierDAO(){
        connect=Connect.getInstance();
        IMPastOrdersFromSupplier = new HashMap<>();
    }
}

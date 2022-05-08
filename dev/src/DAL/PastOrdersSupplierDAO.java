package DAL;

import DomainLayer.OrderFromSupplier;
import DomainLayer.PastOrderSupplier;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

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

    public List<PastOrderSupplier> getAllPastOrders(int supplierNumber) {
        return null;
    }

    public void insertPastOrder(PastOrderSupplier pastOrderSupplier) {
    }
}

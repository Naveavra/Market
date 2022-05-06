package DAL;

import DomainLayer.ProductSupplier;
import javafx.util.Pair;

import java.util.HashMap;

public class SupplieresDAO {
    private Connect connect;
    private static HashMap<Integer, ProductSupplier> IMSuppliers;//key: supplierId

    /**
     * constructor
     */
    public SupplieresDAO(){
        connect = Connect.getInstance();
        IMSuppliers = new HashMap<>();
    }
}

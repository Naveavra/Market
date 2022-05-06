package DAL;

import DomainLayer.ProductSupplier;
import javafx.util.Pair;

import java.util.HashMap;

public class SupplieresDAO {
    private Connect connect;
    private static HashMap<Pair<Integer,Integer>, ProductSupplier> IMProductSupplier;

    /**
     * constructor
     */
    public SupplieresDAO(){
        connect=Connect.getInstance();
        IMProductSupplier=new HashMap<>();
    }
}

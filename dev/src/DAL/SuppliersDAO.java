package DAL;

import DomainLayer.ProductSupplier;
import DomainLayer.Supplier;
import javafx.util.Pair;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SuppliersDAO {
    private Connect connect;
    private static HashMap<Integer, Supplier> IMSuppliers;//key: supplierId

    /**
     * constructor
     */
    public SuppliersDAO(){
        connect = Connect.getInstance();
        IMSuppliers = new HashMap<>();
    }

    public void updateSupplier(Supplier s) throws SQLException {

        String query = String.format("UPDATE Suppliers SET name = \"%s\" and bankAccount = %d and active = %d and isDeliver = %d " +
                        "WHERE supplierNumber = %d",
                s.getName(),s.getBankAccount(),s.getActive(), s.getIsDeliver(),
                s.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            for(Map.Entry<Integer,Double> k:s.getDiscounts().entrySet()){
                query = String.format("UPDATE DiscountSupplier ")
            }
            IMSuppliers.put(s.getSupplierNumber(),s);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

}

package DAL;

import DomainLayer.ProductSupplier;
import DomainLayer.Supplier;
import javafx.util.Pair;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SuppliersDAO {
    private Connect connect;
    private static HashMap<Integer, ProductSupplier> IMSuppliers;//key: supplierId

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
                s.getName(),s.getBankAccount(),s.getActive(), s.getIsDelicer(),
                s.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            IMProductSupplier.put(new Pair<>(productSupplier.getProductId(), productSupplier.getCatalogNumber()), productSupplier);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
}

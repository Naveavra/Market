package DAL;

import DomainLayer.ProductSupplier;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ProductSupplierDAO {
    private Connect connect;
    private static HashMap<Pair<Integer,Integer>, ProductSupplier> IMProductSupplier =new HashMap<>();; //key: productID, supplierNumber

    /**
     * constructor
     * the connect is singleton
     */
    public ProductSupplierDAO(){
        connect=Connect.getInstance();
    }
    public void insert(ProductSupplier ps) throws SQLException {
        String query = "INSERT INTO ProductSupplier (catalogNumber,productId,price, supplierNumber)" +
                " VALUES "+String.format("(%d,%d,%f,%d)",ps.getCatalogNumber(),ps.getProductId(),ps.getPrice(), ps.getSupplierNumber());

        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            for(Map.Entry<Integer,Double> k:ps.getDiscount().entrySet()){
                query = "INSERT INTO DiscountProductSupplier (catalogNumber,productId,quantity,discount)"+
                        "VALUES "+String.format("(%d,%d,%d,%f)",ps.getCatalogNumber(),ps.getProductId(),k.getKey().intValue(),k.getValue().doubleValue());
                stmt.execute(query);
            }
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public ProductSupplier getProduct(int supplierNumber,int productId) throws SQLException {
        Pair<Integer,Integer> keySet =new Pair<>(supplierNumber,productId);
        if(IMProductSupplier.containsKey(keySet)) {
            System.out.println("exist in field");
            return IMProductSupplier.get(keySet);
        }
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("supplierNumber=%d and productId=%d", supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            ProductSupplier p = new ProductSupplier(rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"));
            IMProductSupplier.put(new Pair<>(p.getProductId(), p.getCatalogNumber()), p);
            return p;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void updateProduct(ProductSupplier productSupplier) throws SQLException {

        String query = String.format("UPDATE ProductSupplier SET price = %f and catalogNumber = %d WHERE productId = %d and supplierNumber = %d",
                productSupplier.getPrice(),productSupplier.getCatalogNumber(),productSupplier.getProductId(),
                                productSupplier.getSupplierNumber());
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

    public Map<Integer, ProductSupplier> getAllProductsOfSupplier(int supplierNumber) {
        return null;
    }

    public void removeProduct(int catalogNumber, int supplierNumber) {
    }
}


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
    private static HashMap<Pair<Integer,Integer>, ProductSupplier> IMProductSupplier;

    /**
     * constructor
     * the connect is singleton
     */
    public ProductSupplierDAO(){
        connect=Connect.getInstance();
        IMProductSupplier=new HashMap<>();
    }
    public void insert(ProductSupplier ps) throws SQLException {
        String query = "INSERT INTO ProductSupplier (catalogNumber,productId,price)" +
                " VALUES "+String.format("(%d,%d,%f)",ps.getCatalogNumber(),ps.getProductId(),ps.getPrice());

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
    public ProductSupplier get(int catalogNumber,int productId) throws SQLException {
        Pair<Integer,Integer> keySet =new Pair<>(catalogNumber,productId);
        if(IMProductSupplier.containsKey(keySet)) {
            System.out.println("exist in field");
            return IMProductSupplier.get(keySet);
        }
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("catalogNumber=%d and productId=%d", catalogNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            rs.next();
            ProductSupplier p = new ProductSupplier(rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"));
            IMProductSupplier.put(new Pair<>(p.getCatalogNumber(), p.getProductId()), p);
            return p;
        } catch (SQLException throwable) {
            throw throwable;
        }
        finally {
            connect.closeConnect();
        }
    }
}


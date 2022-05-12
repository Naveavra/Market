package DAL;

import DomainLayer.ProductSupplier;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ProductsSupplierDAO {
    private final Connect connect;
    private static HashMap<Pair<Integer,Integer>, ProductSupplier> IMProductSupplier =new HashMap<>(); //key: productID, supplierNumber

    /**
     * constructor
     * the connect is singleton
     */
    public ProductsSupplierDAO(){
        connect=Connect.getInstance();
    }
    public void insert(ProductSupplier ps) throws SQLException {
        String query = String.format("INSERT INTO ProductSupplier (productId,catalogNumber,supplierNumber,price)" +
                " VALUES (%d,%d,%d,%f)",ps.getProductId(),ps.getCatalogNumber(), ps.getSupplierNumber(),ps.getPrice());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            for(Map.Entry<Integer,Double> k:ps.getDiscount().entrySet()){
                query =String.format( "INSERT INTO DiscountProductSupplier (supplierNumber,productId,quantity,discount)"+
                        "VALUES (%d,%d,%d,%f) ",ps.getSupplierNumber(),ps.getProductId(), k.getKey(), k.getValue());
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
            return IMProductSupplier.get(keySet);
        }
        String query = String.format("SELECT * FROM ProductSupplier WHERE supplierNumber = %d and productId = %d "
                ,supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"),rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"),getDiscountsOnProduct(rs.getInt("supplierNumber"),rs.getInt("productId")));
            IMProductSupplier.put(new Pair<>(p.getProductId(), p.getCatalogNumber()), p);
            return p;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void updateProduct(ProductSupplier productSupplier,int newPrice) throws SQLException {
        String query = String.format("UPDATE ProductSupplier SET price = %f WHERE catalogNumber = %d and productId = %d and supplierNumber = %d",
                Float.parseFloat(newPrice+""),productSupplier.getCatalogNumber(),productSupplier.getProductId(),
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

    public Map<Integer, ProductSupplier> getAllProductsOfSupplier(int supplierNumber) throws SQLException {
        String query = String.format("SELECT * FROM ProductSupplier WHERE supplierNumber = %d " ,
                supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            Map<Integer, ProductSupplier> products = new HashMap<>();
            while (rs.next()){
                ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"), rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"),getDiscountsOnProduct(rs.getInt("supplierNumber"),rs.getInt("productId")));

                products.put(p.getProductId(),p);
            }
            return products;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void removeProduct(int catalogNumber, int supplierNumber) throws SQLException {
        String query = String.format("DELETE FROM ProductSupplier WHERE catalogNumber = %d and supplierNumber = %d",
                catalogNumber, supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            Pair<Integer,Integer> key =new Pair<>(catalogNumber, supplierNumber);
            IMProductSupplier.remove(key);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public ProductSupplier getProductByCatalogNumber(int supplierNumber, int catalogNumber) throws SQLException {
        String query =String.format( "SELECT * FROM ProductSupplier WHERE supplierNumber = %d and catalogNumber = %d ",
                supplierNumber, catalogNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"),rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"),getDiscountsOnProduct(rs.getInt("supplierNumber"),rs.getInt("productId")));
            Pair<Integer,Integer> keySet =new Pair<>(supplierNumber,p.getProductId());
            if(IMProductSupplier.containsKey(keySet)) {
                return IMProductSupplier.get(keySet);
            }
            IMProductSupplier.put(new Pair<>(p.getProductId(), p.getCatalogNumber()), p);
            return p;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void insertDiscountOnProduct(ProductSupplier productSupplier, int count, double discount) throws SQLException {
        String query =String.format("INSERT INTO DiscountProductSupplier (supplierNumber,productId,quantity,discount) " +
                "VALUES (%d,%d,%d,%f)",productSupplier.getSupplierNumber(),productSupplier.getProductId(),count,discount);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void removeDiscountOnProduct(ProductSupplier productSupplier, int count) throws SQLException {
        String query =String.format("DELETE from DiscountProductSupplier WHERE supplierNumber = %d and productId = %d and quantity = %d ",
                productSupplier.getSupplierNumber(),productSupplier.getProductId(),count);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public Double getDiscountOnProduct(ProductSupplier productSupplier, int count) throws SQLException {
        String query =String.format("SELECT * FROM DiscountProductSupplier WHERE supplierNumber= %d and productId=%d and quantity <= %d ORDER BY discount ASC LIMIT 1 "
                , productSupplier.getSupplierNumber(), productSupplier.getProductId(),count);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            Double discount =rs.getDouble("discount");
            return discount;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public Map<Integer,Double> getDiscountsOnProduct(int supplierNumber,int productId) throws SQLException {
        String query = String.format("SELECT * FROM DiscountProductSupplier WHERE supplierNumber=%d and productId=%d "
                ,supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            Map<Integer,Double> discounts =new HashMap<>();
            while(rs.next()){
                discounts.put(rs.getInt("quantity"),rs.getDouble("discount"));
            }
            return discounts;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
}


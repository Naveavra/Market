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
            return IMProductSupplier.get(keySet);
        }
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("supplierNumber=%d and productId=%d", supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"),rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"));
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

    public Map<Integer, ProductSupplier> getAllProductsOfSupplier(int supplierNumber) throws SQLException {
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("supplierNumber = %d", supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            Map<Integer, ProductSupplier> products = new HashMap<>();
            while (!rs.next()){
                ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"), rs.getInt("catalogNumber"),rs.getDouble("price"),rs.getInt("productId"));

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
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public ProductSupplier getProductByCatalogNumber(int catalogNumber) throws SQLException {
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("catalogNumber=%d", catalogNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            ProductSupplier ans=null;
            while(!rs.isClosed()) {
                ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"), rs.getInt("catalogNumber"), rs.getDouble("price"), rs.getInt("productId"));
                if(ans==null || ans.getPrice()>p.getPrice())
                    ans=p;
                IMProductSupplier.put(new Pair<>(p.getProductId(), p.getCatalogNumber()), p);
                rs.next();
            }
            return ans;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void insertDiscountOnProduct(ProductSupplier productSupplier, int count, double discount) throws SQLException {
        String query =String.format("INSERT INTO DiscountProductSupplier (supplierNumber,productId,quantity,discount) " +
                "VALUES (%d,%d,%d,%f)",productSupplier.getSupplierNumber(),count,discount);
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
        String query =String.format("DELETE from DiscountProductSupplier WHERE supplierNumber = %d and productId = %d quantity = %d ",
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
        String query = "SELECT * FROM DiscountProductSupplier WHERE " +
                String.format("supplierNumber=%d and productId=%d and quantity=%d", productSupplier.getSupplierNumber(), productSupplier.getProductId(),count);
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
    public Map<Integer,Double> getDiscountsOnProduct(ProductSupplier productSupplier) throws SQLException {
        String query = "SELECT * FROM DiscountProductSupplier WHERE " +
                String.format("supplierNumber=%d and productId=%d", productSupplier.getSupplierNumber(), productSupplier.getProductId());
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

    public int getProductIdByCatalogId(int catalogNumber) throws SQLException {
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("catalogNumber=%d", catalogNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return -1;
            else
                return rs.getInt("productId");
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
}


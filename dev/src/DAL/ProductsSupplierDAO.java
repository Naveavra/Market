package DAL;

import DomainLayer.Supplier.Discount;
import DomainLayer.Supplier.ProductSupplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ProductsSupplierDAO {
    private final Connect connect;
    private static HashMap<HashMap<Integer,Integer>, ProductSupplier> IMProductSupplier =new HashMap<>(); //key: productID, supplierNumber

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
            for(Discount k:ps.getDiscount()){
                query =String.format( "INSERT INTO DiscountProductSupplier (supplierNumber,productId,quantity,discount)"+
                        "VALUES (%d,%d,%d,%f) ",ps.getSupplierNumber(),ps.getProductId(), k.getAmount(), k.getDiscount());
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
        HashMap<Integer,Integer> keySet =new HashMap<>();
        keySet.put(supplierNumber,productId);
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
            IMProductSupplier.put(keySet, p);
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
            HashMap<Integer,Integer> keySet =new HashMap<>();
            keySet.put(productSupplier.getProductId(), productSupplier.getCatalogNumber());
            IMProductSupplier.put(keySet, productSupplier);
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
        String query = String.format("SELECT productId FROM ProductSupplier WHERE catalogNumber = %d and supplierNumber = %d",
                catalogNumber, supplierNumber);
        int productId=0;
        try(Statement stmt = connect.createStatement()){
            ResultSet rs =stmt.executeQuery(query);
            while (rs.next()){
                 productId=rs.getInt("productId");
            }
        }
        String query1 = String.format("DELETE FROM ProductSupplier WHERE catalogNumber = %d and supplierNumber = %d",
                catalogNumber, supplierNumber);
        String query2 =String.format("DELETE FROM DiscountProductSupplier WHERE supplierNumber = %d and productId = %d " ,
                 supplierNumber,productId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query1);
            stmt.execute(query2);
            HashMap<Integer,Integer> key =new HashMap<>();
            key.put(catalogNumber, supplierNumber);
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
            HashMap<Integer,Integer> key =new HashMap<>();
            key.put(supplierNumber,p.getProductId());
            if(IMProductSupplier.containsKey(key)) {
                return IMProductSupplier.get(key);
            }
            IMProductSupplier.put(key, p);
            return p;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public ProductSupplier getMinProductByProductId(int productId, int amount) throws SQLException {
        String query = "SELECT * FROM ProductSupplier WHERE " +
                String.format("productId=%d", productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            ProductSupplier ans=null;
            while(rs.next()) {
                int supplierNumber=rs.getInt("supplierNumber");
                ProductSupplier p = getProduct(supplierNumber, productId);
                if(ans==null || ans.getPriceAfterDiscount(amount)>p.getPriceAfterDiscount(amount))
                    ans=p;
                HashMap<Integer,Integer> key =new HashMap<>();
                key.put(p.getProductId(), p.getCatalogNumber());
                IMProductSupplier.put(key, p);
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
        String query =String.format("SELECT * FROM DiscountProductSupplier WHERE supplierNumber= %d and productId=%d and quantity <= %d ORDER BY discount DESC LIMIT 1 "
                , productSupplier.getSupplierNumber(), productSupplier.getProductId(),count);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            return rs.getDouble("discount");
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public LinkedList<Discount> getDiscountsOnProduct(int supplierNumber, int productId) throws SQLException {
        String query = String.format("SELECT * FROM DiscountProductSupplier WHERE supplierNumber=%d and productId=%d "
                ,supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            LinkedList<Discount> discounts =new LinkedList<>();
            while(rs.next()){
                discounts.add(new Discount(rs.getInt("quantity"),rs.getDouble("discount")));
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


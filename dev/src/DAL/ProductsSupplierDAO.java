package DAL;

import DomainLayer.Suppliers.Discount;
import DomainLayer.Suppliers.ProductSupplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ProductsSupplierDAO {
    private final Connect connect;
    private static HashMap<Integer, HashMap<Integer,ProductSupplier>> IMProductSupplier =new HashMap<>(); //key: productID, supplierNumber

    /**
     * constructor
     * the connect is singleton
     */
    public ProductsSupplierDAO(){
        connect=Connect.getInstance();
    }

    public static void reset() {
        IMProductSupplier =new HashMap<>();
    }

    public void insert(ProductSupplier ps) throws SQLException {
        String query = String.format("INSERT INTO ProductSupplier (productId,catalogNumber,supplierNumber,daysUntilExpiration, price)" +
                " VALUES (%d,%d,%d,%d,%f)",ps.getProductId(),ps.getCatalogNumber(), ps.getSupplierNumber(),ps.getDaysUntilExpiration(), ps.getPrice());
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
        if(IMProductSupplier.containsKey(supplierNumber) && IMProductSupplier.get(supplierNumber).containsKey(productId)) {
            return IMProductSupplier.get(supplierNumber).get(productId);
        }
        String query = String.format("SELECT * FROM ProductSupplier WHERE supplierNumber = %d and productId = %d "
                ,supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next())
                return null;
            ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"),rs.getInt("catalogNumber"),rs.getInt("daysUntilExpiration"),rs.getDouble("price"),rs.getInt("productId"),getDiscountsOnProduct(rs.getInt("supplierNumber"),rs.getInt("productId")));
            HashMap<Integer,ProductSupplier> add = new HashMap<>();
            add.put(productId,p);
            IMProductSupplier.put(supplierNumber, add);
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
            HashMap<Integer,ProductSupplier> add = new HashMap<>();
            add.put(productSupplier.getProductId(),productSupplier);
            IMProductSupplier.put(productSupplier.getSupplierNumber(), add);
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
                ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"), rs.getInt("catalogNumber"),rs.getInt("daysUntilExpiration"),rs.getDouble("price"),rs.getInt("productId"),getDiscountsOnProduct(rs.getInt("supplierNumber"),rs.getInt("productId")));

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
            IMProductSupplier.remove(supplierNumber);
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
            ProductSupplier p = new ProductSupplier(rs.getInt("supplierNumber"),rs.getInt("catalogNumber"),rs.getInt("daysUntilExpiration"),rs.getDouble("price"),rs.getInt("productId"),getDiscountsOnProduct(rs.getInt("supplierNumber"),rs.getInt("productId")));
            HashMap<Integer,Integer> key =new HashMap<>();
            key.put(supplierNumber,p.getProductId());
            if(IMProductSupplier.containsKey(supplierNumber) && IMProductSupplier.get(supplierNumber).containsKey(catalogNumber)) {
                return IMProductSupplier.get(supplierNumber).get(catalogNumber);
            }
            HashMap<Integer,ProductSupplier> add = new HashMap<>();
            add.put(catalogNumber,p);
            IMProductSupplier.put(supplierNumber, add);
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
                    ans = p;
                HashMap<Integer,Integer> key =new HashMap<>();
                HashMap<Integer,ProductSupplier> add = new HashMap<>();
                add.put(productId,p);
                IMProductSupplier.put(supplierNumber, add);
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

    public String getExpirationDate(int supplierNumber, int productId) throws SQLException {
        String query = "SELECT daysUntilExpiration FROM ProductSupplier WHERE " +
                String.format("supplierNumber=%d AND productId=%d", supplierNumber, productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs =stmt.executeQuery(query);
            if (!rs.next()) {
                String curDate = LocalDate.now().toString();
                int curYear = Integer.parseInt(curDate.substring(0, 4)) -1;
                String curMonth = curDate.substring(5, 7);
                String curDay = curDate.substring(8, 10);
                String exp = curYear+"-"+curMonth+"-"+curDay;
                return exp;
            }
            else {
                int daysUntilExpiration = rs.getInt("daysUntilExpiration");
                String curDate = LocalDate.now().toString();
                int curYear = Integer.parseInt(curDate.substring(0, 4)) +daysUntilExpiration/365;
                daysUntilExpiration = daysUntilExpiration % 365;
                int curMonth = Integer.parseInt(curDate.substring(5, 7))+daysUntilExpiration/30;
                daysUntilExpiration = daysUntilExpiration %30;
                int curDay = Integer.parseInt(curDate.substring(8, 10))+daysUntilExpiration;
                curMonth+=curDay/30;
                curDay = curDay%30;
                curYear +=curMonth/12;
                curMonth = curMonth %12;
                String exp;
                if(curDay<10 && curMonth<10)
                    exp = curYear+"-0"+curMonth+"-0"+curDay;
                else if(curMonth < 10)
                    exp = curYear+"-0"+curMonth+"-"+curDay;
                else if(curDay<10)
                    exp = curYear+"-"+curMonth+"-0"+curDay;
                else
                    exp = curYear+"-"+curMonth+"-"+curDay;
                return exp;
            }
        } catch (SQLException e) {
            String curDate = LocalDate.now().toString();
            int curYear = Integer.parseInt(curDate.substring(0, 4)) -1;
            String curMonth = curDate.substring(5, 7);
            String curDay = curDate.substring(8, 10);
            String exp = curYear+"-"+curMonth+"-"+curDay;
            return exp;
        }
        finally {
            connect.closeConnect();
        }
    }
}


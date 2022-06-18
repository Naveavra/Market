package DAL;

import DomainLayer.Suppliers.DeliveryTerm;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.ProductSupplier;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class OrdersFromSupplierDAO {
    private Connect connect;
    private ProductsSupplierDAO productSupplierDAO;
    private static HashMap<Integer, OrderFromSupplier> IMOrdersFromSupplier = new HashMap<>();//key: orderId

    /**
     * constructor
     */
    public OrdersFromSupplierDAO(){
        connect=Connect.getInstance();
        productSupplierDAO = new ProductsSupplierDAO();
    }

    public static void reset() {
        IMOrdersFromSupplier = new HashMap<>();
    }

    public void createOrderFromSupplier(OrderFromSupplier order) throws SQLException {
        String query =String.format("INSERT INTO OrdersFromSupplier (date ,supplierNumber) " +
                "VALUES ('%s',%d)", order.getDate(), order.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            query ="SELECT * FROM OrdersFromSupplier ORDER BY orderId DESC LIMIT 1";
            ResultSet rs = stmt.executeQuery(query);
            order.setOrderId(rs.getInt("orderId"));
            IMOrdersFromSupplier.put(order.getOrderId(), order);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void addProductToOrder(ProductSupplier p, int orderId, int count) throws SQLException {
        String query =String.format("INSERT INTO ProductsInOrder (orderId ,productId, count) " +
                "VALUES (%d,%d,%d)",orderId , p.getProductId(), count);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            //IMOrdersFromSupplier.put(order.getOrderId(), order);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void updateCountProductToOrder(int productId, int orderId, int count) throws SQLException {
        if (count == 0){
            removeProductFromOrder(productId,orderId);
            return;
        }
        String query =String.format("UPDATE ProductsInOrder SET count = %d where orderId =%d and productId = %d "
                , count, orderId, productId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            //IMOrdersFromSupplier.put(order.getOrderId(), order);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void removeProductFromOrder(int productId, int orderId) throws SQLException {
        String query =String.format("DELETE FROM ProductsInOrder where orderId =%d and productId = %d "
                , orderId, productId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            //IMOrdersFromSupplier.put(order.getOrderId(), order);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }


    //need to add return products..
    public OrderFromSupplier getOrder(int orderId) throws SQLException {
        if (IMOrdersFromSupplier.containsKey(orderId)){
            return IMOrdersFromSupplier.get(orderId);
        }
        String query =String.format("SELECT * FROM OrdersFromSupplier WHERE orderId = %d"
                , orderId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return null;
            DeliveryTerm dt = getDeliveryTermOfOrder(orderId);
            OrderFromSupplier order = new OrderFromSupplier(rs.getInt("supplierNumber"),orderId,rs.getString("date"), dt);
            IMOrdersFromSupplier.put(orderId, order);
            return order;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public DeliveryTerm getDeliveryTermOfOrder(int orderId) throws SQLException {
        String query =String.format("SELECT * FROM DeliveryTerms WHERE orderId = %d"
                , orderId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            String days = "";
            while (rs.next()){
                days+=(rs.getString("daysToDeliver"));
            }

            DeliveryTerm d ;
            if(days==""){
                DeliveryTerm.DaysInWeek[] dd=new DeliveryTerm.DaysInWeek[1];
                dd[0]= DeliveryTerm.DaysInWeek.NoDays;
                d=new DeliveryTerm(dd);
            }
            else{
                d=new DeliveryTerm(days);
            }
            return d;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public Map<ProductSupplier,Integer> getAllProductsOfOrder(int orderId) throws SQLException {
        String query =String.format("SELECT * FROM ProductsInOrder p INNER JOIN OrdersFromSupplier o ON o.orderId = p.orderId and o.orderId = %d"
                , orderId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            Map<ProductSupplier,Integer> products = new HashMap<>();
            while (rs.next()){
                ProductSupplier p = productSupplierDAO.getProduct(rs.getInt("supplierNumber"), rs.getInt("productId"));
                products.put(p,rs.getInt("count"));
            }
            return products;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders(int supplierNumber) throws SQLException {
        String query =String.format("SELECT * FROM OrdersFromSupplier WHERE  supplierNumber = %d"
                , supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            Map<Integer,OrderFromSupplier> orders = new HashMap<>();
            int i=0;
            while (rs.next()){
                OrderFromSupplier order = new OrderFromSupplier(rs.getInt("supplierNumber"),rs.getInt("orderId"), rs.getString("date")
                        ,getDeliveryTermOfOrder(rs.getInt("orderId")));
                orders.put(i,order);
                i++;
            }
            return orders;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public Map<Integer, OrderFromSupplier> getActiveOrders() throws SQLException {
        String query = "SELECT * FROM OrdersFromSupplier";
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            Map<Integer,OrderFromSupplier> orders = new HashMap<>();
            int i=0;
            while (rs.next()){
                OrderFromSupplier order = new OrderFromSupplier(rs.getInt("supplierNumber"),rs.getInt("orderId"), rs.getString("date")
                        ,getDeliveryTermOfOrder(rs.getInt("orderId")));
                orders.put(i,order);
                i++;
            }
            return orders;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public boolean removeOrder(int orderId) throws SQLException {
        String query =String.format("DELETE FROM OrdersFromSupplier WHERE orderId = %d"
                , orderId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            IMOrdersFromSupplier.remove(1);
            return true;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void updateDeliveryTermsInOrder(int orderId, String[] daysInWeek) throws SQLException {
        DeliveryTerm d=getDeliveryTermOfOrder(orderId);
        if(d.isEmpty()) {
            addDeliveryTermsToOrder(orderId, daysInWeek);
        }
        else {
            StringBuilder days = new StringBuilder();
            for (String s : daysInWeek) {
                days.append(s);
            }
            String query = String.format("UPDATE DeliveryTerms SET daysToDeliver = %s where orderId = %d "
                    , days, orderId);
            try (Statement stmt = connect.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                throw e;
            } finally {
                connect.closeConnect();
            }
        }
    }

    public void addDeliveryTermsToOrder(int orderId, String[] daysInWeek) throws SQLException {
        String days= new String();
        for(String s:daysInWeek){
            days+=s;
        }
        String query =String.format("INSERT INTO DeliveryTerms (orderId,daysToDeliver)"+
                "VALUES (%d,%s)",orderId, days);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void updateCount(int productId, int count, int orderId) throws SQLException {
        String query = "UPDATE ProductsInOrder" +
                String.format(" SET count=%d", count) +
                String.format(" WHERE productId=%d AND orderId=%d", productId, orderId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException throwable) {
            //System.out.println(throwable.getMessage());
        } finally {
            connect.closeConnect();
        }
    }

    public List<Integer> getRegularOrdersIds() throws SQLException {
        String query ="SELECT orderId FROM DeliveryTerms";
        List<Integer> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs= stmt.executeQuery(query);
            while(rs.next()) {
                ans.add(rs.getInt("orderId"));
            }
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
        return ans;
    }

    public OrderFromSupplier getPastOrder(int orderId) throws SQLException {
        String query =String.format("SELECT * FROM PastOrdersSupplier WHERE orderId = %d"
                , orderId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return null;
            DeliveryTerm dt = getDeliveryTermOfOrder(orderId);
            OrderFromSupplier order = new OrderFromSupplier(rs.getInt("supplierNumber"),orderId,rs.getString("date"), dt);
            IMOrdersFromSupplier.put(orderId, order);
            return order;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public boolean removeAllProductsFromOrder(int orderId) throws SQLException {
        String query =String.format("DELETE FROM ProductsInOrder where orderId = %d ", orderId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            //IMOrdersFromSupplier.put(order.getOrderId(), order);
        } catch (SQLException e) {
            return false;
        }
        finally {
            connect.closeConnect();
        }
        return true;
    }

    public int allOrdersOfSupplier(int supplierNumber, int pastOrders) throws SQLException {
        String query = String.format("SELECT * FROM OrdersFromSupplier WHERE supplierNumber = %d"
                , supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            int ans = pastOrders;
            while (rs.next()) {
                ans++;
            }
            return ans;
        } catch (SQLException e) {
            return 0;
        } finally {
            connect.closeConnect();
        }
    }

    public int getSupplierNumberFromOrderDoc(int orderDocId) throws SQLException {
        String query =String.format("SELECT supplierNumber FROM PastOrdersSupplier WHERE orderId=%d",orderDocId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if(rs.isClosed())
                return rs.getInt("supplierNumber");
            else
                return 1;
        }
        catch (Exception e){
            return 1;
        }
        finally {
            connect.closeConnect();
        }
    }
}

package DAL;

import DomainLayer.DeliveryTerm;
import DomainLayer.OrderFromSupplier;
import DomainLayer.ProductSupplier;
import DomainLayer.Supplier;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersFromSupplierDAO {
    private Connect connect;
    private ProductSupplierDAO productSupplierDAO;
    private static HashMap<Integer, OrderFromSupplier> IMOrdersFromSupplier = new HashMap<>();;//key: orderId

    /**
     * constructor
     */
    public OrdersFromSupplierDAO(){
        connect=Connect.getInstance();
        productSupplierDAO = new ProductSupplierDAO();
    }

    public void createOrderFromSupplier(OrderFromSupplier order, int supplierNumber) throws SQLException {
        String query =String.format("INSERT INTO OrdersFromSupplier (orderId ,date ,supplierId) " +
                "VALUES (%d,'%s',%d)", order.getOrderId(), order.getDate(), supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
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
                "VALUES (%d,%d)",orderId , p.getProductId(), count);
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
    public OrderFromSupplier getOrderWithAllTheProducts(int orderId) throws SQLException {
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
            OrderFromSupplier order = new OrderFromSupplier(orderId,rs.getString("date"), dt);
            IMOrdersFromSupplier.put(orderId, order);
            return order;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    private DeliveryTerm getDeliveryTermOfOrder(int orderId) throws SQLException {
        String query =String.format("SELECT * FROM DeliveryTerms WHERE orderId = %d"
                , orderId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            List<String> days = new ArrayList<>();
            while (rs.next()){
                days.add(rs.getString(1));
            }
            return new DeliveryTerm(days);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public Map<ProductSupplier,Integer> getAllProductsOfOrder(int orderId) throws SQLException {
        String query =String.format("SELECT * FROM ProductsInOrder p, OrdersFromSupplier o WHERE o.orderId = p.orderId orderId = %d"
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

    public Map<Integer, OrderFromSupplier> getActiveOrders() {
        return null;
    }

    public void removeOrder(int orderId) {
    }
}

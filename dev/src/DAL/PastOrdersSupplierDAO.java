package DAL;

import DomainLayer.Supplier.DeliveryTerm;
import DomainLayer.Supplier.OrderFromSupplier;
import DomainLayer.Supplier.PastOrderSupplier;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PastOrdersSupplierDAO {
    private Connect connect;
    private OrdersFromSupplierDAO ordersDAO;
    private static HashMap<Pair<Integer,String>, PastOrderSupplier> IMPastOrdersFromSupplier;//key: orderId and finish date

    /**
     * constructor
     */
    public PastOrdersSupplierDAO(){
        connect = Connect.getInstance();
        IMPastOrdersFromSupplier = new HashMap<>();
        ordersDAO= new OrdersFromSupplierDAO();
    }

    public Map<Integer,PastOrderSupplier> getAllPastOrders(int supplierNumber) throws SQLException {
        String query =String.format("SELECT * FROM PastOrdersSupplier WHERE supplierNumber = %d"
                , supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            Map<Integer,PastOrderSupplier> orders = new HashMap<>();
            int i=0;
            while (rs.next()){
                OrderFromSupplier order = new OrderFromSupplier(rs.getInt("supplierNumber"),rs.getInt("orderId"),rs.getString("finishDate"),new DeliveryTerm(""));
                PastOrderSupplier p = new PastOrderSupplier(order, rs.getDouble("totalPrice"),  rs.getString("finishDate"));
                orders.put(i,p);
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

    public void insertPastOrder(PastOrderSupplier order) throws SQLException {
        String query =String.format("INSERT INTO PastOrdersSupplier (orderId,finishDate,totalPrice,supplierNumber) " +
                "VALUES (%d,'%s',%f,%d)", order.getOrderId() ,order.getDate(), order.getTotalPrice(),order.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            IMPastOrdersFromSupplier.put(new Pair<>(order.getOrderId(), order.getDate()), order);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
}

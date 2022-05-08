package DAL;

import DomainLayer.OrderFromSupplier;
import DomainLayer.PastOrderSupplier;
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
    }

    public List<PastOrderSupplier> getAllPastOrders(int supplierNumber) throws SQLException {
        String query =String.format("SELECT * FROM PastOrdersSupplier WHERE supplierNumber = %d"
                , supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            List<PastOrderSupplier> orders = new LinkedList<>();
            while (rs.next()){
                OrderFromSupplier order = ordersDAO.getOrder(rs.getInt("orderId"));
                PastOrderSupplier p = new PastOrderSupplier(order, rs.getDouble("totalPrice"),  rs.getString("finishDate"));
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
        String query =String.format("INSERT INTO PastOrdersSupplier (orderId, date ,totalPrice) " +
                "VALUES ('%s',%d)", order.getOrderId() ,order.getDate(), order.getTotalPrice());
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

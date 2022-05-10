package DomainLayer;

import DAL.OrdersFromSupplierDAO;
import DAL.PastOrdersSupplierDAO;
import DAL.ProductsSupplierDAO;
import DAL.SuppliersDAO;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrdersController {
    private OrdersFromSupplierDAO ordersDAO;
    private ProductsSupplierDAO productsDAO;
    private static int orderNum=0;
    private PastOrdersSupplierDAO pastOrdersDAO;
    private SuppliersDAO suppliersDAO;

    public OrdersController() {
        this.ordersDAO = new OrdersFromSupplierDAO();
        this.productsDAO = new ProductsSupplierDAO();
        this.pastOrdersDAO =new PastOrdersSupplierDAO();
        this.suppliersDAO=new SuppliersDAO();
    }

    public OrderFromSupplier createOrder(int supplierNumber){
        OrderFromSupplier order = new OrderFromSupplier(supplierNumber);
        try {
            ordersDAO.createOrderFromSupplier(order);
        } catch (SQLException e) {
            return null;
        }
        //orderNum++;
        return order;
    }
    public boolean finishOrder(int orderId){
        if(orderId<0){
            return false;
        }
        OrderFromSupplier o = getOrder(orderId);
        if(o == null){
            return false;
        }
        //do something with isDeliver
        double totalPrice = 0;
        try {
            totalPrice = updateTotalIncludeDiscounts(orderId);
            pastOrdersDAO.insertPastOrder(new PastOrderSupplier(o,totalPrice));
            ordersDAO.removeOrder(orderId);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }
    public OrderFromSupplier getOrder(int orderId) {
        try {
            return ordersDAO.getOrder(orderId);
        } catch (SQLException e) {
            return null;
        }
    }
    public double updateTotalIncludeDiscounts(int orderId)  {
        OrderFromSupplier order = getOrder(orderId);
        int count = order.getCountProducts();
        double price = order.getTotalIncludeDiscounts();
        return price*findMaxUnder(count,order.getSupplierNumber());
    }
    private double findMaxUnder(int count,int supplierId){
        int out=0;
        Map<Integer,Double> discountByAmount = null;
        try {
            discountByAmount = suppliersDAO.getDiscountsSupplier(supplierId);
        } catch (SQLException throwables) {
            return -1;
        }
        for(int s:discountByAmount.keySet()){
            if(s<=count){
                out=s;
            }
            else{
                return discountByAmount.get(out);
            }
        }
        return discountByAmount.get(out);
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders(int supplierNumber) {
        try {
            return ordersDAO.getActiveOrders(supplierNumber);
        } catch (SQLException e) {
            return null;
        }
    }


    public List<PastOrderSupplier> getFinalOrders(int supplierNumber) {
        try {
            return pastOrdersDAO.getAllPastOrders(supplierNumber);
        } catch (SQLException e) {
            return null;
        }
    }

    public void updateOrders(List<Pair<Integer, Integer>> catalogNumbers){
        for(Pair<Integer, Integer> catalogNumber : catalogNumbers){
            int productId;
            try {
                productId= productsDAO.getProductIdByCatalogId(catalogNumber.getKey());
            } catch (SQLException e) {
                productId=-1;
            }
            if (productId!=-1) {
                try {
                    ordersDAO.updateCount(productId, catalogNumber.getValue());
                } catch (SQLException ignored) {
                }
            }
        }
    }


    public void createOrderWithMinPrice(int catalogNumber, int amount){
        ProductSupplier ps=getProductWithMinPrice(catalogNumber);

        if(ps!=null) {
            OrderFromSupplier order = createOrder(ps.getSupplierNumber());
            if(order!=null) {
                try {
                    ordersDAO.addProductToOrder(ps, order.getOrderId(), amount);
                } catch (SQLException ignored) {

                }
            }
        }
    }

    public ProductSupplier getProductWithMinPrice(int catalogNumber){
        try {
            return productsDAO.getProductByCatalogNumber(catalogNumber);
        } catch (SQLException e) {
            return null;
        }
    }

}

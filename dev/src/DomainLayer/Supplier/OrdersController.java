package DomainLayer.Supplier;

import DAL.OrdersFromSupplierDAO;
import DAL.PastOrdersSupplierDAO;
import DAL.ProductsSupplierDAO;
import DAL.SuppliersDAO;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
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
        boolean finish =finishFixedDaysDeliveryOrder(orderId);
        try {
            ordersDAO.removeOrder(orderId);
        } catch (SQLException throwables) {
            return false;
        }
        return finish;

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
    private double findMaxUnder(int count,int supplierNumber){
        Double out= 0.0;
        LinkedList<Discount> discountByAmount;
        try {
            discountByAmount = suppliersDAO.getDiscountsSupplier(supplierNumber);

        } catch (SQLException e) {
            return -1;
        }
        for(Discount d:discountByAmount){
            if(d.getAmount()<=count){
                out=d.getDiscount();
            }
            else{
                return out;
            }
        }

        return 1;
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders(int supplierNumber) {
        try {
            return ordersDAO.getActiveOrders(supplierNumber);
        } catch (SQLException e) {
            return null;
        }
    }


    public Map<Integer,PastOrderSupplier> getFinalOrders(int supplierNumber) {
        try {
            return pastOrdersDAO.getAllPastOrders(supplierNumber);
        } catch (SQLException e) {
            return null;
        }
    }


    public void updateOrders(Map<Integer, Integer> productIds){
        try {
            List<Integer> orderIds=ordersDAO.getRegularOrdersIds();
            for(Integer orderId: orderIds) {
                DeliveryTerm d =ordersDAO.getDeliveryTermOfOrder(orderId);
                if(checkDays(d)) {
                    Map<ProductSupplier, Integer> products = ordersDAO.getAllProductsOfOrder(orderId);
                    for (ProductSupplier product : products.keySet()) {
                        int productId = product.getProductId();
                        ordersDAO.updateCount(productId, productIds.get(productId), orderId);
                    }
                    boolean send=finishFixedDaysDeliveryOrder(orderId);
                }
            }
        } catch (SQLException ignored) {
        }
    }

    private boolean finishFixedDaysDeliveryOrder(Integer orderId) {
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
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    private boolean checkDays(DeliveryTerm d) {
        Date today =new Date();
        for(DeliveryTerm.DaysInWeek day:d.getDaysInWeeks() ){
             if(d.getDayValue(day)==today.getDay()-1){
                 return true;
             }
        }
        return false;
    }


    public void createOrderWithMinPrice(int productId, int amount){
        ProductSupplier ps=getProductWithMinPrice(productId, amount);
        if(ps!=null) {
            OrderFromSupplier order = createOrder(ps.getSupplierNumber());
            if(order!=null) {
                try {
                    ordersDAO.addProductToOrder(ps, order.getOrderId(), amount);
                    finishOrder(order.getOrderId());
                } catch (SQLException ignored) {

                }
            }
        }
    }

    public ProductSupplier getProductWithMinPrice(int productId, int amount){
        try {
            return productsDAO.getMinProductByCatalogNumber(productId, amount);
        } catch (SQLException e) {
            return null;
        }
    }

}

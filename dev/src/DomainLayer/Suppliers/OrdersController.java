package DomainLayer.Suppliers;

import DAL.*;
import DomainLayer.FacadeEmployees_Transports;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class OrdersController {
    private OrdersFromSupplierDAO ordersDAO;
    private ProductsSupplierDAO productsDAO;
    private PastOrdersSupplierDAO pastOrdersDAO;
    private SuppliersDAO suppliersDAO;
    private FacadeEmployees_Transports employees_transports;

    public OrdersController() {
        this.ordersDAO = new OrdersFromSupplierDAO();
        this.productsDAO = new ProductsSupplierDAO();
        this.pastOrdersDAO =new PastOrdersSupplierDAO();
        this.suppliersDAO=new SuppliersDAO();
        this.employees_transports=new FacadeEmployees_Transports();
    }

    public OrderFromSupplier createOrder(int supplierNumber){
        OrderFromSupplier order = new OrderFromSupplier(supplierNumber);
        try {
            ordersDAO.createOrderFromSupplier(order);
        } catch (SQLException e) {
            return null;
        }
        return order;
    }
    public boolean finishOrder(int orderId){
        boolean finish = finishFixedDaysDeliveryOrder(orderId);
        try {
            if(finish)
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
        Double out= 1.0;
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

        return out;
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders(int supplierNumber) {
        try {
            return ordersDAO.getActiveOrders(supplierNumber);
        } catch (SQLException e) {
            return null;
        }
    }
    public Map<Integer, OrderFromSupplier> getActiveOrders() {
        try {
            return ordersDAO.getActiveOrders();
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
                if(checkDays(d) && productIds.size()!=0) {
                    Map<ProductSupplier, Integer> products = ordersDAO.getAllProductsOfOrder(orderId);
                    for (ProductSupplier product : products.keySet()) {
                        int productId = product.getProductId();
                        ordersDAO.updateCount(productId, productIds.get(productId), orderId);
                    }
                }
                if(checkDays(d))
                    finishFixedDaysDeliveryOrder(orderId);
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
            if(o.getCountProducts()>0) {
                if(!createTransport(o)) {
                    new EmployeeDAO().writeMessageToHR("No available drivers to ship order "+o.getOrderId()+" from supplier "+o.getSupplierNumber());
                    return false;
                }
                pastOrdersDAO.insertPastOrder(new PastOrderSupplier(o, totalPrice));
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //helper function to connect with Transport model
    private boolean createTransport(OrderFromSupplier o){
        String supplierNumber =String.valueOf(o.getSupplierNumber());
        String date = o.getDate();
        Map<ProductSupplier,Integer> productToQuantity= new HashMap<>();
        productToQuantity=o.getProducts();
        return employees_transports.createAutoTransport(supplierNumber,date,productToQuantity);
    }

    private boolean checkDays(DeliveryTerm d) {
        Date today =new Date();
        for(DeliveryTerm.DaysInWeek day:d.getDaysInWeeks() ){
             if(d.getDayValue(day)%7==(today.getDay()-1)%7){
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
            return productsDAO.getMinProductByProductId(productId, amount);
        } catch (SQLException e) {
            return null;
        }
    }

    public Map<ProductSupplier,Integer> getAllProductsOfOrder(int orderId){
        try {
            return ordersDAO.getAllProductsOfOrder(orderId);
        } catch (SQLException e) {
            return new HashMap<>();
        }
    }

    public Boolean cancelOrder(int orderId) {
            try {
                ordersDAO.removeAllProductsFromOrder(orderId);
                ordersDAO.removeOrder(orderId);
                return true;

            } catch (SQLException e) {
                return false;
            }
    }

    public int allOrdersOfSupplier(int supplierNumber){
        try {
            int past = pastOrdersDAO.getAllPastOrders(supplierNumber).size();
            int ans = ordersDAO.allOrdersOfSupplier(supplierNumber, past);
            return ans;
        }
        catch (Exception e){
            return 0;
        }

    }

    public int getSupplierNumberFromOrderDoc(int orderDocId) {
        try {
            return ordersDAO.getSupplierNumberFromOrderDoc(orderDocId);
        } catch (SQLException e) {
            return 1;
        }
    }

    public String getExpirationDate(int supplierNumber, int productId) {
        try {
            return productsDAO.getExpirationDate(supplierNumber, productId);
        } catch (SQLException e) {
            String curDate = LocalDate.now().toString();
            int curYear = Integer.parseInt(curDate.substring(0, 4)) -1;
            String curMonth = curDate.substring(5, 7);
            String curDay = curDate.substring(8, 10);
            String exp = curYear+"-"+curMonth+"-"+curDay;
            return exp;
        }
    }
}

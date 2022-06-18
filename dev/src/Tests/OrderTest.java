package Tests;


import DAL.Connect;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.OrdersController;
import DomainLayer.Suppliers.ProductSupplier;
import PresentationLayer.Menu;
import PresentationLayer.Suppliers.Order;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class OrderTest {
    private FacadeSupplier_Storage facadeSupplier;
    private OrdersController ordersController;
    private static boolean setUpIsDone = false;
    @Before
    public void setUp() throws SQLException {
        if(!setUpIsDone) {
//            File file=new File("..\\dev\\superli.db");
//            file.delete();
            Connect.getInstance().deleteRecordsOfTables();

            facadeSupplier = new FacadeSupplier_Storage();
            facadeSupplier.addCategory("first");
            facadeSupplier.addSubCat("first", "first1");
            facadeSupplier.addSubCat("first", "first1");
            facadeSupplier.addSubSubCat("first", "first1", "first11");
            facadeSupplier.addCategory("second");
            facadeSupplier.addSubCat("second", "second1");
            facadeSupplier.addSubSubCat("second", "second1", "second11");
            facadeSupplier.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            facadeSupplier.addNewProduct(2,"eggs","hello",4,"me","first","first1", "first11");
            facadeSupplier.addAllItems(1,4,"2027-06-01",12);
            ordersController = new OrdersController();
            facadeSupplier.openAccount(1, "eli", 1, new String[]{"1"},0);
            facadeSupplier.openAccount(2, "eli2", 2, new String[]{"1"},0);
            facadeSupplier.addProductToSupplier(1, 1, 100,4, 1);
            facadeSupplier.addProductToSupplier(2, 2, 50,3, 2);
//            facade.createOrder(1);
//            facade.addProductToOrder(1, 1, 1, 3);
//            facade.createOrder(2);
//            facade.addProductToOrder(2, 2, 2, 5);
            facadeSupplier.addDiscount(1,10,0.8);
           // setUpIsDone=true;
        }
        else {
            facadeSupplier = new FacadeSupplier_Storage();
            ordersController = new OrdersController();
        }
    }


    @org.junit.Test
    public void stage1_updateProductToOrder() {
        boolean ans=false;
        String json = facadeSupplier.createOrder(1);
        OrderFromSupplier order = Menu.fromJson(json,OrderFromSupplier.class);
        facadeSupplier.addProductToOrder(1, order.getOrderId(), 1,10);
        for(ProductSupplier p : order.getProducts().keySet()) {
            ans = ans || p.getProductId() == 1;
        }
        assertTrue(ans);


    }
    @org.junit.Test
    public void stage2_getTotalIncludeDiscounts() {
        String json = facadeSupplier.createOrder(1);
        Order order = Menu.fromJson(json,Order.class);
        facadeSupplier.addProductToOrder(1, order.getOrderId(), 1,10);
        assertEquals(32.0, ordersController.updateTotalIncludeDiscounts(order.getOrderId()), 0.0);

    }

    @org.junit.Test
    public void stage3_getCountProducts() {
        String json = facadeSupplier.createOrder(1);
        OrderFromSupplier order = Menu.fromJson(json,OrderFromSupplier.class);
        facadeSupplier.addProductToOrder(1, order.getOrderId(), 1,10);
        assertEquals(10,order.getCountProducts());
    }

    @org.junit.Test
    public void stage4_removeProductFromOrder() {
        String json = facadeSupplier.createOrder(1);
        OrderFromSupplier order = Menu.fromJson(json,OrderFromSupplier.class);
        facadeSupplier.addProductToOrder(1, order.getOrderId(), 1,10);
        for(ProductSupplier p : order.getProducts().keySet()) {
            assertTrue(order.removeProductFromOrder(p));
        }
        assertEquals(0, order.getCountProducts());
    }
}
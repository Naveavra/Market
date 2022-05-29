package Tests;


import DAL.Connect;
import DomainLayer.Facade;
import DomainLayer.Supplier.OrderFromSupplier;
import DomainLayer.Supplier.OrdersController;
import DomainLayer.Supplier.ProductSupplier;
import PresentationLayer.Supplier.Menu;
import PresentationLayer.Supplier.Order;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class OrderTest {
    private Facade facade;
    private OrdersController ordersController;
    private static boolean setUpIsDone = false;
    @Before
    public void setUp() throws SQLException {
        if(!setUpIsDone) {
//            File file=new File("..\\dev\\superli.db");
//            file.delete();
            Connect.getInstance().deleteRecordsOfTables();

            facade = new Facade();
            facade.addCategory("first");
            facade.addSubCat("first", "first1");
            facade.addSubSubCat("first", "first1", "first11");
            facade.addCategory("second");
            facade.addSubCat("second", "second1");
            facade.addSubSubCat("second", "second1", "second11");
            facade.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            facade.addNewProduct(2,"eggs","hello",4,"me","first","first1", "first11");
            facade.addAllItems(1,4,"2022-06-01",12);
            ordersController = new OrdersController();
            facade.openAccount(1, "eli", 1, true);
            facade.openAccount(2, "eli2", 2, true);
            facade.addProductToSupplier(1, 1, 4, 1);
            facade.addProductToSupplier(2, 2, 3, 2);
//            facade.createOrder(1);
//            facade.addProductToOrder(1, 1, 1, 3);
//            facade.createOrder(2);
//            facade.addProductToOrder(2, 2, 2, 5);
            facade.addDiscount(1,10,0.8);
           // setUpIsDone=true;
        }
        else {
            facade = new Facade();
            ordersController = new OrdersController();
        }
    }


    @org.junit.Test
    public void stage1_updateProductToOrder() {
        boolean ans=false;
        String json =facade.createOrder(1);
        OrderFromSupplier order = Menu.fromJson(json,OrderFromSupplier.class);
        facade.addProductToOrder(1, order.getOrderId(), 1,10);
        for(ProductSupplier p : order.getProducts().keySet()) {
            ans = ans || p.getProductId() == 1;
        }
        assertTrue(ans);


    }
    @org.junit.Test
    public void stage2_getTotalIncludeDiscounts() {
        String json =facade.createOrder(1);
        Order order = Menu.fromJson(json,Order.class);
        facade.addProductToOrder(1, order.getOrderId(), 1,10);
        assertEquals(32.0, ordersController.updateTotalIncludeDiscounts(order.getOrderId()), 0.0);

    }

    @org.junit.Test
    public void stage3_getCountProducts() {
        String json =facade.createOrder(1);
        OrderFromSupplier order = Menu.fromJson(json,OrderFromSupplier.class);
        facade.addProductToOrder(1, order.getOrderId(), 1,10);
        assertEquals(10,order.getCountProducts());
    }

    @org.junit.Test
    public void stage4_removeProductFromOrder() {
        String json =facade.createOrder(1);
        OrderFromSupplier order = Menu.fromJson(json,OrderFromSupplier.class);
        facade.addProductToOrder(1, order.getOrderId(), 1,10);
        for(ProductSupplier p : order.getProducts().keySet()) {
            assertTrue(order.removeProductFromOrder(p));
        }
        assertEquals(0, order.getCountProducts());
    }
}
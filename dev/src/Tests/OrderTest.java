package Tests;


import DAL.Connect;
import DomainLayer.Facade;
import DomainLayer.Supplier.OrdersController;
import DomainLayer.Supplier.ProductSupplier;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class OrderTest {
    private Facade facade;
    private OrdersController ordersController;
    private static boolean setUpIsDone = false;
    @org.junit.Before
    public void setUp(){
        if(!setUpIsDone) {
            File file=new File("..\\dev\\superli.db");
            file.delete();
            facade = new Facade();
            ordersController = new OrdersController();
            facade.openAccount(1, "eli", 1, true);
            facade.openAccount(2, "eli2", 2, true);
            facade.addProduct(1, 1, 4, 1);
            facade.addProduct(2, 2, 3, 2);
            facade.createOrder(1);
            facade.addProductToOrder(1, 1, 1, 3);
            facade.createOrder(2);
            facade.addProductToOrder(2, 2, 2, 5);
            setUpIsDone=true;
        }
        else {
            facade = new Facade();
            ordersController = new OrdersController();
        }
    }


    @org.junit.Test
    public void stage1_updateProductToOrder() {
        boolean ans=false;
        for(ProductSupplier p : ordersController.getOrder(1).getProducts().keySet()) {
            ans = ans || p.getProductId() == 1;
        }
        assertTrue(ans);


    }
    @org.junit.Test
    public void stage2_getTotalIncludeDiscounts() {
        assertEquals(12.0, ordersController.getOrder(1).getTotalIncludeDiscounts(), 0.0);

    }

    @org.junit.Test
    public void stage3_getCountProducts() {
        assertEquals(3,ordersController.getOrder(1).getCountProducts());
    }

    @org.junit.Test
    public void stage4_removeProductFromOrder() {
        for(ProductSupplier p : ordersController.getOrder(1).getProducts().keySet()) {
            assertTrue(ordersController.getOrder(1).removeProductFromOrder(p));
        }
        assertEquals(0, ordersController.getOrder(1).getCountProducts());
    }
}
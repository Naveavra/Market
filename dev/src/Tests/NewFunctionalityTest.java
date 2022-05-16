package Tests;

import DomainLayer.Facade;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Supplier.OrdersController;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.junit.Assert.*;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class NewFunctionalityTest {

    private CategoryController cC;
    private OrdersController ordersController;
    private Facade facade;
    private static boolean setUpIsDone = false;
    @Before
    public void setUp()
    {
        if(!setUpIsDone) {
            File file=new File("..\\dev\\superli.db");
            file.delete();
            cC = new CategoryController();
            facade=new Facade();
            ordersController=new OrdersController();
            cC.addCategory("first", 0);
            cC.addSubCategory("first", "first1");
            cC.addSubSubCategory("first", "first1", "first11");
            cC.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            cC.addAllItems(1,3,"2022-06-01",12);
            facade.openAccount(1,"eli", 1, true);
            facade.addProduct(1, 1, 2, 1);
            facade.createOrder(1);
            facade.addProductToOrder(1, 1, 1, 2);
            setUpIsDone=true;
        }
        else {
            cC = new CategoryController();
            facade=new Facade();
            ordersController=new OrdersController();
        }
    }



    @org.junit.Test
    public void stage1_checkRequestingAnOrderAfterShortage() {
        facade.buyItems(1, 3);
        assertEquals(1, ordersController.getFinalOrders(1).size());

    }

    @org.junit.Test
    public void stage2_checkSendingRegularOrder() {
        String[]days=new String[1];
        days[0]="1";
        facade.addFixedDeliveryDaysForOrder(1, 1, days);
        facade.updateOrders();
        assertTrue(ordersController.getFinalOrders(1).containsKey(1));
    }

    @org.junit.Test
    public void stage3_checkUpdatingRegularOrders(){
        cC.addAllItems(1,3,"2022-06-01",12);
        facade.updateOrders();
        assertEquals(cC.getProductWithId(1).getRefill(), ordersController.getOrder(1).getCountProducts());
        cC.buyItems(1, 3);
        facade.updateOrders();
        assertEquals(cC.getProductWithId(1).getRefill(), ordersController.getOrder(1).getCountProducts());
    }


}
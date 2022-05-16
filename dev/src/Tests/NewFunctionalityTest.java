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
            cC.addAllItems(1,7,"2022-06-01",12);
            facade.openAccount(1,"eli", 1, true);
            facade.openAccount(2,"eli2", 2, true);
            facade.addProduct(1, 1, 2, 1);
            facade.addProduct(2, 2, 4, 1);
            facade.createOrder(1);
            facade.createOrder(1);
            String[]days=new String[1];
            days[0]="4";
            facade.addFixedDeliveryDaysForOrder(1, 2, days);
            facade.updateOrders();
            facade.addProductToOrder(1, 1, 1, 2);
            facade.addProductToOrder(1, 2, 1, 2);
            days[0]="1";
            facade.addFixedDeliveryDaysForOrder(1, 1, days);
            setUpIsDone=true;
        }
        else {
            cC = new CategoryController();
            facade=new Facade();
            ordersController=new OrdersController();
        }
    }





    @org.junit.Test
    public void stage1_checkNotOrderingWhenNotDayBeforeOrder(){
        assertEquals(0, ordersController.getFinalOrders(1).size());
    }

    @org.junit.Test
    public void stage2_checkNotOrderingWhenRefillNotNeeded(){
        facade.buyItems(1, 1);
        assertEquals(0, ordersController.getFinalOrders(1).size());
    }
    @org.junit.Test
    public void stage3_checkRequestingAnOrderAfterShortage() {
        facade.buyItems(1, 6);
        assertEquals(1, ordersController.getFinalOrders(1).size());

    }

    @org.junit.Test
    public void stage4_checkUpdatingRegularOrdersCount(){
        facade.updateOrders();
        assertEquals(cC.getProductWithId(1).getRefill(), ordersController.getOrder(1).getCountProducts());
    }


    @org.junit.Test
    public void stage5_checkSendingRegularOrder() {
        assertTrue(ordersController.getFinalOrders(1).containsKey(1));
    }

    @org.junit.Test
    public void stage6_checkSendingOrderWhenRefillIsNotEnough() {
        facade.addAllItems(1,1,"2022-06-01",12);
        assertEquals(3, ordersController.getFinalOrders(1).size());

    }

    @org.junit.Test
    public void stage7_checkStopSendingOrderAfterRefill() {
        cC.addAllItems(1,100,"2022-06-01",12);
        assertEquals(3, ordersController.getFinalOrders(1).size());// one order before and one order because of regular

    }

    @org.junit.Test
    public void stage8_checkUpdatingRegularOrdersAfterRefillTo0(){
        facade.updateOrders();
        assertEquals(0, ordersController.getOrder(1).getCountProducts());
    }

    @org.junit.Test
    public void stage9_checkSendingRegularOrderWhenCountIs0() {
        assertEquals(3, ordersController.getFinalOrders(1).size());//2 were already there, checking no other order was added
    }

    @org.junit.Test
    public void stage9z_checkGotMinPriceForProduct(){
        assertEquals(2, ordersController.getProductWithMinPrice(1, 5).getPrice(), 0.0);
    }




}
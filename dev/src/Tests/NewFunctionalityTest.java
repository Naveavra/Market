package Tests;

import DAL.Connect;
import DomainLayer.Facade;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Supplier.OrderFromSupplier;
import DomainLayer.Supplier.OrdersController;
import PresentationLayer.Menu;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.*;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class NewFunctionalityTest {

    private CategoryController cC;
    private OrdersController ordersController;
    private Facade facade;
    private OrderFromSupplier order;
    private OrderFromSupplier order2;
    private static boolean setUpIsDone = false;
    @Before
    public void setUp() throws SQLException {
//            File file=new File("..\\dev\\superli.db");
//            file.delete();
            Connect.getInstance().deleteRecordsOfTables();
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
            facade.addProductToSupplier(1, 1, 2, 1);
            facade.addProductToSupplier(2, 2, 4, 1);
            String json =facade.createOrder(1);
            order = Menu.fromJson(json,OrderFromSupplier.class);
            String json2 =facade.createOrder(1);
            order2 = Menu.fromJson(json,OrderFromSupplier.class);
//            facade.createOrder(1);
//            facade.createOrder(1);
            String[]days=new String[1];
            days[0]="4";
            facade.addFixedDeliveryDaysForOrder(1, order2.getOrderId(), days);
            facade.updateOrders();
            facade.addProductToOrder(1, order.getOrderId(), 1, 2);
            facade.addProductToOrder(1, order2.getOrderId(), 2, 2);
            days[0]="1";
            facade.addFixedDeliveryDaysForOrder(1, order.getOrderId(), days);
            setUpIsDone=true;
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
        facade.buyItems(1, 6);
        facade.updateOrders();
        assertEquals(cC.getProductWithId(1).getRefill(), order.getCountProducts());
    }


    @org.junit.Test
    public void stage5_checkSendingRegularOrder() {
        facade.buyItems(1, 6);
        assertTrue(ordersController.getFinalOrders(1).size()>0);
    }

    @org.junit.Test
    public void stage6_checkSendingOrderWhenRefillIsNotEnough() {
        facade.buyItems(1, 6);
        facade.addAllItems(1,1,"2022-06-01",12);
        assertEquals(2, ordersController.getFinalOrders(1).size());

    }

    @org.junit.Test
    public void stage7_checkStopSendingOrderAfterRefill() {
        facade.buyItems(1, 6);
        facade.addAllItems(1,1,"2022-06-01",12);
        cC.addAllItems(1,100,"2022-06-01",12);
        assertEquals(2, ordersController.getFinalOrders(1).size());// one order before and one order because of regular

    }

    @org.junit.Test
    public void stage8_checkUpdatingRegularOrdersAfterRefillTo0(){
        facade.buyItems(1, 6);
        facade.addAllItems(1,1,"2022-06-01",12);
        cC.addAllItems(1,100,"2022-06-01",12);
        facade.updateOrders();
        assertEquals(0, order.getCountProducts());
    }

    @org.junit.Test
    public void stage9_checkSendingRegularOrderWhenCountIs0() {
        facade.buyItems(1, 6);
        facade.addAllItems(1,1,"2022-06-01",12);
        cC.addAllItems(1,100,"2022-06-01",12);
        facade.updateOrders();
        assertEquals(2, ordersController.getFinalOrders(1).size());//2 were already there, checking no other order was added
    }

    @org.junit.Test
    public void stage9z_checkGotMinPriceForProduct(){
        facade.buyItems(1, 6);
        facade.addAllItems(1,1,"2022-06-01",12);
        cC.addAllItems(1,100,"2022-06-01",12);
        facade.updateOrders();
        assertEquals(2, ordersController.getProductWithMinPrice(1, 5).getPrice(), 0.0);
    }




}
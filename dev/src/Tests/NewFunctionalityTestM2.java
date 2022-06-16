package Tests;

import DAL.Connect;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.OrdersController;
import PresentationLayer.Menu;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.*;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class NewFunctionalityTestM2 {

    private CategoryController cC;
    private OrdersController ordersController;
    private FacadeSupplier_Storage facadeSupplier;
    private OrderFromSupplier order;
    private OrderFromSupplier order2;
    private static boolean setUpIsDone = false;
    @Before
    public void setUp() throws SQLException {
//            File file=new File("..\\dev\\superli.db");
//            file.delete();
            Connect.getInstance().deleteRecordsOfTables();
            cC = new CategoryController();
            facadeSupplier =new FacadeSupplier_Storage();
            ordersController=new OrdersController();
            cC.addCategory("first", 0);
            cC.addSubCategory("first", "first1");
            cC.addSubSubCategory("first", "first1", "first11");
            cC.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            cC.addAllItems(1,7,"2027-06-01",12);
            facadeSupplier.openAccount(1,"eli", 1, new String[]{"1"},0);
            facadeSupplier.openAccount(2,"eli2", 2, new String[]{"1"},0);
            facadeSupplier.addProductToSupplier(1, 1, 2, 1);
            facadeSupplier.addProductToSupplier(2, 2, 4, 1);
            String json = facadeSupplier.createOrder(1);
            order = Menu.fromJson(json,OrderFromSupplier.class);
            String json2 = facadeSupplier.createOrder(1);
            order2 = Menu.fromJson(json,OrderFromSupplier.class);
//            facade.createOrder(1);
//            facade.createOrder(1);
            String[]days=new String[1];
            days[0]="4";
            facadeSupplier.addFixedDeliveryDaysForOrder(1, order2.getOrderId(), days);
            facadeSupplier.updateOrders();
            facadeSupplier.addProductToOrder(1, order.getOrderId(), 1, 2);
            facadeSupplier.addProductToOrder(1, order2.getOrderId(), 2, 2);
            days[0]="1";
            facadeSupplier.addFixedDeliveryDaysForOrder(1, order.getOrderId(), days);
            setUpIsDone=true;
    }


    @org.junit.Test
    public void stage1_checkNotOrderingWhenNotDayBeforeOrder(){
        assertEquals(0, ordersController.getFinalOrders(1).size());
    }

    @org.junit.Test
    public void stage2_checkNotOrderingWhenRefillNotNeeded(){
        facadeSupplier.buyItems(1, 1);
        assertEquals(0, ordersController.getFinalOrders(1).size());
    }
    @org.junit.Test
    public void stage3_checkRequestingAnOrderAfterShortage() {
        facadeSupplier.buyItems(1, 6);
        assertEquals(1, ordersController.getFinalOrders(1).size());

    }

    @org.junit.Test
    public void stage4_checkUpdatingRegularOrdersCount(){
        facadeSupplier.buyItems(1, 6);
        facadeSupplier.updateOrders();
        assertEquals(cC.getProductWithId(1).getRefill(), order.getCountProducts());
    }


    @org.junit.Test
    public void stage5_checkSendingRegularOrder() {
        facadeSupplier.buyItems(1, 6);
        assertTrue(ordersController.getFinalOrders(1).size()>0);
    }

    @org.junit.Test
    public void stage6_checkSendingOrderWhenRefillIsNotEnough() {
        facadeSupplier.buyItems(1, 6);
        facadeSupplier.addAllItems(1,1,"2027-06-01",12);
        assertEquals(2, ordersController.getFinalOrders(1).size());

    }

    @org.junit.Test
    public void stage7_checkStopSendingOrderAfterRefill() {
        facadeSupplier.buyItems(1, 6);
        facadeSupplier.addAllItems(1,1,"2027-06-01",12);
        cC.addAllItems(1,100,"2027-06-01",12);
        assertEquals(2, ordersController.getFinalOrders(1).size());// one order before and one order because of regular

    }

    @org.junit.Test
    public void stage8_checkUpdatingRegularOrdersAfterRefillTo0(){
        facadeSupplier.buyItems(1, 6);
        facadeSupplier.addAllItems(1,1,"2027-06-01",12);
        cC.addAllItems(1,100,"2027-06-01",12);
        facadeSupplier.updateOrders();
        assertEquals(0, order.getCountProducts());
    }

    @org.junit.Test
    public void stage9_checkSendingRegularOrderWhenCountIs0() {
        facadeSupplier.buyItems(1, 6);
        facadeSupplier.addAllItems(1,1,"2027-06-01",12);
        cC.addAllItems(1,100,"2027-06-01",12);
        facadeSupplier.updateOrders();
        assertEquals(2, ordersController.getFinalOrders(1).size());//2 were already there, checking no other order was added
    }

    @org.junit.Test
    public void stage9z_checkGotMinPriceForProduct(){
        facadeSupplier.buyItems(1, 6);
        facadeSupplier.addAllItems(1,1,"2027-06-01",12);
        cC.addAllItems(1,100,"2027-06-01",12);
        facadeSupplier.updateOrders();
        assertEquals(2, ordersController.getProductWithMinPrice(1, 5).getPrice(), 0.0);
    }




}
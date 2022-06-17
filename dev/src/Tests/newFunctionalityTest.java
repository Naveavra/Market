package Tests;

import DAL.Connect;
import DAL.DriverDocDAO;
import DAL.OrderDocDAO;
import DomainLayer.Employees.JobType;
import DomainLayer.Employees.Time;
import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.OrdersController;
import PresentationLayer.Menu;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
import ServiceLayer.transport.OrderTransportService;
import ServiceLayer.transport.UserService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class NewFunctionalityTest {

    private CategoryController cC;
    private OrdersController ordersController;
    private FacadeSupplier_Storage facadeSupplier;

    @Before
    public void setUp(){
        cC = new CategoryController();
        facadeSupplier =new FacadeSupplier_Storage();
        ordersController=new OrdersController();
        Menu menu = new Menu();
        menu.loadInitialData();
    }


    @org.junit.Test
    public void stage1_checkSendingAnOrderAfterShortage(){
        facadeSupplier.buyItems(1, 3);
        assertEquals(1, ordersController.getFinalOrders(1).size());
    }

    @org.junit.Test
    public void stage2_checkTransportShippingTheOrder(){
        //need to check that in order4Dest there is 1 line with the correct orderDoc
        facadeSupplier.buyItems(1, 3);
        OrderDocDAO dao = new OrderDocDAO();
        int id = dao.getLastId()-1;
        ConcurrentHashMap<String,Integer> lst = dao.showSupplies(id+"","616");
        assertEquals(1,lst.size());
    }
//
//    @org.junit.Test
//    public void stage3_checkRightAmount(){
//        //need to check that in order4Dest there is 1 line with the correct orderDoc
//        facadeSupplier.buyItems(1, 3);
//        OrderDocDAO dao = new OrderDocDAO();
//        ConcurrentHashMap<String,Integer> lst = dao.showSupplies("5","616");
//        int count = 0;
//        for(String id : lst.keySet())
//            count+=lst.get(id);
//        assertEquals(cC.getProductWithId(1).getRefill(),count);
//    }

//    @org.junit.Test
//    public void stage3_checkStorageGotCorrectAmountFromTransport() {
//        facadeSupplier.buyItems(1, 4);
//        int before = cC.getProductWithId(1).getCurAmount();
////        facadeSupplier.getItemsFromTransport(orderDocId);
//        int added = cC.getProductWithId(1).getCurAmount()-before;
//        int inOrder = 0;
//        for(int count : ordersController.getAllProductsOfOrder(order.getOrderId()).values()){
//            inOrder +=count;
//        }
//        assertEquals(added, inOrder);
//
//    }
//
//    @org.junit.Test
//    public void stage4_checkGettingMessageWhenNoAvailableDriver(){
//        //make here the there is no driver available and try to send an order
//        try {
//            ProductSupplier p = new ProductsSupplierDAO().getProduct(1,1);
//            HashMap<ProductSupplier,Integer> suppl = new HashMap<>();
//            suppl.put(p,15);
//            facadeTransport.createAutoTransport("1","19/20/2030",suppl);
//            String ans = facadeTransport.displayMessages("318856994");
//            System.out.println(ans);
//            assertFalse(ans.isEmpty());
//
//        } catch (SQLException e) {
//            assertTrue(false);
//        }
//
//    }

}
package Tests;

import DAL.Connect;
import DAL.OrderDocDAO;
import DAL.ProductsSupplierDAO;
import DomainLayer.Employees.JobType;
import DomainLayer.Employees.Time;
import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.OrdersController;
import DomainLayer.Suppliers.ProductSupplier;
import PresentationLayer.Menu;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class newFunctionalityTest {

    private CategoryController cC;
    private OrdersController ordersController;
    private FacadeSupplier_Storage facadeSupplier;
    private FacadeEmployees_Transports facadeTransport;
    private OrderFromSupplier order;
    @Before
    public void setUp() throws SQLException {
            Connect.getInstance().deleteRecordsOfTables();
            cC = new CategoryController();
            facadeSupplier =new FacadeSupplier_Storage();
            facadeTransport=new FacadeEmployees_Transports();
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
            String[]days=new String[1];
            facadeSupplier.updateOrders();
            facadeSupplier.addProductToOrder(1, order.getOrderId(), 1, 2);
            days[0]="1234567";
            facadeSupplier.addFixedDeliveryDaysForOrder(1, order.getOrderId(), days);


            facadeTransport.createEmployee("333333333", "ziv cohen gvura", "123456", 1, "yahav", "good");
            facadeTransport.createEmployee("234567891", "gal halifa", "123456", 1000, "yahav", "good conditions");
            facadeTransport.createEmployee("123456780", "nadia zlenko", "123456", 1000, "yahav", "good conditions");
            facadeTransport.createEmployee("000000000", "may terem", "123456", 1, "yahav", "good");
            facadeTransport.createEmployee("123456789", "dan terem", "123456", 1000, "yahav", "good conditions");
            facadeTransport.createEmployee("111111111", "miki daniarov", "123456", 1, "yahav", "good");
            facadeTransport.certifyEmployee(JobType.SHIFT_MANAGER ,"318856994");
            facadeTransport.certifyEmployee( JobType.CASHIER,"333333333");
            facadeTransport.certifyEmployee(  JobType.CASHIER,"234567891");
            facadeTransport.certifyDriver("123456780","C");
            facadeTransport.certifyDriver( "000000000","C1");
            facadeTransport.certifyEmployee(JobType.MERCHANDISER ,"123456789");
            facadeTransport.certifyEmployee( JobType.STOCK_KEEPER ,"111111111");

//            Calendar calendar = Calendar.getInstance();
//            String sTodayDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
//            String sTodayMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
//            String sTodayYear = String.valueOf(calendar.get(Calendar.YEAR));
            facadeTransport.createShift(new ShiftPair(new ShiftDate(), Time.MORNING), "318856994",
                "333333333,234567891", "123456780,000000000", "123456789", "111111111");
            facadeTransport.addTruck("C","Truck1",15000,0);
            facadeTransport.addTruck("C1","Truck2",15000,0);




            //add here the new driver with the attributes needed to start the system

    }


    @org.junit.Test
    public void stage1_checkSendingAnOrderAfterShortage(){
        facadeSupplier.buyItems(1, 4);
        assertEquals(1, ordersController.getFinalOrders(1).size());
    }

    @org.junit.Test
    public void stage2_checkTransportShippingTheOrder(){
        //need to check that in order4Dest there is 1 line with the correct orderDoc
        facadeSupplier.buyItems(1, 4);
        OrderDocDAO dao = new OrderDocDAO();
        ConcurrentHashMap<String,Integer> lst = dao.showSupplies("1","616");
        assertEquals(1,lst.size());
    }

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
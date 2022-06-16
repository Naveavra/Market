package Tests;

import DAL.Connect;
import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.OrdersController;
import DomainLayer.Suppliers.ProductSupplier;
import PresentationLayer.Menu;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    }

    @org.junit.Test
    public void stage3_checkStorageGotCorrectAmountFromTransport() {
        facadeSupplier.buyItems(1, 4);
        int before = cC.getProductWithId(1).getCurAmount();
//        facadeSupplier.getItemsFromTransport(orderDocId);
        int added = cC.getProductWithId(1).getCurAmount()-before;
        int inOrder = 0;
        for(int count : ordersController.getAllProductsOfOrder(order.getOrderId()).values()){
            inOrder +=count;
        }
        assertEquals(added, inOrder);

    }

    @org.junit.Test
    public void stage4_checkGettingMessageWhenNoAvailableDriver(){
        //make here the there is no driver available and try to send an order
    }

}
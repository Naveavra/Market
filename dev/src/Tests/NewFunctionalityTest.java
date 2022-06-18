
package Tests;

import DAL.Connect;
import DAL.OrderDocDAO;
import DAL.ProductsSupplierDAO;
import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Storage.CategoryController;
import DomainLayer.Suppliers.OrderFromSupplier;
import DomainLayer.Suppliers.OrdersController;
import DomainLayer.Suppliers.ProductSupplier;
import PresentationLayer.Menu;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class NewFunctionalityTest {

    private CategoryController cC ;
    private OrdersController ordersController ;
    private FacadeSupplier_Storage facadeSupplier ;
    private FacadeEmployees_Transports facadeTransport;


    public void setUp() throws SQLException {
        Connect.getInstance().deleteRecordsOfTables();
        Menu menu = new Menu();
        menu.loadInitialData();

    }

    public void assign(){
        cC = new CategoryController();
        ordersController = new OrdersController();
        facadeSupplier = new FacadeSupplier_Storage();
        facadeTransport = new FacadeEmployees_Transports();
    }
    @org.junit.Test
    public void stage1_checkSendingAnOrderAfterShortage() throws SQLException {
        setUp();
        assign();
        facadeSupplier.buyItems(1, 3);
        assertEquals(1, ordersController.getFinalOrders(1).size());
    }

    @org.junit.Test
    public void stage2_checkTransportShippingTheOrder(){
        assign();
        //need to check that in order4Dest there is 1 line with the correct orderDoc
        OrderDocDAO dao = new OrderDocDAO();
        int id = dao.getLastId() -1;
        facadeSupplier.buyItems(1, 3);
        ConcurrentHashMap<String,Integer> lst = dao.showSupplies(id+"","616");
        assertEquals(1,lst.size());
    }

    @org.junit.Test
    public void stage3_checkStorageGotCorrectAmountFromTransport() {
        assign();
        OrderDocDAO dao = new OrderDocDAO();
        facadeSupplier.buyItems(1, 4);
        int id = dao.getLastId()-1;
        int before = cC.getProductWithId(1).getCurAmount();
        facadeSupplier.getItemsFromTransport(id);
        int added = cC.getProductWithId(1).getCurAmount()-before;
        int inOrder = 0;
        ConcurrentHashMap<String,Integer> lst = dao.showSupplies(id+"","616");
        for(String productId : lst.keySet())
            inOrder+=lst.get(productId);
        assertEquals(added, inOrder);

    }

    @org.junit.Test
    public void stage4_checkGettingMessageWhenNoAvailableDriver(){

        //make here the there is no driver available and try to send an order
        assign();
        facadeSupplier.buyItems(1,1);
        facadeSupplier.buyItems(1,1);
        String ans = facadeTransport.displayMessages("318856994");
        assertTrue(ans.length()>2);

    }

    @org.junit.Test
    public void stage5_checkRegularOrderIsSendingAnOrderAndTransportWhenNeeded(){
        assign();
        OrderDocDAO dao = new OrderDocDAO();
        int id = dao.getLastId()-1;
        while(id>0){
            facadeTransport.transportIsDone(id+"");
            id--;
        }
        facadeSupplier.updateOrders();
        id = dao.getLastId()-1;
        facadeSupplier.getItemsFromTransport(id);
        assertEquals(0, cC.getProductWithId(1).getRefill() );
    }
}

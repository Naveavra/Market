package Tests;

import DomainLayer.OrderFromSupplier;
import DomainLayer.ProductSupplier;
import DomainLayer.Supplier;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SupplierTest {
private Supplier s;
private Supplier s1;
private Map<String,String> sMap;
private Map<String,String> s1Map;
private ProductSupplier p1;
private ProductSupplier p2;

    @Before
    public void setUp() throws Exception {
        sMap=new HashMap<>();
        sMap.put("eyal", "eya;@gmail.com");
        s1Map=new HashMap<>();
        s1Map.put("ziv", "ziv@gmail.com");
//        s=new Supplier(1, "ziv", 11,sMap,true);
//        s1=new Supplier(2, "eyal", 12, s1Map,true);
//        p1=new ProductSupplier(0,6, 100);
//        p2=new ProductSupplier(1,7, 100);
    }

    @Test
    public void updateAccount() {
        s.updateAccount("ziv", 13, sMap);
        assertTrue(s.getName().equals("ziv")& s.getBankAccount()==13);
        for(String key:s.getContacts().keySet()){
            assertTrue(sMap.containsKey(key));
        }
    }

    @Test
    public void addContact() {
//        assertTrue(s.addContact("tamir", "tamir@gmail.com"));
//        assertFalse(s.addContact("tamir", "tamir@gmail.com"));
    }

    @Test
    public void addDiscount() {
        assertTrue(s.addDiscount(10, 0.5));
        assertFalse(s.addDiscount(10, 0.5));
        assertFalse(s.addDiscount(10, -0.5));
        assertFalse(s.addDiscount(-10, 0.5));
    }

    @Test
    public void removeDiscountOnAmount() {
        assertFalse(s.removeDiscountOnAmount(-10));
        assertFalse(s.removeDiscountOnAmount(10));
        s.addDiscount(10, 0.5);
        assertTrue(s.removeDiscountOnAmount(10));
    }

    @Test
    public void addProduct() {
//        assertTrue(s.addProduct(p1.getCatalogNumber(), p1.getName(), p1.getPrice())) ;
//        assertFalse(s.addProduct(p1.getCatalogNumber(), p1.getName(), p1.getPrice()));
//        assertFalse(s.addProduct(-334, p1.getName(), p1.getPrice()));
//        assertFalse(s.addProduct(p1.getCatalogNumber(), p1.getName(), -50));


    }

    @Test
    public void removeProduct() {
//        s.addProduct(p1.getCatalogNumber(), p1.getName(), p1.getPrice());
//        assertTrue(s.removeProduct(p1.getCatalogNumber()));
//        assertFalse(s.removeProduct(p1.getCatalogNumber()));
    }

    @Test
    public void createOrder() {
//        s.createOrder();
//        assertTrue(s.getActiveOrders().containsKey(0));
//        s.createOrder();
//        assertTrue(s.getActiveOrders().containsKey(1));
//        s.createOrder();
//        assertFalse(s.getActiveOrders().containsKey(3));

    }

    @Test
    public void getProduct() {
//      //  s.addProduct(p1.getCatalogNumber(), p1.getName(),p1.getPrice());
//        ProductSupplier p2=s.getProduct(p1.getCatalogNumber());
//        assertEquals(p2.getName(), p1.getName());
//        assertTrue(p2.getPrice()==p1.getPrice());
//        assertSame(p2.getCatalogNumber(), p1.getCatalogNumber());
//        s.removeProduct(p1.getCatalogNumber());
//        assertNull(s.getProduct(p1.getCatalogNumber()));
    }

    @Test
    public void updateTotalIncludeDiscounts() throws SQLException {
//        OrderFromSupplier o1 =s.createOrder();
//        o1.updateProductToOrder(p1,10);
//        o1.updateProductToOrder(p2,20);
//        double total = s.updateTotalIncludeDiscounts(o1.getOrderId());
//        assertEquals(3000.0,total,0.0);
//        s.finishOrder(0);
    }

    @Test
    public void finishOrder() {
//        OrderFromSupplier o2 =s.createOrder();
//        o2.updateProductToOrder(p1,10);
//        o2.updateProductToOrder(p2,20);
//        assertTrue(s.finishOrder(o2.getOrderId()));
//        assertFalse(s.finishOrder(o2.getOrderId()+1));
//        assertFalse(s.finishOrder(-1));
//        assertEquals(s.getFinalOrders().get(0).getOrderId(), o2.getOrderId());
    }


    @Test
    public void isProductExist() {
//        s.addProduct(1, "ziv", 100);
//        assertTrue(s.isProductExist(1));
//        assertFalse(s.isProductExist(12));
//        s.removeProduct(1);
//        assertFalse(s.isProductExist(1));
//        assertFalse(s.isProductExist(-1));
    }

    @Test
    public void closeAccount() {
        s.closeAccount();
        assertFalse(s.isActive());
    }
}
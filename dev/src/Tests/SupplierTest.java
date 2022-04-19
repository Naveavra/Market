package Tests;

import DomainLayer.Product;
import DomainLayer.Supplier;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SupplierTest {
private Supplier s;
private Supplier s1;
private Map<String,String> sMap;
private Map<String,String> s1Map;
private Product p1;
    @Before
    public void setUp() throws Exception {
        sMap=new HashMap<>();
        sMap.put("eyal", "eya;@gmail.com");
        s1Map=new HashMap<>();
        s1Map.put("ziv", "ziv@gmail.com");
        s=new Supplier(1, "ziv", 11,sMap );
        s1=new Supplier(2, "eyal", 12, s1Map);
        p1=new Product(0,"chair", 100);
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
        assertTrue(s.addContact("tamir", "tamir@gmail.com"));
        assertFalse(s.addContact("tamir", "tamir@gmail.com"));
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
        assertTrue(s.addProduct(p1.getCatalogNumber(), p1.getName(), p1.getPrice())) ;
        assertFalse(s.addProduct(p1.getCatalogNumber(), p1.getName(), p1.getPrice()));
        assertFalse(s.addProduct(334, p1.getName(), p1.getPrice()));
        assertFalse(s.addProduct(p1.getCatalogNumber(), p1.getName(), -50));


    }

    @Test
    public void removeProduct() {
        s.addProduct(p1.getCatalogNumber(), p1.getName(), p1.getPrice());
        assertTrue(s.removeProduct(p1.getCatalogNumber()));
        assertFalse(s.removeProduct(p1.getCatalogNumber()));
    }

    @Test
    public void createOrder() {
    }

    @Test
    public void getProduct() {
    }

    @Test
    public void updateTotalIncludeDiscounts() {
    }

    @Test
    public void finishOrder() {
    }

    @Test
    public void getName() {
    }

    @Test
    public void getOrder() {
    }

    @Test
    public void getActiveOrders() {
    }

    @Test
    public void isProductExist() {
    }

    @Test
    public void closeAccount() {
    }

    @Test
    public void isActive() {
    }

    @Test
    public void getProducts() {
    }

    @Test
    public void getProductsNames() {
    }

    @Test
    public void addToFinish() {
    }
}
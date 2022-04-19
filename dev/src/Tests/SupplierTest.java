package Tests;

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
    @Before
    public void setUp() throws Exception {
        sMap=new HashMap<>();
        sMap.put("eyal", "eya;@gmail.com");
        s1Map=new HashMap<>();
        s1Map.put("ziv", "ziv@gmail.com");
        s=new Supplier(1, "ziv", 11,sMap );
        s1=new Supplier(2, "eyal", 12, s1Map);
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
    }

    @Test
    public void addDiscount() {
    }

    @Test
    public void removeDiscountOnAmount() {
    }

    @Test
    public void addProduct() {
    }

    @Test
    public void removeProduct() {
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
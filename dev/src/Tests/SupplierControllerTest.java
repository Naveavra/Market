package Tests;

import DomainLayer.SupplierController;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SupplierControllerTest {
private SupplierController sc;
private Map<String,String> contacts;

    @Before
    public void setUp() throws Exception {
        sc=new SupplierController();
        contacts=new HashMap<>();
        contacts.put("ziv", "ziv@gmail.com");
        contacts.put("eyal", "eyal@gmail.com");
    }

    @Test
    public void openAccount() {
        assertTrue(sc.openAccount(1, "ziv", 12, contacts,true));
        assertFalse(sc.openAccount(1, "ziv", 12, contacts,true));
        assertFalse(sc.openAccount(1, "ziv", -12, contacts,true));
        assertFalse(sc.openAccount(-1, "ziv", 12, contacts,true));

    }

    @Test
    public void closeAccount() {
       sc.openAccount(2, "ziv", 12, contacts,true);
        assertTrue(sc.closeAccount(2));
        assertFalse(sc.closeAccount(2));
        assertFalse(sc.closeAccount(-1));

    }
}
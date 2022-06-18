package Tests;

import DAL.Connect;
import DomainLayer.Suppliers.ProductSupplier;
import DomainLayer.Suppliers.SupplierController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class SupplierControllerTest {
private SupplierController sc;

    @Before
    public void setUp() throws SQLException {
            Connect.getInstance().deleteRecordsOfTables();
            sc=new SupplierController();
            sc.openAccount(1, "ziv", 12, new String[]{"1"},0);
            sc.openAccount(2, "ziv", 12, new String[]{"1"},0);
    }

    @Test
    public void openAccount() {
        assertFalse(sc.openAccount(1, "ziv", 12,new String[]{"1"},0));
        assertFalse(sc.openAccount(1, "ziv", -12,new String[]{"1"},0));
        assertFalse(sc.openAccount(-1, "ziv", 12, new String[]{"1"},0));

    }



    @Test
    public void updateAccount() {
        sc.getSupplier(1).updateAccount("ziv", 13);
        assertTrue(sc.getSupplier(1).getName().equals("ziv")& sc.getSupplier(1).getBankAccount()==13);
    }

    @Test
    public void addContact() {
      assertTrue(sc.getSupplier(1).addContact("tamir", "tamir@gmail.com", "0501234567"));
      assertFalse(sc.getSupplier(1).addContact("tamir", "tamir@gmail.com", "0501234567"));
    }

    @Test
    public void addDiscount() {
        Assert.assertTrue(sc.getSupplier(1).addDiscount(10, 0.5));
        Assert.assertFalse(sc.getSupplier(1).addDiscount(10, 0.5));
        Assert.assertFalse(sc.getSupplier(1).addDiscount(10, -0.5));
        Assert.assertFalse(sc.getSupplier(1).addDiscount(-10, 0.5));
    }

    @Test
    public void removeDiscountOnAmount() {
        Assert.assertFalse(sc.getSupplier(1).removeDiscountOnAmount(-10));
        sc.getSupplier(1).addDiscount(10, 0.5);
        Assert.assertTrue(sc.getSupplier(1).removeDiscountOnAmount(10));
    }

    @Test
    public void addProduct() {
        Assert.assertTrue(sc.getSupplier(1).addProduct(1, 100,5,1)) ;
        Assert.assertFalse(sc.getSupplier(1).addProduct(1,100,5,1));
        Assert.assertFalse(sc.getSupplier(1).addProduct(-334, 50,5, 2));
        Assert.assertFalse(sc.getSupplier(1).addProduct(2, 50,5, -50));


    }

    @Test
    public void removeProduct() {
        sc.getSupplier(1).addProduct(1, 100,5,1);
        Assert.assertTrue(sc.getSupplier(1).removeProduct(1));
        Assert.assertFalse(sc.getSupplier(1).removeProduct(1));
    }

    @Test
    public void getProduct() {
        sc.getSupplier(1).addProduct(1,100,5,1);
        ProductSupplier p2=sc.getSupplier(1).getProduct(1);
        assertEquals(p2.getProductId(), 1);
        assertEquals(p2.getPrice(), 5, 0.0);
        assertSame(p2.getCatalogNumber(), 1);
        sc.getSupplier(1).removeProduct(1);
        assertNull(sc.getSupplier(1).getProduct(1));
    }


    @Test
    public void isProductExist() {
        sc.getSupplier(1).addProduct(1,100,5,1);
        Assert.assertTrue(sc.getSupplier(1).isProductExist(1));
        Assert.assertFalse(sc.getSupplier(1).isProductExist(12));
        sc.getSupplier(1).removeProduct(1);
        Assert.assertFalse(sc.getSupplier(1).isProductExist(1));
        Assert.assertFalse(sc.getSupplier(1).isProductExist(1));
    }


    @Test
    public void closeAccount() {
        assertTrue(sc.closeAccount(2));
        assertFalse(sc.closeAccount(2));
        assertFalse(sc.closeAccount(-1));

    }
}
package Tests;

import DomainLayer.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProductTest {
private Product p;
private Product p1;
    @Before
    public void setUp() throws Exception {
        p=new Product(0, "chair", 10);
        p1=new Product(1,"table", 50);

    }

    @Test
    public void setName() {
        p.setName("pen");
        assertEquals("pen", p.getName());
    }

    @Test
    public void getName() {
        assertEquals("chair", p.getName());
    }

    @Test
    public void setPrice() {
        p.setPrice(5);
        assertEquals(5, p.getPrice(), 0.0);
    }

    @Test
    public void addDiscount() {
        p.addDiscount(15,0.5);
        assertTrue(p.getDiscount().containsKey(15));
        p1.addDiscount(20, 0.4);
        assertFalse(p1.getDiscount().containsKey(15));
    }

    @Test
    public void removeDiscountOnProduct() {
        assertFalse(p.getDiscount().containsKey(15));
        p.addDiscount(15,0.5);
        p.removeDiscountOnProduct(15);
        assertFalse(p.getDiscount().containsKey(15));
    }

    @Test
    public void getPriceAfterDiscount() {
        double total =p.getPriceAfterDiscount(5);
        assertEquals(50, total, 0.0);
        p.addDiscount(15,0.5);
        total =p.getPriceAfterDiscount(5);
        assertEquals(50, total, 0.0);
        p.addDiscount(4,0.5);
        total=p.getPriceAfterDiscount(5);
        assertEquals(25, total, 0.0);
    }

    @Test
    public void getPrice() {
        assertEquals(10, p.getPrice(), 0.0);
        assertEquals(50, p1.getPrice(), 0.0);
    }
}
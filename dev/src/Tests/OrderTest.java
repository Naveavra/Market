package Tests;


import DomainLayer.Order;
import DomainLayer.Product;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class OrderTest {
    private Order order;
    private Product p;
    private Product p1;
    @org.junit.Before
    public void setUp() throws Exception {
         order =new Order(0);
         p=new Product(0, "chair", 100);
         p1=new Product(1,"table", 25);
    }

    @org.junit.Test
    public void updateProductToOrder() {
        assertTrue(order.updateProductToOrder(p,10));
        assertTrue(order.getProducts().containsKey(p));

    }

    @org.junit.Test
    public void getTotalIncludeDiscounts() {
        order.updateProductToOrder(p,10);
        order.updateProductToOrder(p1,10);
        assertEquals(1250.0, order.getTotalIncludeDiscounts(), 0.0);

    }

    @org.junit.Test
    public void getCountProducts() {
        order.updateProductToOrder(p,10);
        assertEquals(1,order.getCountProducts());
        order.removeProductFromOrder(p);
        assertEquals(0,order.getCountProducts());
    }

    @org.junit.Test
    public void removeProductFromOrder() {
        order.updateProductToOrder(p,10);
        assertFalse(order.removeProductFromOrder(p));
        assertFalse(order.getProducts().containsKey(p));
    }

    @org.junit.Test
    public void getProducts() {
        order.updateProductToOrder(p,10);
        Map<Product,Integer> tmp=new HashMap<>();
        tmp.put(p,10);
        assertEquals(tmp,order.getProducts());

    }
}
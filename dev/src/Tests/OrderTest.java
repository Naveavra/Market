package Tests;


import DomainLayer.OrderFromSupplier;
import DomainLayer.ProductSupplier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class OrderTest {
    private OrderFromSupplier order;
    private ProductSupplier p;
    private ProductSupplier p1;
    @org.junit.Before
    public void setUp() throws Exception {
         order =new OrderFromSupplier(0);
         p=new ProductSupplier(1,1,100,1);
         p1=new ProductSupplier(1,2,50,2);
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
        assertEquals(10,order.getCountProducts());
        order.removeProductFromOrder(p);
        assertEquals(0,order.getCountProducts());
    }

    @org.junit.Test
    public void removeProductFromOrder() {
        order.updateProductToOrder(p,10);
        assertTrue(order.removeProductFromOrder(p));
        assertFalse(order.getProducts().containsKey(p));
    }

    @org.junit.Test
    public void getProducts() {
        order.updateProductToOrder(p,10);
        Map<ProductSupplier,Integer> tmp=new HashMap<>();
        tmp.put(p,10);
        assertEquals(tmp,order.getProducts());

    }
}
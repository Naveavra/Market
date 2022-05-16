package Tests;

import DAL.Connect;
import DomainLayer.Facade;
import DomainLayer.Supplier.Discount;
import DomainLayer.Supplier.ProductSupplier;
import DomainLayer.Supplier.SupplierController;
import ServiceLayer.ProductSupplierService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.*;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class ProductSupplierTest {
private Facade facade;
private static boolean setUpIsDone = false;
private SupplierController supplierController;
    @Before
    public void setUp(){
        if(!setUpIsDone) {
            File file=new File("..\\dev\\superli.db");
            file.delete();
            facade=new Facade();
            supplierController=new SupplierController();
            facade.openAccount(1, "eli", 3, true);
            facade.addProduct(1, 1, 5, 1);
            setUpIsDone=true;
        }
        else{
            facade=new Facade();
            supplierController=new SupplierController();
        }

    }

    @Test
    public void stage1_addDiscount() {
        facade.addDiscount(1, 1, 2,0.5);
        boolean ans=false;
        for( Discount discount : supplierController.getSupplier(1).getProduct(1).getDiscount())
            ans= discount.getDiscount() == 0.5 || ans;
        assertTrue(ans);
    }

    @Test
    public void stage2_removeDiscountOnProduct() {
        facade.removeDiscountOnProduct(1, 1, 2);
        assertEquals(1, supplierController.getSupplier(1).getProduct(1).getDiscount().size());
    }

    @Test
    public void stage3_getPriceAfterDiscount() {
        facade.addDiscount(1, 1, 2,0.5);
        double total =supplierController.getSupplier(1).getProduct(1).getPriceAfterDiscount(2);
        assertEquals(5, total, 0.0);
    }

    @Test
    public void stage4_getPrice() {
        assertEquals(5, supplierController.getSupplier(1).getProduct(1).getPrice(), 0.0);
    }
}
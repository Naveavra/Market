package Tests;

import DAL.Connect;
import DomainLayer.Facade;
import DomainLayer.Supplier.Discount;
import DomainLayer.Supplier.SupplierController;
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
    public void setUp() throws SQLException {
        if(!setUpIsDone) {
            Connect.getInstance().deleteRecordsOfTables();
            facade=new Facade();
            supplierController=new SupplierController();
            facade.addCategory("first");
            facade.addSubCat("first", "first1");
            facade.addSubSubCat("first", "first1", "first11");
            facade.addCategory("second");
            facade.addSubCat("second", "second1");
            facade.addSubSubCat("second", "second1", "second11");
            facade.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            facade.addNewProduct(2,"eggs","hello",4,"me","first","first1", "first11");
            facade.addAllItems(1,4,"2022-06-01",12);
            facade.openAccount(1, "eli", 3, true);
            facade.addProductToSupplier(1, 1, 5, 1);
            //setUpIsDone=true;
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
        assertEquals(5, total, 0.1);//total = 2.0
    }

    @Test
    public void stage4_getPrice() {
        assertEquals(5, supplierController.getSupplier(1).getProduct(1).getPrice(), 0.1);
    }
}
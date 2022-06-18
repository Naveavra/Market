package Tests;

import DAL.Connect;
import DomainLayer.FacadeSupplier_Storage;
import DomainLayer.Suppliers.Discount;
import DomainLayer.Suppliers.SupplierController;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;

import static org.junit.Assert.*;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class ProductSupplierTest {
private FacadeSupplier_Storage facadeSupplier;
private static boolean setUpIsDone = false;
private SupplierController supplierController;
    @Before
    public void setUp() throws SQLException {
        if(!setUpIsDone) {
            Connect.getInstance().deleteRecordsOfTables();
            facadeSupplier =new FacadeSupplier_Storage();
            supplierController=new SupplierController();
            facadeSupplier.addCategory("first");
            facadeSupplier.addSubCat("first", "first1");
            facadeSupplier.addSubSubCat("first", "first1", "first11");
            facadeSupplier.addCategory("second");
            facadeSupplier.addSubCat("second", "second1");
            facadeSupplier.addSubSubCat("second", "second1", "second11");
            facadeSupplier.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            facadeSupplier.addNewProduct(2,"eggs","hello",4,"me","first","first1", "first11");
            facadeSupplier.addAllItems(1,4,"2027-06-01",12);
            facadeSupplier.openAccount(1, "eli", 3, new String[]{"1"},0);
            facadeSupplier.addProductToSupplier(1, 1, 50,5, 1);
            //setUpIsDone=true;
        }
        else{
            facadeSupplier =new FacadeSupplier_Storage();
            supplierController=new SupplierController();
        }

    }

    @Test
    public void stage1_addDiscount() {
        facadeSupplier.addDiscount(1, 1, 2,0.5);
        boolean ans=false;
        for( Discount discount : supplierController.getSupplier(1).getProduct(1).getDiscount())
            ans= discount.getDiscount() == 0.5 || ans;
        assertTrue(ans);
    }

    @Test
    public void stage2_removeDiscountOnProduct() {
        facadeSupplier.removeDiscountOnProduct(1, 1, 2);
        assertEquals(1, supplierController.getSupplier(1).getProduct(1).getDiscount().size());
    }

    @Test
    public void stage3_getPriceAfterDiscount() {
        facadeSupplier.addDiscount(1, 1, 2,0.5);
        double total =supplierController.getSupplier(1).getProduct(1).getPriceAfterDiscount(2);
        assertEquals(5, total, 0.1);//total = 2.0
    }

    @Test
    public void stage4_getPrice() {
        assertEquals(5, supplierController.getSupplier(1).getProduct(1).getPrice(), 0.1);
    }
}
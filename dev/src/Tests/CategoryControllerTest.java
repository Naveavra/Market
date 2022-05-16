package Tests;

import DomainLayer.Storage.CategoryController;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.junit.Assert.*;
@FixMethodOrder( MethodSorters.NAME_ASCENDING ) // force name ordering
public class CategoryControllerTest {

    private CategoryController cC;
    private static boolean setUpIsDone = false;
    @Before
    public void setUp()
    {
        if(!setUpIsDone) {
            File file=new File("..\\dev\\superli.db");
            file.delete();
            cC = new CategoryController();
            cC.addCategory("first", 0);
            cC.addSubCategory("first", "first1");
            cC.addSubSubCategory("first", "first1", "first11");
            cC.addCategory("second", 0);
            cC.addSubCategory("second", "second1");
            cC.addSubSubCategory("second", "second1", "second11");
            cC.addNewProduct(1,"milk","hello",3,"me","first","first1", "first11");
            cC.addNewProduct(2,"eggs","hello",4,"me","first","first1", "first11");
            cC.addAllItems(1,4,"2022-06-01",12);
            setUpIsDone=true;
        }
        else {
            cC = new CategoryController();
        }
    }

    @org.junit.Test
    public void stage1_getProductWithId() {
        assertEquals("the product is not the same", "milk", cC.getProductWithId(1).getName());
    }

    @org.junit.Test
    public void stage2_setDiscountToOneItem() {
        cC.setDiscountToOneItem(1,10);
        assertEquals("the discount was not set", 10, cC.getProductWithId(1).getDiscount(), 0.0);
        cC.setDiscountToOneItem(1, 0);
    }

    @org.junit.Test
    public void stage3_defineAsDamaged(){
        cC.defineAsDamaged(1,"very bad", "STORAGE",12,"2022-06-01");
        assertEquals("was not defined as bad", 1, cC.getDamagedItems().size());
    }

    @org.junit.Test
    public void stage4_buyItems() {
        double price=cC.buyItems(1, 2);
        assertEquals(6, price, 0.0);
    }

    @org.junit.Test
    public void stage5_transferItem() {
        cC.moveItemsToStore(1,1);
        assertEquals(1, cC.getProductWithId(1).getStoreAmount());
    }
    @org.junit.Test
    public void stage6_getProductIdWithName() {
        int id=cC.getProductIdWithName("milk");
        assertEquals(1, id);
    }

    @org.junit.Test
    public void stage6_setDiscountToCategory() {
        cC.setDiscount("first", 30);
        assertEquals("the discount was not set", 30, cC.getProductWithId(1).getDiscount(), 0.0);
        cC.setDiscount("first", 0);
    }

    @org.junit.Test
    public void stage7_transferProductToAnotherCategory() {
        assertEquals("products were not inserted correctly", 2, cC.getProductsOfCategory("first").size());
        assertEquals("products were not inserted correctly", 0, cC.getProductsOfCategory("second").size());
        cC.transferProduct(2, "second", "second1", "second11");
        assertEquals("product has not transferred", 1, cC.getProductsOfCategory("first").size());
        assertEquals("product has not transferred", 1, cC.getProductsOfCategory("second").size());
        cC.setDiscount("first", 0);
    }

    @org.junit.Test
    public void stage8_removeCategoryFromManu() {
        cC.removeCat("first");
        assertTrue("the category was removed when not needed", cC.hasCategory("first"));
    }

    @org.junit.Test
    public void stage9_removeProductFromManu() {
        cC.removeFromCatalog(1);
        assertNull("the product was not removed", cC.getProductWithId(1));
    }

    @org.junit.Test
    public void stage9z_removeCategoryFromManu() {
        cC.removeCat("first");
        assertFalse("the category was not removed when needed", cC.hasCategory("first"));
    }



}
package PresentationLayer;

import ServiceLayer.ProductService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class ProductMenu {

    private Supplier supplier;
    private Scanner sc;
    private SupplierMenu sm;
    private SupplierService ss;
    private ProductService ps;

    public ProductMenu(Supplier s) {
        sc=new Scanner(System.in);
        supplier = s;
        sm = new SupplierMenu();
        ss = new SupplierService();
        ps = new ProductService();
    }


    public void manageProductsSupplierMenu() {
        System.out.println("Manage products of supplier: "+ supplier.getSupplierName());
        System.out.println("Choose what you want to do:");
        System.out.println("\t1. watch all supplier products.");
        System.out.println("\t2. Add new product.");
        System.out.println("\t3. Remove product.");
        System.out.println("\t4. Update product.");
        System.out.println("\t5. Add new discount on count of product.");
        System.out.println("\t6. Return to supplier page.");

        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            manageProductsSupplierMenu();
        }
        switch (choise){
            case 1:
                watchSupplierProducts(supplier);
                break;
            case 2:
                addProduct(supplier);
                break;
            case 3:
                removeProduct(supplier);
                break;
            case 4:
                updateProduct(supplier);
                break;
            case 5:
                addDiscountOnProduct(supplier);
                break;
            case 6:
                sm.inSupplierMenu(supplier.getSupplierNumber());
                break;
        }
    }

    private void addProduct(Supplier supplier) {
        System.out.println("Add new product:");
        System.out.println("Enter catalog number:");
        int catalogNum = 0;
        try{catalogNum = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            manageProductsSupplierMenu();
        }
        System.out.println("Enter name of product:");
        String name = sc.next();
        System.out.println("Enter price:");
        int price = 0;
        try{price = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            manageProductsSupplierMenu();
        }
        boolean added=ps.addProduct(supplier.getSupplierNumber(), catalogNum,name, price);
        if(added) {
            System.out.println("product added");
        }
        else{
            System.out.println("The product is already exist or the price is invalid ");
        }
        manageProductsSupplierMenu();

    }

    private void removeProduct(Supplier supplier) {
        System.out.println("Remove product:");
        System.out.println("Enter catalog number of the product:");
        int catalogNum = 0;
        try{catalogNum = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            removeProduct(supplier);
        }
        boolean removed=ps.removeProduct(supplier.getSupplierNumber(), catalogNum);
        if(removed) {
            System.out.println("product remove");
        }
        else{
            System.out.println("product didn't exist");
        }
        manageProductsSupplierMenu();
    }

    private void updateProduct(Supplier supplier) {
        System.out.println("Update product:");
        System.out.println("Enter catalog number of the product:");
        int catalogNum = 0;
        try{catalogNum = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            updateProduct(supplier);
        }
        System.out.println("Enter new name for product:");
        String newName = sc.next();
        System.out.println("Enter new price for product:");
        int price = 0;
        try{price = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            updateProduct(supplier);
        }
        boolean updated=ps.updateProduct(supplier.getSupplierNumber(),catalogNum, newName, price);
        if(updated) {
            System.out.println("product updated");
        }
        else{
            System.out.println("product didn't found or the price was invalid number");
        }
        manageProductsSupplierMenu();
    }

    private void watchSupplierProducts(Supplier supplier) {
        String json = ps.getProductsOfSupplier(supplier.getSupplierNumber());
        Gson gson = new Gson();
        Map<Integer, LinkedTreeMap> products = new HashMap<>();
        products = gson.fromJson(json,products.getClass());
        int i = 1;
        if(products == null){
            System.out.println("There is no products to show");
            manageProductsSupplierMenu();
        }
        assert products != null;
        for (LinkedTreeMap p: products.values()){
            Product product = Menu.fromJson(p.toString(), Product.class);
            System.out.println(i + ". "+ product.toString());
            i++;
        }
        manageProductsSupplierMenu();
    }
    public void addDiscountOnProduct(Supplier supplier){
        System.out.println("enter catalog number of product to add discount");
        int catalogNum = 0;
        try{catalogNum = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            manageProductsSupplierMenu();
        }
        System.out.println("enter amount of discount");
        double discount = 0;
        try{discount = sc.nextDouble();}
        catch (Exception e){
            System.out.println("you must enter only number");
            manageProductsSupplierMenu();
        }
        System.out.println("Please write on how much product you want add discount?");
        int count = 0;
        try{count = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only digits number");
            manageProductsSupplierMenu();
        }
        boolean added = ss.addDiscount(supplier.getSupplierNumber(),catalogNum ,count , discount);
        if (added){
            System.out.println("discount added to product");
        }
        else{
            System.out.println("invalid input");
        }

        manageProductsSupplierMenu();
    }
}

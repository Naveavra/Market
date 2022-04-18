package PresentationLayer;

import ServiceLayer.ProductService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("\t5. Return to supplier page.");

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
        ps.addProduct(supplier.getSupplierNumber(), catalogNum,name, price);
        System.out.println("product added");
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
        ps.removeProduct(supplier.getSupplierNumber(), catalogNum);
        System.out.println("product remove");
        manageProductsSupplierMenu();
    }

    private void updateProduct(Supplier supplier) {
        System.out.println("");

    }

    private void watchSupplierProducts(Supplier supplier) {
        String json = ps.getProductsOfSupplier(supplier.getSupplierNumber());
        Gson gson = new Gson();
        List<Product>products = new ArrayList<>();
        products = gson.fromJson(json,products.getClass());
        int i= 1;
        for (Product p: products){
            System.out.println(i + ". "+ p);
            i++;
        }
        manageProductsSupplierMenu();
    }
}

package PresentationLayer;

import ServiceLayer.SupplierService;
import com.google.gson.Gson;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SupplierMenu {

    private Scanner sc=new Scanner(System.in);
    private SupplierService ss = new SupplierService();

    public void chooseSupplierMenu() {
        System.out.println("We enter to Suppliers page:");
        System.out.println("Please choose what you whant to do:");
        System.out.println("\t1. Open new Supplier");
        System.out.println("\t2. See details of Supplier that exist in tha system");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            chooseSupplierMenu();
        }
        switch (choise){
            case 1:
                openNewAccountSupplier();
                break;
            case 2:
                int supNumber = 0;
                System.out.println("Enter the supplier number you whant to see:");
                supNumber =- sc.nextInt();
                inSupplierMenu(supNumber);
                break;
            default:
                System.out.println("You must type digit 1 to 2");
                chooseSupplierMenu();

        }


    }

    private void inSupplierMenu(int supplierNumber) {
        String json = ss.getSupplier(supplierNumber);
        Gson gson = new Gson();
        Supplier s = gson.fromJson(json, Supplier.class);
        System.out.println("You see details of supplier: "+ s.getSupplierName());
        System.out.println("Choose what you want to do:");
        System.out.println("\t1. Watch supplier details.");
        System.out.println("\t2. update supplier details.");
        System.out.println("\t3. manage supplier products.");
        System.out.println("\t4. Add discount on amount of products to supplier.");
        System.out.println("\t5. Create new order from the supplier.");
        System.out.println("\t6. Watch existing orders from the supplier.");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            inSupplierMenu(supplierNumber);
        }
        switch (choise){
            case 1:
                seeSupplierDetails(s);
                break;
            case 2:
                updateSupplierDetails(s);
                break;
            case 3:
                ProductMenu pm = new ProductMenu(s);
                pm.manageProductsSupplierMenu();
                break;
            case 4:
                addDiscountMenu(supplierNumber);
                break;
            case 5:
                OrderMenu om = new OrderMenu(s);
                om.newOrder();
                break;
            case 6:
                OrderMenu om6 = new OrderMenu(s);
                om6.watchOrdersMenu();
                break;
            default:
                System.out.println("You must type digit 1 to 2");
                inSupplierMenu(supplierNumber);

        }

    }

    private void addDiscountMenu(int supplierNum) {
        System.out.println("Please write on how much product you want add discount?");
        int count = 0;
        try{count = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only digits number");
            addDiscountMenu(supplierNum);
        }
        System.out.println("Please write the discount?");
        double discount = 1;
        try{discount = sc.nextDouble();}
        catch (Exception e){
            System.out.println("you must enter only number");
            addDiscountMenu(supplierNum);
        }
        ss.addDiscount(supplierNum,count,discount);
        System.out.println("discount is added to supplier");
        new Menu().intialMenu();

    }

    private void seeSupplierDetails(Supplier s) {
        System.out.println(s.toString());
        chooseSupplierMenu();


    }

    private void updateSupplierDetails(Supplier s) {//need to imp
        System.out.println(s.toString());

        chooseSupplierMenu();

    }


    private void openNewAccountSupplier() {
        System.out.println("You open new supplier Account:");
        String supName = "";
        System.out.println("Enter Supplier's name: ");
        supName = sc.next();
        int supNumber = 0;
        System.out.println("Enter Supplier's number: ");
        supNumber = sc.nextInt();

        int bankNumber = 0;
        System.out.println("Enter Supplier's bankNumber: ");
        bankNumber = sc.nextInt();

        int countContacts = 0;
        System.out.println("Enter count of contacts you have: ");
        countContacts = sc.nextInt();
        Map<String,String> contacts = new HashMap<>();
        for (int i=1; i<=countContacts; i++){
            System.out.println(i + ". name: ");
            String name = sc.next();
            System.out.println(i + ". email: ");
            String email = sc.next();
            contacts.put(name,email);
        }
        ss.openAccount(supNumber,supName, bankNumber,contacts);
        System.out.println("The account opened");

        chooseSupplierMenu();


    }
}

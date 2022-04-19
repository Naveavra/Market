package PresentationLayer;

import ServiceLayer.SupplierService;
import com.google.gson.Gson;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SupplierMenu {

    private Scanner sc=new Scanner(System.in);
    private SupplierService ss = new SupplierService();

    public void chooseSupplierMenu() {
        CharSequence charSequence ="qwertyuiopasdfghjklzxcvbnm";
        System.out.println("***You enter to Suppliers page:***");
        System.out.println("Please choose what you whant to do:");
        System.out.println("\t1. Open new Supplier");
        System.out.println("\t2. See details of Supplier that exist in tha system");
        int choice = 0;
        try{
            choice = sc.nextInt();
            if(Integer.toString(choice).contains(charSequence)){
                System.out.println("you must enter a valid number");
                chooseSupplierMenu();
            }
        }
        catch (Exception e){

            System.out.println("you must enter only 1 digit number");


        }
        switch (choice){
            case 1:
                openNewAccountSupplier();
                break;
            case 2:
                int supNumber = 0;
                System.out.println("Enter the supplier number you want to see:");
                supNumber = sc.nextInt();
                inSupplierMenu(supNumber);
                break;
            default:
                System.out.println("You must type digit 1 to 2");
                chooseSupplierMenu();

        }


    }

    public void inSupplierMenu(int supplierNumber) {
        String json = ss.getSupplier(supplierNumber);
        if (json == null || json.equals("null")){
            System.out.println("supplier number don't found");
            chooseSupplierMenu();
            return;
        }
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
        System.out.println("\t7. close supplier account.");
        System.out.println("\t8. Return to choose anther supplier.");
        int choice = 0;
        try{choice = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            inSupplierMenu(supplierNumber);
        }
        if(choice >7 || choice<=0){
            System.out.println("you must enter only 1 digit number");
        }
        switch (choice){
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
            case 7:
                ss.closeAccount(supplierNumber);
            case 8:
                chooseSupplierMenu();
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
        inSupplierMenu(supplierNum);

    }

    private void seeSupplierDetails(Supplier s) {
        System.out.println(s.toString());
        inSupplierMenu(s.getSupplierNumber());


    }

    private void updateSupplierDetails(Supplier s) {//need to imp
        System.out.println(s.toString());
        System.out.println("Choose what you want to update:");
        System.out.println("\t1. supplier name");
        System.out.println("\t2. bank number");
        System.out.println("\t3. contacts");
        System.out.println("\t4. Return to supplier page");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only number");
            updateSupplierDetails(s);
        }
        switch (choise){
            case 1:
                String newSupplierName = "";
                System.out.println("Enter Supplier's name: ");
                newSupplierName = sc.next();
                ss.updateAccount(s.getSupplierNumber(),newSupplierName, s.getBankNumber(), s.getContacts());
                s.setSupplierName(newSupplierName);
                break;
            case 2:
                int bankNumber = 0;
                System.out.println("Enter Supplier's bankNumber: ");
                try{bankNumber = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only digits number");
                    openNewAccountSupplier();
                }
                ss.updateAccount(s.getSupplierNumber(),s.getSupplierName(), bankNumber, s.getContacts());
                s.setBankAccount(bankNumber);
                break;
            case 3://contacts
                int countContacts = 0;
                System.out.println("Enter count of contacts you have: ");
                try{countContacts = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only digits number");
                    openNewAccountSupplier();
                }
                Map<String,String> contacts = new HashMap<>();
                for (int i=1; i<=countContacts; i++){
                    System.out.println(i + ". name: ");
                    String name = sc.next();
                    System.out.println(i + ". email: ");
                    String email = sc.next();
                    contacts.put(name,email);
                }
                ss.updateAccount(s.getSupplierNumber(),s.getSupplierName(), s.getBankNumber(), contacts);
                break;
            case 4:
                inSupplierMenu(s.getSupplierNumber());
                break;

        }
        updateSupplierDetails(s);

    }


    private void openNewAccountSupplier() {
        System.out.println("You open new supplier Account:");
        String supName = "";
        System.out.println("Enter Supplier's name: ");
        supName = sc.next();
        int supNumber = 0;
        System.out.println("Enter Supplier's number: ");
        try{supNumber = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        if(supNumber<=0){
            System.out.println("you must enter only positive number");
            openNewAccountSupplier();
        }

        int bankNumber = 0;
        System.out.println("Enter Supplier's bankNumber: ");
        try{bankNumber = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        if(bankNumber<=0){
            System.out.println("you must enter only positive number");
            openNewAccountSupplier();
        }
        int countContacts = 0;
        System.out.println("Enter count of contacts you have: ");
        try{countContacts = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        if(countContacts<0) {
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        Map<String,String> contacts = new HashMap<>();
        for (int i=1; i<=countContacts; i++){
            System.out.println(i + ". name: ");
            String name = sc.next();
            System.out.println(i + ". email: ");
            String email = sc.next();
            if(!checkEmail(email)){
                System.out.println("you must enter a valid email");
                i=i-1;
            }
            else {
                contacts.put(name, email);
            }
        }
        boolean open=ss.openAccount(supNumber,supName, bankNumber,contacts);
        if(open) {
            System.out.println("The account opened");
            chooseSupplierMenu();
        }
        else{
            System.out.println("one of the details is invalid");
            openNewAccountSupplier();
        }
    }
    public boolean checkEmail(String email){
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}

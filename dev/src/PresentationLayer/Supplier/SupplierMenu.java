package PresentationLayer.Supplier;

import ServiceLayer.DeliveryService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SupplierMenu {

    private Scanner sc=new Scanner(System.in);
    private SupplierService ss = new SupplierService();
    private DeliveryService ds =new DeliveryService();

    public void chooseSupplierMenu() {
        System.out.println("***You enter to Suppliers page:***");
        System.out.println("Please choose what you whant to do:");
        System.out.println("\t1. Open new Supplier");
        System.out.println("\t2. See details of Supplier that exist in tha system");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){

            System.out.println("you must enter only 1 digit number");
            chooseSupplierMenu();
        }
        switch (choice){
            case 1:
                openNewAccountSupplier();
                break;
            case 2:
                System.out.println("Enter the supplier number you want to see:");
                choiceStr = "";
                int supNumber =0;
                try{
                    choiceStr = sc.next();
                    supNumber=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    chooseSupplierMenu();
                }
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
        if(!s.isActive()){
            System.out.println("supplier account is closed");
            chooseSupplierMenu();
            return;
        }
        System.out.println("You see details of supplier: "+ s.getSupplierName());
        System.out.println("Choose what you want to do:");
        System.out.println("\t1. Watch supplier details.");
        System.out.println("\t2. update supplier details.");
        System.out.println("\t3. manage supplier products.");
        System.out.println("\t4. Add discount on amount of products to supplier.");
        System.out.println("\t5. remove discount on amount of product to supplier ");
        System.out.println("\t6. Create new order from the supplier.");
        System.out.println("\t7. Watch existing orders from the supplier.");
        System.out.println("\t8. close supplier account.");
        System.out.println("\t9. Return to choose another supplier.");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e ){
            System.out.println("you must enter only 1 digit number");
            inSupplierMenu(supplierNumber);
        }
        if(choice >10 || choice<=0){
            System.out.println("you must enter only digit between 1 to 9");
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
                removeDiscountMenu(supplierNumber);
                break;
            case 6:
                OrderMenu om = new OrderMenu(s);
                om.newOrder();
                break;
            case 7:
                OrderMenu om6 = new OrderMenu(s);
                om6.watchOrdersMenu();
                break;
            case 8:
                boolean close =ss.closeAccount(supplierNumber);
                if(close){
                    System.out.println("account closed!");
                }
                else{
                    System.out.println("account is already closed");
                }
                chooseSupplierMenu();
                break;
            case 9:
                chooseSupplierMenu();
                break;
            default:
                System.out.println("You must type digit 1 to 10");
                inSupplierMenu(supplierNumber);
        }
    }

//    private void watchPastOrders(int supplierNumber) {
//        String json = ss.watchPastOrders(supplierNumber);
//        List<LinkedTreeMap> pastOrders=new ArrayList<>();
//        pastOrders=Menu.fromJson(json, pastOrders.getClass());
//        int i=1;
//        for (LinkedTreeMap p: pastOrders){
//            PastOrder pastOrder = Menu.fromJson(p.toString(), PastOrder.class);
//            System.out.println(i + ". "+ pastOrder.toString());
//            i++;
//        }
//    }

    private void removeDiscountMenu(int supplierNumber) {
        System.out.println("Please write on how much product you want remove discount?");
        String choiceStr = "";
        int count =0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            addDiscountMenu(supplierNumber);
        }
        boolean removed=ss.removeDiscountOnAmount(supplierNumber, count);
        if(removed) {
            System.out.println("discount is removed to supplier");
        }
        else {
            System.out.println("catalog number or count were invalid");
        }
        inSupplierMenu(supplierNumber);
    }

    private void addDiscountMenu(int supplierNum) {
        System.out.println("Please write on how much product you want add discount?");
        String choiceStr = "";
        int count =0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            addDiscountMenu(supplierNum);
        }
        System.out.println("Please write the discount?");
        choiceStr = "";
        double discount =0;
        try{
            choiceStr = sc.next();
            discount=Double.parseDouble(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            addDiscountMenu(supplierNum);
        }
        boolean added=ss.addDiscount(supplierNum,count,discount);
        if(added) {
            System.out.println("discount is added to supplier");
        }
        else {
            System.out.println("catalog number or count or discount were invalid");
        }
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
        System.out.println("\t4. transportation");
        System.out.println("\t5. Return to supplier page");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            updateSupplierDetails(s);
        }
        switch (choice){
            case 1:
                String newSupplierName = "";
                System.out.println("Enter Supplier's name: ");
                newSupplierName = sc.next();
                ss.updateAccount(s.getSupplierNumber(),newSupplierName, s.getBankNumber(), s.getContacts());
                s.setSupplierName(newSupplierName);
                break;
            case 2:
                System.out.println("Enter Supplier's bankNumber: ");
                choiceStr = "";
                int bankNumber =0;
                try{
                    choiceStr = sc.next();
                    bankNumber=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only digits number");
                    openNewAccountSupplier();
                }
                ss.updateAccount(s.getSupplierNumber(),s.getSupplierName(), bankNumber, s.getContacts());
                s.setBankAccount(bankNumber);
                break;
            case 3://contacts
                System.out.println("Enter count of contacts you have: ");
                choiceStr = "";
                int countContacts =0;
                try{
                    choiceStr = sc.next();
                    countContacts=Integer.parseInt(choiceStr);
                }
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
                System.out.println("Choose who responsible for Future orders");
                System.out.println("\t1. SUPER LI");
                System.out.println("\t2. "+s.getSupplierName());
                choiceStr = "";
                int supNum =0;
                try{
                    choiceStr = sc.next();
                    supNum=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only digits number");
                    openNewAccountSupplier();
                }
                if(supNum!=1& supNum!=2){
                    System.out.println("you must enter only 1 or 2");
                    updateSupplierDetails(s);
                }
                boolean deliver=supNum==2;
                boolean update=ss.updateDeliveration(s.getSupplierNumber(),deliver);
                if(update & deliver){
                    System.out.println(s.getSupplierName()+" is responsible for transformation");
                }
                else if(update &!deliver){
                    System.out.println("SUPER LI is responsible for transformation");
                }
                else if(!update & !deliver){
                    System.out.println("SUPER LI is already responsible for transformation");
                    updateSupplierDetails(s);
                }
                else if(!update & deliver){
                    System.out.println(s.getSupplierName()+" is already responsible for transformation");
                    updateSupplierDetails(s);
                }
                break;
            case 5:
                inSupplierMenu(s.getSupplierNumber());
                break;
            default:
                System.out.println("you must peak number between 1 to 5");
                updateSupplierDetails(s);

        }
        updateSupplierDetails(s);

    }


    private void openNewAccountSupplier() {
        System.out.println("You open new supplier Account:");
        String supName = "";
        System.out.println("Enter Supplier's name: ");
        supName = sc.next();
        System.out.println("Enter Supplier's number: ");
        String choiceStr = "";
        int supNumber =0;
        try{
            choiceStr = sc.next();
            supNumber=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        if(supNumber<=0){
            System.out.println("you must enter only positive number");
            openNewAccountSupplier();
        }
        System.out.println("Enter Supplier's bankNumber: ");
        choiceStr = "";
        int bankNumber =0;
        try{
            choiceStr = sc.next();
            bankNumber=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        if(bankNumber<=0){
            System.out.println("you must enter only positive number");
            openNewAccountSupplier();
        }
        System.out.println("Enter count of contacts you have: ");
        choiceStr = "";
        int countContacts =0;
        try{
            choiceStr = sc.next();
            countContacts=Integer.parseInt(choiceStr);
        }
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
        System.out.println("Choose who responsible for Future orders");
        System.out.println("\t1. SUPER LI");
        System.out.println("\t2. "+supName);
        choiceStr = "";
        int supNum =0;
        try{
            choiceStr = sc.next();
            supNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            openNewAccountSupplier();
        }
        if(supNum!=1& supNum!=2){
            System.out.println("you must enter only 1 or 2");
            openNewAccountSupplier();
        }
        //
        boolean deliver=supNum==2;

        boolean open=ss.openAccount(supNumber,supName, bankNumber,contacts,deliver);
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

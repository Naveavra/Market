package PresentationLayer.Suppliers;

import DomainLayer.Employees.JobType;
import PresentationLayer.Menu;
import ServiceLayer.OrderService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;

import java.util.*;
import java.util.regex.Pattern;

public class SupplierMenu {

    private Scanner sc = new Scanner(System.in);
    private SupplierService ss = new SupplierService();
    private Set<JobType> roles = new HashSet<>();
    private OrderService orderService =new OrderService();

// HR_MANAGER, SHIFT_MANAGER ,CASHIER, STOCK_KEEPER, DRIVER, MERCHANDISER, LOGISTICS_MANAGER,
//    TRANSPORT_MANAGER, STORE_MANAGER;

    public void setRoles(Set<JobType> roles){
        this.roles =roles;
    }
    public void chooseSupplierMenu() {
        int choice =0;
        while(choice != 5){
        System.out.println("***You enter to Suppliers page:***");
        System.out.println("Please choose what you want to do:");
        System.out.println("\t1. supplier management");
        System.out.println("\t2. supplier's product management");
        System.out.println("\t3. supplier's order management");
        System.out.println("\t4. cancel orders");
        System.out.println("\t5. go back");

        String choiceStr = "";
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){

            System.out.println("you must enter only 1 digit number");
            return;
        }
        int supNumber =0;
        if(choice==2 | choice ==3){
            System.out.println("Enter the supplier number you want to see:");
            choiceStr = "";
             supNumber =0;
            try{
                choiceStr = sc.next();
                supNumber=Integer.parseInt(choiceStr);
            }
            catch (Exception e){
                System.out.println("you must enter only 1 digit number");
                return;
            }
        }
        switch (choice) {
            case 1:
                openSuppliersManagement();
                break;
            case 2:
                openSupplierProductManagement(supNumber);
                break;
            case 3:
                openOrderSupplierManagement(supNumber);
                break;
            case 4:
                cancelOrder();
                break;
            case 5:
                break;
        }
        }

    }

    private void cancelOrder() {
        watchWaitOrders();
        System.out.println("please choose order number to cancel or \"break\" if u want to return to supplier menu");
        int orderId = 0;
        String choiceStr = "";
        try {
            choiceStr = sc.next();
            if (choiceStr.equals("break")) {
                return;
            }
            orderId = Integer.parseInt(choiceStr);
        } catch (Exception e) {
            System.out.println("you must enter only 1 digit number");
            return;
        }
        LinkedList<Integer> ids =ordersIds();
        if(!ids.contains(orderId)){
            System.out.println("order doesn't found");
        }
        else {
            Boolean check = orderService.cancelOrder(orderId);
            if (check) {
                System.out.println("order deleted");
            } else {
                System.out.println("order doesn't found");
            }
        }
    }
    private void watchWaitOrders() {
        Map<Integer, Order> orders=orderService.getActiveOrders();
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
            return;
        }
        for(Order o: orders.values()){
            System.out.println(o.toString());
        }
    }
    private LinkedList<Integer> ordersIds(){
        Map<Integer, Order> orders=orderService.getActiveOrders();
        LinkedList<Integer> ids =new LinkedList<>();
        for(Order x: orders.values()){
            ids.addLast(x.getOrderId());
        }
        return ids;
    }

    private void openOrderSupplierManagement(int supplierNumber) {
        String json = ss.getSupplier(supplierNumber);
        if (json == null || json.equals("null")){
            System.out.println("supplier number don't found");
            return;
        }
        Gson gson = new Gson();
        Supplier s = gson.fromJson(json, Supplier.class);
        if(!s.isActive()){
            System.out.println("supplier account is closed");
            return;
        }
        OrderMenu om6 = new OrderMenu(s);
        om6.setRoles(roles);
        om6.watchOrdersMenu(s);
    }

    private void openSupplierProductManagement(int supplierNumber) {
        String json = ss.getSupplier(supplierNumber);
        if (json == null || json.equals("null")){
            System.out.println("supplier number don't found");
            return;
        }
        Gson gson = new Gson();
        Supplier s = gson.fromJson(json, Supplier.class);
        if(!s.isActive()){
            System.out.println("supplier account is closed");
            return;
        }
        ProductMenu pm = new ProductMenu(s);
        pm.setRole(roles);
        pm.manageProductsSupplierMenu();
    }


    private void openSuppliersManagement() {
        int choice =0;
        while(choice != 7){
        System.out.println("Enter the number of action you want to do:");
        System.out.println("\t 1. open supplier account");
        System.out.println("\t 2. close supplier number");
        System.out.println("\t 3. update supplier details");
        System.out.println("\t 4. watch supplier details");
        System.out.println("\t 5. add discount to supplier");
        System.out.println("\t 6. remove discount from supplier");
        System.out.println("\t 7. go back");
        String choiceStr = "";
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){

            System.out.println("you must enter only 1 digit number");
            openSuppliersManagement();
        }

        Supplier s =null;
        if(choice!=1 & choice!=7){
            System.out.println("please enter supplier number");
             choiceStr = "";
            int supplierNumber =0;
            try{
                choiceStr = sc.next();
                supplierNumber=Integer.parseInt(choiceStr);
            }
            catch (Exception e){

                System.out.println("you must enter only 1 digit number");
            }
            String json = ss.getSupplier(supplierNumber);
            if (json == null || json.equals("null")){
                System.out.println("supplier number don't found");
                return;
            }
            Gson gson = new Gson();
            s = gson.fromJson(json, Supplier.class);
            if(!s.isActive()){
                System.out.println("supplier account is closed");
                return;
            }
        }
        switch (choice) {
            case 1:
                openNewAccountSupplier();
                break;
            case 2:
                closeSupplierAccount(s);
                break;
            case 3:
                updateSupplierDetails(s);
                break;
            case 4:
                seeSupplierDetails(s);
                break;
            case 5:
                addDiscountMenu(s);
                break;
            case 6:
                removeDiscountMenu(s);
                break;
            case 7:
                break;
            default:
                System.out.println("u must choose number between 1 to 7");
        }
        }


    }
    private void closeSupplierAccount(Supplier s) {
        boolean close = ss.closeAccount(s.getSupplierNumber());
        if(close){
            System.out.println("account closed!");
        }
        else{
            System.out.println("account is already closed");
        }

    }

    private void removeDiscountMenu(Supplier s) {
        System.out.println("Please write on how much product you want remove discount?");
        String choiceStr = "";
        int count =0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            return;
        }
        boolean removed=ss.removeDiscountOnAmount(s.getSupplierNumber(), count);
        if(removed) {
            System.out.println("discount is removed to supplier");
        }
        else {
            System.out.println("catalog number or count were invalid");
        }
    }

    private void addDiscountMenu(Supplier s) {
        System.out.println("Please write on how much product you want add discount?");
        String choiceStr = "";
        int count =0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            return;
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
        }
        boolean added=ss.addDiscount(s.getSupplierNumber(),count,discount);
        if(added) {
            System.out.println("discount is added to supplier");
        }
        else {
            System.out.println("catalog number or count or discount were invalid");
        }

    }

    private void seeSupplierDetails(Supplier s) {
        System.out.println(s.toString());
    }

    private void updateSupplierDetails(Supplier s) {//need to imp
        System.out.println(s.toString());
        int choice =0;
        while (choice != 5){
        System.out.println("Choose what you want to update:");
        System.out.println("\t1. supplier name");
        System.out.println("\t2. bank number");
        System.out.println("\t3. contacts");
        System.out.println("\t4. transportation");
        System.out.println("\t5. go back");
        String choiceStr = "";
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            updateSupplierDetails(s);
        }
        switch (choice) {
            case 1:
                String newSupplierName = "";
                System.out.println("Enter Supplier's new name: ");
                newSupplierName = sc.next();
                boolean updateAccount = ss.updateAccount(s.getSupplierNumber(), newSupplierName, s.getBankNumber());
                s.setSupplierName(newSupplierName);
                break;
            case 2:
                System.out.println("Enter Supplier's new bankNumber: ");
                choiceStr = "";
                int bankNumber = 0;
                try {
                    choiceStr = sc.next();
                    bankNumber = Integer.parseInt(choiceStr);
                } catch (Exception e) {
                    System.out.println("you must enter only digits number");
                    return;
                }
                ss.updateAccount(s.getSupplierNumber(), s.getSupplierName(), bankNumber);
                s.setBankAccount(bankNumber);
                break;
            case 3://contacts
                System.out.println("Enter count of contacts you have: ");
                choiceStr = "";
                int countContacts = 0;
                try {
                    choiceStr = sc.next();
                    countContacts = Integer.parseInt(choiceStr);
                } catch (Exception e) {
                    System.out.println("you must enter only digits number");
                    return;
                }
                LinkedList<Contact> contacts = new LinkedList<>();
                for (int i = 1; i <= countContacts; i++) {
                    System.out.println(i + ". enter the name: ");
                    String name = sc.next();
                    System.out.println(i + ". email: ");
                    String email = sc.next();
                    System.out.println(i + ". telephone: ");
                    String telephone = sc.next();
                    contacts.add(new Contact(name, email, telephone));
                }
                // ss.updateAccount(s.getSupplierNumber(),s.getSupplierName(), s.getBankNumber());
                for (Contact c : contacts) {
                    ss.updateContact(s.getSupplierNumber(), c.getName(), c.getEmail(), c.getTelephone());
                    s.setContact(c);
                }
                break;
            case 4:
                System.out.println("Choose on which days the supplier " + s.getSupplierName() + " supplies");
                System.out.println("Enter the numbers so that they are not separated by a space");
                System.out.println("\t1. SUNDAY.");
                System.out.println("\t2. MONDAY.");
                System.out.println("\t3. TUESDAY.");
                System.out.println("\t4. WEDNESDAY.");
                System.out.println("\t5. THURSDAY.");
                System.out.println("\t6. FRIDAY.");
                System.out.println("\t7. SATURDAY.");
                String days = "";
                days = sc.next();
                String[] Days = days.split("");
                for (String day : Days) {
                    if (day.length() >= 2) {
                        System.out.println("invalid input");
                        return;
                    }
                    if (Integer.parseInt(day) < 1 | Integer.parseInt(day) > 7) {
                        System.out.println("invalid input");
                        return;
                    }
                }
                for (int i = 0; i < Days.length; i++) {
                    for (int j = i + 1; j < Days.length - 1; j++) {
                        if (Days[i].equals(Days[j])) {
                            System.out.println("invalid input");
                            return;
                        }
                    }
                }
                if (Days.length > 7) {
                    System.out.println("invalid input");
                    return;
                }
                boolean update = ss.updateDeliveration(s.getSupplierNumber(), Days);
                if (update) {
                    System.out.println(s.getSupplierName() + " has new delivery days");
                } else if (!update) {
                    System.out.println("!invalid input");
                }

                break;
            case 5:
                break;
            default:
                System.out.println("you must peak number between 1 to 5");
        }

        }
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
            return;
        }
        if(supNumber<=0){
            System.out.println("you must enter only positive number");
            return;
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
            return;
        }
        if(bankNumber<=0){
            System.out.println("you must enter only positive number");
            return;
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
            return;
        }
        if(countContacts<0) {
            System.out.println("you must enter only digits number");
            return;
        }
        LinkedList<Contact> contacts = new LinkedList<>();
        for (int i=1; i<=countContacts; i++){
            System.out.println(i + ". name: ");
            String name = sc.next();
            System.out.println(i + ". email: ");
            String email = sc.next();
            System.out.println(i + ". telephone: ");
            String telephone = sc.next();
            if(!checkEmail(email)){
                System.out.println("you must enter a valid email");
                i=i-1;
            }
            else {
                contacts.add(new Contact(name, email,telephone));
            }
        }System.out.println("Choose the shipping area of the supplier");
        System.out.println("\t1. 0.");
        System.out.println("\t2. 1.");
        System.out.println("\t3. 2.");
        choiceStr = "";
        int area =0;
        try{
            choiceStr = sc.next();
            area=Integer.parseInt(choiceStr);
        }catch (Exception e){
            System.out.println("you must enter only digits number");
            return;
        }
        if(area<0 |area >2){
            System.out.println("you must enter only  0 or 1 or 2");
            return;
        }
        System.out.println("Choose on which days the supplier "+supName+" supplies");
        System.out.println("\t1. SUNDAY.");
        System.out.println("\t2. MONDAY.");
        System.out.println("\t3. TUESDAY.");
        System.out.println("\t4. WEDNESDAY.");
        System.out.println("\t5. THURSDAY.");
        System.out.println("\t6. FRIDAY.");
        System.out.println("\t7. SATURDAY.");
        String days = "";
        days = sc.next();
        String[] Days=days.split("");
        for (String day : Days) {
            if (day.length() >= 2) {
                System.out.println("invalid input");
                return;
            }
            if(Integer.parseInt(day)<1|Integer.parseInt(day)>7){
                System.out.println("invalid input");
                return;
            }
        }
        for(int i=0;i< Days.length;i++){
            for(int j=i+1;j< Days.length-1;j++){
                if(Days[i].equals(Days[j])){
                    System.out.println("invalid input");
                    return;
                }
            }
        }
        if(Days.length>7){
            System.out.println("invalid input");
            return;
        }
        boolean open=ss.openAccount(supNumber,supName, bankNumber,Days,area);
        boolean addContact =true;
        for(Contact c:contacts) {
            addContact=addContact & ss.addContact(supNumber,c.getName(),c.getEmail(),c.getTelephone());
        }
        if(open & addContact) {
            System.out.println("The account opened and contacts created");
        }
        if(open & !addContact) {
            System.out.println("The account opened but contacts was not created");
        }
        else{
            System.out.println("one of the details is invalid");
            return;
        }
    }
    public boolean checkEmail(String email){
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}

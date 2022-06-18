package PresentationLayer;

import DAL.Connect;
import DomainLayer.Employees.JobType;
import PresentationLayer.Suppliers.Supplier;
import PresentationLayer.Transport_Emploees.EmployeeMainCLI;
import ServiceLayer.EmployeeService;
import PresentationLayer.Storage.CLI;
import PresentationLayer.Suppliers.Order;
import PresentationLayer.Suppliers.SupplierMenu;
import PresentationLayer.Transport_Emploees.UserInterface;
import ServiceLayer.*;
import ServiceLayer.transport.OrderTransportService;
import ServiceLayer.transport.UserService;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    private Scanner sc = new Scanner(System.in);
    private EmployeeMainCLI employeeCLI = new EmployeeMainCLI();

    public static void main(String[] args) throws SQLException {
        Menu m = new Menu();
        m.initialMenu();
    }
    public void initialMenu() throws SQLException {

        int choice = 0;
        System.out.println("Welcome to Supper LI!!");
        System.out.println("Load Initial Data?");
        System.out.println("\t1.yes");
        System.out.println("\t2.no");
        String choiceStr = "";
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            initialMenu();
        }
        if(choice==1)
            loadInitialData();
        boolean end = false;
        while (!end){
            while (!employeeCLI.isLoggedIn()) {
                employeeCLI.login();
            }
            Set<JobType> roles = employeeCLI.getLoggedInUserRoles();
            if (roles.contains(JobType.HR_MANAGER)) {
                System.out.println(employeeCLI.displayLoggedInUserMessages());
            }
            System.out.println("Choose module:");
            System.out.println("\t1.Employee Model");
            System.out.println("\t2.Transport Model");
            System.out.println("\t3.Supplier Model");
            System.out.println("\t4.Storage Model");
            System.out.println("To close the system - enter the word 'exit'");
            try{
                choiceStr = sc.next();
                if (choiceStr.equals("exit")){
                    System.out.println("Goodbye!");
                    end = true;
                    break;
                }
                choice=Integer.parseInt(choiceStr);
            }
            catch (Exception e){
                System.out.println("you must enter only 1 digit number");
                continue;
            }
            switch (choice) {
                case 1: // employees
                    employeeCLI.start();
                    break;
                case 2: // transport
                    if (canUseTransportModule(roles)) {
                        UserInterface cli3 = new UserInterface(); // transport
                        cli3.start(roles);
                    } else {
                        System.out.println("You are not authorized to enter this page");
                    }
                    break;
                case 3: // suppliers
                    if (canUseSupplierModule(roles)) {
                        SupplierMenu sm = new SupplierMenu();
                        sm.setRoles(roles);
                        sm.chooseSupplierMenu();
                    } else {
                        System.out.println("You are not authorized to enter this page");
                    }
                    break;

                case 4:// storage
                    if (canUseStorageModule(roles)) {
                        CLI cli = new CLI();//storage
                        cli.startStorageModel(roles);
                    } else {
                        System.out.println("You are not authorized to enter this page");
                    }
                    break;
                default:
                    System.out.println("You must type digit 1 to 4");
//                    initialMenu();
            }
        }
    }

    private boolean canUseTransportModule(Set<JobType> roles) {
        return roles.contains(JobType.STORE_MANAGER) || roles.contains(JobType.TRANSPORT_MANAGER);
    }

    private boolean canUseStorageModule(Set<JobType> roles) {
        return roles.contains(JobType.STOCK_KEEPER) || roles.contains(JobType.STORE_MANAGER);
    }

    private boolean canUseSupplierModule(Set<JobType> roles) {
        return roles.contains(JobType.STOCK_KEEPER) || roles.contains(JobType.STORE_MANAGER)
                || roles.contains(JobType.LOGISTICS_MANAGER);
    }

    public void loadInitialData() {

        Gson gson = new Gson();
        try {
            Connect.getInstance().deleteRecordsOfTables();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SupplierService ss =  new SupplierService();
        ProductSupplierService ps =new ProductSupplierService();
        OrderService os = new OrderService();
        CategoryService cC=new CategoryService();

        cC.addCategory("first");
        cC.addSubCat("first", "first1");
        cC.addSubSubCat("first", "first1", "first11");
        cC.addCategory("second");
        cC.addSubCat("second", "second1");
        cC.addSubSubCat("second", "second1", "second11");
        cC.addNewProduct(1, "milk", "from cow", 5, "me", "first", "first1", "first11");
        cC.addNewProduct(2, "eggs", "from chicken", 5, "me",
                "second", "second1", "second11");
        cC.addAllItems(1, 7, "2027-06-01", 1);
        cC.addAllItems(2, 3, "2019-06-01", 1);

        ss.openAccount(1,"OSEM", 5555, new String[]{"1"},0);
        ss.openAccount(2,"TNUVA", 456, new String[]{"1"},0);

        ss.addContact(1, "Dan", "dan@gmail.com", "0501234567");
        ss.addContact(1, "nave","nave@gmail.com", "0501234567");
        ss.addContact(2, "itay", "itay@gmail.com", "0501234567");

        ps.addProduct(1, 1, 100,2, 1);
        ps.addProduct(1, 2, 50,4, 2);
        ps.addProduct(2, 2,50,3, 2);

        ss.addDiscount(1, 5, 0.8);

        ss.addDiscount(2, 10, 0.6);


        String json = os.createOrder(1);
        Order o1 = gson.fromJson(json,Order.class);
        os.addProductToOrder(1, o1.getOrderId(), 1 ,5);
        String[] days={"1", "2", "3", "4", "5", "6", "7"};
        os.addFixedDeliveryDaysForOrder(1, o1.getOrderId(), days);
        cC.updateOrders();
//        os.sendOrder(1, 1);

        // empoly info
        LoadEmployeeData();
        LoadTransportData();

    }
    public void LoadEmployeeData() {
        EmployeeService es =new EmployeeService();
        es.register("234567891", "gal halifa", "123456", 1000, "yahav", "good conditions");
        es.register("123456789", "dan terem", "123456", 1000, "yahav", "good conditions");
        es.register("345678912", "noa aviv", "123456", 1000, "yahav", "good conditions");
        es.register("456789123", "nave avraham", "123456", 1000, "yahav", "good conditions");
        es.register("789123456", "gili gershon", "123456", 1000, "yahav", "good conditions");
        es.register("891234567", "amit halifa", "123456", 1000, "yahav", "good conditions");
        es.register("012345678", "shachar bardugo", "123456", 1000, "yahav", "good conditions");
        es.register("123456780", "nadia zlenko", "123456", 1000, "yahav", "good conditions");
        es.register("234567801", "yossi gershon", "123456", 1000, "yahav", "good conditions");
        es.register("345678012", "eti gershon", "123456", 1000, "yahav", "good conditions");
        es.register("456780123", "amit sasson", "123456", 1000, "yahav", "good conditions");
        es.register("567801234", "itamar shemesh", "123456", 1000, "yahav", "good conditions");
        es.register("147258369", "dina agapov", "123456", 1, "yahav", "bad");
        es.register("258369147", "mor shuker", "123456", 1, "yahav", "bad");
        es.register("000000000", "may terem", "123456", 1, "yahav", "good");
        es.register("111111111", "miki daniarov", "123456", 1, "yahav", "good");
        es.register("222222222", "eyal german", "123456", 1, "yahav", "good");
        es.register("333333333", "ziv cohen gvura", "123456", 1, "yahav", "good");
        es.register("318856994", "Itay Gershon", "123456", 1000000000, "Hapoalim 12 115", "The conditions for this employee are really terrific");
        es.certifyEmployee("318856994", "1");
        es.certifyEmployee("318856994", "9");
        es.certifyEmployee( "318856994", "4");
        es.certifyEmployee("318856994", "3");
        es.certifyEmployee("333333333", "1"); //HR M
        es.certifyEmployee("111111111", "8"); // TRANSPORT M
        es.certifyEmployee("222222222", "7"); // LOGISTICS M
        es.certifyEmployee("234567891", "3");
        es.certifyEmployee("345678912", "3");
        es.certifyEmployee("123456789", "3");
        es.certifyDriver("258369147", "c");
        es.certifyDriver("123456789", "c1");
        es.certifyDriver("456780123", "c");
        es.certifyDriver("234567891", "c");
        es.certifyDriver("012345678", "c1");
        es.certifyEmployee("234567891","6");
        es.certifyEmployee("123456789","6");
        es.certifyEmployee("789123456", "6");
        es.certifyEmployee("234567891","6");
        es.certifyEmployee("123456789", "6");
        es.certifyEmployee("123456780", "4");
        es.certifyEmployee("345678912", "4");
        es.certifyEmployee("456780123", "4");
        es.certifyEmployee("123456789", "4"); // STOCK
        es.certifyEmployee("234567891", "2");
        es.certifyEmployee("318856994", "2");
        es.certifyEmployee("222222222", "2");
        es.certifyEmployee("111111111", "2" );
        es.certifyDriver( "000000000", "c1");
        es.certifyEmployee("456789123", "2");
        es.certifyEmployee("333333333", "3");
        es.certifyEmployee("567801234", "6");
        es.certifyDriver("123456780", "c");


        es.createShift("01/08/2022 morning", "318856994",
                "333333333,234567891", "123456780", "123456789", "111111111");
        es.createShift("01/08/2022 evening", "456789123",
                "333333333,234567891", "258369147", "567801234", "345678912");
        es.createShift("20/05/2022 evening", "456789123",
                "222222222,234567891", "258369147", "567801234", "345678912");

        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (day.length() == 1)
            day = "0" + day;
        if (month.length() == 1)
            month = "0" + month;
        String thisDay = day + '/' + month + '/' + year;
        es.createShift(thisDay + " morning", "456789123",
                "222222222,234567891", "258369147", "567801234", "345678912");
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+3);
        String thisDay2 = day + '/' + month + '/' + year;
        es.createShift(thisDay2 + " morning", "456789123",
                "333333333,234567891", "123456789", "567801234", "345678912");
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+4);
        String thisDay3 = day + '/' + month + '/' + year;
        es.createShift(thisDay3 + " morning", "456789123",
                "222222222,234567891", "123456789", "567801234", "345678912");

        createAvailabilities("318856994");
        createAvailabilities("333333333");
        createAvailabilities("111111111");
        createAvailabilities("222222222");
        createAvailabilities("123456780");
        createAvailabilities("123456789");
        createAvailabilities("567801234");
        createAvailabilities("789123456");
        createAvailabilities("000000000");
        createAvailabilities("234567891");
        createAvailabilities("456780123");
        createAvailabilities("234567891");



    }

    /**
     * used only for loading data, creates some availabilities in July
     * @param id employee to add availability to
     */
    private void createAvailabilities(String id){
        EmployeeService es = new EmployeeService();
        for (int i = 1; i < 31; i++){
            String day;
            if (i < 10)
                day = "0" + i;
            else
                day = String.valueOf(i);
            Random r = new Random();
            int x = r.nextInt(4);
            if (x == 0)
                es.addAvailableTimeSlotToEmployee(day + "/07/2022 morning" ,id);
            else if (x == 1)
                es.addAvailableTimeSlotToEmployee(day + "/07/2022 evening" ,id);
            else{
                es.addAvailableTimeSlotToEmployee(day + "/07/2022 morning" ,id);
                es.addAvailableTimeSlotToEmployee(day + "/07/2022 evening" ,id);
            }
        }
    }

    public void LoadTransportData() {
        UserService us = new UserService();
        OrderTransportService os = new OrderTransportService();
        us.createTruck("C", "shahar", 150, 100);
        us.createTruck("C1", "nadia", 100, 50);
        us.createTruck("C", "nastia", 200, 100);
        us.createDriver("nave", "315809376", "C");
        us.createDriver("miki", "208163709", "C1");
        us.createSite("1567", "hakanaim 16", "liron", "05068582", 0, 1);
        us.createSite("156", "hakanaim 15", "liro", "0506858", 1, 1);
        us.createSite("15", "hakanaim 14", "lir", "050685", 2, 1);
        us.createSite("14", "hakanaim 13", "li", "05858", 0, 1);
        us.createSite("13", "hakanaim 12", "lin", "05858", 1, 1);
        us.createSite("12", "hakanaim 11", "lid", "058528", 2, 1);
//        us.createSupply("milk", 1);
//        us.createSupply("eggs", 2);
        os.orderList();
    }


    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

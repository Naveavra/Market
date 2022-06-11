package PresentationLayer;

import DAL.Connect;
import DomainLayer.Employees.JobType;
import PresentationLayer.Transport_Emploees.EmployeeMainCLI;
import ServiceLayer.EmployeeService;
import PresentationLayer.Storage.CLI;
import PresentationLayer.Suppliers.Order;
import PresentationLayer.Suppliers.SupplierMenu;
import PresentationLayer.Transport_Emploees.UserInterface;
import ServiceLayer.*;
import com.google.gson.Gson;
import java.sql.SQLException;
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
        while (true){
            while (!employeeCLI.isLoggedIn()) {
                employeeCLI.login();
            }
            Set<JobType> roles = employeeCLI.getLoggedInUserRoles();
            System.out.println(employeeCLI.displayLoggedInUserMessages());
            System.out.println("Choose module:");
            System.out.println("\t1.Supplier Model");
            System.out.println("\t2.Storage Model");
            System.out.println("\t3.Employee Model");
            System.out.println("\t4.Transport Model");
            System.out.println("To close the system - enter the word 'exit'");
            try{
                choiceStr = sc.next();
                if (choiceStr.equals("exit")){
                    System.out.println("Goodbye!");
                    break;
                }
                choice=Integer.parseInt(choiceStr);
            }
            catch (Exception e){
                System.out.println("you must enter only 1 digit number");
                continue;
//                initialMenu();
            }
            switch (choice) {
                case 1:
                    if (canUseSupplierModule(roles)) {
                        SupplierMenu sm = new SupplierMenu();
                        sm.chooseSupplierMenu();
                    } else {
                        System.out.println("You are not authorized to enter this page");
                    }
                    break;
                case 2://storage
                    if (canUseStorageModule(roles)) {
                        CLI cli = new CLI();//storage
                        cli.startStorageModel();
                    } else {
                        System.out.println("You are not authorized to enter this page");
                    }
                    break;
                case 3://employees - transport
        //                EmployeeMainCLI cli2 = new EmployeeMainCLI();
                    employeeCLI.start();
                    break;
                case 4://employees - transport
                    if (canUseTransportModule(roles)) {
                        UserInterface cli3 = new UserInterface();
                        cli3.start();
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
        return roles.contains(JobType.STOCK_KEEPER) || roles.contains(JobType.STORE_MANAGER) ||
                roles.contains(JobType.SHIFT_MANAGER) || roles.contains(JobType.TRANSPORT_MANAGER);
    }

    private boolean canUseStorageModule(Set<JobType> roles) {
        return roles.contains(JobType.STOCK_KEEPER) || roles.contains(JobType.STORE_MANAGER) ||
                roles.contains(JobType.SHIFT_MANAGER);
    }

    private boolean canUseSupplierModule(Set<JobType> roles) {
        return roles.contains(JobType.STOCK_KEEPER) || roles.contains(JobType.STORE_MANAGER)
                || roles.contains(JobType.SHIFT_MANAGER) || roles.contains(JobType.LOGISTICS_MANAGER);
    }

    private void loadInitialData() {

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
        cC.addNewProduct(1, "milk", "from cow", 3, "me"
                , "first", "first1", "first11");
        cC.addNewProduct(2, "eggs", "from chicken", 5, "me",
                "second", "second1", "second11");
        cC.addAllItems(1, 7, "2022-06-01", 1);
        cC.addAllItems(2, 3, "2019-06-01", 1);

        ss.openAccount(1,"OSEM", 5555, new String[]{"1"},0);
        ss.openAccount(2,"TNUVA", 456, new String[]{"1"},0);

        ss.addContact(1, "Dan", "dan@gmail.com", "0501234567");
        ss.addContact(1, "nave","nave@gmail.com", "0501234567");
        ss.addContact(2, "itay", "itay@gmail.com", "0501234567");

        ps.addProduct(1, 1, 2, 1);
        ps.addProduct(2, 2,3, 2);

        ss.addDiscount(1, 5, 0.8);

        ss.addDiscount(2, 10, 0.6);


        String json = os.createOrder(1);
        Order o1 = gson.fromJson(json,Order.class);
        os.addProductToOrder(1, o1.getOrderId(), 1 ,5);
        String[] days={"1", "2", "3", "4", "5", "6", "7"};
        os.addFixedDeliveryDaysForOrder(1, 1, days);
        os.sendOrder(1, 1);

        // empoly info
        LoadEmployeeData();

    }
    public void LoadEmployeeData() {
        EmployeeService es = EmployeeService.getInstance();
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


        es.createShift("01/07/2022 morning", "318856994",
                "333333333,234567891", "123456780,000000000", "123456789", "111111111");
        es.createShift("01/07/2022 evening", "456789123",
                "333333333,234567891", "258369147,000000000", "567801234", "345678912");

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
        EmployeeService es = EmployeeService.getInstance();
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


    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

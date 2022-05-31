package PresentationLayer;

import DAL.Connect;
import DomainLayer.Employees.JobType;
import PresentationLayer.Storage.CLI;
import PresentationLayer.Supplier.Order;
import PresentationLayer.Supplier.SupplierMenu;
import PresentationLayer.Transport_Emploees.EmployeeMainCLI;
import PresentationLayer.Transport_Emploees.MainCLI;
import PresentationLayer.Transport_Emploees.UserInterface;
import ServiceLayer.*;
import ServiceLayer.Utility.Response;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private Scanner sc=new Scanner(System.in);

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
        System.out.println("\t1.Supplier Model");
        System.out.println("\t2.Storage Model");
        System.out.println("\t3.Employee Model");
        System.out.println("\t4.Transport Model");
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            initialMenu();
        }
        switch (choice) {
            case 1:
                SupplierMenu sm = new SupplierMenu();
                sm.chooseSupplierMenu();
                break;
            case 2://storage
                CLI cli=new CLI();//storage
                cli.startStorageModel();
                break;
            case 3://emploees - transport
                EmployeeMainCLI cli2 = new EmployeeMainCLI();
                cli2.start();
                break;
            case 4://emploees - transport
                UserInterface cli3 = new UserInterface();
                cli3.start();
                break;
            default:
                System.out.println("You must type digit 1 to 2");
                initialMenu();

        }

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

        ss.openAccount(1,"OSEM", 5555, true);
        ss.openAccount(2,"TNUVA", 456, false);

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
        es.register("111111111", "wendy the dog", "123456", 1, "yahav", "good");
        es.register("222222222", "savta tova", "123456", 1, "yahav", "good");
        es.register("333333333", "liron marinberg", "123456", 1, "yahav", "good");
        es.certifyEmployee("CASHIER", "318856994");
        es.certifyEmployee("CASHIER", "234567891");
        es.certifyEmployee("CASHIER", "345678912");
        es.certifyEmployee("CASHIER", "123456789");
        es.certifyDriver("258369147", "c");
        es.certifyDriver("123456789", "c1");
        es.certifyDriver("456780123", "c");
        es.certifyDriver("234567891", "c");
        es.certifyDriver("012345678", "c1");
        es.certifyEmployee("MERCHANDISER", "234567891");
        es.certifyEmployee("MERCHANDISER", "123456789");
        es.certifyEmployee("MERCHANDISER", "789123456");
        es.certifyEmployee("MERCHANDISER", "234567891");
        es.certifyEmployee("MERCHANDISER", "123456789");
        es.certifyEmployee("STOCK_KEEPER", "123456780");
        es.certifyEmployee("STOCK_KEEPER", "345678912");
        es.certifyEmployee("STOCK_KEEPER", "456780123");
        es.certifyEmployee("STOCK_KEEPER", "123456789");
        es.certifyEmployee("SHIFT_MANAGER", "234567891");
        es.certifyEmployee("SHIFT_MANAGER", "318856994");
        es.certifyEmployee("SHIFT_MANAGER", "222222222");
        es.certifyEmployee("STOCK_KEEPER", "111111111");
        es.certifyDriver( "000000000", "c1");
        es.certifyEmployee("SHIFT_MANAGER", "456789123");
        es.certifyEmployee("CASHIER", "333333333");
        es.certifyEmployee("MERCHANDISER", "567801234");
        es.certifyDriver("123456780", "c");
//        employeeController.putBackAll();

//        Response r = es.createShift(new ShiftPair(new ShiftDate("01", "06", "2022"), Time.MORNING), "318856994",
//                "333333333,234567891", "123456780,000000000", "123456789", "111111111");
//        if (r.errorOccurred())
//            System.out.println("r1 = " + r.getErrorMessage());
//        r = createShift(new ShiftPair(new ShiftDate("02", "06", "2022"), Time.MORNING), "456789123",
//                "333333333,234567891", "258369147,000000000", "567801234", "345678912");
//        if (r.errorOccurred())
//            System.out.println("r2 = " + r.getErrorMessage());

    }


    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

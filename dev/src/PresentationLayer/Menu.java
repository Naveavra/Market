package PresentationLayer;

import DAL.Connect;
import PresentationLayer.Storage.CLI;
import PresentationLayer.Supplier.Order;
import PresentationLayer.Supplier.SupplierMenu;
import PresentationLayer.Transport_Emploees.MainCLI;
import ServiceLayer.*;
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
        System.out.println("\t3.Employee/Transport Model");
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
                MainCLI cli2 = new MainCLI();
                cli2.start();
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


    }

    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

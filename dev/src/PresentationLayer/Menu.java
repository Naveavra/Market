package PresentationLayer;

import DomainLayer.Product;
import ServiceLayer.OrderService;
import ServiceLayer.ProductService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Menu {
    private Scanner sc=new Scanner(System.in);

    public static void main(String[] args){
        Menu m = new Menu();
        m.initialMenu();
    }



    public void initialMenu(){
        int choise = 0;
        System.out.println("Welcome to Supplier Model!!");
        System.out.println("How do you want to start?");
        System.out.println("\t1. with empty data ");
        System.out.println("\t2. with initial data");
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            initialMenu();
        }
        if (choise==2){
            loadInitialData();
        }
        SupplierMenu sm = new SupplierMenu();
        sm.chooseSupplierMenu();

    }

    private void loadInitialData() {
//        Product p1 =new Product(1, "chair",50 );
//        Product p2 =new Product(2, "table",60 );
//        Product p3 =new Product(3, "pencil",5 );
//        Product p4 =new Product(4, "pen",10 );
//        Product p5 =new Product(5, "flashlight",70 );
//        Product p6 =new Product(6, "iphone",3300 );

        Gson gson = new Gson();

        SupplierService ss =  new SupplierService();
        ProductService ps =new ProductService();
        OrderService os = new OrderService();
        SupplierMenu sm =new SupplierMenu();

        HashMap<String,String> contacts = new HashMap<>();
        contacts.put("eyal", "eyal@gmail.com");
        contacts.put("eldad","eldad@gmail.com");
        contacts.put("ziv", "ziv@gmail.com");

        ss.openAccount(1,"OSEM", 5555, contacts,true);
        ps.addProduct(1, 1, "ptitim", 15);
        ps.addProduct(1, 2, "spageti", 6);
        ps.addProduct(1, 3, "bamba", 10);

        contacts = new HashMap<>();
        contacts.put("Dan", "dan@gmail.com");
        contacts.put("nave","nave@gmail.com");
        contacts.put("itay", "itay@gmail.com");

        ss.openAccount(2,"TNUVA", 456, contacts,false);
        ps.addProduct(2, 3, "Milk 3%", 10);
        ps.addProduct(2, 5, "Cheeze", 20);
        ps.addProduct(2, 6, "Dani", 7);

        ss.addDiscount(1, 5, 0.8);

        ss.addDiscount(2, 10, 0.6);


        String json = os.createOrder(1);
        Order o1 = gson.fromJson(json,Order.class);
        os.addProductToOrder(1, o1.getOrderId(), 1 ,1000);
        os.addProductToOrder(1, o1.getOrderId(), 2,200);
        os.sendOrder(1, o1.getOrderId());
        String json1 = os.createOrder(2);
        Order o2 = gson.fromJson(json1,Order.class);
        os.addProductToOrder(2,1,3,100);
        os.addProductToOrder(2,1,5,200);


    }

    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

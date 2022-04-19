package PresentationLayer;

import ServiceLayer.OrderService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;

import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

public class Menu {
    private Scanner sc=new Scanner(System.in);

    public static void main(String[] args){
        Menu m = new Menu();
        m.intialMenu();
    }


    public void intialMenu(){
        int choise = 0;
        System.out.println("Welcome to Supllier Model!!");
        System.out.println("How do you want to start?");
        System.out.println("\t1. with empty data ");
        System.out.println("\t2. with intial data");
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            intialMenu();
        }
        if (choise==2){
            loadIntialData();
        }
        SupplierMenu sm = new SupplierMenu();
        sm.chooseSupplierMenu();

    }

    private void loadIntialData() {

        Gson gson = new Gson();
        SupplierService ss =  new SupplierService();
        HashMap<String,String> contacts = new HashMap<>();
        contacts.put("eyal", "eyal@gmail.com");
        ss.openAccount(123,"LG", 5555, contacts);

        contacts = new HashMap<>();
        contacts.put("Dan", "dan@gmail.com");
        ss.openAccount(1235,"Boxit", 456, contacts);

        ss.addDiscount(123, 5, 0.8);
        OrderService os = new OrderService();
        String json = os.createOrder(1235);
        Order o1 = gson.fromJson(json,Order.class);



    }

    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

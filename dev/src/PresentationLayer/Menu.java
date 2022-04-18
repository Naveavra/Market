package PresentationLayer;

import com.google.gson.Gson;

import java.io.Console;
import java.util.Scanner;

public class Menu {
    private Scanner sc=new Scanner(System.in);

    public static void main(String[] args){
        Menu m = new Menu();
        m.intialMenu();
    }


    public void run(){

        intialMenu();
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

    }

    public static<T> T fromJson(String json, Class<T> c){
        Gson gson = new Gson();
        T t = gson.fromJson(json, c);
        return t;
    }
}

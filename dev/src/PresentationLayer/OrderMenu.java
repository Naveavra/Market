package PresentationLayer;

import DomainLayer.Product;
import ServiceLayer.OrderService;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Scanner;

public class OrderMenu {

    private Scanner sc=new Scanner(System.in);
    private Supplier supplier;
    private ProductMenu pm;
    private OrderService orderService;
    public OrderMenu(Supplier s) {
        this.supplier = s;
        pm = new ProductMenu(supplier);
        orderService = new OrderService();
    }

    public void newOrder() {
        System.out.println("Open new Order from "+ supplier.getSupplierName());
        String json  = orderService.createOrder(supplier.getSupplierNumber());
        Gson gson = new Gson();
        Order o = gson.fromJson(json, Order.class);
        System.out.println("order created. you can mow add products.");
        addProductsToOrder(o);


    }

    private void addProductsToOrder(Order o) {
    }

    public void watchOrdersMenu() {
        System.out.println("You can now see and manage your orders:");
        System.out.println("Choose what you want");
        System.out.println("\t1. Update order.");
        System.out.println("\t2. See all your wait orders.");
        System.out.println("\t3. See all your orders in fixed days delivery.");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            watchOrdersMenu();
        }
        switch (choise){
            case 1:
                System.out.println("Enter orderID:");
                int orderID = 0;
                try{orderID = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    watchOrdersMenu();
                }
                updateOrderMenu(orderID);
                break;
            case 2:
                watchWaitOrders();
                break;
            case 3:
                watchFIxedDaysOrders();
                break;

            default:
                System.out.println("You must type number between 1 to 6");
                watchOrdersMenu();

        }
    }

    private void watchFIxedDaysOrders() {


    }

    private void watchWaitOrders() {

    }

    private void updateOrderMenu(int orderID) {
        Order o = orderService.getOrder(orderID);
        System.out.println("Order: "+ orderID);
        System.out.println("Choose what you want:");
        System.out.println("\t1. add new product.");
        System.out.println("\t2. watch all the product in the order.");
        System.out.println("\t3. update product in order.");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            watchOrdersMenu();
        }
        switch (choise){
            case 1:
                System.out.println("Enter catalog number:");
                int catalogNum = 0;
                try{catalogNum = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only numbers");
                    updateOrderMenu(orderID);
                }
                System.out.println("Enter count:");
                int count = 0;
                try{count = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only numbers");
                    updateOrderMenu(orderID);
                }
                orderService.addProductToOrder(orderID,catalogNum, count);
                break;
            case 2:
                watchProductInOrder(orderID);
                break;
            case 3:
                updateProductInOrder(orderID);
                break;

            default:
                System.out.println("You must type number between 1 to 3");
                watchOrdersMenu();

        }


    }

    private void updateProductInOrder(int orderID) {

    }

    private void watchProductInOrder(int orderID) {
        Map<Product,Integer> products = Menu.fromJson(orderService.getProductsInOrder(orderID), Map.class);
        System.out.println("Product in Order: "+ orderID);
        int i = 1;
        for (Map.Entry<Product,Integer> e: products.entrySet()){
            System.out.println("\t"+i+ ". "+ e.getKey() + "in count: "+ e.getValue() );
        }

    }


}

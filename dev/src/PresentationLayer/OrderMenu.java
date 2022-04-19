package PresentationLayer;

import ServiceLayer.OrderService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class OrderMenu {

    private Scanner sc=new Scanner(System.in);
    private Supplier supplier;
    private ProductMenu pm;
    private OrderService orderService;
    private Gson gson;
    private SupplierMenu sm ;

    public OrderMenu(Supplier s) {
        this.supplier = s;
        orderService = new OrderService();
        gson = new Gson();
        sm =new SupplierMenu();
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
        System.out.println("Do you want to add products to this order?");
        System.out.println("\t1. YES.");
        System.out.println("\t2. NO.");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            addProductsToOrder(o);
        }
        switch (choise){
            case 1:
                System.out.println("Enter a catalog number");
                int catalogNum = 0;
                try{catalogNum = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    addProductsToOrder(o);
                }
                System.out.println("Enter an amount");
                int count = 0;
                try{count = sc.nextInt();}
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    addProductsToOrder(o);
                }
                boolean added =orderService.addProductToOrder(supplier.getSupplierNumber(), o.getOrderId(), catalogNum, count);
                if(!added){
                    System.out.println("The product isn't exist or the amount was invalid");
                    addProductsToOrder(o);
                }
                break;
            case 2:
                sm.inSupplierMenu(supplier.getSupplierNumber());
        }
        addProductsToOrder(o);

    }

    public void watchOrdersMenu() {
        System.out.println("You can now see and manage your orders:");
        System.out.println("Choose what you want");
        System.out.println("\t1. Update order.");
        System.out.println("\t2. See all your wait orders.");
        System.out.println("\t3. See all your orders in fixed days delivery.");
        System.out.println("\t4. Return to supplier page");
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
                watchFixedDaysOrders();
                break;

            default:
                System.out.println("You must type number between 1 to 6");
                watchOrdersMenu();

        }
        sm.inSupplierMenu(supplier.getSupplierNumber());
    }

    private void watchFixedDaysOrders() {
        String json = orderService.getFixedDaysOrders(supplier.getSupplierNumber());

        System.out.println("NOT IMPL");

        watchOrdersMenu();

    }

    private void watchWaitOrders() {
        String json=orderService.getActiveOrders(supplier.getSupplierNumber());
//        List<Order> orders=new ArrayList<>();
//
//        //orders=gson.fromJson(
//        orders= Menu.fromJson(json,orders.getClass());
        Map<Integer, LinkedTreeMap> orders = new HashMap<Integer, LinkedTreeMap>();
        orders = Menu.fromJson(json, orders.getClass());

        for(LinkedTreeMap o: orders.values()){
            Order order = Menu.fromJson(o.toString(), Order.class);
            System.out.println(order.toString());
        }
        watchOrdersMenu();
    }

    private void updateOrderMenu(int orderID) {
        String json=orderService.getOrder(supplier.getSupplierNumber(), orderID);
        Order o = gson.fromJson(json,Order.class);
        System.out.println("Order: "+ orderID);
        System.out.println("Choose what you want:");
        System.out.println("\t1. add new product.");
        System.out.println("\t2. watch all the product in the order.");
        System.out.println("\t3. update product in order.");
        System.out.println("\t4. Return to choose orders.");
        int choise = 0;
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            updateOrderMenu(orderID);
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
                orderService.addProductToOrder(supplier.getSupplierNumber(),orderID,catalogNum, count);
                break;
            case 2:
                watchProductInOrder(orderID);
                break;
            case 3:
                updateProductInOrder(orderID);
                break;
            case 4:
                watchOrdersMenu();
                break;
            default:
                System.out.println("You must type number between 1 to 3");
                updateOrderMenu(orderID);
                break;
        }
        System.out.println("You want to edit anther product?\n1.YES\n2.NO");
        try{choise = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            updateOrderMenu(orderID);
        }
        switch (choise){
            case 1:
                updateOrderMenu(orderID);
                break;
            case 2:
                new SupplierMenu().inSupplierMenu(supplier.getSupplierNumber());
        }
        updateOrderMenu(orderID);
    }

    private void updateProductInOrder(int orderID) {
        watchOrdersMenu();
        System.out.println("choose product you want to edit and enter the catalog number:");
        int catalogNum = 0;
        try{catalogNum = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only numbers");
            updateProductInOrder(orderID);
        }
        System.out.println("Enter the new count for product (0 for delete");
        int count = 0;
        try{count = sc.nextInt();}
        catch (Exception e){
            System.out.println("you must enter only numbers");
            updateProductInOrder(orderID);
        }
        if (count > 0){
            orderService.updateProductInOrder(supplier.getSupplierNumber(),orderID, catalogNum, count);
        }
        else{
            orderService.deleteProductFromOrder(supplier.getSupplierNumber(), orderID,catalogNum);
        }
        updateOrderMenu(orderID);

    }

    private void watchProductInOrder(int orderID) {
        Map<Product,Integer> products = Menu.fromJson(orderService.getProductsInOrder(supplier.getSupplierNumber(), orderID), Map.class);
        System.out.println("Product in Order: "+ orderID);
        int i = 1;
        for (Map.Entry<Product,Integer> e: products.entrySet()){
            System.out.println("\t"+i+ ". "+ e.getKey() + "in count: "+ e.getValue() );
            i++;
        }
        updateOrderMenu(orderID);

    }


}

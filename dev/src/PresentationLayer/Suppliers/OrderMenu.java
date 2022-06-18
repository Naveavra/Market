package PresentationLayer.Suppliers;

import DomainLayer.Employees.JobType;
import DomainLayer.Suppliers.PastOrderSupplier;
import PresentationLayer.Menu;
import ServiceLayer.OrderService;
import ServiceLayer.ProductSupplierService;
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
    private Set<JobType> roles;

    public OrderMenu(Supplier s) {
        this.supplier = s;
        orderService = new OrderService();
        gson = new Gson();
        sm =new SupplierMenu();
        roles=new HashSet<>();
    }
    public void setRoles(Set<JobType> roles){
        this.roles=roles;
        sm.setRoles(roles);

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
        int choice =0;
        while(choice != 2) {
            System.out.println("Do you want to add products to this order?");
            System.out.println("\t1. YES.");
            System.out.println("\t2. NO.");
            String choiceStr = "";
            try {
                choiceStr = sc.next();
                choice = Integer.parseInt(choiceStr);
            } catch (Exception e) {
                System.out.println("you must enter only 1 digit number");
                return;
            }
            switch (choice) {
                case 1:
                    System.out.println("The products which the supplier supply are: ");
                    ProductSupplierService ps = new ProductSupplierService();
                    String json1 = ps.getProductsOfSupplier(supplier.getSupplierNumber());
                    if (json1 == "fail") {
                        System.out.println("supplier didnt found");
                        return;
                    }
                    Map<Integer, LinkedTreeMap> productMap = new HashMap<>();
                    productMap = gson.fromJson(json1, productMap.getClass());
                    int i = 1;
                    for (LinkedTreeMap p : productMap.values()) {
                        Product product = Menu.fromJson(p.toString(), Product.class);
                        System.out.println("\t" + i + ". " + product.toString());
                        i++;
                    }
                    System.out.println("Enter a catalog number");
                    choiceStr = "";
                    int catalogNum = 0;
                    try {
                        choiceStr = sc.next();
                        catalogNum = Integer.parseInt(choiceStr);
                    } catch (Exception e) {
                        System.out.println("you must enter only 1 digit number");
                        return;
                    }
                    System.out.println("Enter an amount");
                    choiceStr = "";
                    int count = 0;
                    try {
                        choiceStr = sc.next();
                        count = Integer.parseInt(choiceStr);
                    } catch (Exception e) {
                        System.out.println("you must enter only 1 digit number");
                        return;
                    }
                    boolean added = orderService.addProductToOrder(supplier.getSupplierNumber(), o.getOrderId(), catalogNum, count);
                    if (!added) {
                        System.out.println("The product isn't exist or the amount was invalid");
                        return;
                    }
                    break;
                case 2:
                    determineDeliveryDays(o);
            }
        }
    }

    private void determineDeliveryDays(Order o) {
        int choice =0;
        while(choice != 2) {
            System.out.println(" Do to want to add delivery days to this order?");
            System.out.println("\t1. YES.");
            System.out.println("\t2. NO.");
            String choiceStr = "";
            try {
                choiceStr = sc.next();
                choice = Integer.parseInt(choiceStr);
            } catch (Exception e) {
                System.out.println("you must enter only 1 digit number");
                return;
            }
            switch (choice) {
                case 1:
                    addDeliveryDays(o);
                    break;
                case 2:
                    break;
            }
        }
    }

    private void addDeliveryDays(Order o) {
        System.out.println(" Which days do yo want the order to arrive? Enter the numbers so that they are not separated by a space");
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
            try {
                if (Integer.parseInt(day) < 1 | Integer.parseInt(day) > 7) {
                    System.out.println("invalid input");
                    return;
                }
            }catch (Exception e){
                System.out.println("invalid input");
                return;
            }
        }
        for(int i=0;i< Days.length-1;i++){
           for(int j=i+1;j< Days.length;j++){
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
        boolean added=orderService.addFixedDeliveryDaysForOrder(supplier.getSupplierNumber(), o.getOrderId(), Days);
        if(added){
            System.out.println("days added!");
        }
        else {
            System.out.println("invalid input");
            return;
        }
    }
    public void watchOrdersMenu(Supplier s) {
        int choice =0;
        while(choice != 7) {
            System.out.println("You can now see and manage your orders:");
            System.out.println("Choose what you want");
            System.out.println("\t1. create order to supplier.");
            System.out.println("\t2. Send orders.");
            System.out.println("\t3. update order.");
            System.out.println("\t4. See all your wait orders.");
            System.out.println("\t5. See all your orders in fixed days delivery.");
            System.out.println("\t6. See all your past orders..");
            System.out.println("\t7. go back");
            String choiceStr = "";
            try {
                choiceStr = sc.next();
                choice = Integer.parseInt(choiceStr);
            } catch (Exception e) {
                System.out.println("you must enter only 1 digit number");
                return;
            }
            if (choice != 4 & choice != 5 & choice != 6 & choice != 7) {
                if (!roles.contains(JobType.STOCK_KEEPER)) {
                    System.out.println("u dont have access to this area");
                    return;
                }

            }
            int orderId = 0;
            if (choice == 2 | choice == 3) {
                watchWaitOrders1();
                System.out.println("Enter orderID:");
                choiceStr = "";
                orderId = 0;
                try {
                    choiceStr = sc.next();
                    orderId = Integer.parseInt(choiceStr);
                } catch (Exception e) {
                    System.out.println("you must enter only 1 digit number");
                    return;
                }
            }
            switch (choice) {
                case 1:
                    OrderMenu om = new OrderMenu(s);
                    om.setRoles(roles);
                    om.newOrder();
                    break;
                case 2:
                    sendOrders(orderId);
                    break;
                case 3:
                    updateOrderMenu(orderId);
                    break;
                case 4:
                    watchWaitOrders();
                    break;
                case 5:
                    watchFixedDaysOrders();
                    break;
                case 6:
                    watchPastOrders();
                    break;
                case 7:
                    break;
                default:
                    System.out.println("You must type number between 1 to 8");
            }
            //sm.chooseSupplierMenu();
        }
    }

    private void watchPastOrders() {
        Map<Integer, PastOrder> orders =orderService.getPastOrders(supplier.getSupplierNumber());
        if(orders.isEmpty()){
            System.out.println("there is no past orders to display");
        }
        for(PastOrder p :orders.values()){
            System.out.println(p.toString());
        }
    }

    private void sendOrders(int orderId) {
        printOrderDetailsBeforeClose(orderId);
        boolean send=orderService.sendOrder(supplier.getSupplierNumber(), orderId);
        if(send){
         System.out.println("Order sent successfully");
        }
        else{
          System.out.println("The order wasn't found");
        }
    }

    private void printOrderDetailsBeforeClose(int orderId) {
        String print=orderService.getOrderDetails(orderId);
        if(!print.equals("fail")){
            System.out.println("the final price of the order is:"+print);
        }
    }

    private void watchFixedDaysOrders() {
        Map<Integer,DeliveryTerm> orders= orderService.getFixedDaysOrders(supplier.getSupplierNumber());
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
        }
        for(Integer x: orders.keySet()){
            System.out.println("\nOrderID:" +" "+x+" , "+"days to deliver: "+ orders.get(x).toString());
        }
    }

    private void watchWaitOrders() {
        Map<Integer, Order> orders=orderService.getActiveOrders(supplier.getSupplierNumber());
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
        }
        for(Order o: orders.values()){
            System.out.println(o.toString());
        }
    }
    private void watchWaitOrders1() {
        Map<Integer, Order> orders=orderService.getActiveOrders(supplier.getSupplierNumber());
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
        }
        for(Order o: orders.values()){
            System.out.println(o.toString());
        }
    }

    private void updateOrderMenu(int orderID) {
        String json = orderService.getOrder(supplier.getSupplierNumber(), orderID);
        if(json.equals("fail")){
           System.out.println("Order does not exist");
           return;
        }
        int choice =0;
        while (choice != 5) {
            System.out.println("Order: " + orderID);
            System.out.println("Choose what you want:");
            System.out.println("\t1. add new product.");
            System.out.println("\t2. watch all the product in the order.");
            System.out.println("\t3. update product in order.");
            System.out.println("\t4. update deliver days in order.");
            System.out.println("\t5. Return to choose orders.");
            String choiceStr = "";
            try {
                choiceStr = sc.next();
                choice = Integer.parseInt(choiceStr);
            } catch (Exception e) {
                System.out.println("you must enter only 1 digit number");
                return;
            }
            switch (choice) {
                case 1:
                    System.out.println("Enter catalog number:");
                    choiceStr = "";
                    int catalogNum = 0;
                    try {
                        choiceStr = sc.next();
                        catalogNum = Integer.parseInt(choiceStr);
                    } catch (Exception e) {
                        System.out.println("you must enter only numbers");
                        return;
                    }
                    System.out.println("Enter count:");
                    choiceStr = "";
                    int count = 0;
                    try {
                        choiceStr = sc.next();
                        count = Integer.parseInt(choiceStr);
                    } catch (Exception e) {
                        System.out.println("you must enter only numbers");
                        return;
                    }
                    orderService.addProductToOrder(supplier.getSupplierNumber(), orderID, catalogNum, count);
                    break;
                case 2:
                    watchProductInOrder(orderID);
                    break;
                case 3:
                    updateProductInOrder(orderID);
                    break;
                case 4:
                    updateDeliverDaysInOrder(orderID);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("You must type number between 1 to 3");
                    break;
            }
//            System.out.println("You want to edit anther product?\n1.YES\n2.NO");
//            choiceStr = "";
//            choice = 0;
//            try {
//                choiceStr = sc.next();
//                choice = Integer.parseInt(choiceStr);
//            } catch (Exception e) {
//                System.out.println("you must enter only 1 digit number");
//                return;
//            }
//            switch (choice) {
//                case 1:
//                    break;
//                case 2:
//                    break;
//
//            }
        }
    }

    private void updateDeliverDaysInOrder(int orderID) {
        System.out.println(" Which days do yo want the order to arrive? Enter the numbers so that they are not separated by a space");
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
        boolean added=orderService.updateFixedDeliveryDaysForOrder(supplier.getSupplierNumber(), orderID, Days);
        if(added){
            System.out.println("days changed!");
        }
        else{
            System.out.println("Invalid Input");
        }
        updateOrderMenu(orderID);
    }

    private void updateProductInOrder(int orderID) {
        System.out.println("choose product you want to edit and enter the catalog number:");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only numbers");
            return;
        }
        System.out.println("Enter the new count for product (0 for delete");
        choiceStr = "";
        int count =0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only numbers");
            return;
        }
        if (count > 0){
            orderService.updateProductInOrder(supplier.getSupplierNumber(),orderID, catalogNum, count);
        }
        else{
            orderService.deleteProductFromOrder(supplier.getSupplierNumber(), orderID,catalogNum);
        }
    }

    private void watchProductInOrder(int orderID) {
        String json = orderService.getProductsInOrder(supplier.getSupplierNumber(), orderID);
        if(json.equals("fail")){
            System.out.println("Order or supplier does not exist");
            return;
        }
        Map<String,Integer> products = new HashMap<>();
        products=Menu.fromJson(json, products.getClass());
        System.out.println("Product in Order: "+ orderID);
        int i = 1;
        if(products.isEmpty()){
            System.out.println("There is no products to show");
        }
        for (Map.Entry<String,Integer> e: products.entrySet()){
            Product p = gson.fromJson(e.getKey(), Product.class);
            System.out.println("\t"+i+ ". "+ p.toString() + ", in count: "+ e.getValue() );
            i++;
        }
    }


}

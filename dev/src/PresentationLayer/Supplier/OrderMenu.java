package PresentationLayer.Supplier;

import ServiceLayer.DeliveryService;
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
    private DeliveryService ds;

    public OrderMenu(Supplier s) {
        this.supplier = s;
        orderService = new OrderService();
        gson = new Gson();
        sm =new SupplierMenu();
        ds =new DeliveryService();

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
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            addProductsToOrder(o);
        }
        switch (choice){
            case 1:
                System.out.println("The products which the supplier supply are: ");
                ProductSupplierService ps=new ProductSupplierService();
                String json1 = ps.getProductsOfSupplier(supplier.getSupplierNumber());
                if(json1=="fail"){
                    System.out.println("supplier didnt found");
                    addProductsToOrder(o);
                }
                Map<Integer, LinkedTreeMap> productMap=new HashMap<>();
                productMap=gson.fromJson(json1, productMap.getClass());
                int i=1;
                for (LinkedTreeMap p: productMap.values()){
                    Product product = Menu.fromJson(p.toString(), Product.class);
                    System.out.println("\t"+i + ". "+ product.toString());
                    i++;
                }
                System.out.println("Enter a catalog number");
                choiceStr = "";
                int catalogNum =0;
                try{
                    choiceStr = sc.next();
                    catalogNum=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    addProductsToOrder(o);
                }
                System.out.println("Enter an amount");
                choiceStr = "";
                int count =0;
                try{
                    choiceStr = sc.next();
                    count=Integer.parseInt(choiceStr);
                }
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
                determineDeliveryDays(o);
                sm.inSupplierMenu(supplier.getSupplierNumber());
        }
        addProductsToOrder(o);

    }

    private void determineDeliveryDays(Order o) {
        System.out.println(" Do to want to add delivery days to this order?");
        System.out.println("\t1. YES.");
        System.out.println("\t2. NO.");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            determineDeliveryDays(o);
        }
        switch (choice) {
            case 1:
                addDeliveryDays(o);
                break;
            case 2 :
                sm.inSupplierMenu(supplier.getSupplierNumber());
                break;
        }
     sm.inSupplierMenu(supplier.getSupplierNumber());
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
                addDeliveryDays(o);
            }
            if(Integer.parseInt(day)<1|Integer.parseInt(day)>7){
                System.out.println("invalid input");
                addDeliveryDays(o);
            }
        }
        for(int i=0;i< Days.length-1;i++){
           for(int j=i+1;j< Days.length;j++){
               if(Days[i].equals(Days[j])){
                   System.out.println("invalid input");
                   addDeliveryDays(o);
               }
           }
        }
        if(Days.length>7){
            System.out.println("invalid input");
            addDeliveryDays(o);
        }
        boolean added=ds.addFixedDeliveryDaysForOrder(supplier.getSupplierNumber(), o.getOrderId(), Days);
        if(added){
            System.out.println("days added!");
        }
        else {
            System.out.println("invalid input");
            addDeliveryDays(o);
        }
    }

    public void watchOrdersMenu() {
        System.out.println("You can now see and manage your orders:");
        System.out.println("Choose what you want");
        System.out.println("\t1. Update order.");
        System.out.println("\t2. See all your wait orders.");
        System.out.println("\t3. See all your orders in fixed days delivery.");
        System.out.println("\t4. Send orders.");
        System.out.println("\t5. Return to supplier page");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            watchOrdersMenu();
            return;
        }
        switch (choice){
            case 1:
                watchWaitOrdersForUpdate();
                System.out.println("Enter orderID:");
                choiceStr = "";
                int orderID =0;
                try{
                    choiceStr = sc.next();
                    orderID=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    watchOrdersMenu();
                    return;
                }
                updateOrderMenu(orderID);
                break;
            case 2:
                watchWaitOrders();
                break;
            case 3:
                watchFixedDaysOrders();
                break;
            case 4:
                System.out.println("Enter orderID:");
                choiceStr = "";
                int orderId =0;
                try{
                    choiceStr = sc.next();
                    orderId=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only 1 digit number");
                    watchOrdersMenu();
                    return;
                }
                sendOrders(orderId);
                break;
            case 5:
                sm.inSupplierMenu(supplier.getSupplierNumber());
            default:
                System.out.println("You must type number between 1 to 5");
                watchOrdersMenu();

        }
        sm.inSupplierMenu(supplier.getSupplierNumber());
    }

    private void sendOrders(int orderId) {
        boolean send=orderService.sendOrder(supplier.getSupplierNumber(), orderId);
        if(send){
         System.out.println("Order sent successfully");
        }
        else{
          System.out.println("The order wasn't found");
        }
    }

    private void watchFixedDaysOrders() {
        String json = orderService.getFixedDaysOrders(supplier.getSupplierNumber());
        Map<Integer, LinkedTreeMap> orders = new HashMap<Integer, LinkedTreeMap>();
        orders = Menu.fromJson(json, orders.getClass());
        List<Integer> list = new LinkedList<>();
        for(Object x: orders.keySet()){
         Integer integer=Menu.fromJson(x.toString(),Integer.class);
         list.add(integer);
        }
        int i=0;
        for(LinkedTreeMap x: orders.values()){
            DeliveryTerm deliveryTerm = Menu.fromJson(x.toString(), DeliveryTerm.class);
            System.out.println("\nOrderID:" +" "+list.get(i)+" , "+"days to deliver: "+ deliveryTerm.toString());
            i++;
        }
        watchOrdersMenu();

    }

    private void watchWaitOrders() {
        String json=orderService.getActiveOrders(supplier.getSupplierNumber());
        Map<Integer, LinkedTreeMap> orders = new HashMap<Integer, LinkedTreeMap>();
        orders = Menu.fromJson(json, orders.getClass());
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
        }
        for(LinkedTreeMap o: orders.values()){
            Order order = Menu.fromJson(o.toString(), Order.class);
            System.out.println(order.toString());
        }
        watchOrdersMenu();
    }
    private void watchWaitOrdersForUpdate() {
        String json=orderService.getActiveOrders(supplier.getSupplierNumber());
        Map<Integer, LinkedTreeMap> orders = new HashMap<Integer, LinkedTreeMap>();
        orders = Menu.fromJson(json, orders.getClass());
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
        }
        for(LinkedTreeMap o: orders.values()){
            Order order = Menu.fromJson(o.toString(), Order.class);
            System.out.println(order.toString());
        }
    }

    private void updateOrderMenu(int orderID) {
        String json = orderService.getOrder(supplier.getSupplierNumber(), orderID);
        if(json.equals("fail")){
           System.out.println("Order does not exist");
           watchOrdersMenu();
           return;
        }
        Order o = gson.fromJson(json,Order.class);
        System.out.println("Order: "+ orderID);
        System.out.println("Choose what you want:");
        System.out.println("\t1. add new product.");
        System.out.println("\t2. watch all the product in the order.");
        System.out.println("\t3. update product in order.");
        System.out.println("\t4. update deliver days in order.");
        System.out.println("\t5. Return to choose orders.");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            updateOrderMenu(orderID);
        }
        switch (choice){
            case 1:
                System.out.println("Enter catalog number:");
                choiceStr = "";
                int catalogNum =0;
                try{
                    choiceStr = sc.next();
                    catalogNum=Integer.parseInt(choiceStr);
                }
                catch (Exception e){
                    System.out.println("you must enter only numbers");
                    updateOrderMenu(orderID);
                    return;
                }
                System.out.println("Enter count:");
                choiceStr = "";
                int count =0;
                try{
                    choiceStr = sc.next();
                    count=Integer.parseInt(choiceStr);
                }
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
                updateDeliverDaysInOrder(orderID);
                break;
            case 5:
                watchOrdersMenu();
                break;
            default:
                System.out.println("You must type number between 1 to 3");
                updateOrderMenu(orderID);
                break;
        }
        System.out.println("You want to edit anther product?\n1.YES\n2.NO");
        choiceStr = "";
        choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            updateOrderMenu(orderID);
        }
        switch (choice){
            case 1:
                updateOrderMenu(orderID);
                break;
            case 2:
                new SupplierMenu().inSupplierMenu(supplier.getSupplierNumber());
        }
        updateOrderMenu(orderID);
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
                updateDeliverDaysInOrder(orderID);
            }
            if(Integer.parseInt(day)<1|Integer.parseInt(day)>7){
                System.out.println("invalid input");
                updateDeliverDaysInOrder(orderID);
            }
        }
        for(int i=0;i< Days.length;i++){
            for(int j=i+1;j< Days.length-1;j++){
                if(Days[i].equals(Days[j])){
                    System.out.println("invalid input");
                    updateDeliverDaysInOrder(orderID);
                }
            }
        }
        if(Days.length>7){
            System.out.println("invalid input");
            updateDeliverDaysInOrder(orderID);
        }
        boolean added=ds.updateFixedDeliveryDaysForOrder(supplier.getSupplierNumber(), orderID, Days);
        if(added){
            System.out.println("days changed!");
        }
        else{
            System.out.println("Invalid Input");
        }
        updateOrderMenu(orderID);
    }

    private void updateProductInOrder(int orderID) {
        watchOrdersMenu();
        System.out.println("choose product you want to edit and enter the catalog number:");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only numbers");
            updateProductInOrder(orderID);
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
        String json = orderService.getProductsInOrder(supplier.getSupplierNumber(), orderID);
        if(json.equals("fail")){
            System.out.println("Order or supplier does not exist");
            updateOrderMenu(orderID);
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
        updateOrderMenu(orderID);

    }


}

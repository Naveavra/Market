package PresentationLayer.Suppliers;

import DomainLayer.Employees.JobType;
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
    }
    public void setRoles(Set<JobType> roles){
        this.roles=roles;
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
                watchOrdersMenu(supplier);
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
                watchOrdersMenu(supplier);
                break;
        }
        watchOrdersMenu(supplier);
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
            try {
                if (Integer.parseInt(day) < 1 | Integer.parseInt(day) > 7) {
                    System.out.println("invalid input");
                    addDeliveryDays(o);
                }
            }catch (Exception e){
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
        boolean added=orderService.addFixedDeliveryDaysForOrder(supplier.getSupplierNumber(), o.getOrderId(), Days);
        if(added){
            System.out.println("days added!");
        }
        else {
            System.out.println("invalid input");
            addDeliveryDays(o);
        }
    }
    public void watchOrdersMenu(Supplier s) {
        System.out.println("You can now see and manage your orders:");
        System.out.println("Choose what you want");
        System.out.println("\t1. create order to supplier.");
        System.out.println("\t2. Send orders.");
        System.out.println("\t3. update order.");
        System.out.println("\t4. See all your wait orders.");
        System.out.println("\t5. See all your orders in fixed days delivery.");
        System.out.println("\t6. See all your past orders..");
        System.out.println("\t7. Return to supplier page");
        String choiceStr = "";
        int choice =0;
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only 1 digit number");
            watchOrdersMenu(s);
            return;
        }
        if(choice!=4& choice!=5 & choice!=6 & choice!=7){
            if(!roles.contains(JobType.STOCK_KEEPER)){
                System.out.println("u dont have access to this area");
                watchOrdersMenu(s);
            }

        }
        int orderId =0;
        if(choice ==2 | choice ==3){
            System.out.println("Enter orderID:");
            choiceStr = "";
            orderId =0;
            try{
                choiceStr = sc.next();
                orderId=Integer.parseInt(choiceStr);
            }
            catch (Exception e){
                System.out.println("you must enter only 1 digit number");
                watchOrdersMenu(s);
                return;
            }
        }
        switch (choice){
            case 1:
                OrderMenu om = new OrderMenu(s);
                om.setRoles(roles);
                om.newOrder();
                break;
            case 2:
                sendOrders(orderId);
                break;
            case 3:
                watchWaitOrdersForUpdate();
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
            case 7:
                sm.chooseSupplierMenu();
            default:
                System.out.println("You must type number between 1 to 8");
                watchOrdersMenu(s);
        }
        sm.chooseSupplierMenu();
    }

    private void watchPastOrders() {
        String json =orderService.getPastOrders(supplier.getSupplierNumber());
        Map<Integer,LinkedTreeMap> pastOrders =new HashMap<>();
        pastOrders = Menu.fromJson(json, pastOrders.getClass());
        if(pastOrders.isEmpty()){
            System.out.println("there is no past orders to display");
        }
        for(LinkedTreeMap p :pastOrders.values()){
            PastOrder pastOrder =Menu.fromJson(p.toString(), PastOrder.class);
            System.out.println(pastOrder.toString());
        }
        watchOrdersMenu(supplier);
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
        watchOrdersMenu(supplier);
    }

    private void printOrderDetailsBeforeClose(int orderId) {
        String print=orderService.getOrderDetails(orderId);
        if(!print.equals("fail")){
            System.out.println("the final price of the order is:"+print);
        }
    }

    private void watchFixedDaysOrders() {
        String json = orderService.getFixedDaysOrders(supplier.getSupplierNumber());
        Map<Integer, LinkedTreeMap> orders = new HashMap<Integer, LinkedTreeMap>();
        orders = Menu.fromJson(json, orders.getClass());
        List<Integer> list = new LinkedList<>();
        if(orders.isEmpty()){
            System.out.println("there is no orders to show");
        }
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
        watchOrdersMenu(supplier);
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
        watchOrdersMenu(supplier);
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
           watchOrdersMenu(supplier);
           return;
        }
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
                watchOrdersMenu(supplier);
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
                watchOrdersMenu(supplier);
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
        //watchOrdersMenu();
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

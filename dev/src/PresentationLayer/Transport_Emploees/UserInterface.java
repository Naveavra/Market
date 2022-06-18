package PresentationLayer.Transport_Emploees;

import DomainLayer.Employees.JobType;
import PresentationLayer.Suppliers.Contact;
import ServiceLayer.transport.UserService;
import ServiceLayer.transport.OrderTransportService;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserInterface extends MainCLI {


    public void start(Set<JobType> roles) {
        menu();
    }
    private UserService us = new UserService();
    private OrderTransportService os = new OrderTransportService();
//    private boolean createDriver(String name, String id, String license) {
  //      return us.createDriver(name, id, license);
   // }

    private boolean createTruck(String type, String licensePlate, double maxWeight, double initialweight) {
        return us.createTruck(type, licensePlate, maxWeight, initialweight);
    }

    private boolean createSite(String id, String address, String name, String pNumber, int area, int type) {
        return us.createSite(id, address, name, pNumber, area, type);
    }
//
//    private boolean createSupply(String name, double weight) {
//        return us.createSupply(name, weight);
//    }

//    private boolean removeDriver(String id) {
       // return us.removeDriver(id);
    //}

    private boolean removeTruck(String licensePlate) {
        return us.removeTruck(licensePlate);
    }

    private boolean removeSitebyID(String id) {
        return us.removeSite(id);
    }
//    private String removeSupply(String suppName2Remove){return us.removeSupply(suppName2Remove);}
//


    private void menu() {
        boolean closed = false;
        while (!closed) {

            System.out.println("Welcome to the Transport Management System.\n" +
                    "Please choose an option\n" +
                    "\t1.Add/Remove resources\n" +
                    "\t2.Orders\n" +
                    "\t3.Exit System");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            try {
                switch (choice) {
                    case 1: //add or remove trucks drivers and sites
                        editResources();
                        break;
                    case 2: //Orders
                        orders();
                        break;
                    case 3:
                        closed=true;
                        System.out.println("Thank you for using our Transport System, see you again next time!\n");
                        break;
                }
            }catch (Exception e){
                System.out.println("Something went wrong, please try again.");
            }
        }
    }

    private void orders() {
        boolean finished = false;
        while (!finished) {
            System.out.println("Please select an option:\n" +
                    "\t1.Edit Documents\n" +
                    "\t2.View Order\n" +
                    "\t3.Create new Order\n" +
                    "\t4.Show Driver Docs\n" +
                    "\t5.go back");

            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    editDocuments();
                    break;
                case 2: //View orders
                    viewOrder();
                    break;
                case 3:
                    if(!createOrder()){
                        System.out.println("Order was not created");
                    }
                    break;
                case 4: //view driver docs
                    try{
                        String docID = getDoc();
                        System.out.println(os.showDriverDocs(docID));
                    }catch (Exception e){
                        //System.out.println(e.getMessage());
                        System.out.println("Doc doesnt exist or ID is wrong");
                    }
                    break;
                case 5:
                    finished=true;
                    break;
            }
        }
    }

    private void changeOrderList(ArrayList<String> suppnames,ArrayList<Integer> quantities,String docID,String storeID){
        while(true) {
            System.out.println("Please select supplies ,When you are done enter OK:");
            System.out.println(os.showSuppliesByDoc(docID, storeID));
            Scanner input = new Scanner(System.in);
            String res = input.nextLine();
            if (res.equalsIgnoreCase( "ok")) {
                return;
            }
            int miki = Integer.parseInt(res);
            suppnames.add(os.getSupplyByIdAndDoc(miki,docID,storeID));
            System.out.println("Please enter desired quantity:");
            input = new Scanner(System.in);
            int quantity = input.nextInt();
            quantities.add(quantity);
        }
    }
    private void changeSuppQuantity(){
        String doc=null;
        String storeID =null;
        ArrayList<String> suppNames = new ArrayList<>();
        ArrayList<Integer> quantities =new ArrayList<>();
        try {
            doc = getDoc();
            String finish = os.GetFinish(doc);
            if (finish.equals("#t")){
                System.out.println("Transport is already done homie");
                return;
            }
            System.out.println("Please enter store -ID- from the list below:");
            System.out.println(showStores(doc));
            if(showStores(doc).isEmpty()){
                return;
            }
            Scanner input = new Scanner(System.in);
            storeID = input.nextLine();
            if(!areYouSure()){
                System.out.println("Action Canceled Successfully");
                return;
            }
            changeOrderList(suppNames,quantities,doc,storeID);
            if(os.changeOrder(doc,storeID,suppNames,quantities)) {
                System.out.println("Order changed hopefully");
            }
//            while (true){
//                System.out.println("Please select supplies, When you are done enter OK:");
//                System.out.println(os.showSuppliesByDoc(doc, storeID));
//                input = new Scanner(System.in);
//                String suppName = os.getSupplyByIdx(input.nextInt());
//                System.out.println("Please enter new quantity:");
//                input = new Scanner(System.in);
//                int quantity = input.nextInt();
//                os.changeQuantity(doc, storeID, suppName, quantity);
//            }
        } catch (Exception e) {
            if(doc==null) {
                System.out.println("Some information is wrong or missing, please try again.");
            }
            else{
                os.changeOrder(doc,storeID,suppNames,quantities);
            }
        }
    }
    private void setTruckWeight(){
        boolean temp = true;
        String doc;
        while(true){
            try {
                doc = getDoc();
                String finish = os.GetFinish(doc);
                if (finish == "#t"){
                    System.out.println("Transport is already finished G");
                    return;
                }
                double currentWeight = os.getCurrWeight(doc);
                System.out.println("Current weight is: "+currentWeight+" \n" +
                        "Please enter new weight:");
                Scanner input = new Scanner(System.in);
                double weight2add = input.nextDouble();
                if(!areYouSure()){
                    System.out.println("Action Canceled Successfully");
                    return;
                }
                temp = os.setTrucksWeight(doc, weight2add);
                while(!temp){
                    System.out.println("Weight exceeds maximum");
                    overload();
                    System.out.println("Please enter the new and valid weight:");
                    Scanner input1 = new Scanner(System.in);
                    double weight2add1 = input1.nextInt();
                    temp = os.setTrucksWeight(doc, weight2add1);
                }
                System.out.println("Noted!");
                break;

            } catch (Exception e) {
                System.out.println("Problem reaching the order doc or weight input is not valid");
            }
                }
            }


//    private static void setDeparture(String docID){
//        while (true) {
//            System.out.println("Please enter the time in a HH:MM format:");
//            Scanner input = new Scanner(System.in);
//            String time = input.nextLine();
//            if (os.validhour(time)){
//                os.setDepartureTime(docID, time);
//                break;
//            }
//        }}
    private void editDocuments(){
        System.out.println("Please select an option:\n" +
                "\t1.change quantity\n" +
                "\t2.set current weight\n" +
                "\t3.Delete Document\n" +
                "\t4.Transport finished\n" +
                "\t5.Delete Sites from Order Docs\n" +
                "\t6.go back");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        switch (choice) {
            case 1: //change the quantity of a supply in orderDoc
                changeSuppQuantity();
                break;
            case 2: //set weight for a truck
                setTruckWeight();
                break;
            case 3:
                try{
                    os.removeDoc(getDoc());
                    System.out.println("Document removed successfully");
                }
                catch (Exception e){
                    System.out.println("Document doesnt exist");
                }
                break;
            case 4:
                try{
                    String doc = getDoc();
                    if(doc!=null) {
                        if (os.GetFinish(doc).equals("#t")) {
                            System.out.println("Transport is already finished cuz");
                            break;
                        }
                        os.transportIsDone(doc);
                        System.out.println("Transport ID: " + doc + " Is Finished, Nice Work G");
                    }
                }catch (Exception e){
                    System.out.println("Doc doesnt exist.");
                }
                break;
            case 5:
                try {
                    if(!removeSitesFromDoc(getDoc())){
                        System.out.println("Something went wrong");
                        break;
                    }
                    System.out.println("Site removed Successfully");
                }catch (Exception e){
                    System.out.println("Something went wrong");
                }
                break;
            case 6:
                break;
        }
    }
    private  void viewOrder(){
        try {
            System.out.println(viewOrder(getDoc()));
        } catch (Exception e) {
            System.out.println("ID or Date are incorrect or Order doesn't exist, please try again:");
        }
    }
    private  String viewOrder(String docID){
        return os.viewOrder(docID);
    }
    private boolean createOrder() {
        ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders = new ConcurrentHashMap<>();
        Scanner input;
        String areacode = "";
        System.out.println("Please choose the area for this transport (0, 1, 2):");
        input = new Scanner(System.in);
        areacode = input.nextLine();
        while (true) {
            System.out.println("Would you like to create a supply list?(enter 1 or 2)\n" +
                    "\t1. Yes.\n" +
                    "\t2. No.");
            input = new Scanner(System.in);
            String cond = input.nextLine();
            if(Objects.equals(cond, "2")){break;}
            System.out.println("Please enter a store"+RED_BOLD+" ID"+reset+" from the list below\n");
            String stores = os.showStores(areacode);
            if(stores==""){
                System.out.println("There are no stores at this area yet");
                return false;
            }
            System.out.println(stores);
            input = new Scanner(System.in);
            String storeID = input.nextLine();
            orders.put(storeID, createOrderList());
        }
        System.out.println("Please enter a supplier ID:");
        String suppliers = os.showSuppliers(areacode);
        if(suppliers.equals("No suppliers available, sorry G")){
            System.out.println(suppliers);
            return false;
        }
        System.out.println(suppliers);
        input = new Scanner(System.in);
        String supplierID = input.nextLine();
        boolean validDate = false;
        String date = "";
        while (!validDate) {
            System.out.println("Please enter a date in a DD/MM/YYYY Format:");
            input = new Scanner(System.in);
            date = input.nextLine();
            validDate = checkDate(date);
        }
        System.out.println("Please choose the time for the Transport:\n1.Morning\n2.Evening");
        input = new Scanner(System.in);
        String time = input.nextLine();
        if (time.equals("1")){
            time = "MORNING";
        }
        else {
            time = "EVENING";
        }
        String driverID = chooseDriver(date,time);
        if(driverID==null){return false;}
        String truckPlate = chooseTruck(date, driverID,time);
        if(truckPlate==null){
            return false;
        }
        if(!areYouSure()){
            System.out.println("Action Canceled Successfully");
            return false;
        }
        if (!os.setTruckAndDriver(driverID, truckPlate, time, date))//set for their availability
         {
            System.out.println("Something went wrong while trying set driver and truck availability");
            return false;
        }
        String id=os.createDoc(orders, supplierID, date,driverID,truckPlate, time);
        System.out.println("Order created successfully please keep the document id for future needs, id:" + id);
        return true;
    }



    //creates a HashMap<Supply,Quantity(int)>.  i.e supplies list
    private ConcurrentHashMap<String, Integer> createOrderList() {
        ConcurrentHashMap<String, Integer> supplies = new ConcurrentHashMap<>();
        Scanner input;
        try {
            while (true) {
                System.out.println("Please select supplies, when you are done enter OK:");
                System.out.println(os.showSupplies());
                input = new Scanner(System.in);
                String suppName = os.getSupplyByIdx(input.nextInt());
                System.out.println("Please select quantity:");
                input = new Scanner(System.in);
                int quantity = input.nextInt();
                supplies.put(suppName, quantity);
            }
        } catch (Exception e) {
            return supplies;
        }
    }
    private void changeTruck(String docID){
        String truckPlate = chooseTruck(getDate(docID),getDriverID(docID),getTime(docID));
        os.setNewTruck(docID,truckPlate);
    }

    private String getTime(String docID) {
        return os.getTime(docID);
    }

    private String getDate(String docID){
        return os.getDate(docID);
    }
    private String getDriverID(String docID){
        return os.getDriverID(docID);
    }
    private void changeDests(String docID){
        System.out.println("What would you like to do?\n" +
                "1.Replace destinations\n" +
                "2.Remove destination");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice == 1) {
            System.out.println("Please choose area code:\n" +
                    "0-North\n" +
                    "1-Center\n" +
                    "2-South");
            input = new Scanner(System.in);
            String areaCode = input.nextLine();
            System.out.println("Please choose Store ID to replace:");
            System.out.println(showStores(docID));
            input = new Scanner(System.in);
            String id2replace = input.nextLine();
            System.out.println("Please choose new Store ID");
            System.out.println(os.showStores(areaCode));
            input = new Scanner(System.in);
            String newStoreID = input.nextLine();
            try {
                if(!areYouSure()){
                    System.out.println("Action Canceled Successfully");
                    return;
                }
                ConcurrentHashMap<String, Integer> supplies = createOrderList();
                os.replaceStores(docID, id2replace, newStoreID, supplies);
            } catch (NullPointerException ignored) {
            }
        } else if (choice == 2) {
            removeSitesFromDoc(docID);
        }
    }
    private String showStores(String docID){
        return os.showStoresForDoc(docID);
    }
    private boolean removeSitesFromDoc(String docID) {
        try {
            String finish = os.GetFinish(docID);
            if (finish == "#t"){
                return false;
            }
            System.out.println("Please choose Store ID to remove:");
            System.out.println(showStores(docID));
            Scanner input = new Scanner(System.in);
            String storeID = input.nextLine();
            if(!areYouSure()){
                System.out.println("Action Canceled Successfully");
                return false;
            }
            os.removeSiteFromDoc(docID, storeID);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    private void removeSupplies(String docID){
        System.out.println("Please select a -Store ID- you want to remove from:");
        System.out.println(showStores(docID));
        Scanner input = new Scanner(System.in);
        String storeID = input.nextLine();
        ArrayList<String> supplyNames = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
            try {
                changeOrderList(supplyNames,quantities,docID,storeID);
                if(!areYouSure()){
                    System.out.println("Action Canceled Successfully");
                    return;
                }
                if(os.changeOrder(docID,storeID,supplyNames,quantities)) {
                    System.out.println("Order changed hopefully");
                }
            }catch (Exception e) {
                if(os.changeOrder(docID,storeID,supplyNames,quantities)) {
                    System.out.println("Order changed hopefully");
                }else {
                    System.out.println("Order did not change, something went wrong");
                }
                }
    }

    private void overload() {
        System.out.println("What would you like to do?:\n" +
                "1.Change Truck.\n" +
                "2.Change destinations.\n" +
                "3.Remove supplies\n" +
                "4.go back");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        try {
            String doc = getDoc();
            if (doc != null) {
                switch (choice) {
                    case 1: // change truck
                        changeTruck(doc);
                        break;
                    case 2: //change destinations
                        changeDests(doc);
                        break;
                    case 3:
                        removeSupplies(doc);
                        break;
                    case 4:
            }
        }
        }catch (Exception e){
            System.out.println("Document doesnt exist.");
            overload();
        }
    }

    private String chooseTruck(String date,String driverID,String time) {
        String truckLst = "";
        String t;
        while(true){
            System.out.println("Please enter a truck"+RED_BOLD+ " license plate:"+reset);
            truckLst = os.showTrucks(date,driverID,time);
            if(truckLst.isEmpty()){
                System.out.println("No trucks available");
                return null;
            }
            System.out.println(truckLst);
            Scanner input = new Scanner(System.in);
            t=os.getTruck(input.nextLine());
            if(t!=null){
                break;
            }
            else {
                System.out.println("Something went wrong, please try again.");
            }
        }
        return t;
    }


    private String chooseDriver(String date,String time) {
        String driversLst = "";
        String d;
        while (true) {
            System.out.println("Please enter a "+" -Driver ID- "+" from the list below:");
            driversLst = os.showDrivers(date,time);
            if(driversLst.isEmpty()){
                return null;
            }
            System.out.println(driversLst);
            Scanner input = new Scanner(System.in);
            d = os.getDriver(input.nextLine());
            if (d != null) {
                break;
            } else {
                System.out.println("Something went wrong, please try again.");
            }
        }
        return d;
    }
    /*    public boolean matchTruckDriver(Truck t,Driver d, Date date){
            return os.matchTruckDriver(t,d, date);
        }*/
//    private void addDriver(){
//        System.out.println("Please enter the drivers name:");
//        Scanner input = new Scanner(System.in);
//        String name = input.nextLine();
//        System.out.println("Please enter drivers' ID:");
//        input = new Scanner(System.in);
//        String id = input.nextLine();
//        System.out.println("Please enter drivers' license type (C or C1):");
//        input = new Scanner(System.in);
//        String license = input.nextLine();
//        if (createDriver(name, id, license)) {
//            System.out.println("Welcome to the Family G\n");
//        } else {
//            System.out.println("Something went wrong please try again");
//        }
//    }
//    private void removeDriver(){
//        System.out.println("Please enter the ID of the driver you would like to remove:");
//        Scanner input = new Scanner(System.in);
//        String id2remove = input.nextLine();
//        if (removeDriver(id2remove)) {
//            System.out.println("Action succeeded");
//        } else {
//            System.out.println("Something went wrong please try again");
//        }
//    }
    private void addTruck(){
        System.out.println("Please choose trucks' type(1 or 2):\n" +
                "1. C\n" +
                "2. C1");
        Scanner input = new Scanner(System.in);
        int truckType = input.nextInt();
        String type;
        if (truckType == 1) {
            type = "C";
        } else {
            type = "C1";
        }
        System.out.println("Please enter trucks' license plate:");
        input = new Scanner(System.in);
        String licenseP = input.nextLine();
        System.out.println("Please enter trucks' max weight:");
        input = new Scanner(System.in);
        double maxWeight = input.nextDouble();
        System.out.println("Please enter trucks' initial weight:");
        input = new Scanner(System.in);
        double initialWeight = input.nextDouble();
        if(!areYouSure()){
            System.out.println("Action Canceled Successfully");
            return;
        }
        if (createTruck(type, licenseP, maxWeight, initialWeight)) {
            System.out.println("Action succeeded, new whip skrrttt skkkrrrtt!!");
        } else {
            System.out.println("Something went wrong please try again");
        }

    }
    private void removeTruck(){
        System.out.println("Please enter the license plate of the truck you would like to remove:");
        Scanner input = new Scanner(System.in);
        String truck2remove = input.nextLine();
        if(!areYouSure()){
            System.out.println("Action Canceled Successfully");
            return;
        }
        if (removeTruck(truck2remove)) {
            System.out.println("Action succeeded");
        } else {
            System.out.println("Something went wrong please try again");
        }
    }
    private void addSite(){
        System.out.println("Please enter sites' id:");
        Scanner input = new Scanner(System.in);
        String siteID = input.nextLine();
        int type = 1;
        System.out.println("Please enter contacts' address:");
        input = new Scanner(System.in);
        String addr = input.nextLine();
        System.out.println("Please enter contacts' name:");
        input = new Scanner(System.in);
        String cName = input.nextLine();
        System.out.println("Please enter contacts' phone number:");
        input = new Scanner(System.in);
        String pNum = input.nextLine();
        System.out.println("Please choose shipping area:\n" +
                "0.North\n" +
                "1.Center\n" +
                "2.South");
        input = new Scanner(System.in);
        int shippingArea = input.nextInt();
        if(!areYouSure()){
            System.out.println("Action Canceled Successfully");
            return;
        }
        createSite(siteID, addr, cName, pNum, shippingArea, type);
        System.out.println("Store created successfully");
    }
    private void removeSite(){
        System.out.println("Please enter the sites' ID you would like to remove");
        Scanner input = new Scanner(System.in);
        String site2Remove = input.nextLine();
        if(!areYouSure()){
            System.out.println("Action Canceled Successfully");
            return;
        }
        if(removeSitebyID(site2Remove)){
            System.out.println("Site removed");
        }else{
            System.out.println("Site doesnt exist");
        }
    }
//    private void removeSupply(){
//        System.out.println("Please enter supply name to remove:");
//        Scanner input = new Scanner(System.in);
//        String suppName2Remove = input.nextLine();
//        System.out.println(removeSupply(suppName2Remove));
//    }

//    private void addSupply(){
//        System.out.println("Please enter supply name:");
//        Scanner input = new Scanner(System.in);
//        String suppName = input.nextLine();
//        System.out.println("Please enter supply weight:");
//        input = new Scanner(System.in);
//        double suppWeight = input.nextDouble();
//        createSupply(suppName,suppWeight);
//        System.out.println("Supply created.");
//    }
    private void editResources() {
        boolean c1;
        while (true) {
            System.out.println("You chose Resource Control.\nPlease choose an option:\n" +
                    "\t1.Edit Trucks\n" +
                    "\t2.Edit Stores\n" +
//                    "\t2.Edit Supplies\n" +
                    "\t3.Go back\n");

            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
                case 1: //add/remove trucks
                    c1 = false;
                    while (!c1) {
                        System.out.println("Would you like to add or remove a truck?\n" +
                                "\t1.Add truck.\n" +
                                "\t2.Remove truck.\n" +
                                "\t3.Go back.");
                        input = new Scanner(System.in);
                        choice = input.nextInt();
                        switch (choice) {
                            case 1:  //add truck
                                addTruck();
                                break;
                            case 2://remove truck
                                removeTruck();
                                break;
                            case 3:
                                c1=true;
                                break;
                        }
                    }
                    break;
                case 2: //add or remove sites
                    c1 = false;
                    while (!c1) {
                        System.out.println("Would you like to add or remove stores?:\n" +
                                "\t1.Add store.\n" +
                                "\t2.Remove store.\n" +
                                "\t3.go back.");
                        input = new Scanner(System.in);
                        choice = input.nextInt();
                        if (choice == 1) { //add site
                            addSite();
                        } else if (choice == 2) { //remove site
                            removeSite();
                        } else c1=true;
                    }
                    break;
                case 3: //add/remove supplies
//                    c1 =false;
//                    while (!c1) {
//                        System.out.println("Would you like to add or remove supplies?\n" +
//                                "\t1.Add supplies.\n" +
//                                "\t2.Remove supplies.\n" +
//                                "\t3.Go back.");
//                        input = new Scanner(System.in);
//                        choice = input.nextInt();
//                        switch (choice) {
//                            case 1:// add supplies
//                                addSupply();
//                                break;
//                            case 2: //remove supplies
//                                removeSupply();
//                                break;
//                            case 3:
//                                c1=true;
//                        }
//                    }
//                    break;
                case 4:
                    return;
            }

        }
    }

    private String getDoc(){
        System.out.println("Enter Order id from the list below:");
        String ids = os.getAllOrderDocIDs();
        if(ids.length()==0){
            System.out.println("there are no available orders you can choose from");
            return null;
        }
        else {
            System.out.println(ids);
            Scanner input = new Scanner(System.in);
            String orderID = input.nextLine();
            if (!os.getDoc(orderID)) {
                return null;
            }
            return orderID;
        }
    }


        private boolean checkDate (String date){
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6));
            return day <= 31 && month <= 12 && (year <= 2050 && year >= 1948);

        }
        public boolean areYouSure(){
            System.out.println("Please Select (1 or 2),\nAre you sure you would like to complete this action? \n"+
                    "\t1. Complete.\n"+
                    "\t2. Cancel.");
            Scanner input = new Scanner(System.in);
            String res= input.nextLine();
            return Objects.equals(res, "1");
        }
    }



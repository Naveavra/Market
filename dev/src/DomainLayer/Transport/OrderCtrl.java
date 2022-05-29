package DomainLayer.Transport;

import DAL.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderCtrl {
    public DriverDocDAO driverDocs = DriverDocDAO.getInstance();
    public OrderDocDAO orderDocs = OrderDocDAO.getInstance();
    public TruckDAO trucks = TruckDAO.getInstance();
    public SiteDAO sites = SiteDAO.getInstance();
    public SuppliesDAO supplies = SuppliesDAO.getInstance();
    public DriverDAO drivers =DriverDAO.getInstance();
    AtomicInteger id = new AtomicInteger();
    AtomicInteger driverDocID = new AtomicInteger();
    private final static OrderCtrl INSTANCE = new OrderCtrl();
    public static OrderCtrl getInstance(){
        return INSTANCE;
    }
//    public OrderDoc getDocByID(String id){
//        return pool.getOrderDocByID(id);
//    }

    public Date createDate(String date){
        String[] temp = date.split("/");
        return new Date(temp[0], temp[1], temp[2]);

    }
    public void build(){
        trucks.addTruck("C", "nadia", 123456, 120000);
        trucks.addTruck("C1", "shahar", 12345, 12000);
        trucks.addTruck("C", "optimusprime", 2000, 1000);
        trucks.addTruck("C1", "megatron", 1500, 1000);
        supplies.addSupply("milk", 5);
        supplies.addSupply("eggs", 2.13);
        supplies.addSupply("matza", 20);
        supplies.addSupply("computer", 1000);
        supplies.addSupply("bmw", 5000);
        supplies.addSupply("volvo", 10000);
        sites.addSite("1567", 0, 0, "hakanaim 16", "liron", "123456789");
        sites.addSite("156", 0, 1, "eretzhakulim", "dan", "123333");
        sites.addSite("136", 0, 1, "eretz", "dannyboy", "1233713");
        sites.addSite("153", 1, 1, "ereulim", "shuli", "124333");
        sites.addSite("111101", 1, 0,"yotveta 6", "david", "050000" );
        sites.addSite("11111", 2, 1, "rager 52", "stas", "000055");
        sites.addSite("3333", 1, 0,"jaja", "yuri", "0022" );
        sites.addSite("77", 2, 0, "m", "ni", "043333"); //new
        sites.addSite("76", 2, 0, "f", "df", "ds");
        sites.addSite("75", 2, 1, "dww", "w", "545");
        Driver dan =new Driver("Dan", "11123", "C1");
        Driver nave = new Driver("nave", "123", "C1");
        Driver miki = new Driver("miki", "1234", "C");
        Driver itay = new Driver("itay", "1111", "C");
        drivers.addDriver(dan);
        drivers.addDriver(nave);
        drivers.addDriver(miki);
        drivers.addDriver(itay);
        Contact con1 = new Contact("hakanaim 16", "liron", "123456789");
        Site hakanaim_16 = new Site("1567", con1,Site.ShippingArea.North, 0);
        Truck nadia =  new Truck("C", "nadia", 123456, 120000);
        Truck shahar = new Truck("C1", "shahar", 12345, 12000);
        Contact con5 = new Contact("yotveta 6", "david", "050000");
        Site yotveats6 = new Site("111101", con5, Site.ShippingArea.Center, 0);
        Contact con2 = new Contact("eretzhakulim", "dan", "123333");
        Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
        Supply milk =  new Supply("milk", 5);
        Supply eggs = new Supply("eggs", 2.13);
        Supply matza =  new Supply("matza", 20);
        Supply computer = new Supply("computer", 1000);
        Date date1 = new Date("16","06","2022");
        Date date2 = new Date("17","06","2022");
        ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
        ConcurrentHashMap<Supply,Integer> supplst2 = new ConcurrentHashMap<>();
        supplst1.put(milk,15);
        supplst1.put(eggs,6);
        supplst1.put(matza,20);
        supplst1.put(computer,1);
        supplst2.put(milk,1);
        supplst2.put(eggs,1);
        supplst2.put(matza,1);
        ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
        des1.put(eretz_hakulin, supplst1);
        Contact con4 = new Contact( "ereulim", "shuli", "124333");
        Site ereulim = new Site("153",con4, Site.ShippingArea.Center, 1);
        ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> des2=new ConcurrentHashMap<>();
        des2.put(ereulim,supplst1);
        des2.put(eretz_hakulin,supplst2);
        OrderDoc doc1 = new OrderDoc("100", hakanaim_16, des1, date1, "MORNING" );
        doc1.setTruckandDriver(shahar,nave);
        OrderDoc doc2 = new OrderDoc("101",yotveats6,des2,date2, "MORNING");
        doc2.setTruckandDriver(nadia,miki);
        orderDocs.addDoc(doc1);
        orderDocs.addDoc(doc2);
        createDriverDocs(doc1.getId());
        createDriverDocs(doc2.getId());
    }
    /**        ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
     ConcurrentHashMap<Supply,Integer> supplst2 = new ConcurrentHashMap<>();
     Supply milk =  new Supply("milk", 5);
     Supply eggs = new Supply("eggs", 2.13);
     Supply matza =  new Supply("matza", 20);
     Supply computer = new Supply("computer", 1000);
     Supply bmw = new Supply("bmw", 5000);
     Supply volvo = new Supply("volvo", 10000);
     pool.addSupply(milk);
     pool.addSupply(eggs);
     pool.addSupply(matza);
     pool.addSupply(bmw);
     pool.addSupply(volvo);
     supplst1.put(milk,15);
     supplst1.put(eggs,6);
     supplst1.put(matza,20);
     supplst1.put(computer,1);
     supplst2.put(milk,1);
     supplst2.put(eggs,1);
     supplst2.put(matza,1);
     Driver nave = new Driver("nave", "123", "C1");
     Driver miki = new Driver("miki", "1234", "C");
     Driver itay = new Driver("itay", "1111", "C");
     Driver dan =new Driver("Dan", "11123", "C1");
     pool.addDriver(nave);
     pool.addDriver(miki);
     pool.addDriver(itay);
     pool.addDriver(dan);
     Truck nadia =  new Truck("C", "nadia", 123456, 120000);
     Truck shahar = new Truck("C1", "shahar", 12345, 12000);
     Truck optimusprime = new Truck("C", "optimusprime", 2000, 1000);
     Truck megatron =  new Truck("C1", "megatron", 1500, 1000);
     pool.addTruck(nadia);
     pool.addTruck(shahar);
     pool.addTruck(optimusprime);
     pool.addTruck(megatron);
     Contact con1 = new Contact("hakanaim 16", "liron", "123456789");
     Site hakanaim_16 = new Site("1567", con1,Site.ShippingArea.North, 0);
     Contact con2 = new Contact("eretzhakulim", "dan", "123333");
     Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
     Contact con3 = new Contact("eretz", "dannyboy", "1233713");
     Site eretz = new Site("136", con3, Site.ShippingArea.North, 1);
     Contact con4 = new Contact( "ereulim", "shuli", "124333");
     Site ereulim = new Site("153",con4, Site.ShippingArea.Center, 1);
     Contact con5 = new Contact("yotveta 6", "david", "050000");
     Site yotveats6 = new Site("111101", con5, Site.ShippingArea.Center, 0);
     Contact con6 =new Contact("rager 52", "stas", "000055");
     Site reager = new Site("11111",con6 , Site.ShippingArea.South, 1);
     Contact con7 = new Contact("jaja", "yuri", "0022");
     Site jaja = new Site("3333", con7, Site.ShippingArea.Center, 0 );
     pool.addSite(hakanaim_16);
     pool.addSite(eretz_hakulin);
     pool.addSite(eretz);
     pool.addSite(ereulim);
     pool.addSite(yotveats6);
     pool.addSite(reager);
     pool.addSite(jaja);
     Date date1 = new Date("12","02","2022");
     Date date2 = new Date("15","06","1256");
     nave.isoccupied(date1);
     shahar.isoccupied(date1);
     miki.isoccupied(date2);
     nadia.isoccupied(date2);
     ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
     des1.put(eretz_hakulin, supplst1);
     ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> des2=new ConcurrentHashMap<>();
     des2.put(ereulim,supplst1);
     des2.put(eretz_hakulin,supplst2);
     OrderDoc doc1 = new OrderDoc("100", hakanaim_16, des1, date1, "Morning" );
     doc1.setTruckandDriver(shahar,nave);
     OrderDoc doc2 = new OrderDoc("101",yotveats6,des2,date2, "Evening");
     doc2.setTruckandDriver(nadia,miki);
     pool.addDoc(doc1);
     pool.addDoc(doc2);
     doc1.setWeight(123400);
     doc2.setWeight(12300);
     os.createDriverDocs(doc1.getId());
     os.createDriverDocs(doc2.getId());**/
//    public Supply getSupplybyDoc(String docID,String storeID,String supp){
//        for(Supply supply: pool.getOrderDoc(docID).getDestinations().get(pool.getSite(storeID)).keySet()){
//            if(supply.name.equalsIgnoreCase(supp)){
//                return supply;
//            }
//        }
//        throw new NullPointerException();
//    }
//    public static void changeQuantity(String docID, String siteID, ArrayList<String> suppName, ArrayList<Integer> quantities){
//        Site store = pool.getSite(siteID);
//        ConcurrentHashMap<String,Integer> supplies=new ConcurrentHashMap<>();
//        for(int i = 0;i< quantities.size();i++){
//            supplies.put(suppName.get(i),quantities.get(i));
//        }
//        for(String s: supplies.keySet()){
//            if(supplies.get(s)>0){
//                pool.getOrderDoc(docID).getDestinations().get(store).put(pool.getSupply(s),supplies.get(s));
//            }
//            else {
//                pool.getOrderDoc(docID).getDestinations().get(store).remove(pool.getSupply(s));
//            }
//        }
//        changeDriverDoc(docID,siteID,pool.getOrderDoc(docID).getDestinations().get(store));
//
//    }
    public  boolean setTrucksWeight(String docID,double weight){
        orderDocs.updateDocWeight(docID,String.valueOf(weight),"weight");
        return orderDocs.getOrderDoc(docID).setWeight(weight).equals("Success");
    }
//    public static Site getSite(String id) {
//        return pool.getSite(id);
//    }
    public String showTrucks(String date,String driverID,String time){
        String res = "Trucks:\n";
        int idx=1;
        String licenseType = drivers.getDriver(driverID).getLicense();
        for(Truck t:trucks.getTrucks(date,time,licenseType)) {
            if(trucks.getAvailability(t.licenseplate,date) && t.isSuitable(drivers.getDriver(driverID))) {
                res += idx + ". " + (t + "\n");
            }
            idx++;
        }
        return res;
    }
//    public static Truck getTruck(String licensePlate){
//        return pool.getTruck(licensePlate);
//    }

    public void setNewTruck(String doc, String newTruckPlate) {
        Truck newTruck = trucks.getTruck(newTruckPlate);
        OrderDoc orderDoc = orderDocs.getOrderDoc(doc);
        trucks.setAvailability(orderDoc.truck, orderDoc.getDate().toString(), orderDoc.time, true);
        trucks.setAvailability(newTruck, orderDoc.getDate().toString(), orderDoc.getTime(), false);
        orderDocs.set(doc,"licensePlate",newTruckPlate);
        orderDoc.setTruck(newTruck, orderDoc.getDate());
    }

    public String showStores(String areacode) {
        int area = Integer.parseInt(areacode);
        ArrayList<String> lst = sites.showSites(area,1);
        if(lst.isEmpty()){
            return "No stores in this area yet.";
        }
        return ShowSitesOption(lst);
    }

    public ConcurrentHashMap<Supply,Integer> createSupplyList(ConcurrentHashMap<String,Integer> supp){
        ConcurrentHashMap<Supply,Integer>supplies = new ConcurrentHashMap<>();
        for(String s:supp.keySet()){
            supplies.put(this.supplies.getSupply(s),supp.get(s));
        }
        return supplies;
    }
    public void replaceStores(String doc, String id2replace, String newStoreID,ConcurrentHashMap<String,Integer>supplies) {
        removeSiteFromDoc(doc,id2replace);
        orderDocs.getOrderDoc(doc).addDest(sites.getSite(newStoreID),createSupplyList(supplies));
        if(Objects.equals(orderDocs.replaceStore(doc,id2replace,newStoreID,supplies),"Success")){
            driverDocs.addDriverDoc(driverDocID.getAndIncrement(),orderDocs.getOrderDoc(doc).getDriver().getId(),doc,newStoreID);
        };
    }

//    public String showSupplies() {
//        supplies.showSupplies();
//        return "NOT IMPLEMENTED SHOWSUPPLIES ORDERCTRL";
//    }
    public String showSupplies(String docID,String storeID,String method) {
        ConcurrentHashMap<Supply,Integer> supplies;
        String res = "";
        int counter = 0;
        if(Objects.equals(method,"by Doc")){
            supplies = orderDocs.showSupplies(docID,storeID);
            for(Supply supply: supplies.keySet()){
                res+= counter + ".Name: " +supply.getName()+" ,Quantity: "+supplies.get(supply)+"\n";
                counter += 1;
            }
        }else{
            supplies = this.supplies.showSupplies();
            for(Supply supply: supplies.keySet()){
                res+= counter + ".Name: " +supply.getName()+"\n";
                counter += 1;
            }
        }

        return res;
    }
//    public String showSupplies(){
//        String res = "";
//        int idx=0;
//        for(Supply supply:supplies.values()){
//            res+= idx+". "+ supply+"\n";
//            idx++;
//        }
//        return res;
//    }



    public String showSuppliers(String areacode) {
        int area = Integer.parseInt(areacode);
        if (sites.showSites(area, 0).size() == 0){
            return "No suppliers available, sorry G";
        }
        return ShowSitesOption(sites.showSites(area,0));
    }

    private String ShowSitesOption(ArrayList<String> showSites) {
        int counter = 0;
        String res = "";
        for (int i=0;i<showSites.size();i++){
            res += counter + "." + showSites.get(i) + "\n";
            counter++;
        }
        return res;

    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date,String driverID,String truckPlate, String time) {
        int docID = id.getAndIncrement();
        ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>>orderList = new ConcurrentHashMap<>();
        for(String storeName:orders.keySet()){
            orderList.put(sites.getSite(storeName),createSupplyList(orders.get(storeName)));
        }
        OrderDoc doc = new OrderDoc(String.valueOf(docID),sites.getSite(supplier),orderList,createDate(date), time);
        doc.setTruckandDriver(trucks.getTruck(truckPlate),drivers.getDriver(driverID));
        drivers.setAvailability(drivers.getDriver(driverID),doc.getDate().toString(),doc.time,false);
        orderDocs.addDoc(doc);
        createDriverDocs(doc.getId());
        return doc.getId();
    }

    public void removeSiteFromDoc(String docID, String siteID) {
        orderDocs.getOrderDoc(docID).removeDest(sites.getSite(siteID));
        orderDocs.removeDest(docID,siteID,"Destinations");
        orderDocs.removeDest(docID,siteID,"order4Dest");
        removeDriverDoc(docID,siteID);
    }

    public String showDrivers(String d,String time) {
        String res="";
        int idx = 1;
        for(Driver driver: drivers.getDrivers(d,time)){
            if(driver.isFree(createDate(d))) { //i check it by using contains key so if its not contains key hes free
                res += idx + ". " + driver + "\n";
            }
            idx++;
        }
        return res;

    }


    public String getDriver(String driverID) {
        if(drivers.containsDriver(driverID)){
            return driverID;
        }
        return null;
    }


    public void removeDoc(String doc) {
        orderDocs.removeDoc(doc);
    }



    public void changeOrder(String docID, String storeID, ArrayList<String>names,ArrayList<Integer>quantities) {
        ConcurrentHashMap<Supply,Integer> newSupplyOrder = new ConcurrentHashMap<>();
        OrderDoc doc =orderDocs.getOrderDoc(docID);
        Site store = sites.getSite(storeID);
        for(int i = 0;i< names.size();i++){
            if(names.size()==quantities.size()){
                newSupplyOrder.put(supplies.getSupply(names.get(i)),quantities.get(i));
            }
        }
        doc.remove(store.getId());
        orderDocs.removeDest(docID,storeID,"Destinations");
        orderDocs.updateOrder(docID,storeID,names,quantities);
        doc.getDestinations().put(store,newSupplyOrder);
//        changeDriverDoc(docID,storeID,newSupplyOrder);   //TODO Nave: we do not keep driverDoc supplies, we can reach through order4Dest db
    }
    public boolean containsSite(String docID, String storeID) {
        return orderDocs.containsStore(docID,storeID);
    }

    public void transportIsDone(String docID) {
        OrderDoc od = orderDocs.getOrderDoc(docID);
        drivers.setAvailability(od.driver, od.getDate().toString(), od.getTime(), true);
        trucks.setAvailability(od.truck, od.getDate().toString(), od.getTime(), true);
        drivers.RemoveFromIdentityMap(od.driver.getId());
        trucks.RemoveFromIdentityMap(od.truck.getLicenseplate());
        orderDocs.getOrderDoc(docID).finishOrder();
        orderDocs.set(docID,"finished","#t");
    }
    public void createDriverDocs(String docID){
        OrderDoc doc = orderDocs.getOrderDoc(docID);
        for(Site site : doc.getDestinations().keySet()) {
            int id = driverDocID.getAndIncrement();
            driverDocs.addDriverDoc(new DriverDoc(doc.driver,id,doc.getDestinations().get(site),site,doc.getId()));
        }
    }
    public void removeDriverDoc(String orderDocID,String siteID){
        driverDocs.removeDriverDocbyODOC(orderDocID,siteID);
    }
    public void changeDriverDoc(String orderDocID,String siteID,ConcurrentHashMap<Supply,Integer>supplies){
        driverDocs.updateDriverDoc(orderDocID,siteID,supplies);
    }
    public String ShowDriverDocs(String docID) {
        ArrayList<DriverDoc> docs = driverDocs.showDriverDocs(docID);
        String res="";
        for (DriverDoc doc: docs) {
            res += doc.toString() + "\n";
        }
        if(res.length() > 0){
            return res;
        }
        return "No driver docs to show for the order document.";

    }


    public String showStoresforDoc(String docID) {
        ArrayList <String> stores = orderDocs.showStores(docID);
        String res = "";
        for(String s : stores){
            res += "ID: "+ s+"\n";
        }
        return res;
    }

    public String viewOrder(String docID) {
        if(orderDocs.getOrderDoc(docID)!=null){
            return orderDocs.getOrderDoc(docID).toString();
        }
        return "";
    }

    public String getTruck(String licensePlate) {
        if(trucks.containsTruck(licensePlate)){
            return licensePlate;
        }
        return "Truck Does not exist";
    }

    public String getDate(String docID) {
        return orderDocs.getOrderDoc(docID).getDate().toString();
    }

    public String getDriverID(String docID) {
        return orderDocs.getOrderDoc(docID).getDriver().getId();
    }
    public String getTime(String docID){
        return orderDocs.getOrderDoc(docID).getTime();
    }
    public double getCurrWeight(String doc) {
        return orderDocs.getOrderDoc(doc).getWeight();
    }

    public boolean getDoc(String docID) {
        return orderDocs.getOrderDoc(docID)!=null;
    }


    public String getSupplyByIdx(int idx, String docID,String storeID,String pred) {
        ConcurrentHashMap<Supply,Integer> supplies;
        if(Objects.equals(pred, "By Doc")){
            supplies = orderDocs.showSupplies(docID,storeID);
        }
        else{
            supplies = this.supplies.showSupplies();
        }
        int counter = 0;
        for(Supply supp: supplies.keySet()){
            if(counter==idx){
                return supp.name;
            }
            counter++;
        }
        return null;
//        int counter = 0;
//        ConcurrentHashMap<Supply,Integer>supplies = pool.getOrderDoc(docID).getDestinations().get(pool.getSite(storeID));
//        for(Supply supp: supplies.keySet()){
//            if(counter==idx){
//                return supp.name;
//            }
//            counter++;
//        }
//        return null;
    }
}

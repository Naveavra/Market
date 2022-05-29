package DomainLayer.Transport;

import DAL.DriverDAO;
import DAL.SiteDAO;
import DAL.SuppliesDAO;
import DAL.TruckDAO;
import java.util.Locale;
import java.util.Objects;

public class ResourceCtrl {

    public TruckDAO trucks = TruckDAO.getInstance();
    public SuppliesDAO supplies = SuppliesDAO.getInstance();
    public SiteDAO sites = SiteDAO.getInstance();
    public DriverDAO drivers = DriverDAO.getInstance();
    private final static ResourceCtrl INSTANCE = new ResourceCtrl();

    public static ResourceCtrl getInstance(){
        return INSTANCE;
    }

    public ResourceCtrl(){};

    public boolean addTruck(String type, String licenseplate, double maxweight, double initialweight){
        if (trucks.containsTruck(licenseplate)){
            return false;
        }
        else {
            if(maxweight<=initialweight){
                return false;
            }
            trucks.addTruck(type,licenseplate,maxweight,initialweight);
            return true;
        }
    }

    public void build(){
//        Driver dan =new Driver("Dan", "11123", "C1");
//        Driver nave = new Driver("nave", "123", "C1");
//        Driver miki = new Driver("miki", "1234", "C");
//        Driver itay = new Driver("itay", "1111", "C");
//        drivers.addDriver(dan);
//        drivers.addDriver(nave);
//        drivers.addDriver(miki);
//        drivers.addDriver(itay);


/**
 * ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
 *         ConcurrentHashMap<Supply,Integer> supplst2 = new ConcurrentHashMap<>();
 *         Supply milk =  new Supply("milk", 5);
 *         Supply eggs = new Supply("eggs", 2.13);
 *         Supply matza =  new Supply("matza", 20);
 *         Supply computer = new Supply("computer", 1000);
 *         Supply bmw = new Supply("bmw", 5000);
 *         Supply volvo = new Supply("volvo", 10000);
 *         pool.addSupply(milk);
 *         pool.addSupply(eggs);
 *         pool.addSupply(matza);
 *         pool.addSupply(bmw);
 *         pool.addSupply(volvo);
 *         supplst1.put(milk,15);
 *         supplst1.put(eggs,6);
 *         supplst1.put(matza,20);
 *         supplst1.put(computer,1);
 *         supplst2.put(milk,1);
 *         supplst2.put(eggs,1);
 *         supplst2.put(matza,1);
 *         Driver nave = new Driver("nave", "123", "C1");
 *         Driver miki = new Driver("miki", "1234", "C");
 *         Driver itay = new Driver("itay", "1111", "C");
 *         Driver dan =new Driver("Dan", "11123", "C1");
 *         pool.addDriver(nave);
 *         pool.addDriver(miki);
 *         pool.addDriver(itay);
 *         pool.addDriver(dan);
 *         Truck nadia =  new Truck("C", "nadia", 123456, 120000);
 *         Truck shahar = new Truck("C1", "shahar", 12345, 12000);
 *         Truck optimusprime = new Truck("C", "optimusprime", 2000, 1000);
 *         Truck megatron =  new Truck("C1", "megatron", 1500, 1000);
 *         pool.addTruck(nadia);
 *         pool.addTruck(shahar);
 *         pool.addTruck(optimusprime);
 *         pool.addTruck(megatron);
 *         Contact con1 = new Contact("hakanaim 16", "liron", "123456789");
 *         Site hakanaim_16 = new Site("1567", con1,Site.ShippingArea.North, 0);
 *         Contact con2 = new Contact("eretzhakulim", "dan", "123333");
 *         Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
 *         Contact con3 = new Contact("eretz", "dannyboy", "1233713");
 *         Site eretz = new Site("136", con3, Site.ShippingArea.North, 1);
 *         Contact con4 = new Contact( "ereulim", "shuli", "124333");
 *         Site ereulim = new Site("153",con4, Site.ShippingArea.Center, 1);
 *         Contact con5 = new Contact("yotveta 6", "david", "050000");
 *         Site yotveats6 = new Site("111101", con5, Site.ShippingArea.Center, 0);
 *         Contact con6 =new Contact("rager 52", "stas", "000055");
 *         Site reager = new Site("11111",con6 , Site.ShippingArea.South, 1);
 *         Contact con7 = new Contact("jaja", "yuri", "0022");
 *         Site jaja = new Site("3333", con7, Site.ShippingArea.Center, 0 );
 *         pool.addSite(hakanaim_16);
 *         pool.addSite(eretz_hakulin);
 *         pool.addSite(eretz);
 *         pool.addSite(ereulim);
 *         pool.addSite(yotveats6);
 *         pool.addSite(reager);
 *         pool.addSite(jaja);
 *         Date date1 = new Date("12","02","2022");
 *         Date date2 = new Date("15","06","1256");
 *         nave.isoccupied(date1);
 *         shahar.isoccupied(date1);
 *         miki.isoccupied(date2);
 *         nadia.isoccupied(date2);
 *         ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
 *         des1.put(eretz_hakulin, supplst1);
 *         ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> des2=new ConcurrentHashMap<>();
 *         des2.put(ereulim,supplst1);
 *         des2.put(eretz_hakulin,supplst2);
 *         OrderDoc doc1 = new OrderDoc("100", hakanaim_16, des1, date1, "Morning" );
 *         doc1.setTruckandDriver(shahar,nave);
 *         OrderDoc doc2 = new OrderDoc("101",yotveats6,des2,date2, "Evening");
 *         doc2.setTruckandDriver(nadia,miki);
 *         pool.addDoc(doc1);
 *         pool.addDoc(doc2);
 *         doc1.setWeight(123400);
 *         doc2.setWeight(12300);
 *         os.createDriverDocs(doc1.getId());
 *         os.createDriverDocs(doc2.getId());
 *         System.out.println("Build finished");**/
    }

    public boolean addDriver(String name, String id, String licence){
        if (drivers.containsDriver(id)){
            return false;
        }
        else {
            Driver d = new Driver(name, id, licence);
            drivers.addDriver(d);
            return true;
        }
    }

    public boolean addSupply(String name, double weight){
        if (supplies.contains(name.toLowerCase(Locale.ROOT))){
            return false;
        }
        else {
            return Objects.equals(supplies.addSupply(name.toLowerCase(Locale.ROOT), weight), "Success");
        }
    }

    public boolean addSite(String id,String contactaddress, String contactname, String contactphonenumber
            , int shippingArea, int type){
        if (sites.contains(id)){
            return false;
        }
        else {
            Site.ShippingArea area = null;
            if(shippingArea==0){
                area = Site.ShippingArea.North;
            }else if(shippingArea==1){
                area = Site.ShippingArea.Center;
            }else if(shippingArea ==2){
                area = Site.ShippingArea.South;
            }
            Contact c = new Contact(contactaddress, contactname, contactphonenumber);
            Site s = new Site(id, c, area, type);
            sites.addSite(id,shippingArea,type,contactaddress,contactname,contactphonenumber);
            return true;
        }
    }

    public boolean removeDriver(String id){
        if (drivers.getDriver(id) == null){
            return false;
        }
        else {
            drivers.removeDriver(id);
            return true;
        }
    }

    public boolean removeTruck(String licenseplate){
        if (trucks.getTruck(licenseplate) == null){
            return false;
        }
        else {
            if(Objects.equals(trucks.removeTruck(licenseplate),"Success")){
                return true;
            }
            return false;
        }
    }

    public boolean removeSite(String id){
        if (sites.getSite(id) == null){
            return false;
        }
        else {
            sites.removeSite(id);
            return true;
        }
    }


    public String removeSupply(String suppName2Remove) {
        if(!supplies.contains(suppName2Remove.toLowerCase(Locale.ROOT))){
            return "Supply doesn't exist";
        }else{
            supplies.removeSupply(suppName2Remove);
        }
        return "Supply removed successfully";
    }
}

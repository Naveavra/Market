package DomainLayer.Transport;

import DAL.DriverDAO;
import DAL.ProductDAO;
import DAL.StoreDAO;
//import DAL.SuppliesDAO;
import DAL.TruckDAO;
import java.util.Locale;
import java.util.Objects;

public class ResourceController {

    public TruckDAO trucks = new TruckDAO();
    public ProductDAO supplies = new ProductDAO();
    public StoreDAO sites = new StoreDAO();
    public DriverDAO drivers = new DriverDAO();
    private final static ResourceController INSTANCE = new ResourceController();

    public static ResourceController getInstance(){
        return INSTANCE;
    }

    public ResourceController(){};

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

/*    public boolean addSupply(String name, double weight){
        if (supplies.contains(name.toLowerCase(Locale.ROOT))){
            return false;
        }
        else {
            return Objects.equals(supplies.addSupply(name.toLowerCase(Locale.ROOT), weight), "Success");
        }
    }*/

    public boolean addSite(String id,String contactaddress, String contactname, String contactphonenumber
            , int shippingArea, int type){
        if (sites.contains(id)){
            return false;
        }
        else {
            Store.ShippingArea area = null;
            if(shippingArea==0){
                area = Store.ShippingArea.North;
            }else if(shippingArea==1){
                area = Store.ShippingArea.Center;
            }else if(shippingArea ==2){
                area = Store.ShippingArea.South;
            }
            Contact c = new Contact(contactaddress, contactname, contactphonenumber);
            Store s = new Store(id, c, area, type);
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


//    public String removeSupply(String suppName2Remove) {
//        if(!supplies.contains(suppName2Remove.toLowerCase(Locale.ROOT))){
//            return "Supply doesn't exist";
//        }else{
//            supplies.removeSupply(suppName2Remove);
//        }
//        return "Supply removed successfully";
//    }
}

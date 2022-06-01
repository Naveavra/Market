package ServiceLayer.transport;

//import BusinessLayer.*;

import DomainLayer.FacadeEmployees;
import DomainLayer.Transport.ResourceController;

public class UserService {
    FacadeEmployees facadeEmployees;
    ResourceController resourceController = ResourceController.getInstance();
    public UserService(){
        facadeEmployees =FacadeEmployees.getInstance();
    }

    public boolean createDriver(String name, String id, String licence){
        return facadeEmployees.createDriver(name, id, licence);
    }

    public boolean createTruck(String type, String licenseplate, double maxweight, double initialweight ){
        return facadeEmployees.addTruck(type, licenseplate, maxweight, initialweight);
    }

    public boolean createSupply(String name, double weight){
        return facadeEmployees.addSupply(name, weight);
    }

    public boolean createSite(String id,String contactaddress, String contactname, String contactphonenumber
            , int shippingArea, int type){
        return facadeEmployees.addSite(id, contactaddress, contactname, contactphonenumber, shippingArea, type);
    }

    public boolean removeDriver(String id){
        return facadeEmployees.removeDriver(id);
    }

    public boolean removeTruck(String id){ //the condition should be in the userctrl and not here, cohesion!!!!!!!
        return facadeEmployees.removeTruck(id);
    }

    public boolean removeSite(String id){
        return facadeEmployees.removeSite(id);
    }


    public String removeSupply(String suppName2Remove) {
        return facadeEmployees.removeSupply(suppName2Remove);
    }


    //#TODO transport doc!!!!
}

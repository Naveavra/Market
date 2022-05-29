package ServiceLayer.transport;

//import BusinessLayer.*;

import DomainLayer.Transport.ResourceController;

public class UserService {
    ResourceController resourceController = ResourceController.getInstance();
    public UserService(){}

    public boolean createDriver(String name, String id, String licence){
        if(licence.equalsIgnoreCase("C")){
            licence = "C";
        }
        else{licence ="C1";}
        return resourceController.addDriver(name, id, licence) ;
    }

    public boolean createTruck(String type, String licenseplate, double maxweight, double initialweight ){
         return resourceController.addTruck(type, licenseplate, maxweight, initialweight);
    }

    public boolean createSupply(String name, double weight){
        return resourceController.addSupply(name, weight);
    }

    public boolean createSite(String id,String contactaddress, String contactname, String contactphonenumber
            , int shippingArea, int type){
        return resourceController.addSite(id, contactaddress, contactname, contactphonenumber, shippingArea, type);
    }

    public boolean removeDriver(String id){
       return resourceController.removeDriver(id);
    }

    public boolean removeTruck(String id){ //the condition should be in the userctrl and not here, cohesion!!!!!!!
        return resourceController.removeTruck(id);
    }

    public boolean removeSite(String id){
        return resourceController.removeSite(id);
    }


    public String removeSupply(String suppName2Remove) {
        return resourceController.removeSupply(suppName2Remove);
    }


    //#TODO transport doc!!!!
}

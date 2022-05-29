package ServiceLayer.transport;

//import BusinessLayer.*;

import DomainLayer.Transport.ResourceCtrl;

public class UserService {
    ResourceCtrl resourceCtrl = ResourceCtrl.getInstance();
    public UserService(){}

    public boolean createDriver(String name, String id, String licence){
        if(licence.equalsIgnoreCase("C")){
            licence = "C";
        }
        else{licence ="C1";}
        return resourceCtrl.addDriver(name, id, licence) ;
    }

    public boolean createTruck(String type, String licenseplate, double maxweight, double initialweight ){
         return resourceCtrl.addTruck(type, licenseplate, maxweight, initialweight);
    }

    public boolean createSupply(String name, double weight){
        return resourceCtrl.addSupply(name, weight);
    }

    public boolean createSite(String id,String contactaddress, String contactname, String contactphonenumber
            , int shippingArea, int type){
        return resourceCtrl.addSite(id, contactaddress, contactname, contactphonenumber, shippingArea, type);
    }

    public boolean removeDriver(String id){
       return resourceCtrl.removeDriver(id);
    }

    public boolean removeTruck(String id){ //the condition should be in the userctrl and not here, cohesion!!!!!!!
        return resourceCtrl.removeTruck(id);
    }

    public boolean removeSite(String id){
        return resourceCtrl.removeSite(id);
    }


    public String removeSupply(String suppName2Remove) {
        return resourceCtrl.removeSupply(suppName2Remove);
    }


    //#TODO transport doc!!!!
}

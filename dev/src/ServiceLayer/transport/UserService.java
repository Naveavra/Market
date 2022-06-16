package ServiceLayer.transport;

//import BusinessLayer.*;

import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.Transport.ResourceController;

public class UserService {
    FacadeEmployees_Transports facadeEmployeesTransports;
    ResourceController resourceController = ResourceController.getInstance();
    public UserService(){
        facadeEmployeesTransports = new FacadeEmployees_Transports();
    }

    public boolean createDriver(String name, String id, String licence){
        return facadeEmployeesTransports.createDriver(name, id, licence);
    }

    public boolean createTruck(String type, String licenseplate, double maxweight, double initialweight ){
        return facadeEmployeesTransports.addTruck(type, licenseplate, maxweight, initialweight);
    }

//    public boolean createSupply(String name, double weight){
//        return facadeEmployeesTransports.addSupply(name, weight);
//    }

    public boolean createSite(String id,String contactaddress, String contactname, String contactphonenumber
            , int shippingArea, int type){
        return facadeEmployeesTransports.addSite(id, contactaddress, contactname, contactphonenumber, shippingArea, type);
    }

    public boolean removeDriver(String id){
        return facadeEmployeesTransports.removeDriver(id);
    }

    public boolean removeTruck(String id){ //the condition should be in the userctrl and not here, cohesion!!!!!!!
        return facadeEmployeesTransports.removeTruck(id);
    }

    public boolean removeSite(String id){
        return facadeEmployeesTransports.removeSite(id);
    }


//    public String removeSupply(String suppName2Remove) {
//        return facadeEmployeesTransports.removeSupply(suppName2Remove);
//    }


    //#TODO transport doc!!!!
}

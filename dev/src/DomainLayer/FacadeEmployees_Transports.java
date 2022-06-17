package DomainLayer;


//import SharedSpace.DBConnector;
import DomainLayer.Employees.*;
import DomainLayer.Suppliers.ProductSupplier;
import DomainLayer.Transport.OrderController;
import DomainLayer.Transport.ResourceController;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftPair;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FacadeEmployees_Transports {
    private final EmployeeController employeeController;
    private final ShiftController shiftController;
    private final JobController jobController;
    private final OrderController orderController;
    ResourceController resourceController;

    public FacadeEmployees_Transports(){
        employeeController = new EmployeeController();
        shiftController = new ShiftController();
        jobController = new JobController();
        createEmployee("318856994", "Itay Gershon", "123456", 1000000000, "Hapoalim 12 115", "The conditions for this employee are really terrific");
        certifyEmployee(JobType.HR_MANAGER, "318856994");
        certifyEmployee(JobType.STORE_MANAGER, "318856994");
        certifyEmployee(JobType.STOCK_KEEPER, "318856994");
        orderController= new OrderController();
        resourceController = ResourceController.getInstance();
    }



    public Response createEmployee(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment){
        return employeeController.createEmployee(id, name, password, salary, bankAccount, contractOfEmployment);
    }

    public Response removeEmployee(String id) {
        return employeeController.removeEmployee(id);
    }

    public Response certifyEmployee(JobType job, String id){
        return employeeController.certifyEmployee(job, id);
    }

    public boolean certifyDriver(String id, String license) {
        Response r = certifyEmployee(JobType.DRIVER, id);
        if (r.errorOccurred())
            return false;
        Employee e = employeeController.getEmployee(id);
        return jobController.addLicense(e.getName() ,id, license);
    }


    public Response login(String id, String password) {
        return employeeController.login(id, password);
    }

    public boolean exists(String id) {
        return employeeController.isEmployed(id);
    }

    public Employee getEmployee(String id) {
        return employeeController.getEmployee(id);
    }


    public boolean isHRManager(String id) {
        return employeeController.isHRManager(id);
    }

    public Response isCertified(String id, JobType jobType){
        return employeeController.isCertified(id, jobType);
    }

    public String getScheduleOf(String id) {
        return employeeController.getScheduleOf(id);
    }

    public boolean resetSchedule(String id) {
        return employeeController.resetSchedule(id);
    }

    public boolean addAvailableTimeSlotToEmployee(String id, ShiftPair shift) {return employeeController.addAvailableTimeSlotTo(id, shift); }

    //return value can be ignored because the employee invoking it is logged in => employee != null
    public boolean removeShift(String id, ShiftPair shift) {
        return employeeController.removeAvailableTimeSlot(id, shift);
    }

    public Response createShift(ShiftPair shift, String shiftManagerID, String cashiersIDs, String driversIDs, String merchandisersIDs, String stockKeepersIDs) {
        List<Employee> cashiers = new ArrayList<>();
        addAll(cashiers, cashiersIDs);
        List<Employee> drivers = new ArrayList<>();
        addAll(drivers, driversIDs);
        List<Employee> merchandisers = new ArrayList<>();
        addAll(merchandisers, merchandisersIDs);
        List<Employee> stockKeepers = new ArrayList<>();
        addAll(stockKeepers, stockKeepersIDs);
        Employee shiftManager = getEmployee(shiftManagerID);

        List<Employee> allEmployees = new ArrayList<>();
        allEmployees.addAll(cashiers);
        allEmployees.addAll(drivers);
        allEmployees.addAll(merchandisers);
        allEmployees.addAll(stockKeepers);
        allEmployees.add(shiftManager);
        for (Employee e : allEmployees){
            if (e == null)
                return new Response("One of the ids was not in the system. Please make sure to enter valid ids");
        }


        if(!shiftController.createShift(shift, shiftManager, cashiers, drivers, merchandisers, stockKeepers)){
            return new Response("The shift you are trying to create has already been scheduled." +
                    " Make sure you have the right date and time");
        }
        return new Response();
    }

    private void addAll(List<Employee> employees, String ids){
        String[] idsArr = ids.split(",");
        for (String s: idsArr) {
            employees.add(getEmployee(s));
        }
    }


    public List<String[]> getAllCertified(JobType jobType, ShiftPair shift, boolean displayInShift) {
        Set<Employee> certified = jobController.getCertifiedEmployees(jobType);
        List<String[]> namesAndIDs = new ArrayList<>();
        String isAvailable;
        for (Employee e : certified){
            if(e.inShift() && !displayInShift) // if the employee is currently working, and the flag is false meaning we DON'T want to display those in shift
                continue;
            if(e.isAvailable(shift.getDate(), shift.getTime())) {
                isAvailable = "Available";
            }
            else {
                isAvailable = "Not Available";

            }
            String[] nameAndID = {"\nName: " + e.getName(), ", ID: " + e.getId(), ", Availability: " + isAvailable
                    , ", Monthly hours: "+e.getAccMonthlyHours()};
            namesAndIDs.add(nameAndID);
        }
        return namesAndIDs;

    }

    public boolean startShift(ShiftPair shiftPair, String id){
        return shiftController.startShift(shiftPair, id);
    }

    public String getDetailsOf(String id) {
        return employeeController.getDetailsOf(id);
    }

    public boolean editName(String id, String newName) {
        return employeeController.editName(id, newName);
    }

    public boolean editPassword(String id, String newPassword) {
        return employeeController.editPassword(id, newPassword);
    }

    public boolean editSalary(String id, float newSalary) {
        return employeeController.editSalary(id, newSalary);
    }

    public boolean editBankInfo(String id, String newBankInfo) {
        return employeeController.editBankInfo(id, newBankInfo);
    }

    public boolean editContract(String id, String newContract) {
        return employeeController.editContract(id, newContract);
    }

    public boolean isCurrentShiftManager(String empID) {
        return empID.equals(shiftController.getCurrentShiftManger().getId());
    }

    public ShiftPair getCurrentShiftTime() {
        return shiftController.getCurrentShift().getShiftTime();
    }

    public boolean addEmployeeToCurrentShift(String id, JobType jobType) {
        Employee eToAdd = getEmployee(id);
        return shiftController.addEmployeeToShift(eToAdd, jobType, shiftController.getCurrentShift());

    }

    public Response endShift(String id) {
        employeeController.endShift();
        return shiftController.endShift(id);
    }

    /**
     * handles the shutting down of the system. more functionality will be added later on
     */
    public void shutDown() {
        shiftController.shutDown();
        employeeController.shutDown();
    }

    public boolean shiftIsRunning() {
        return shiftController.shiftIsRunning();
    }

    public boolean removeFromShift(String idToRemove) {
        return shiftController.removeFromCurrentShift(getEmployee(idToRemove));
    }

    public String displayWorkersOfCurrentShift() {
        return shiftController.displayWorkersOfCurrentShift();
    }

    public boolean setTrucksWeight(String docID, double weight2add) {
        return orderController.setTrucksWeight(docID, weight2add);
    }

    public String showTrucks(String date, String driverID, String time) {
        return orderController.showTrucks(date,driverID,time);
    }

    public void setNewTruck(String docID, String newTruckPlate) {
        orderController.setNewTruck(docID, newTruckPlate);
    }

    public String showStores(String areaCode) {
        return orderController.showStores(areaCode);
    }

    public void replaceStores(String docID, String id2replace, String newStoreID, ConcurrentHashMap<String, Integer> supplies) {
        orderController.replaceStores(docID, id2replace, newStoreID, supplies);
    }

    public String showSupplies(String s, String s1, String s2) {
        return orderController.showSupplies("","","");
    }

    public String showSuppliers(String areaCode) {
        return orderController.showSuppliers(areaCode);
    }

    public String createDoc(ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> orders, String supplier, String date, String driverID, String truckPlate, String time) {
        return orderController.createDoc(orders, supplier, date,driverID,truckPlate, time);
    }

    public void removeSiteFromDoc(String docID, String siteID) {
        orderController.removeSiteFromDoc(docID, siteID);
    }

    public String showDrivers(String d, String time) {
        return orderController.showDrivers(d,time);
    }

    public String getDriver(String driverID) {
        return orderController.getDriver(driverID);
    }

    public void removeDoc(String doc) {
        orderController.removeDoc(doc);
    }

    public boolean changeOrder(String docID, String storeID, ArrayList<String> names, ArrayList<Integer> quantities) {
        if (orderController.containsSite(docID, storeID)) {
            orderController.changeOrder(docID,storeID, names,quantities);
            return true;
        } else {
            throw new NullPointerException();
        }
    }

    public void createDriverDocs(String doc) {
        orderController.createDriverDocs(doc);
    }

    public void transportIsDone(String doc) {
        orderController.transportIsDone(doc);
    }

    public String ShowDriverDocs(String docID) {
        return orderController.ShowDriverDocs(docID);
    }

    public String showStoresforDoc(String docID) {
        return orderController.showStoresforDoc(docID);
    }

    public String viewOrder(String docID) {
        return orderController.viewOrder(docID);
    }

    public String getSupplyByIdx(int nextInt, String s, String s1, String s2) {
        return orderController.getSupplyByIdx(nextInt,"","","");
    }

    public String getTruck(String licensePlate) {
        return orderController.getTruck(licensePlate);
    }

    public String getDate(String docID) {
        return orderController.getDate(docID);
    }

    public String getDriverID(String docID) {
        return orderController.getDriverID(docID);
    }

    public double getCurrWeight(String doc) {
        return orderController.getCurrWeight(doc);
    }

    public boolean getDoc(String docID) {
        return orderController.getDoc(docID);
    }

    public String getTime(String docID) {
        return orderController.getTime(docID);
    }

    public boolean createDriver(String name, String id, String licence) {
        if(licence.equalsIgnoreCase("C")){
            licence = "C";
        }
        else{licence ="C1";}
        return resourceController.addDriver(name, id, licence) ;
    }

    public boolean addTruck(String type, String licensePlate, double maxWeight, double initialWeight) {
        return resourceController.addTruck(type, licensePlate, maxWeight, initialWeight);
    }

//    public boolean addSupply(String name, double weight) {
//        return resourceController.addSupply(name, weight);
//    }

    public boolean addSite(String id, String contactaddress, String contactname, String contactphonenumber, int shippingArea, int type) {
        return resourceController.addSite(id, contactaddress, contactname, contactphonenumber, shippingArea, type);
    }

    public boolean removeDriver(String id) {
        return resourceController.removeDriver(id);
    }

    public boolean removeTruck(String id) {
        return resourceController.removeTruck(id);
    }

    public boolean removeSite(String id) {
        return resourceController.removeSite(id);
    }
//
//    public String removeSupply(String suppName2Remove) {
//        return resourceController.removeSupply(suppName2Remove);
//    }

    public Set<JobType> getEmployeeRoles(String id) {
        return employeeController.getEmployeeRoles(id);
    }

    public String displayMessages(String id) {
        return employeeController.displayMessages(id);
    }

    public String viewShift(ShiftPair shiftPair) {
        return shiftController.viewShift(shiftPair);
    }

    public Response deleteShift(ShiftPair shiftPair) {
        return shiftController.deleteShift(shiftPair);
    }

    public boolean createAutoTransport(String supplierNumber, String date, Map<ProductSupplier, Integer> supplyList) {
        ConcurrentHashMap<String,Integer> supplies = new ConcurrentHashMap<>();
        for(ProductSupplier ps : supplyList.keySet()){
            supplies.put(String.valueOf(ps.getProductId()),supplyList.get(ps));
        }
        return orderController.createAutoTransport(supplierNumber,date,supplies);
    }


    public HashMap<Integer, Integer> getProductsFromOrderDoc(int orderDocId){
        return orderController.getOrderIdFromOrderDoc(orderDocId);
    }

    public String getAllOrderDocIDs() {
        return orderController.getAllOrderDocIDs();
    }

}

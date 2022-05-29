package DomainLayer.Employees;


//import SharedSpace.DBConnector;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Facade {
    private static final Facade instance = new Facade();
    private final EmployeeController employeeController;
    private final ShiftController shiftController;
    private final JobController jobController;

    private Facade(){
        employeeController = new EmployeeController();
        shiftController = new ShiftController();
        jobController = new JobController();
        createEmployee("318856994", "Itay Gershon", "123456", 1000000000, "Hapoalim 12 115", "The conditions for this employee are really terrific");
        certifyEmployee(JobType.HR_MANAGER, "318856994");
    }

    public static Facade getInstance() {
        return instance;
    }


    public Response createEmployee(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment){
        return employeeController.createEmployee(id, name, password, salary, bankAccount, contractOfEmployment);
    }

    public Response removeEmployee(String id) {
        return employeeController.removeEmployee(id);
    }

    public boolean certifyEmployee(JobType job, String id){
        Response empResponse =  employeeController.certifyEmployee(job, id);
        return !empResponse.errorOccurred();
//        jobController.certifyEmployee(job, id);
    }

    public boolean certifyDriver(String id, String license) {
        if (!certifyEmployee(JobType.DRIVER, id))
            return false;
        return jobController.addLicense(id, license);
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

    public boolean endShift(String id) {
        employeeController.putBackAll();
        return shiftController.endShift(id);
    }

    /**
     * handles the shutting down of the system. more functionality will be added later on
     */
    public void shutDown() {
        shiftController.shutDown();
        employeeController.shutDown();
//        try {
//            employeeController.shutDown();
//            jobController.shutDown();
//            DBConnector.getInstance().close();
//        }
//        catch (SQLException e){
//            System.out.println("Unsuccessful shutdown");
//        }
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


//    public void closeDbConnection() {
//        DBConnector.getInstance().close();
//    }
//
//    public void initializeDBConnection() {
//        DBConnector.getInstance().startConnection();
//    }
}

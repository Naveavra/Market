package DomainLayer;


//import SharedSpace.DBConnector;
import DomainLayer.Employees.*;
import DomainLayer.Transport.OrderController;
import DomainLayer.Transport.ResourceController;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FacadeEmployees {
    private static final FacadeEmployees instance = new FacadeEmployees();
    private final EmployeeController employeeController;
    private final ShiftController shiftController;
    private final JobController jobController;

        private FacadeEmployees(){
            employeeController = new EmployeeController();
            shiftController = new ShiftController();
            jobController = new JobController();
            createEmployee("318856994", "Itay Gershon", "123456", 1000000000, "Hapoalim 12 115", "The conditions for this employee are really terrific");
            certifyEmployee(JobType.HR_MANAGER, "318856994");
        }

        public static FacadeEmployees getInstance() {
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

    public boolean addAvailableTimeSlotToEmployee(String id, ShiftPair shift) {
        return employeeController.addAvailableTimeSlotTo(id, shift);
    }

    //return value can be ignored because the employee invoking it is logged in => employee != null
    public boolean removeShift(String id, ShiftPair shift) {
        return employeeController.removeAvailableTimeSlot(id, shift);
    }


    public Response areCertified(String ids, JobType jobType){
        String[] employees = ids.split(",");
        for (String id: employees){
            Response response = isCertified(id, jobType);
            if (response.errorOccurred())
                return response;
        }
        return new Response();
    }

    public Response createShift(ShiftPair shift, String shiftManagerID, String cashiersIDs, String driversIDs, String merchandisersIDs, String stockKeepersIDs) {

        Response rManager = areCertified(shiftManagerID, JobType.SHIFT_MANAGER);
        if (rManager.errorOccurred())
            return rManager;
        Response rCashier = areCertified(cashiersIDs, JobType.CASHIER);
        if (rCashier.errorOccurred())
            return rCashier;
        Response rDriver = areCertified(driversIDs, JobType.DRIVER);
        if (rDriver.errorOccurred())
            return rDriver;
        Response rMerchandiser = areCertified(merchandisersIDs, JobType.MERCHANDISER);
        if (rMerchandiser.errorOccurred())
            return rMerchandiser;
        Response rStockKeepers = areCertified(stockKeepersIDs, JobType.STOCK_KEEPER);
        if (rStockKeepers.errorOccurred())
            return rStockKeepers;


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
        boolean started = shiftController.startShift(shiftPair, id);
        if (started){
            for (List<Employee> emps : shiftController.getCurrentShift().getEmployeesInShift().values()){
                for (Employee emp : emps){
                    employeeController.addToMap(emp);
                }
            }
        }
        return started;
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

//    /**
//     * handles the shutting down of the system. more functionality will be added later on
//     */
//    public void shutDown() {
////        try {
//        //employeeController.shutDown();
////        jobController.shutDown();
//        shiftController.shutDown();
//        employeeController.shutDown();
////            DBConnector.getInstance().close();
////        }
////        catch (SQLException e){
////            System.out.println("Unsuccessful shutdown");
////        }
//    }

    public boolean shiftIsRunning() {
        return shiftController.shiftIsRunning();
    }

    public boolean removeFromShift(String idToRemove) {
        return shiftController.removeFromCurrentShift(getEmployee(idToRemove));
    }

    public String displayWorkersOfCurrentShift() {
        return shiftController.displayWorkersOfCurrentShift();
    }

//    public void loadPreMadeData() {
//        createEmployee("234567891", "gal halifa", "123456", 1000, "yahav", "good conditions");
//        createEmployee("123456789", "dan terem", "123456", 1000, "yahav", "good conditions");
//        createEmployee("345678912", "noa aviv", "123456", 1000, "yahav", "good conditions");
//        createEmployee("456789123", "nave avraham", "123456", 1000, "yahav", "good conditions");
//        createEmployee("789123456", "gili gershon", "123456", 1000, "yahav", "good conditions");
//        createEmployee("891234567", "amit halifa", "123456", 1000, "yahav", "good conditions");
//        createEmployee("012345678", "shachar bardugo", "123456", 1000, "yahav", "good conditions");
//        createEmployee("123456780", "nadia zlenko", "123456", 1000, "yahav", "good conditions");
//        createEmployee("234567801", "yossi gershon", "123456", 1000, "yahav", "good conditions");
//        createEmployee("345678012", "eti gershon", "123456", 1000, "yahav", "good conditions");
//        createEmployee("456780123", "amit sasson", "123456", 1000, "yahav", "good conditions");
//        createEmployee("567801234", "itamar shemesh", "123456", 1000, "yahav", "good conditions");
//        createEmployee("147258369", "dina agapov", "123456", 1, "yahav", "bad");
//        createEmployee("258369147", "mor shuker", "123456", 1, "yahav", "bad");
//        createEmployee("000000000", "may terem", "123456", 1, "yahav", "good");
//        createEmployee("111111111", "wendy the dog", "123456", 1, "yahav", "good");
//        createEmployee("222222222", "savta tova", "123456", 1, "yahav", "good");
//        createEmployee("333333333", "liron marinberg", "123456", 1, "yahav", "good");
//        certifyEmployee(JobType.CASHIER, "318856994");
//        certifyEmployee(JobType.CASHIER, "234567891");
//        certifyEmployee(JobType.CASHIER, "345678912");
//        certifyEmployee(JobType.CASHIER, "123456789");
//        certifyDriver("258369147", "c");
//        certifyDriver("123456789", "c1");
//        certifyDriver("456780123", "c");
//        certifyDriver("234567891", "c");
//        certifyDriver("012345678", "c1");
//        certifyEmployee(JobType.MERCHANDISER, "234567891");
//        certifyEmployee(JobType.MERCHANDISER, "123456789");
//        certifyEmployee(JobType.MERCHANDISER, "789123456");
//        certifyEmployee(JobType.MERCHANDISER, "234567891");
//        certifyEmployee(JobType.MERCHANDISER, "123456789");
//        certifyEmployee(JobType.STOCK_KEEPER, "123456780");
//        certifyEmployee(JobType.STOCK_KEEPER, "345678912");
//        certifyEmployee(JobType.STOCK_KEEPER, "456780123");
//        certifyEmployee(JobType.STOCK_KEEPER, "123456789");
//        certifyEmployee(JobType.SHIFT_MANAGER, "234567891");
//        certifyEmployee(JobType.SHIFT_MANAGER, "318856994");
//        certifyEmployee(JobType.SHIFT_MANAGER, "222222222");
//        certifyEmployee(JobType.STOCK_KEEPER, "111111111");
//        certifyDriver( "000000000", "c1");
//        certifyEmployee(JobType.SHIFT_MANAGER, "456789123");
//        certifyEmployee(JobType.CASHIER, "333333333");
//        certifyEmployee(JobType.MERCHANDISER, "567801234");
//        certifyDriver("123456780", "c");
//
//        Response r = createShift(new ShiftPair(new ShiftDate("01", "06", "2022"), Time.EVENING), "318856994",
//                "333333333,234567891", "123456780,000000000", "123456789", "111111111");
//        if (r.errorOccurred())
//            System.out.println("r1 = " + r.getErrorMessage());
//        r = createShift(new ShiftPair(new ShiftDate("02", "06", "2022"), Time.MORNING), "456789123",
//                "333333333,234567891", "258369147,000000000", "567801234", "345678912");
//        if (r.errorOccurred())
//            System.out.println("r2 = " + r.getErrorMessage());
//
//    }

}

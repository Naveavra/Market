package DomainLayer.Employees;

import java.util.*;

//import EmployeesModule.BusinessLayer.Utility.*;
import DAL.EmployeeDAO;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftPair;

public class EmployeeController {
    private final EmployeeDAO employeeDAO;

    public EmployeeController(){
        employeeDAO = new EmployeeDAO();
    }

    public Employee getEmployee(String id){
        return employeeDAO.getEmployee(id);
    }

    public boolean isEmployed(String id){
        return getEmployee(id) != null;
    }

    public Response createEmployee(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment){//} throws DoubleRegistrationException {
        return employeeDAO.registerEmployee(id, name, password, salary, bankAccount, contractOfEmployment, 0.001f , new Date());
    }

    public Response removeEmployee(String id){

        return employeeDAO.removeEmployee(id);
    }

    public Response login(String id, String password) {
        Employee e = getEmployee(id);
        if (e == null)
            return new Response("Employee does not exist");
        if (!e.login(password))
            return new Response("Incorrect password");
        return new Response();
    }

    public boolean isHRManager(String id) {
        return getEmployee(id).isHRManager();
    }

    public String getScheduleOf(String id) {
        Schedule s = employeeDAO.getScheduleOf(id);
        if (s == null)
            return "Unspecified";
        return s.toString();
    }

    public boolean addAvailableTimeSlotTo(String id, ShiftPair shift) {
        return employeeDAO.addAvailableTimeSlotTo(id, shift);
    }

    public boolean removeAvailableTimeSlot(String id, ShiftPair shift) {
        return employeeDAO.removeAvailableTimeSlot(id, shift);
    }

    public Response certifyEmployee(JobType job, String id) {
        return employeeDAO.certifyEmployee(id, job);
    }

    public Response isCertified(String id, JobType jobType) {
        return employeeDAO.isCertified(id, jobType);
    }

    public String getDetailsOf(String id) {
        Employee e = getEmployee(id);
        if (e == null){
            return "Employee does not exist";
        }
        return e.toString();
    }

    public boolean editName(String id, String newName) {
        return employeeDAO.editName(id, newName);
    }

    public boolean editPassword(String id, String newPassword) {
        return employeeDAO.editPassword(id, newPassword);
    }

    public boolean editSalary(String id, float newSalary) {
        return employeeDAO.editSalary(id, newSalary);
    }

    public boolean editBankInfo(String id, String newBankInfo) {
        return employeeDAO.editBankInfo(id, newBankInfo);
    }

    public boolean editContract(String id, String newContract) {
        return employeeDAO.editContract(id, newContract);
    }

    public boolean resetSchedule(String id) {
        employeeDAO.resetSchedule(id);
        return true;
    }

//    public boolean addLicenseTo(String id, String license) {
//        return employeeDAO.addLicense(id, license);
//    }

    public void endShift() {
        employeeDAO.clearMap();
    }

    public void shutDown() {
        employeeDAO.shutDown();
    }

    public void addToMap(Employee emp) {
        employeeDAO.addToMap(emp);
    }

    public Set<JobType> getEmployeeRoles(String id) {
        return employeeDAO.getEmployeeRoles(id);
    }

    public String displayMessages(String id) {
        return employeeDAO.displayMessages(id);
    }
}


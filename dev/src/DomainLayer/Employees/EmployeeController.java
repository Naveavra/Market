package DomainLayer.Employees;

import java.util.*;

//import EmployeesModule.BusinessLayer.Utility.*;
import DAL.EmployeeDAO;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftPair;

public class EmployeeController {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public EmployeeController(){
    }

    public Employee getEmployee(String id){
        return employeeDAO.getEmployee(id);
    }

    public boolean isEmployed(String id){
        return getEmployee(id) != null;
    }

    public Response createEmployee(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment){//} throws DoubleRegistrationException {
        return employeeDAO.registerEmployee(id, name, password, salary, bankAccount, contractOfEmployment, 0 , new Date());
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
//        return employeeDAO.getScheduleOf(id);
        return getEmployee(id).getAvailabilitySchedule().toString();
    }

    public boolean addAvailableTimeSlotTo(String id, ShiftPair shift) {
        Employee e = getEmployee(id);
        if (e == null)
            return false;
        e.addAvailableTimeSlot(shift);
        employeeDAO.putBackEmployee(e);
        return true;
    }

    public boolean removeAvailableTimeSlot(String id, ShiftPair shift) {
        Employee e = getEmployee(id);
        if (e == null)
            return false;
        employeeDAO.removeShift(id, shift);
        e.deleteShift(id, shift);
        return true;
    }

    public Response certifyEmployee(JobType job, String id) {
        Employee e = getEmployee(id);
        if (e == null)
            return new Response("Employee does not exist");
        e.addCertification(job);
//        employeeDAO.putBackEmployee(e);
        return new Response();
    }

    public Response isCertified(String id, JobType jobType) {
        Employee e = getEmployee(id);
        if (e == null)
            return new Response("Employee with id " + id + " does not exist");
        if (!e.isCertified(jobType))
            return new Response("Employee with id " + id + " is not certified to be a " + jobType);
        return new Response();
    }

    public String getDetailsOf(String id) {
        Employee e = getEmployee(id);
        if (e == null){
            return "Employee does not exist";
        }
        return e.toString();
    }

    public boolean editName(String id, String newName) {
        Employee e = getEmployee(id);
        if(e == null) {
            return false;
        }
        e.setName(newName);
        return true;
    }

    public boolean editPassword(String id, String newPassword) {
        Employee e = getEmployee(id);
        if(e == null){
            return false;
        }
        e.setPassword(newPassword);
        return true;
    }

    public boolean editSalary(String id, float newSalary) {
        Employee e = getEmployee(id);
        if(e == null){
            return false;
        }
        e.setSalary(newSalary);
        return true;
    }

    public boolean editBankInfo(String id, String newBankInfo) {
        Employee e = getEmployee(id);
        if(e == null){
            return false;
        }
        e.setBankAccount(newBankInfo);
        return true;
    }

    public boolean editContract(String id, String newContract) {
        Employee e = getEmployee(id);
        if(e == null){
            return false;
        }
        e.setContractOfEmployment(newContract);
        return true;
    }

    public boolean resetSchedule(String id) {
        employeeDAO.resetSchedule(id);
        return true;
    }

//    public boolean addLicenseTo(String id, String license) {
//        return employeeDAO.addLicense(id, license);
//    }

    public void putBackAll() {
        employeeDAO.putBackAll();
    }

    public void shutDown() {
        employeeDAO.shutDown();
    }

}

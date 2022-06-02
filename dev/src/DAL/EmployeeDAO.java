package DAL;

import DomainLayer.Employees.*;
import DomainLayer.Employees.Employee;
import DomainLayer.Employees.Time;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
//import SharedSpace.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EmployeeDAO {
    private static Map<String, Employee> idMap = new HashMap<>();;
    private Connect conn = Connect.getInstance();
    private static final EmployeeDAO instance = new EmployeeDAO();
//    private EmployeeDAO(){
//        idMap = new HashMap<>();
//    }
    public static EmployeeDAO getInstance() {
        return instance;
    }

    public Employee getEmployee(String id){
        if (idMap.containsKey(id))
            return idMap.get(id);
        try {
            ResultSet personalDetails = conn.executeQuery("SELECT * FROM Employees WHERE id = ?", id);
            if (!personalDetails.next())
                return null;
            return recordToEmployee(personalDetails);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        finally {
            try {
                conn.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    private Employee recordToEmployee(ResultSet res) {
        try {
            String id = res.getString("id");
            String name = res.getString("name");
            String password = res.getString("password");
            String contract = res.getString("contract");
            String bankAccount = res.getString("bankAccount");
            float monthlyHrs = res.getFloat("monthlyHours");
            float salary = res.getFloat("salary");
            Date DoE = res.getDate("dateOfEmployment");
            Employee e = new Employee(id, name, password, salary, bankAccount, contract, DoE);
            e.setMonthlyHours(monthlyHrs);
            ResultSet rolesSet = conn.executeQuery("SELECT jobType FROM Roles WHERE id = ?", id);
            ResultSet schedule = conn.executeQuery("SELECT * FROM Schedules WHERE id = ?", id);
            reconstructEmployeeAvailability(e, schedule);
            reconstructEmployeeRoles(e, rolesSet);
            idMap.put(id, e);
            return e;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void reconstructEmployeeAvailability(Employee e, ResultSet schedule) throws SQLException {
        while (schedule.next()){
            String timeOfDay = schedule.getString("timeOfDay");
            Time t;
            if (timeOfDay.equals("MORNING"))
                t = Time.MORNING;
            else
                t = Time.EVENING;
            String day = schedule.getString("day");
            String month = schedule.getString("month");
            String year = schedule.getString("year");
            e.addAvailableTimeSlot(new ShiftPair(new ShiftDate(day, month, year), t));
        }
    }

    private JobType getJobType(String job){
        switch (job) {
            case "HR_MANAGER":
                return JobType.HR_MANAGER;
            case "CASHIER":
                return JobType.CASHIER;
            case "DRIVER":
                return JobType.DRIVER;
            case "MERCHANDISER":
                return JobType.MERCHANDISER;
            case "STOCK_KEEPER":
                return JobType.STOCK_KEEPER;
            case "SHIFT_MANAGER":
                return JobType.SHIFT_MANAGER;
            case "TRANSPORT_MANAGER":
                return JobType.TRANSPORT_MANAGER;
            case "LOGISTICS_MANAGER":
                return JobType.LOGISTICS_MANAGER;
            default: return null;
        }
    }

    private void reconstructEmployeeRoles(Employee e, ResultSet rolesSet) throws SQLException {
        while(rolesSet.next()){
            String role = rolesSet.getString("jobType");
            if (role == null)
                return;
            e.addCertification(getJobType(role));
        }
    }

    public Response putBackEmployee(Employee e){
        try{
            conn.executeUpdate("UPDATE Employees SET name = ?, password = ?, salary = ?, contract = ?, bankAccount " +
                            "= ?, monthlyHours = ?, dateOfEmployment = ? WHERE id = ?",e.getName(), e.getPassword(), e.getSalary(),
                    e.getContractOfEmployment(), e.getBankAccount(), e.getAccMonthlyHours(), e.getDateOfEmployment(),e.getId());
            List<ShiftPair> schedule = e.getAvailabilitySchedule().getSchedule();
            for (ShiftPair shiftPair : schedule) {
                //ShiftDate shiftDate = shiftPair.getDate();
                ShiftDate date = shiftPair.getDate();
                String time = shiftPair.getTime().toString();
                conn.executeUpdate("INSERT OR IGNORE INTO Schedules(id,timeOfDay,day,month,year) VALUES(?,?,?,?,?)", e.getId(), time, date.getDay(),date.getMonth(),date.getYear());
            }
            Set<JobType> roles = e.getRoles();
            for (JobType jt : roles){
                conn.executeUpdate("INSERT OR IGNORE INTO Roles(id, jobType) VALUES(?,?)", e.getId(), jt.toString());
            }
            idMap.remove(e.getId());
            return new Response();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            return new Response("An unknown error occurred");
        }
    }

//    public boolean addLicense(String id, String license){
//        try {
//            conn.executeQuery("INSERT OR IGNORE INTO DriversLicenses(id, license) VALUES(?,?)", id, license);
//            return true;
//        }
//        catch (SQLException e){
//            return false;
//        }
//    }

    public boolean isEmployed(String id){
        if (idMap.containsKey(id))
            return true;
        try {
            return !conn.executeQuery("SELECT id FROM Employees WHERE id = " + id).next();
        }
        catch (SQLException e) {
            return false;
        }
        finally {
            try {
                conn.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    public Response registerEmployee(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment , float monthlyHours, Date date) {
        try{
//            idMap.put(id, new Employee(id,name, password, salary, bankAccount, contractOfEmployment, date));
            conn.executeUpdate("INSERT INTO Employees (id, name, password, salary, contract, bankAccount, monthlyHours," +
                    " dateOfEmployment) VALUES(?,?,?,?,?,?,?,?)",id, name, password, salary, contractOfEmployment, bankAccount, monthlyHours ,date);
            return new Response();
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            return new Response("Employee with id " + id + " is already registered in the system");
        }
    }

    public Response removeEmployee(String id) {
        idMap.remove(id);
        try {
            conn.deleteRecordFromTableSTR("Employees", "id", id);
            return new Response();
        }
        catch (SQLException e){
            return new Response("Unable to delete employee. Make sure the id is correct");
        }
    }

    public void clearMap() {
        idMap.clear();
    }

    public Map<String, Employee> getIdMap() {
        return idMap;
    }

    public void shutDown() {
        clearMap();
    }

    public void resetSchedule(String id) {
        if (idMap.containsKey(id))
            idMap.get(id).resetSchedule();
        try {
            conn.deleteRecordFromTableSTR("Schedules", "id", id);
        }
        catch (SQLException ignored){}
    }

    public void removeShift(String id, ShiftPair shift){
//        Employee e = getEmployee(id);
        if(idMap.containsKey(id)){
            idMap.get(id).deleteShift(id,shift);
        }
        ShiftDate date = shift.getDate();
        try {
            conn.executeUpdate("DELETE FROM Schedules WHERE id = ? AND timeOfDay = ? AND " +
                            "day = ? AND month = ? AND year = ?", id, shift.getTime().toString(), date.getDay()
                    , date.getMonth(), date.getYear());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean addAvailableTimeSlotTo(String id, ShiftPair shift) {
        if (idMap.containsKey(id))
            idMap.get(id).addAvailableTimeSlot(shift);
        try {
            ShiftDate date = shift.getDate();
            conn.executeUpdate("INSERT INTO Schedules (id, timeOfDay, day, month, year)",
                    id, shift.getTime().toString(), date.getDay(), date.getMonth(), date.getYear());
            return true;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Schedule getScheduleOf(String id) {
        if (idMap.containsKey(id))
            return idMap.get(id).getAvailabilitySchedule();
        try {
            ResultSet res = conn.executeQuery("SELECT * FROM Schedules WHERE id = ?", id);
            Schedule schedule = new Schedule(new ArrayList<>());
            while (res.next()){
                String sTimeOfDay = res.getString("timeOfDay");
                Time timeOfDay;
                if (sTimeOfDay.equalsIgnoreCase("morning"))
                    timeOfDay = Time.MORNING;
                else
                    timeOfDay = Time.EVENING;
                String day = res.getString("day");
                String month = res.getString("month");
                String year = res.getString("year");
                schedule.addTimeSlot(new ShiftPair(new ShiftDate(day, month, year), timeOfDay));
            }
            return schedule;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
        finally {
            try {
                conn.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    public boolean removeAvailableTimeSlot(String id, ShiftPair shift) {
        if (idMap.containsKey(id))
            idMap.get(id).deleteShift(id, shift);
        try {
            ShiftDate date = shift.getDate();
            conn.executeUpdate("DELETE FROM Schedules WHERE timeOfDay = ? AND day = ? AND month = ? AND year = ?",
                    shift.getTime().toString(), date.getDay(), date.getMonth(), date.getYear());
            return true;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public Response certifyEmployee(String id, JobType job) {
        if (idMap.containsKey(id))
            idMap.get(id).addCertification(job);
        try {
            conn.executeUpdate("INSERT INTO Roles (id, jobType) VALUES(?,?)",
                    id, job);
            return new Response();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new Response("No such employee exists");
        }

    }

    public Response isCertified(String id, JobType jobType) {
        if (idMap.containsKey(id) && idMap.get(id).isCertified(jobType))
            return new Response();
        try {
            ResultSet res = conn.executeQuery("SELECT * FROM Roles WHERE id = ? AND jobType = ?", id, jobType);
            if (res.next())
                return new Response();
            return new Response("Employee is not certified to be a " + jobType.toString().toLowerCase());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return new Response("No such employee exists");
        }
        finally {
            try {
                conn.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    public boolean editName(String id, String name) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setName(name);
        }
        Response r = conn.updateRecordInTable("Employees", "name", id, name);
        return !r.errorOccurred();
    }

    public boolean editPassword(String id, String password) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setPassword(password);
        }
        Response r = conn.updateRecordInTable("Employees", "password", id, password);
        return !r.errorOccurred();
    }

    public boolean editSalary(String id, float newSalary) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setSalary(newSalary);
        }
        Response r = conn.updateRecordInTable("Employees", "salary", id, newSalary);
        return !r.errorOccurred();
    }

    public boolean editContract(String id, String newContract) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setContractOfEmployment(newContract);
        }
        Response r = conn.updateRecordInTable("Employees", "contract", id, newContract);
        return !r.errorOccurred();
    }

    public boolean editBankInfo(String id, String newBankInfo) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setBankAccount(newBankInfo);
        }
        Response r = conn.updateRecordInTable("Employees", "bankAccount", id, newBankInfo);
        return !r.errorOccurred();
    }

    public void addToMap(Employee emp) {
        idMap.put(emp.getId(), emp);
    }
}

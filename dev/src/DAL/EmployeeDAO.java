package DAL;

import DomainLayer.Employees.Employee;
import DomainLayer.Employees.Time;
import DomainLayer.Employees.Employee;
import DomainLayer.Employees.JobType;
import DomainLayer.Employees.Shift;
import DomainLayer.Employees.Time;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
//import SharedSpace.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EmployeeDAO {
    private final Map<String, Employee> idMap;
    private Connect conn = Connect.getInstance();
    private static final EmployeeDAO instance = new EmployeeDAO();

    private EmployeeDAO(){
        idMap = new HashMap<>();
    }

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
            return !conn.executeQuery("SELECT id FROM Employees WHERE id = " + id).wasNull();
        }
        catch (SQLException e) {
            return false;
        }
    }

    public Response registerEmployee(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment , float monthlyHours, Date date) {
        try{
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
            String ans = conn.deleteRecordFromTableSTR("Employees", "id", id);
            if (ans.equals("Success"))
                return new Response();
            return new Response("Failed to connect to DB");
        } catch (SQLException e) {
            return new Response("Failed to connect to DB");
        }
    }

    public void putBackAll() {
        for (Employee e : idMap.values())
            putBackEmployee(e);
    }

    public Map<String, Employee> getIdMap() {
        return idMap;
    }

    public void shutDown() {
        putBackAll();
    }

    public boolean resetSchedule(String id) {
        if (idMap.containsKey(id))
            idMap.get(id).resetSchedule();
        try {
            String ans = conn.deleteRecordFromTableSTR("Schedules", "id",id);
            if (ans.equals("Success"))
                return true;
            return false;
        } catch (SQLException e) {
            return false;
        }
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
}

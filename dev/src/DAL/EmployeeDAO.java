package DAL;

import DomainLayer.Employees.*;
import DomainLayer.Employees.Employee;
import DomainLayer.Employees.Time;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
//import SharedSpace.DBConnector;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class EmployeeDAO {
    private static Map<String, Employee> idMap = new HashMap<>();;
    private Connect conn = Connect.getInstance();
//    private static final EmployeeDAO instance = new EmployeeDAO();

//    public static EmployeeDAO getInstance() {
//        return instance;
//    }
    public EmployeeDAO(){}

    public Employee getEmployee(String id){
        if (idMap.containsKey(id))
            return idMap.get(id);
        try {
            // was ResultSet
            List<HashMap<String,Object>> personalDetails = conn.executeQuery("SELECT * FROM Employees WHERE id = ?", id);
/*            if (!personalDetails.next())
                return null;*/
            if (personalDetails.isEmpty())
                return null;
            return recordToEmployee(personalDetails);
        }
        catch (SQLException e){
           // System.out.println(e.getMessage());
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


    private Employee recordToEmployee(List<HashMap<String,Object>> res) {
        try {
            String id = (String) res.get(0).get("id");
            String name = (String) res.get(0).get("name");
            String password = (String) res.get(0).get("password");
            String contract = (String) res.get(0).get("contract");
            String bankAccount = (String) res.get(0).get("bankAccount");
            double monthlyHrs = (Double) res.get(0).get("monthlyHours");
            float salary = (Integer) res.get(0).get("salary");
            long doe = (long) res.get(0).get("dateOfEmployment");
            Date DoE = new Date(doe);
            Employee e = new Employee(id, name, password, salary, bankAccount, contract, DoE);
            e.setMonthlyHours(monthlyHrs);
            List<HashMap<String,Object>> rolesSet = conn.executeQuery("SELECT jobType FROM Roles WHERE id = ?", id);
            List<HashMap<String,Object>> schedule = conn.executeQuery("SELECT * FROM Schedules WHERE id = ?", id);
            List<HashMap<String,Object>> messages = conn.executeQuery("SELECT message FROM Messages WHERE read = ?", "false");
            reconstructEmployeeAvailability(e, schedule);
            reconstructEmployeeRoles(e, rolesSet);
            reconstructEmployeeMessages(e, messages);
            idMap.put(id, e);
            return e;
        }
        catch (SQLException e){
            //System.out.println(e.getMessage());
            return null;
        }
    }

    private void reconstructEmployeeMessages(Employee e, List<HashMap<String, Object>> messages) {
        for (int i = 0; i < messages.size(); i++){
            String message = (String) messages.get(i).get("message");
            e.addMessage(message);
        }
    }

    private void reconstructEmployeeAvailability(Employee e, List<HashMap<String,Object>> schedule) throws SQLException {
        for (int i = 0; i < schedule.size(); i++){
            String timeOfDay = (String) schedule.get(i).get("timeOfDay");
            Time t;
            if (timeOfDay.equals("MORNING"))
                t = Time.MORNING;
            else
                t = Time.EVENING;
            String day = (String) schedule.get(i).get("day");
            String month = (String) schedule.get(i).get("month");
            String year = (String) schedule.get(i).get("year");
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
            case "STORE_MANAGER":
                return JobType.STORE_MANAGER;
            default: return null;
        }
    }

    private void reconstructEmployeeRoles(Employee e, List<HashMap<String,Object>> rolesSet) throws SQLException {
        for (int i = 0; i < rolesSet.size(); i++){
            String role = (String) rolesSet.get(i).get("jobType");
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
          //  System.out.println(se.getMessage());
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
            return conn.executeQuery("SELECT id FROM Employees WHERE id = " + id).size() != 0;
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
        if (hasUpcomingShifts(id)){
            return new Response("Employee has upcoming shifts and cannot be deleted");
        }
        idMap.remove(id);
        try {
            conn.deleteRecordFromTableSTR("Employees", "id", id);
            return new Response();
        }
        catch (SQLException e){
            return new Response("Unable to delete employee. Make sure the id is correct");
        }
    }

    private boolean hasUpcomingShifts(String id) {
        return new ShiftDAO().hasUpcomingShifts(id);
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
           // System.out.println(e.getMessage());
        }
    }

    public boolean addAvailableTimeSlotTo(String id, ShiftPair shift) {
        if (idMap.containsKey(id))
            idMap.get(id).addAvailableTimeSlot(shift);
        try {
            ShiftDate date = shift.getDate();
            conn.executeUpdate("INSERT INTO Schedules (id, timeOfDay, day, month, year) VALUES (?,?,?,?,?)",
                    id, shift.getTime().toString(), date.getDay(), date.getMonth(), date.getYear());
            return true;
        }
        catch (SQLException e){
            //System.out.println(e.getMessage());
            return false;
        }
    }

    public Schedule getScheduleOf(String id) {
        if (idMap.containsKey(id))
            return idMap.get(id).getAvailabilitySchedule();
        try {
            List<HashMap<String, Object>> res = conn.executeQuery("SELECT * FROM Schedules WHERE id = ?", id);
            Schedule schedule = new Schedule(new ArrayList<>());
           for (int i = 0; i < res.size(); i++){
                String sTimeOfDay = (String) res.get(i).get("timeOfDay");
                Time timeOfDay;
                if (sTimeOfDay.equalsIgnoreCase("morning"))
                    timeOfDay = Time.MORNING;
                else
                    timeOfDay = Time.EVENING;
                String day = (String) res.get(i).get("day");
                String month = (String) res.get(i).get("month");
                String year = (String) res.get(i).get("year");
                schedule.addTimeSlot(new ShiftPair(new ShiftDate(day, month, year), timeOfDay));
            }
            return schedule;
        }
        catch (SQLException e){
            //System.out.println(e.getMessage());
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
           // System.out.println(e.getMessage());
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
           // System.out.println(e.getMessage());
            return new Response("No such employee exists");
        }

    }

    public Response isCertified(String id, JobType jobType) {
        if (idMap.containsKey(id) && idMap.get(id).isCertified(jobType))
            return new Response();
        try {
            List<HashMap<String, Object>> res = conn.executeQuery("SELECT * FROM Roles WHERE id = ? AND jobType = ?", id, jobType);
            if (res.size() > 0)
                return new Response();
            return new Response("Employee is not certified to be a " + jobType.toString().toLowerCase());
        }
        catch (SQLException e){
            //System.out.println(e.getMessage());
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

    public boolean writeMessageToHR(String message){
        try{
            String query = "INSERT INTO Messages (message, read) VALUES(?,?)";
            conn.executeUpdate(query, message, "false");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean editName(String id, String name) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setName(name);
        }
        try {
            Response r = conn.updateRecordInTable("Employees", "name","id", id, name);
            return !r.errorOccurred();
        }
        catch (SQLException e){
            return false;
        }
    }

    public boolean editPassword(String id, String password) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setPassword(password);
        }
        try {
            Response r = conn.updateRecordInTable("Employees", "password","id", id, password);
            return !r.errorOccurred();
        }

        catch (SQLException e){
            return false;
        }
    }

    public boolean editSalary(String id, float newSalary) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setSalary(newSalary);
        }
        try {
            Response r = conn.updateRecordInTable("Employees", "salary","id", id, newSalary);
            return !r.errorOccurred();
        }

        catch (SQLException e){
            return false;
        }
    }

    public boolean editContract(String id, String newContract) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setContractOfEmployment(newContract);
        }
        try {
            Response r = conn.updateRecordInTable("Employees", "contract", "id",id, newContract);
            return !r.errorOccurred();
        }

        catch (SQLException e){
            return false;
        }
    }

    public boolean editBankInfo(String id, String newBankInfo) {
        if (idMap.containsKey(id)) {
            idMap.get(id).setBankAccount(newBankInfo);
        }
        try {
            Response r = conn.updateRecordInTable("Employees", "bankAccount","id", id, newBankInfo);
            return !r.errorOccurred();
        }

        catch (SQLException e){
            return false;
        }
    }

    public void addToMap(Employee emp) {
        idMap.put(emp.getId(), emp);
    }

    public Set<JobType> getEmployeeRoles(String id) {
        if (idMap.containsKey(id)) {
            return idMap.get(id).getRoles();
        }
        return getEmployee(id).getRoles();
    }

    public String displayMessages(String id) {
//        if (idMap.containsKey(id)) {
//            markAllAsRead(id);
//            return idMap.get(id).displayMessages();
//        }
        try {
            List<HashMap<String, Object>> messagesDB = conn.executeQuery("SELECT message FROM Messages WHERE read = ?", "false");
            List<String> messages = new ArrayList<>();
            for (int i = 0; i < messagesDB.size(); i++) {
                String message = (String) messagesDB.get(i).get("message");
                messages.add(message);
            }
            markAllAsRead(id);
            return messages.toString();
        } catch (SQLException e) {
            return "Couldn't fetch employee messages";
        }
    }

    private void markAllAsRead(String id) {
        if (idMap.containsKey(id)) {
            idMap.get(id).clearMessages();
        }
        try {
            String query = "UPDATE Messages" +
                    String.format(" SET read=\"%s\" WHERE read=\"%s\"", "true", "false");
           conn.executeUpdate(query);
        }
        catch (SQLException ignored){}
    }
}

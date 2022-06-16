package DAL;
import DomainLayer.Employees.Shift;
import DomainLayer.Employees.JobType;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import DomainLayer.Employees.Employee;
import ServiceLayer.Utility.ShiftPair;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ShiftDAO {
    private static Map<ShiftPair, Shift> shiftID = new HashMap<>();;
    private Connect connect = Connect.getInstance();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private DriverDAO driverDAO = new DriverDAO();


    public Shift getShift(ShiftPair shiftPair){
        if(shiftID.containsKey(shiftPair)){
            return shiftID.get(shiftPair);
        }
        try {
            ShiftDate date = shiftPair.getDate();

            List<HashMap<String, Object>> res = connect.executeQuery("SELECT * FROM Shifts WHERE timeOfDay = ?" +
                    "AND day = ? AND month = ? AND year = ?", shiftPair.getTime(), date.getDay(), date.getMonth(), date.getYear());
            if (res.size() == 0)
                return null;
            return shiftConvertor(shiftPair);
        }
        catch (SQLException e){
            return null;
        }
        finally {
            try {
                connect.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    public List<Employee> getEmployeesInShift(JobType jobType, ShiftPair shiftPair){
        ShiftDate shiftID = shiftPair.getDate();
        try {
            List<Employee> employees = new ArrayList<>();
            List<HashMap<String, Object>> employeesIDs = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? AND timeOfDay = ? AND day = ? AND month = ? AND year = ?", jobType, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            for (int i = 0; i < employeesIDs.size(); i++) {
                String id = (String) employeesIDs.get(i).get("employeeID");
                Employee e = employeeDAO.getEmployee(id);
                employees.add(e);
            }
            return employees;

        }
        catch(SQLException e){
            //System.out.println(e.getMessage());
            return null;
        }
        finally {
            try {
                connect.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    private Map<JobType, List<Employee>> getEmployeesInShift(ShiftPair shiftPair){
        Map<JobType, List<Employee>> res = new HashMap<>();
        res.put(JobType.CASHIER, getEmployeesInShift(JobType.CASHIER, shiftPair));
        res.put(JobType.DRIVER, getEmployeesInShift(JobType.DRIVER, shiftPair));
        res.put(JobType.MERCHANDISER, getEmployeesInShift(JobType.MERCHANDISER, shiftPair));
        res.put(JobType.STOCK_KEEPER, getEmployeesInShift(JobType.STOCK_KEEPER, shiftPair));
        return res;
    }

    public Shift shiftConvertor(ShiftPair shiftPair){
        try{
            ShiftDate date = shiftPair.getDate();
            List<HashMap<String, Object>> shiftDate = connect.executeQuery("SELECT * FROM Shifts WHERE timeOfDay = ? and day = ? and month = ? and year = ?",
                    shiftPair.getTime(), date.getDay(), date.getMonth(), date.getYear());
            if(shiftDate.size() == 0){
                return null;
            }
            Map<JobType, List<Employee>> employees = getEmployeesInShift(shiftPair);
            if (employees == null)
                return null;
            Employee manager = getShiftManager(shiftPair);
            if(manager == null)
                return null;
            Shift s = new Shift(manager, employees, shiftPair);
            shiftID.put(shiftPair, s);
            return s;
        }
        catch (SQLException e){
//            System.out.println("problem in shiftConverter");
            return null;
        }
        finally {
            try {
                connect.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    private Employee getShiftManager(ShiftPair shiftPair) {
        try {
            ShiftDate shiftID = shiftPair.getDate();
            List<HashMap<String, Object>> managerID = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? and " +
                    "timeOfDay = ? and day = ? and month = ? and year = ?", JobType.SHIFT_MANAGER, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            return employeeDAO.getEmployee((String) managerID.get(0).get("employeeID"));
        }
        catch (SQLException e){
            return null;
        }
    }

    public void insertShift(Shift shift) {
        List<Employee> cashiers = shift.getAll(JobType.CASHIER);
        List<Employee> drivers = shift.getAll(JobType.DRIVER);
        List<Employee> merchandisers = shift.getAll(JobType.MERCHANDISER);
        List<Employee> stockKeepers = shift.getAll(JobType.STOCK_KEEPER);
        Employee manager = shift.getManager();
        try {
            ShiftPair shiftPair = shift.getShiftTime();
            ShiftDate shiftDate = shiftPair.getDate();
            updateDriversAvailability(shift, drivers);
            connect.executeUpdate("INSERT OR IGNORE INTO Shifts(timeOfDay,day,month,year,start" +
                    ",end) VALUES(?,?,?,?,?,?)", shiftPair.getTime(), shiftDate.getDay(), shiftDate.getMonth(), shiftDate.getYear(), shift.getStart(), shift.getEnd());
            for (Employee e : cashiers)
            connect.executeUpdate("INSERT OR IGNORE INTO EmployeesInShift(timeOfDay,day,month,year,employeeID" +
                    ",job) VALUES(?,?,?,?,?,?)", shiftPair.getTime(), shiftDate.getDay(), shiftDate.getMonth(), shiftDate.getYear(), e.getId(), JobType.CASHIER);
            for (Employee e : drivers)
                connect.executeUpdate("INSERT OR IGNORE INTO EmployeesInShift(timeOfDay,day,month,year,employeeID" +
                        ",job) VALUES(?,?,?,?,?,?)", shiftPair.getTime(), shiftDate.getDay(), shiftDate.getMonth(), shiftDate.getYear(), e.getId(), JobType.DRIVER);
            for (Employee e : merchandisers)
                connect.executeUpdate("INSERT OR IGNORE INTO EmployeesInShift(timeOfDay,day,month,year,employeeID" +
                        ",job) VALUES(?,?,?,?,?,?)", shiftPair.getTime(), shiftDate.getDay(), shiftDate.getMonth(), shiftDate.getYear(), e.getId(), JobType.MERCHANDISER);
            for (Employee e : stockKeepers)
                connect.executeUpdate("INSERT OR IGNORE INTO EmployeesInShift(timeOfDay,day,month,year,employeeID" +
                        ",job) VALUES(?,?,?,?,?,?)", shiftPair.getTime(), shiftDate.getDay(), shiftDate.getMonth(), shiftDate.getYear(), e.getId(), JobType.STOCK_KEEPER);
            connect.executeUpdate("INSERT OR IGNORE INTO EmployeesInShift(timeOfDay,day,month,year,employeeID" +
                    ",job) VALUES(?,?,?,?,?,?)", shiftPair.getTime(), shiftDate.getDay(), shiftDate.getMonth(), shiftDate.getYear(), manager.getId(), JobType.SHIFT_MANAGER);
        }
        catch (SQLException ignore){
        }
    }

    public boolean shiftExists(ShiftPair shiftPair) {
        if (shiftID.containsKey(shiftPair))
            return true;
        try {
            ShiftDate date = shiftPair.getDate();
            return connect.executeQuery("SELECT * FROM Shifts WHERE timeOfDay = ? AND day = ? AND month = ?" +
                    " AND year = ?", shiftPair.getTime(), date.getDay(),date.getMonth(), date.getYear()).size() > 0;
        }
        catch (SQLException e) {
            return false;
        }
    }

    public void updateDriversAvailability(Shift shift, List<Employee> drivers){
        try{
            List<HashMap<String, Object>> rs;
            for(Employee e : drivers) {
                connect.executeUpdate("INSERT OR IGNORE INTO DriverAvailability(id, date, time, available) " +
                        "VALUES (?,?,?,?)", e.getId(), shift.getShiftTime().getDate().toString(), shift.getShiftTime().getTime().toString().toUpperCase(), "#t");
//                rs = connect.executeQuery("SELECT * FROM DriversLicenses WHERE id = ?",e.getId());
//                if(rs.size() > 0) {
//                    String id = (String) rs.get(0).get("id");
//                    String license = (String) rs.get(0).get("license");
////                    driverDAO.addDriver(e.getName(), id, license);
//                }
            }
        } catch (SQLException ignore) {
        }
        finally {
            try {
                connect.closeConnect();
            }
            catch (SQLException ignored){
            }
        }
    }

    public void endShift(Shift currentShift) {
        try {
            ShiftPair shiftPair = currentShift.getShiftTime();
            ShiftDate date = shiftPair.getDate();
            connect.executeUpdate("UPDATE Shifts SET start = ? AND end = ?" +
                            " WHERE timeOfDay = ? AND day = ? AND month = ?" + " AND year = ?",
                    currentShift.getStart(), currentShift.getEnd() ,shiftPair.getTime(), date.getDay(),date.getMonth(), date.getYear());
        }
        catch (SQLException e) {
           // System.out.println("problem in shiftExists");
            //System.out.println(e.getMessage());
        }
    }

    public void shutDown() {
        for (Shift shift : shiftID.values())
            insertShift(shift);
    }

    public Response deleteShift(ShiftPair shiftPair) {
        try{
            ShiftDate date = shiftPair.getDate();
            String query = "DELETE FROM Shifts WHERE timeOfDay = ? AND day = ? AND month = ? AND year = ?";
            connect.executeUpdate(query, shiftPair.getTime(), date.getDay(), date.getMonth(), date.getYear());
            return new Response();
        } catch (SQLException e) {
            return new Response("Cannot find the specified shift");
        }
    }

    public boolean hasUpcomingShifts(String id) {
        try{
            String query = "SELECT * FROM EmployeesInShift WHERE employeeID = ?";
            List<HashMap<String, Object>> shifts = connect.executeQuery(query, id);
            Calendar calendar = Calendar.getInstance();
            String sTodayDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String sTodayMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
            String sTodayYear = String.valueOf(calendar.get(Calendar.YEAR));
            for (int i = 0; i < shifts.size(); i++){
                String sDay = (String) shifts.get(i).get("day");
                String sMonth = (String) shifts.get(i).get("month");
                String sYear = (String) shifts.get(i).get("year");
                int year = Integer.parseInt(sYear);
                int tYear = Integer.parseInt(sTodayYear);
                if (year > tYear){
                    return true;
                }
                int month = Integer.parseInt(sMonth);
                int tMonth = Integer.parseInt(sTodayMonth);
                if (month > tMonth && year == tYear){
                    return true;
                }
                int day = Integer.parseInt(sDay);
                int tDay = Integer.parseInt(sTodayDay);
                if (day > tDay && month == tMonth && year == tYear ){
                    return true;
                }
            }
            return false;
        } catch (SQLException | NumberFormatException e) {
            return false;
        }
    }
}

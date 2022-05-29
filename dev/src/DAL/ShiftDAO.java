package DAL;
import DomainLayer.Employees.Shift;
import DomainLayer.Employees.JobType;
import ServiceLayer.Utility.ShiftDate;
import DomainLayer.Employees.Employee;
import ServiceLayer.Utility.ShiftPair;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ShiftDAO {
    private final Map<ShiftPair, Shift> shiftID;
    private Connect connect = Connect.getInstance();
    private static final ShiftDAO instance = new ShiftDAO();

    private ShiftDAO(){ shiftID = new HashMap<>();}

    public static ShiftDAO getInstance(){return instance;}

    public Shift getShift(ShiftPair shiftPair){
        if(shiftID.containsKey(shiftPair)){
            return shiftID.get(shiftPair);
        }
        try {
            ResultSet res = connect.executeQuery("SELECT * FROM Shifts WHERE shiftID = ?", shiftID);
            if (!res.next())
                return null;
            return shiftConvertor(shiftPair);
        }
        catch (SQLException e){
            System.out.println("problem in getShift");
            return null;
        }
    }

    private Map<JobType, List<Employee>> getEmployeesInShift(ShiftPair shiftPair){
        try {
            ShiftDate shiftID = shiftPair.getDate();
            ResultSet cashiersIDs = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? and timeOfDay = ? and day = ? and month = ? and year = ?", JobType.CASHIER, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            ResultSet driversIDs = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? and timeOfDay = ? and day = ? and month = ? and year = ?", JobType.DRIVER, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            ResultSet merchandisersIDs = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? and timeOfDay = ? and day = ? and month = ? and year = ?", JobType.MERCHANDISER, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            ResultSet stockKeepersIDs = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? and timeOfDay = ? and day = ? and month = ? and year = ?", JobType.STOCK_KEEPER, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            List<Employee> cashiers = new ArrayList<>();
            List<Employee> drivers = new ArrayList<>();
            List<Employee> merchandisers = new ArrayList<>();
            List<Employee> stockKeepers = new ArrayList<>();
            while (cashiersIDs.next())
                cashiers.add(EmployeeDAO.getInstance().getEmployee(cashiersIDs.getString("employeeID")));
            while (driversIDs.next())
                drivers.add(EmployeeDAO.getInstance().getEmployee(driversIDs.getString("employeeID")));
            while (merchandisersIDs.next())
                merchandisers.add(EmployeeDAO.getInstance().getEmployee(merchandisersIDs.getString("employeeID")));
            while (stockKeepersIDs.next())
                stockKeepers.add(EmployeeDAO.getInstance().getEmployee(stockKeepersIDs.getString("employeeID")));
            Map<JobType, List<Employee>> res = new HashMap<>();
            res.put(JobType.CASHIER, cashiers);
            res.put(JobType.DRIVER, drivers);
            res.put(JobType.MERCHANDISER, merchandisers);
            res.put(JobType.STOCK_KEEPER, stockKeepers);
            return res;
        }
        catch (SQLException e){
            System.out.println("problem in getEmployeesInShift");
            return null;
        }
    }

    public Shift shiftConvertor(ShiftPair shiftPair){
        try{
            ShiftDate shiftID = shiftPair.getDate();
            ResultSet shiftDate = connect.executeQuery("SELECT * FROM Shifts WHERE timeOfDay = ? and day = ? and month = ? and year = ?",
                    shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            if(!shiftDate.next()){
                return null;
            }
            Map<JobType, List<Employee>> employees = getEmployeesInShift(shiftPair);
            if (employees == null)
                return null;
            Employee manager = getShiftManager(shiftPair);
            if(manager == null)
                return null;
            Shift s = new Shift(manager, employees, shiftPair);
            this.shiftID.put(shiftPair, s);
            return s;
        }
        catch (SQLException e){
            System.out.println("problem in shiftConverter");
            return null;
        }
    }

    private Employee getShiftManager(ShiftPair shiftPair) {
        try {
            ShiftDate shiftID = shiftPair.getDate();
            ResultSet managerID = connect.executeQuery("SELECT * FROM EmployeesInShift WHERE job = ? and " +
                    "timeOfDay = ? and day = ? and month = ? and year = ?", JobType.SHIFT_MANAGER, shiftPair.getTime(), shiftID.getDay(), shiftID.getMonth(), shiftID.getYear());
            return EmployeeDAO.getInstance().getEmployee(managerID.getString("id"));
        }
        catch (SQLException e){
            System.out.println("problem in getShiftManager");
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
            System.out.println("problem in insertShift");
        }
    }

    public boolean shiftExists(ShiftPair shiftPair) {
        if (shiftID.containsKey(shiftPair))
            return true;
        try {
            ShiftDate date = shiftPair.getDate();
            return connect.executeQuery("SELECT * FROM Shifts WHERE timeOfDay = ? AND day = ? AND month = ?" +
                    " AND year = ?", shiftPair.getTime(), date.getDay(),date.getMonth(), date.getYear()).next();
        }
        catch (SQLException e) {
            System.out.println("problem in shiftExists");
            return false;
        }
    }

    public void updateDriversAvailability(Shift shift, List<Employee> drivers){
        try{
            ResultSet rs;
            for(Employee e : drivers) {
                connect.executeUpdate("INSERT OR IGNORE INTO DriverAvailability(id, date, time, available) " +
                        "VALUES (?,?,?,?)", e.getId(), shift.getShiftTime().getDate().toString(), shift.getShiftTime().getTime().toString().toUpperCase(), "#t");
                rs = connect.executeQuery("SELECT * FROM DriversLicenses WHERE id = ?",e.getId());
                if(rs.next()) {
                    String id = rs.getString("id");
                    String license = rs.getString("license");
                    DriverDAO.getInstance().addDriver(e.getName(), id, license);
                }
            }
        } catch (SQLException ignore) {
            System.out.println(ignore.getMessage());
            System.out.println("problem in updateDriversAvail");
        }

    }

    public void shutDown() {
        for (Shift shift : shiftID.values())
            insertShift(shift);
    }
}

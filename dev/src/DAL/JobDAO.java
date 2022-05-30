package DAL;

import DomainLayer.Employees.Employee;
import DomainLayer.Employees.JobType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class JobDAO {
    private Connect conn = Connect.getInstance();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
//    private static final JobDAO instance = new JobDAO();
//    private JobDAO(){}
//    public static JobDAO getInstance() {
//        return instance;
//    }

    public boolean addLicense(String id, String license){
        try {
            conn.executeUpdate("INSERT OR IGNORE INTO DriversLicenses(id, license) VALUES(?,?)", id, license.toUpperCase(Locale.ROOT));
            return true;
        } catch (SQLException ignore) {
            return false;
        }
    }

    public Set<Employee> getCertifiedEmployees(JobType role) {
        Set<Employee> employees = new HashSet<>();
        for(Employee e : employeeDAO.getIdMap().values()){
            if(e.isCertified(role))
                employees.add(e);
        }
        try {
            ResultSet res = conn.executeQuery("SELECT * FROM Roles WHERE jobType = ?", role);
            while (res.next()){
                employees.add(employeeDAO.getEmployee(res.getString("id")));
            }
            return employees;
        } catch (SQLException ignore) {
            return employees;
        }
    }

}

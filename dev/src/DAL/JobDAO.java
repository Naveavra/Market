package DAL;

import DomainLayer.Employees.Employee;
import DomainLayer.Employees.JobType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JobDAO {
    private Connect conn = Connect.getInstance();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
//    private static final JobDAO instance = new JobDAO();
//    private JobDAO(){}
//    public static JobDAO getInstance() {
//        return instance;
//    }

    public boolean addLicense(String name, String id, String license){
        try {
            conn.executeUpdate("INSERT OR IGNORE INTO Drivers(name, id, license) VALUES(?,?,?)", name,
                    id, license.toUpperCase(Locale.ROOT));
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
            List<HashMap<String, Object>> res = conn.executeQuery("SELECT * FROM Roles WHERE jobType = ?", role);
           for (int i = 0; i < res.size(); i++){
                employees.add(employeeDAO.getEmployee((String)res.get(i).get("id")));
            }
            return employees;
        } catch (SQLException ignore) {
            return employees;
        }
    }

}

package DomainLayer.Employees;

import DAL.JobDAO;
import java.util.*;

public class JobController {

    private final JobDAO jobDAO = new JobDAO();

    public JobController(){
    }

    public Set<Employee> getCertifiedEmployees(JobType role){
        return jobDAO.getCertifiedEmployees(role);
    }



    public boolean addLicense(String name, String id, String license) {
        return jobDAO.addLicense(name, id, license);
    }

}

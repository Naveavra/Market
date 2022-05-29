package DomainLayer.Employees;

import DAL.JobDAO;
import java.util.*;

public class JobController {
//    private static final JobController instance = new JobController();
//    private final Map<JobType, List<Employee>> roles;
    private final JobDAO jobDAO = JobDAO.getInstance();

    public JobController(){
//        roles = new HashMap<>();
//        roles.put(JobType.HR_MANAGER, new ArrayList<>());
//        roles.put(JobType.SHIFT_MANAGER, new ArrayList<>());
//        roles.put(JobType.CASHIER, new ArrayList<>());
//        roles.put(JobType.STOCK_KEEPER, new ArrayList<>());
//        roles.put(JobType.DRIVER, new ArrayList<>());
//        roles.put(JobType.MERCHANDISER, new ArrayList<>());
//        roles.put(JobType.LOGISTICS_MANAGER, new ArrayList<>());
//        roles.put(JobType.TRANSPORT_MANAGER, new ArrayList<>());
    }

    public Set<Employee> getCertifiedEmployees(JobType role){
        return jobDAO.getCertifiedEmployees(role);
    }



    public boolean addLicense(String id, String license) {
        return jobDAO.addLicense(id, license);
    }

    public void shutDown() {
        jobDAO.shutDown();
    }
}

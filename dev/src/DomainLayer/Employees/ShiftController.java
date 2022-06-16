package DomainLayer.Employees;

import DAL.ShiftDAO;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftController {
//    private final List<Shift> pastShifts;
//    private final List<Shift> upcomingShifts;
    private Shift currentShift;
    private final ShiftDAO shiftDAO = new ShiftDAO();

    public ShiftController(){
//        pastShifts = new ArrayList<>();
//        upcomingShifts = new ArrayList<>();
    }

    public Shift getCurrentShift() {
        return currentShift;
    }


    public boolean startShift(ShiftPair shiftPair, String id) {
        Shift starting = shiftDAO.getShift(shiftPair);
        if (starting.getManager().getId().equals(id)){
            currentShift = starting;
            return true;
        }
        return false;
    }

    public Response endShift(String id){
        if (currentShift == null)
            return new Response("There is not shift currently running");
        if(currentShift.getManager().getId().equals(id)) {
            currentShift.endShift();
            shiftDAO.endShift(currentShift);
            currentShift = null;
            return new Response();
        }
        return new Response("Only the shift manager can end the shift");
    }

//    public static ShiftController getInstance() {
//        return instance;
//    }

    public Employee getCurrentShiftManger(){
        return currentShift.getManager();
    }

    //public List<Shift> getPastShifts() { return pastShifts; }

    public boolean createShift(ShiftPair shiftPair, Employee shiftManager, List<Employee> cashiers, List<Employee> drivers, List<Employee> merchandisers, List<Employee> stockKeepers) {
        if (shiftExists(shiftPair)){
            return false;
        }
        Map<JobType, List<Employee>> map = new HashMap<>();
        map.put(JobType.CASHIER, cashiers);
        map.put(JobType.DRIVER, drivers);
        map.put(JobType.MERCHANDISER, merchandisers);
        map.put(JobType.STOCK_KEEPER, stockKeepers);
        Shift shift = new Shift(shiftManager, map, shiftPair);
        shiftDAO.insertShift(shift);
        return true;
    }

    private boolean shiftExists(ShiftPair shiftPair) {
        return shiftDAO.shiftExists(shiftPair);
    }

    public boolean addEmployeeToShift(Employee eToAdd, JobType jobType, Shift shift) {
        return shift.addEmployee(eToAdd, jobType);
    }

    public void shutDown() {
        shiftDAO.shutDown();
//        if(currentShift != null)
//            endShift(getCurrentShiftManger().getId());
    }

    public boolean shiftIsRunning() {
        return currentShift != null;
    }

    public boolean removeFromCurrentShift(Employee employeeToRemove) {
        return currentShift.removeEmployee(employeeToRemove);
    }

    public String displayWorkersOfCurrentShift() {
        return currentShift.displayWorkers();
    }

    public String viewShift(ShiftPair shiftPair) {
        Shift s = shiftDAO.getShift(shiftPair);
        if (s == null){
            return "Unable to access the specified shift. Make sure you have the right date and time";
        }
        return s.displayWorkers();
    }

    public Response deleteShift(ShiftPair shiftPair) {
        return shiftDAO.deleteShift(shiftPair);
    }
}

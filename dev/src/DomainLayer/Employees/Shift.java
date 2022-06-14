package DomainLayer.Employees;

import ServiceLayer.Utility.ShiftPair;

import java.util.*;

public class Shift {
    private Employee manager;
    private final Map<JobType, List<Employee>> employeesInShift;
    private final List<Employee> earlyLeavers;
    private Date start;
    private Date end;
    private final ShiftPair shiftTime;

    public Shift(Employee manager, Map<JobType, List<Employee>> employeesInShift, ShiftPair shiftTime){
        this.manager = manager;
        this.employeesInShift = employeesInShift;
        this.shiftTime = shiftTime;
        earlyLeavers = new ArrayList<>();
    }

    public void startShift(){
        this.start = new Date();
        manager.startShift();
        for (List<Employee> employees : employeesInShift.values()){
            for (Employee employee : employees){
                employee.startShift();
            }
        }
    }

    public Employee getManager() {
        return manager;
    }

    public Map<JobType, List<Employee>> getEmployeesInShift() {
        return employeesInShift;
    }

    public Date getStart() {
        return start;
    }

    public ShiftPair getShiftTime() {
        return shiftTime;
    }

    public boolean hasStarted(){
        return start != null;
    }

    public boolean hasEnded(){
        return end != null;
    }

    public boolean isInShift(Employee employee){
        for (List<Employee> employees : employeesInShift.values()){
            if (employees.contains(employee))
                return true;
        }
        return false;
    }

    private JobType getEmployeeRole(Employee e){
        Set<JobType> roles = employeesInShift.keySet();
        for (JobType jobType : roles){
            for (Employee employee : employeesInShift.get(jobType)){
                if (employee == e)
                    return jobType;
            }
        }
        return null;
    }

    public String displayWorkers(){
        List<String[]> emps = new ArrayList<>();
        emps.add(new String[]{manager.getId(), manager.getName(), "shift manager"});
        Set<JobType> roles = employeesInShift.keySet();
        for (JobType jobType : roles){
            for (Employee employee : employeesInShift.get(jobType)){
                emps.add(new String[]{employee.getId(), employee.getName(), jobType.name().toLowerCase(Locale.ROOT)});
            }
        }
        StringBuilder toRet = new StringBuilder();
        for (String[] emp : emps){
            toRet.append(Arrays.toString(emp)).append("\n");
        }
        return toRet.toString();
    }

    public boolean addEmployee(Employee eToAdd, JobType jobType) {
        if (isInShift(eToAdd))
            return false;
        if (jobType == JobType.SHIFT_MANAGER){
            manager.endShift();
            manager = eToAdd;
        }
        else if(employeesInShift.containsKey(jobType)) {
            employeesInShift.get(jobType).add(eToAdd);
        }
        else{
            List<Employee> lst = new ArrayList<>();
            lst.add(eToAdd);
            employeesInShift.put(jobType, lst);
        }
        eToAdd.startShift();
        return true;
    }

    public void endShift() {
        end = new Date();
        manager.endShift();
        for (List<Employee> employees : employeesInShift.values()){
            for (Employee employee : employees){
                employee.endShift();
            }
        }
    }

    public boolean removeEmployee(Employee employeeToRemove) {
        if (manager == employeeToRemove)
            return false;
        for (List<Employee> employees : employeesInShift.values()){
            for (Employee employee : employees){
                if (employeeToRemove == employee) {
                    employee.endShift();
                    employees.remove(employeeToRemove);
                    earlyLeavers.add(employeeToRemove);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSameTime(Shift other) {
        return this.shiftTime.equals(other.shiftTime);
    }

    public List<Employee> getAll(JobType jobType) {
        return employeesInShift.get(jobType);
    }

    public Date getEnd() {
        return end;
    }

}

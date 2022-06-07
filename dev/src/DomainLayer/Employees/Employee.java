package DomainLayer.Employees;

import ServiceLayer.Utility.ShiftPair;
import ServiceLayer.Utility.ShiftDate;
import java.util.*;

public class Employee {
    private final String id;
    private String name;
    private String password;
    private float salary;
    private String bankAccount;
    private String contractOfEmployment;
    private Schedule availabilitySchedule;
    private final Date dateOfEmployment;
    private double accMonthlyHours;
    private final Set<JobType> roles;
    private Date shiftStart;
    private List<String> messages;

    //constructor
    public Employee(String id, String name, String password, float salary, String bankAccount,
                    String contractOfEmployment, Date dateOfEmployment){
        this.id = id;
        this.name = name;
        this.password = password;
        this.salary = salary;
        this.bankAccount = bankAccount;
        this.contractOfEmployment = contractOfEmployment;
        this.availabilitySchedule = new Schedule(new ArrayList<>());
        this.accMonthlyHours = 0;
        this.roles = new HashSet<>();
        this.dateOfEmployment = dateOfEmployment;
        this.messages = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getContractOfEmployment() {
        return contractOfEmployment;
    }

    public void setContractOfEmployment(String contractOfEmployment) {
        this.contractOfEmployment = contractOfEmployment;
    }

    public Schedule getAvailabilitySchedule() {
        return availabilitySchedule;
    }

    public void setAvailabilitySchedule(List<ShiftPair> schedule) {
        this.availabilitySchedule = new Schedule(schedule);
    }

    public void addAvailableTimeSlot(ShiftPair shiftTime){
        availabilitySchedule.addTimeSlot(shiftTime);
    }


    public double getAccMonthlyHours() {
        return accMonthlyHours;
    }

    public Set<JobType> getRoles() {
        return roles;
    }

    public void addCertification(JobType jobType){
        this.roles.add(jobType);
    }

    public Date getDateOfEmployment() {
        return dateOfEmployment;
    }

    public boolean isAvailable(ShiftDate date, Time timeOfDay){
        return availabilitySchedule.getAvailabilityStatus(date, timeOfDay);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public boolean isHRManager() {
        return roles.contains(JobType.HR_MANAGER);
    }

    public boolean isCertified(JobType jobType) {
        return roles.contains(jobType);
    }

    public boolean inShift(){
        return shiftStart != null;
    }

    public void startShift(){
        shiftStart = new Date();
    }

    public void endShift(){
        long diff = Math.abs(shiftStart.getTime() - (new Date()).getTime());
        double diffInHours = diff / (double) 3600000;
        accMonthlyHours += diffInHours;
        shiftStart = null;
    }

    public void resetSchedule(){
        this.availabilitySchedule = new Schedule(new ArrayList<>());
    }

    public String toString(){
        return "ID: " + id + ", Name: " + name + ", Available on: " + availabilitySchedule.toString() + ", Salary: " + salary + "\nBank account information:" +
                " " + bankAccount + ", Contract of employment: " + contractOfEmployment + "\nMonthly Hours: " + accMonthlyHours +
                ", Date of employment: " + dateOfEmployment + ", Certifications: " + roles;
    }

    public void setMonthlyHours(double monthlyHrs) {
        this.accMonthlyHours = monthlyHrs;
    }

    public void deleteShift(String id, ShiftPair shiftPair){
        availabilitySchedule.removeTimeSlot(shiftPair);

    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    public String displayMessages() {
        if (messages.isEmpty())
            return "No new messages";
        return messages.toString();
    }

    public void clearMessages() {
        messages.clear();
    }
}



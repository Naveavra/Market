package ServiceLayer;

import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.Employees.JobType;
import PresentationLayer.Transport_Emploees.EmployeeMainCLI;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftPair;

import java.util.*;
import java.util.function.Function;

public class EmployeeService {
    private EmployeeServiceGeneric employeeService;// = new EmployeeServiceGeneric("");
    private final FacadeEmployees_Transports facade;
    private final Parser parser;
    private boolean dataHasLoaded; // this is only to indicate if the pre-made data was loaded into the system or not, so we wouldn't load it twice

    public EmployeeService() {
        facade = new FacadeEmployees_Transports();
        parser = new Parser();
        dataHasLoaded = false;
    }


    public String start() {
        String GREEN_BOLD = "\033[1;32m";  // GREEN
        String reset = "\033[0m";
        return GREEN_BOLD+"Hello,Welcome to the Superli management system!"+reset;
    }

    public Action parseActionChoice(String choice){
        return parser.parseActionChoice(choice, facade.isHRManager(employeeService.getID()));
    }

    /**
     * retrieves the names, ids, and availability of all certified employees
     * @param jobNum number of certification as inputted by the user
     * @param displayInShift flag that indicates weather or not to display those that are in shift, comes in handy
     *                       when this function is being called mid-shift
     * @return list in the format of <id> <name> <isAvailable>
     */

    public List<String> getAllCertified(String jobNum, boolean displayInShift){
        JobType jobType = parser.parseJobType(jobNum);
        List<String[]> namesAndIDs = facade.getAllCertified(jobType, facade.getCurrentShiftTime(), displayInShift);
        List<String> ret = new ArrayList<>();
        for (String[] s: namesAndIDs){
            ret.add(String.join(" ", s));
        }
        return ret;
    }

    public List<String> getAllCertified(JobType jobType, String sShift, boolean displayInShift){
        ShiftPair shift = parser.getShiftPair(sShift);
        List<String[]> namesAndIDs = facade.getAllCertified(jobType, shift, displayInShift);
        List<String> ret = new ArrayList<>();
        for (String[] s: namesAndIDs){
            ret.add(String.join(" ", s));
        }
        return ret;
    }

    /**
     * helper function for the getAllCertified function, which asserts that all given employees are certified for the job
     * @param ids ids of employees as inputted by the user
     * @param jobType job type of the employees
     * @return valid response if all ids correspond to certified employees
     */
    public Response areCertified(String ids, JobType jobType){
        String[] employees = ids.split(",");
        for (String id: employees){
            Response response = facade.isCertified(id, jobType);
            if (response.errorOccurred())
                return response;
        }
        return new Response();
    }

    /**
     * gets ids from the user and verified that they are certified
     * @param ids ids of employees as inputted by the user
     * @param employeeCli UI that prints requests to the user if necessary
     * @param jobType required type of certification
     * @return a string comprised of valid and certified ids, separated by commas
     */
    private String getCertifiedIDs(String ids, EmployeeMainCLI employeeCli, JobType jobType){
        Response response = areCertified(ids, jobType);
        while(response.errorOccurred()){
            employeeCli.print(response.getErrorMessage());
            ids = getValidInput(employeeCli, "Enter the ids of the " + jobType.toString().toLowerCase(Locale.ROOT) +
                    "s in the shift" + ", separated by commas", "",parser::isValidIdList);
            response = areCertified(ids, jobType);
        }
        return ids;
    }

    private String getValidInput(EmployeeMainCLI employeeCli, String msgToPrint, String msgInCaseOfErr , Function<String, Response> validationFunction){
        employeeCli.print(msgToPrint);
        String input = employeeCli.getUserInput();
        Response response = validationFunction.apply(input);
        while (response.errorOccurred()){
            employeeCli.print(response.getErrorMessage());
            if(!msgInCaseOfErr.equals(""))
                employeeCli.print(msgInCaseOfErr);
            input = employeeCli.getUserInput();
            response = validationFunction.apply(input);
        }
        return input;
    }

    public Response certifyEmployee(String id, String jobNum){
        JobType job = parser.parseJobType(jobNum);
        return facade.certifyEmployee(job, id);
    }

    public Response certifyDriver(String id, String license){
        if(!facade.certifyDriver(id, license))
            return new Response("Id does not exist in the system");
        return new Response();
    }

    public Response isValidLicense(String license){
        return parser.isValidLicense(license);
    }


    public Response logout(){
        employeeService = null;
        return new Response();
    }


    public String displayActions() {
        return employeeService.displayActions();
    }

    public Response login(String id, String password) {
        Response response = facade.login(id, password);
        if (response.errorOccurred())
            return response;
        createEmployeeService(id);
//        initializeDBConnection();
        return new Response();
    }

/*    private void initializeDBConnection() {
        facade.initializeDBConnection();
    }*/

    public Response isValidPassword(String password) {
        if (password.equals("")){
            return new Response("Password cannot be empty");
        }
        return new Response();
    }

    public Response isValidID(String id){
        return parser.isValidID(id);
    }

    public Response register(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment){
        return facade.createEmployee(id, name, password, salary, bankAccount, contractOfEmployment);
    }

    public Response isValidSalary(String salary) {
        return parser.isValidSalary(salary);
    }

    public Response isValidName(String name) {
        return parser.isValidName(name);
    }

    private void createEmployeeService(String id) {
        if(facade.isHRManager(id)){
            employeeService = new HRManagerService(id);
        }
        else {
            employeeService = new EmployeeServiceGeneric(id);
        }
    }

    public Response isValidBankAccount(String bankAccount) {
        return parser.isValidBankAccount(bankAccount);
    }

    public Response isValidContractOfEmployment(String terms) {
        if (terms.equals(""))
            return new Response("Contract of employment cannot be empty");
        return new Response();
    }

    public Response assertShiftInput(String input) {
        return parser.isValidShiftInput(input);
    }

    public String displaySchedule() {
        return facade.getScheduleOf(employeeService.getID());
    }

    public String displaySchedule(String id) {
        return facade.getScheduleOf(id);
    }

    public boolean isLoggedIn() {
        return employeeService != null;
    }

/*    public void loadPreMadeData() {
        if(!dataHasLoaded){
            facade.loadPreMadeData();
        }
    }*/

/*    public void closeDbConnection() {
        facade.closeDbConnection();
    }*/

    public boolean resetSchedule() {

        return facade.resetSchedule(employeeService.getID());
    }

    /**
     * used only for loading data, **NOT FOR ACTUAL USE**
     * @param shiftInput VALID shift string
     * @param id employee to add availability slot to
     */
    public void addAvailableTimeSlotToEmployee(String shiftInput, String id){
        ShiftPair sp = parser.getShiftPair(shiftInput);
        facade.addAvailableTimeSlotToEmployee(id, sp);
    }

    public Response addAvailableTimeSlotToEmployee(String shiftInput) {
        Response r = parser.isValidShiftInput(shiftInput);
        if (r.errorOccurred())
            return r;
        ShiftPair sp = parser.getShiftPair(shiftInput);
        if (facade.addAvailableTimeSlotToEmployee(employeeService.getID(), sp))
            return new Response();
        else
            return new Response("Something went wrong");
    }

    public Response removeShift(String shiftInput) {
        Response r = parser.isValidShiftInput(shiftInput);
        if (r.errorOccurred())
            return r;
        ShiftPair sp = parser.getShiftPair(shiftInput);
        if (facade.removeShift(employeeService.getID(), sp))
            return new Response();
        else
            return new Response("Something went wrong");
    }

    public Response startShift(String shiftInput) {
        if(facade.shiftIsRunning()) {
            return new Response("You cannot start a new shift while the current shift is still running");
        }
        Response r = parser.isValidShiftInput(shiftInput);
        if (r.errorOccurred())
            return r;
        ShiftPair sp = parser.getShiftPair(shiftInput);
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (day.length() == 1)
            day = "0" + day;
        if (month.length() == 1)
            month = "0" + month;
        String thisDay = day + '/' + month + '/' + year;
        if(!thisDay.equals(sp.getDate().toString())){
            return new Response("Today's date is " + thisDay + ", not " + sp.getDate() + ". You can only start today's shift");
        }
        if (facade.startShift(sp, employeeService.getID()))
            return new Response();
        else
            return new Response("Something went wrong. Make sure that the shift manager is the one trying " +
                    "to start the shift");
    }

    public Response endShift() {
        return facade.endShift(employeeService.getID());
    }

    public Response isValidJobNumber(String num){
        return parser.isValidJobNumber(num);
    }

    public Response isCertified(String id, String jobNum) {
        JobType jobType = parser.parseJobType(jobNum);
        return facade.isCertified(id, jobType);
    }

    public boolean addEmployeeToCurrentShift(String id, String jobNum) {
        JobType jobType = parser.parseJobType(jobNum);
        return facade.addEmployeeToCurrentShift(id, jobType);
    }

    public Response createShift(String sShift, String managerID, String cashiersIDs, String driversIDs,
                                String merchandisersIDs, String stockKeepersIDs){
        ShiftPair shift = parser.getShiftPair(sShift);
        Response rIntersection = parser.assertNoIntersection(managerID.split(", "), cashiersIDs.split(", "),
                driversIDs.split(", "), merchandisersIDs.split(", "), stockKeepersIDs.split(", "));
        if (rIntersection.errorOccurred())
            return rIntersection;
        return facade.createShift(shift, managerID, cashiersIDs, driversIDs, merchandisersIDs, stockKeepersIDs);
    }

    public Response isValidIdList(String s) {
        return parser.isValidIdList(s);
    }

    public String getDetailsOf(String id) {
        return facade.getDetailsOf(id);
    }

    public boolean editName(String id, String newName) {
        return facade.editName(id, newName);
    }

    public boolean editPassword(String id, String newPassword) {
        return facade.editPassword(id, newPassword);
    }

    public boolean editSalary(String id, String sNewSalary) {
        try {
            float newSalary = Float.parseFloat(sNewSalary);
            return facade.editSalary(id, newSalary);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean editBankInfo(String id, String newBankInfo) {
        return facade.editBankInfo(id, newBankInfo);
    }

    public boolean editContract(String id, String newContract) {
        return facade.editContract(id, newContract);
    }

    public Response removeEmployee(String id) {
        return facade.removeEmployee(id);
    }

    public Set<JobType> getEmployeeRoles(String id){
        return facade.getEmployeeRoles(id);
    }

    public Set<JobType> getLoggedInEmployeeRoles() {
        return getEmployeeRoles(employeeService.getID());
    }



    public String displayLoggedInUserMessages() {
        return displayMessages(employeeService.getID());
    }

    private String displayMessages(String id) {
        return facade.displayMessages(id);
    }

    public String viewShift(String sShift) {
        Response r = parser.isValidShiftInput(sShift);
        if (r.errorOccurred())
            return r.getErrorMessage();
        return facade.viewShift(parser.getShiftPair(sShift));
    }

    public Response deleteShift(String sShift) {
        Response r = parser.isValidShiftInput(sShift);
        if (r.errorOccurred())
            return r;
        return facade.deleteShift(parser.getShiftPair(sShift));
    }

    public String displaySubMenu(String action) {
        return parser.displaySubMenu(action, facade.isHRManager(employeeService.getID()));
    }

    public boolean isHR(){
        return facade.isHRManager(employeeService.getID());
    }


    public Action parseManageSchedule(String choice) {
        return parser.parseManageSchedule(choice);
    }

    public Action parseManageEmployees(String choice) {
        return parser.parseManageEmployees(choice);
    }

    public Action parseManageDetails(String choice) {
        return parser.parseManageDetails(choice);
    }

    public Action parseManageShifts(String choice) {
        return parser.parseManageShifts(choice);
    }
}

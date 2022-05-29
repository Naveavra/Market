package ServiceLayer;

import DomainLayer.Employees.Facade;
import DomainLayer.Employees.JobType;
import PresentationLayer.Transport_Emploees.EmployeeMainCLI;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftPair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class ServiceController {
    private static final ServiceController instance = new ServiceController();
    private EmployeeService employeeService;
    private final Facade facade;
    private final Parser parser;
    private boolean dataHasLoaded; // this is only to indicate if the pre-made data was loaded into the system or not, so we wouldn't load it twice

    private ServiceController() {
        facade = Facade.getInstance();
        parser = new Parser();
        dataHasLoaded = false;
    }

    public static ServiceController getInstance() {
        return instance;
    }

    public String start() {
        String GREEN_BOLD = "\033[1;32m";  // GREEN
        String reset = "\033[0m";
        return GREEN_BOLD+"Hello,Welcome to the Superli management system!"+reset;
    }

    public Runnable doAction(String choice, EmployeeMainCLI employeeCli) {
        Action action = parser.parseActionChoice(choice, facade.isHRManager(employeeService.getID()));
        if (action == Action.DISPLAY_SCHEDULE) {
            return ()-> employeeCli.print(displaySchedule());
        } else if (action == Action.CHANGE_SCHEDULE) {
                return ()->handleChangeSchedule(employeeCli);
        }
        else if (action == Action.START_SHIFT){
            return () -> handleStartShift(employeeCli);
        }
        else if (action == Action.END_SHIFT){
            return () -> handleEndShift(employeeCli);
        }
        else if (action == Action.MID_SHIFT_ACTIONS){
            return () -> handleMidShiftActions(employeeCli);
        }
        else if (action == Action.LOGOUT){
            return ()-> handleLogOut(employeeCli);
        }
        else if (action == Action.REGISTER_EMPLOYEE){
            return ()-> employeeCli.register();
        } else if (action == Action.CERTIFY_EMPLOYEE){
            return ()->handleCertification(employeeCli);
        }
        else if (action == Action.CREATE_SHIFT){
            return ()-> handleCreateShift(employeeCli);
        } else if(action == Action.VIEW_EMPLOYEE_DETAILS){
            return ()-> handleViewDetails(employeeCli);
        } else if(action == Action.EDIT_EMPLOYEE_DETAILS){
            return ()->handleEditDetails(employeeCli);
        } else if (action == Action.DELETE_EMPLOYEE){
            return () -> handleDelete(employeeCli);}
//        } else if (action == Action.EXIT_SYSTEM){
//            return () -> handleCloseSystem(employeeCli);
//        }
        return ()->{};
    }

    public void handleCloseSystem(EmployeeMainCLI employeeCli) {
        facade.shutDown();
        employeeCli.print("Goodbye!");
//        System.exit(0);
    }

    private void handleEndShift(EmployeeMainCLI employeeCli) {
        if (!facade.shiftIsRunning()){
            employeeCli.print("Cannot end shift since there is no shift running at the moment");
            return;
        }
        if(!facade.endShift(employeeService.getID())){
            employeeCli.print("Make sure that the employee logged in is the shift manager. Only the shift manager can end the shift.");
        }
        else{
            employeeCli.print("Shift has ended");
        }
    }

    private void handleStartShift(EmployeeMainCLI employeeCli) {
        if(facade.shiftIsRunning()) {
            employeeCli.print("You cannot start a new shift while the current shift is still running");
            return;
        }
        String shift = getValidInput(employeeCli, "Enter the starting shift time",
                "Invalid shift time", this::assertShiftInput);
        ShiftPair shiftPair = parser.getShiftPair(shift);
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String thisDay = day + '/' +month + '/' + year;
        if(!thisDay.equals(shiftPair.toString())){
            employeeCli.print("Today's date is " + thisDay + ", not " + shiftPair.getDate() + ". You can only start today's shift");
        }
        else if(!facade.startShift(shiftPair, employeeService.getID())){
            employeeCli.print("Something's wrong." +
                    " Make sure that the shift time is correct and that the user logged in is the shift manager");
        }
        else{
            employeeCli.print("Shift has started");
        }
    }

    private void handleMidShiftActions(EmployeeMainCLI employeeCli) {
        String empID = employeeService.getID();
        if(!facade.shiftIsRunning()){
            employeeCli.print("Cannot perform mid-shift actions since there is no shift running at the moment");
            return;
        }
        if (!facade.isCurrentShiftManager(empID)){
            employeeCli.print("Only the shift manager is authorized to perform this action");
            return;
        }
        String optionList = "\nChoose your action:\n1. Cancel customer purchase\n2. Add an employee to the shift" +
                "\n3. Remove an employee from the shift";
        employeeCli.print(optionList);
        String input = employeeCli.getUserInput();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            employeeCli.print("You must choose the number of operation you want to perform");
            employeeCli.print(optionList);
            input = employeeCli.getUserInput();
        }
        if (input.equals("1")){
            employeeCli.print("Sorry, this feature is not supported at this moment.");
        }
        else if (input.equals("2")){
            String toPrint = "Choose the type of job the employee will be doing in this shift:\n1.Human resources manager\n2.Shift manager (notice that the new shift manager will replace the current one, and the current one will leave the shift)" +
                    "\n3.Cashier\n4.Stock keeper\n5.Driver\n6.Merchandiser\n7.Logistics manager\n8.Transport manager";
            String jobNum = getValidInput(employeeCli, toPrint,"", parser::isValidJobNumber);
            JobType jobType = parser.parseJobType(jobNum);
            employeeCli.print(getAllCertified(jobType, facade.getCurrentShiftTime(), false).toString());
            employeeCli.print("From the above list, enter the id of the employee you want to add");
            String id = employeeCli.getValidId();
            Response response = facade.isCertified(id, jobType);
            if(response.errorOccurred()){
                employeeCli.print(response.getErrorMessage());
            }
            else{
                if(!facade.addEmployeeToCurrentShift(id, jobType)){
                    employeeCli.print("Employee is already working in this shift");
                }
                else {
                    employeeCli.print("Employee was added successfully");
                }
            }
        }
        else { // input.equals("3")
            employeeCli.print(facade.displayWorkersOfCurrentShift());
            employeeCli.print("Enter the id of the employee leaving the shift");
            employeeCli.print("Notice that you cannot remove the shift manager, but you can provide a different manager to replace them");
            String idToRemove = employeeCli.getValidId();
            if (!facade.removeFromShift(idToRemove)){
                employeeCli.print("Unable to remove employee.\nMake sure that the id inserted is selected from the provided list" +
                        ", and that it isn't the manager's id");
            }
            else{
                employeeCli.print("The employee has left the shift successfully");
            }
        }
    }

    private void handleDelete(EmployeeMainCLI employeeCli) {
        employeeCli.print("Enter the id of the employee you want to delete");
        String id = employeeCli.getValidId();
        Response response = facade.removeEmployee(id);
        if(response.errorOccurred()){
            employeeCli.print(response.getErrorMessage());
        }
        else {
            employeeCli.print("Employee was deleted successfully");
        }
    }


    private void handleEditDetails(EmployeeMainCLI employeeCli){
        employeeCli.print("Enter the id of the employee you want to edit");
        String id = employeeCli.getValidId();
        if(!facade.exists(id)){
            employeeCli.print("Employee does not exist.");
            return;
        }
        String detailList = "Choose which detail you want to edit:\n1.name\n2.password\n3.salary\n4.bank account " +
                "information\n5.contract of employment";
        employeeCli.print(detailList);
        String sChoice = employeeCli.getUserInput();
        Response response = parser.isValidChoice(1, 5, sChoice);
        while (response.errorOccurred()){
            employeeCli.print(response.getErrorMessage());
            employeeCli.print(detailList);
            sChoice = employeeCli.getUserInput();
            response = parser.isValidChoice(1, 5, sChoice);
        }
        boolean success = false;
        switch (sChoice) {
            case "1" : {
                employeeCli.print("Enter the new name of the employee");
                String newName = employeeCli.getValidName();
                success = facade.editName(id, newName);
                break;
            }
            case "2" : {
                employeeCli.print("Enter the new password of the employee");
                String newPassword = employeeCli.getValidPassword();
                success = facade.editPassword(id, newPassword);
                break;
            }
            case "3" : {
                employeeCli.print("Enter the new salary of the employee");
                String newSalary = employeeCli.getValidSalary();
                success = facade.editSalary(id, Float.parseFloat(newSalary));
                break;
            }
            case "4" : {
                employeeCli.print("Enter the new bank account information of the employee");
                String newBankInfo = employeeCli.getValidBankAccount();
                success = facade.editBankInfo(id, newBankInfo);
                break;
            }
            case "5" : {
                employeeCli.print("Enter the new contract of employment");
                String newContract = employeeCli.getValidContractOfEmployment();
                success = facade.editContract(id, newContract);
                break;
            }
        }
        if (!success)
            employeeCli.print("Something went wrong, the update was unsuccessful");
        else
            employeeCli.print("Your choice was updated successfully");
    }

    private void handleCreateShift(EmployeeMainCLI employeeCli){
        String sShift = getValidInput(employeeCli, "Enter the time of the shift you want to create\n" +
                "Example: dd/mm/yyyy evening", "", this::assertShiftInput);
        ShiftPair shift = parser.getShiftPair(sShift);

        //shift manager
        employeeCli.print("Shift managers:");
        employeeCli.print(getAllCertified(JobType.SHIFT_MANAGER, shift, true).toString());
        String shiftManagerID = getValidInput(employeeCli, "Enter the id " +
                "of the shift manager:", "Every shift must have a shift manager", parser::isValidID);
        Response response = areCertified(shiftManagerID, JobType.SHIFT_MANAGER);
        while(response.errorOccurred()){
            employeeCli.print(response.getErrorMessage());
            employeeCli.print("Please enter a shift manager's id:");
            shiftManagerID = employeeCli.getUserInput();
            response = areCertified(shiftManagerID, JobType.SHIFT_MANAGER);
        }

        //cashiers
        employeeCli.print("Cashiers:");
        employeeCli.print(getAllCertified(JobType.CASHIER, shift, true).toString());
        String cashiersIDs = getValidInput(employeeCli, "Enter the ids of the cashiers in the shift" +
                ", separated by commas", "",parser::isValidIdList);
        cashiersIDs = getCertifiedIDs(cashiersIDs, employeeCli, JobType.CASHIER);


        //drivers
        employeeCli.print("Drivers:");
        employeeCli.print(getAllCertified(JobType.DRIVER, shift, true).toString());
        String driversIDs = getValidInput(employeeCli, "Enter the ids of the drivers in the shift" +
                ", separated by commas", "", parser::isValidIdList);
        driversIDs = getCertifiedIDs(driversIDs, employeeCli, JobType.DRIVER);


        //merchandisers
        employeeCli.print("Merchandisers:");
        employeeCli.print(getAllCertified(JobType.MERCHANDISER, shift, true).toString());
        String merchandisersIDs = getValidInput(employeeCli, "Enter the id's of the merchandisers in the shift" +
                ", separated by commas", "",parser::isValidIdList);
        merchandisersIDs = getCertifiedIDs(merchandisersIDs, employeeCli, JobType.MERCHANDISER);


        //stock keepers
        employeeCli.print("Stock keepers:");
        employeeCli.print(getAllCertified(JobType.STOCK_KEEPER, shift, true).toString());
        String stockKeepersIDs = getValidInput(employeeCli, "Enter the id's of the stock keepers in the shift" +
                ", separated by commas", "",parser::isValidIdList);
        stockKeepersIDs = getCertifiedIDs(stockKeepersIDs, employeeCli, JobType.STOCK_KEEPER);


        Response intersectionCheck = parser.assertNoIntersection(shiftManagerID.split(", "), cashiersIDs.split(", "),
                driversIDs.split(", "), merchandisersIDs.split(", "), stockKeepersIDs.split(", "));
        if (intersectionCheck.errorOccurred()){
            employeeCli.print(intersectionCheck.getErrorMessage());
            return;
        }

        Response shiftCreated = facade.createShift(shift, shiftManagerID, cashiersIDs, driversIDs, merchandisersIDs, stockKeepersIDs);
        if(shiftCreated.errorOccurred())
            employeeCli.print(shiftCreated.getErrorMessage());
        else
            employeeCli.print("The " + shift.when() + " shift was created successfully");
    }

    /**
     * retrieves the names, ids, and availability of all certified employees
     * @param jobType type of certification
     * @param shift used to evaluate the availability of the relevant employees
     * @param displayInShift flag that indicates weather or not to display those that are in shift, comes in handy
     *                       when this function is being called mid-shift
     * @return list in the format of <id> <name> <isAvailable>
     */
    private List<String> getAllCertified(JobType jobType, ShiftPair shift, boolean displayInShift){
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
    private Response areCertified(String ids, JobType jobType){
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

    private void handleCertification(EmployeeMainCLI employeeCli){
            employeeCli.print("Enter the details of the employee being certified");
            String id = employeeCli.getValidId();
            String msg = "Enter the type of certification. it must be one the following:\n1.Human resources manager\n2.Shift manager\n3.Cashier\n4.Stock keeper\n5.Driver\n6.Merchandiser\n7.Logistics manager";
            String jobNum = getValidInput(employeeCli, msg, "",parser::isValidJobNumber);
            JobType jobType = parser.parseJobType(jobNum);
            if (jobType == JobType.DRIVER){
                String license = getValidInput(employeeCli, "Enter the driver's license", "A valid license must be either C or C1", parser::isValidLicense);
                if (!facade.certifyDriver(id, license))
                    employeeCli.print("Id does not exist in the system");
            }
            else if(!facade.certifyEmployee(jobType, id)){
                employeeCli.print("Id does not exist in the system");
            }
            employeeCli.print("You have certified the employee successfully");
    }

    private void handleViewDetails(EmployeeMainCLI employeeCli){
        employeeCli.print("Enter the id of the employee");
        String id = employeeCli.getValidId();
        String details = facade.getDetailsOf(id);
        employeeCli.print(details);
    }

    public void handleLogOut(EmployeeMainCLI employeeCli) {
        employeeService = null;
        employeeCli.print("You have logged out successfully");
    }

    private void handleChangeSchedule(EmployeeMainCLI employeeCli) {
        String today = LocalDate.now().getDayOfWeek().toString().toLowerCase(Locale.ROOT);
        if(today.equals("friday") || today.equals("saturday")){
            employeeCli.print("You cannot change your schedule on fridays or saturdays.");
        }
        employeeCli.print("Choose an option:\n1. Reset your schedule\n2. Add an available shift\n3. Remove an available shift");
        String input = employeeCli.getUserInput();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            employeeCli.print("You must choose the number of operation you want to perform");
            employeeCli.print("Choose an option:\n1. Reset your schedule\n2. Add an available shift\n3. Remove an available shift");
            input = employeeCli.getUserInput();
        }
        if (input.equals("1")) {
            facade.resetSchedule(employeeService.getID());
        }
        else if (input.equals("2")) {
            employeeCli.print("Type in the shift you want to add.\nExample:  19/10/1998 evening");
            input = employeeCli.getUserInput().toLowerCase(Locale.ROOT);
            Response response = assertShiftInput(input);
            if(response.errorOccurred())
                employeeCli.print(response.getErrorMessage());
            else {
                ShiftPair shift = parser.getShiftPair(input);
                if(facade.addAvailableTimeSlotToEmployee(employeeService.getID(), shift)) {
                    employeeCli.print("Your schedule was changed successfully.\nYour new schedule:");
                    employeeCli.print(displaySchedule());
                }
                else {
                    employeeCli.print("Couldn't find the id you provided. Make sure you have the right id");
                }
            }
        }
        else{
            employeeCli.print(displaySchedule());
            employeeCli.print("Enter the shift you want to remove:");
            input = employeeCli.getUserInput();
            Response response = assertShiftInput(input);
            if(response.errorOccurred()){
                employeeCli.print(response.getErrorMessage());
            }
            else{
                ShiftPair shift = parser.getShiftPair(input);
                facade.removeShift(employeeService.getID(), shift);
                employeeCli.print("Shift was removed successfully.\nYour new schedule:");
                employeeCli.print(displaySchedule());
            }
        }
    }

    public String displayActions() {
        return employeeService.displayActions();
    }

    public Response login(String id, String password) {
        Response response = facade.login(id, password);
        if (response.errorOccurred())
            return response;
        createEmployeeService(id);
        initializeDBConnection();
        return new Response();
    }

    private void initializeDBConnection() {
        //facade.initializeDBConnection();
    }

    public Response isValidPassword(String password) {
        if (password.equals("")){
            return new Response("Password cannot be empty");
        }
        return new Response();
    }

    public Response isValidID(String id){
       return parser.isValidID(id);
    }

    public Response register(String id, String name, String password, float salary, String bankAccount, String contractOfEmployment){//){
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
            employeeService = new EmployeeService(id);
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

    private Response assertShiftInput(String input) {
        return parser.isValidShiftInput(input);
    }

    private String displaySchedule() {
        return facade.getScheduleOf(employeeService.getID());
    }

    public boolean isLoggedIn() {
        return employeeService != null;
    }

    public void loadPreMadeData() {
        if(!dataHasLoaded){
            facade.loadPreMadeData();
        }
    }

    public void closeDbConnection() {
        //facade.closeDbConnection();
    }
}

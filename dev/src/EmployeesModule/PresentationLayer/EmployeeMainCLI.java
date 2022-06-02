package EmployeesModule.PresentationLayer;

import DomainLayer.Employees.JobType;
import PresentationLayer.Transport_Emploees.MainCLI;
import ServiceLayer.Action;
import EmployeesModule.ServiceLayer.EmployeeService;
import ServiceLayer.Utility.Response;

//import SharedSpace.MainCLI;

import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class EmployeeMainCLI extends MainCLI {
    private final EmployeeService serviceController;

    public EmployeeMainCLI() {
        serviceController = EmployeeService.getInstance();
    }

    public void start() {
        print(serviceController.start());
        while (true) {
            while (!serviceController.isLoggedIn()) {
                login();
            }
            print(serviceController.displayActions());
            String action = getUserInput();
            if (action.equals("goodbye")) {
                print("Goodbye");
                break;
            }
            parseAndDoAction(action);
//            serviceController.doAction(action, this);
//            ans.run();
        }
    }

    private void parseAndDoAction(String choice) {
        Action action = serviceController.parseActionChoice(choice);
        if (action == Action.DISPLAY_SCHEDULE) {
            displaySchedule();
        } else if (action == Action.CHANGE_SCHEDULE) {
            changeSchedule();
        }
        else if (action == Action.START_SHIFT){
            startShift();
        }
        else if (action == Action.END_SHIFT){
            endShift(); //done
        }
        else if (action == Action.MID_SHIFT_ACTIONS){
            midShiftActions(); //done
        }
        else if (action == Action.LOGOUT){
            logout(); //done
        }
        else if (action == Action.REGISTER_EMPLOYEE){
            register();
        } else if (action == Action.CERTIFY_EMPLOYEE){
            certify();
        }
        else if (action == Action.CREATE_SHIFT){
            createShift(); //done
        } else if(action == Action.VIEW_EMPLOYEE_DETAILS){
            viewDetails(); //done
        } else if(action == Action.EDIT_EMPLOYEE_DETAILS){
            editDetails(); //done
        } else if (action == Action.DELETE_EMPLOYEE){
            deleteEmployee();
        }
//        } else if (action == Action.EXIT_SYSTEM){
//            return () -> handleCloseSystem(employeeCli);
//        }
    }

    public void register() {
        print("Enter the following details of the employee you want to register:");
        String id = getValidId();
        String name = getValidName();
        String password = getValidPassword();
        String sSalary = getValidSalary();
        float salary = Float.parseFloat(sSalary);
        String bankAccount = getValidBankAccount();
        String contractOfEmployment = getValidContractOfEmployment();
        Response response = serviceController.register(id, name, password, salary, bankAccount, contractOfEmployment);
        if (response.errorOccurred()) {
            print(response.getErrorMessage());
        } else {
            print("You have registered successfully");
        }

    }

    public String getValidInput(String msgToPrint, String msgInCaseOfErr, Function<String, Response> validationFunction) {
        print(msgToPrint);
        String input = getUserInput();
        Response response = validationFunction.apply(input);
        while (response.errorOccurred()) {
            print(response.getErrorMessage());
            if (!msgInCaseOfErr.equals(""))
                print(msgInCaseOfErr);
            input = getUserInput();
            response = validationFunction.apply(input);
        }
        return input;
    }

    public String getValidContractOfEmployment() {
        String msgToPrint = "Enter the employee's terms of employment";
        String msgIfErr = "Enter the employee's terms of employment";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidContractOfEmployment);
    }

    public String getValidBankAccount() {
        String msgToPrint = "Enter bank account information:";
        String msgIfErr = "Enter a valid bank account:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidBankAccount);
    }

    public String getValidSalary() {
        String msgToPrint = "Enter salary:";
        String msgIfErr = "Enter a valid salary:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidSalary);
    }

    public String getValidName() {
        String msgToPrint = "Enter full name:";
        String msgIfErr = "Enter a valid name:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidName);
    }

    public String getValidPassword() {
        String msgToPrint = "Enter password:";
        String msgIfErr = "Enter a valid password:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidPassword);
    }

    public String getValidId() {
        String msgToPrint = "Enter id:";
        String msgIfErr = "Enter a valid id:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidID);
    }

    public void login() {
        print("Please log in to the system");
        String id = getValidId();
        String password = getValidPassword();
        Response response = serviceController.login(id, password);
        if (!response.errorOccurred()) {
            print("You have logged in successfully");
        }
        else{
            print(response.getErrorMessage());
        }
    }

    public void logout() {
        print("Please logout from the system");
        Response response = serviceController.logout();
        if (response.errorOccurred()) {
            print(response.getErrorMessage());
        } else {
            print("You have logged out successfully");
        }
    }

    public void displaySchedule() {
        print(serviceController.displaySchedule());
    }

    public void changeSchedule() {
        print("Choose an option:\n1. Reset your schedule\n2. Add an available shift\n3. Remove an available shift");
        String input = getUserInput();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            print("You must choose the number of operation you want to perform");
            print("Choose an option:\n1. Reset your schedule\n2. Add an available shift\n3. Remove an available shift");
            input = getUserInput();
        }
        if (input.equals("1")) {
            if (serviceController.resetSchedule()) {
                print("Your schedule was reset successfully");
            } else {
                print("Something went wrong and we were unable to reset your schedule");
            }
        } else if (input.equals("2")) {
            print("Type in the shift you want to add.\nExample:  19/10/1998 evening");
            String shiftInput = getUserInput().toLowerCase(Locale.ROOT);
            Response r = serviceController.addAvailableTimeSlotToEmployee(shiftInput);
            if (r.errorOccurred()) {
                print(r.getErrorMessage());
            } else {
                print("Your schedule was changed successfully.\nYour new schedule:");
                print(serviceController.displaySchedule());
            }
        } else {
            print(serviceController.displaySchedule());
            print("Enter the shift you want to remove:");
            String shiftInput = getUserInput();
            Response response = serviceController.removeShift(shiftInput);
            if (response.errorOccurred()) {
                print(response.getErrorMessage());
            } else {
                print("Shift was removed successfully.\nYour new schedule:");
                print(serviceController.displaySchedule());
            }
        }
    }

    public void startShift() {
        String shiftInput = getValidInput("Enter the starting shift time",
                "Invalid shift time", serviceController::assertShiftInput);
        Response r = serviceController.startShift(shiftInput);
        if (r.errorOccurred()) {
            print(r.getErrorMessage());
        } else {
            print("Shift has successfully started");
        }
    }

    public void endShift() {
        Response r = serviceController.endShift();
        if (r.errorOccurred()) {
            print(r.getErrorMessage());
        } else {
            print("Shift has ended");
        }
    }

    public void midShiftActions() {
        String optionList = "\nChoose your action:\n1. Cancel customer purchase\n2. Add an employee to the shift" +
                "\n3. Remove an employee from the shift";
        String input = getValidInput(optionList, "",
                (arg) -> !arg.equals("1") && !arg.equals("2") && !arg.equals("3") ?
                        new Response("You must choose the number of operation you want to perform") :
                        new Response());
        if (input.equals("1")) {
            print("Sorry, this feature is not supported at this moment.");
        } else if (input.equals("2")) {
            String toPrint = "Choose the type of job the employee will be doing in this shift:\n1.Human resources manager\n2.Shift manager (notice that the new shift manager will replace the current one, and the current one will leave the shift)" +
                    "\n3.Cashier\n4.Stock keeper\n5.Driver\n6.Merchandiser\n7.Logistics manager\n8.Transport manager";
            String jobNum = getValidInput(toPrint, "", serviceController::isValidJobNumber);
            print(serviceController.getAllCertified(jobNum, false).toString());
            print("From the above list, enter the id of the employee you want to add");
            String id = getValidId();
            Response response = serviceController.isCertified(id, jobNum);
            if (response.errorOccurred()) {
                print(response.getErrorMessage());
            } else {
                if (!serviceController.addEmployeeToCurrentShift(id, jobNum)) {
                    print("Employee is already working in this shift");
                } else {
                    print("Employee was added successfully");
                }
            }
        }
    }

    public void createShift() {
        String sShift = getValidInput("Enter the time of the shift you want to create\n" +
                "Example: dd/mm/yyyy evening", "", serviceController::assertShiftInput);

        //shift manager
        print("Shift managers:");
        print(serviceController.getAllCertified(JobType.SHIFT_MANAGER, sShift, true).toString());
        String shiftManagerID = getValidInput("Enter the id " +
                "of the shift manager:", "Every shift must have a shift manager", serviceController::isValidID);
        //cashiers
        print("Cashiers:");
        print(serviceController.getAllCertified(JobType.CASHIER, sShift, true).toString());
        String cashiersIDs = getValidInput("Enter the ids of the cashiers in the shift" +
                ", separated by commas", "", serviceController::isValidIdList);

        //drivers
        print("Drivers:");
        print(serviceController.getAllCertified(JobType.DRIVER, sShift, true).toString());
        String driversIDs = getValidInput("Enter the ids of the drivers in the shift" +
                ", separated by commas", "", serviceController::isValidIdList);

        //merchandisers
        print("Merchandisers:");
        print(serviceController.getAllCertified(JobType.MERCHANDISER, sShift, true).toString());
        String merchandisersIDs = getValidInput("Enter the id's of the merchandisers in the shift" +
                ", separated by commas", "", serviceController::isValidIdList);

        //stock keepers
        print("Stock keepers:");
        print(serviceController.getAllCertified(JobType.STOCK_KEEPER, sShift, true).toString());
        String stockKeepersIDs = getValidInput("Enter the id's of the stock keepers in the shift" +
                ", separated by commas", "", serviceController::isValidIdList);


        Response shiftCreated = serviceController.createShift(sShift, shiftManagerID, cashiersIDs, driversIDs, merchandisersIDs, stockKeepersIDs);
        if (shiftCreated.errorOccurred())
            print(shiftCreated.getErrorMessage());
        else
            print("The " + sShift + " shift was created successfully");
    }

    public void viewDetails() {
        print("Enter the id of the employee");
        String id = getValidId();
        print(serviceController.getDetailsOf(id));
    }


    public void editDetails() {
        print("Enter the id of the employee you want to edit");
        String id = getValidId();
        String detailList = "Choose which detail you want to edit:\n1.name\n2.password\n3.salary\n4.bank account " +
                "information\n5.contract of employment";
        String sChoice = getValidInput(detailList, "You must choose one of the given options",
                (choice) -> (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4")
                        || choice.equals("5") ?
                        new Response() :
                        new Response("Invalid choice")));
        boolean success = false;
        switch (sChoice) {
            case "1": {
                print("Enter the new name of the employee");
                String newName = getValidName();
                success = serviceController.editName(id, newName);
                break;
            }
            case "2": {
                print("Enter the new password of the employee");
                String newPassword = getValidPassword();
                success = serviceController.editPassword(id, newPassword);
                break;
            }
            case "3": {
                print("Enter the new salary of the employee");
                String newSalary = getValidSalary();
                success = serviceController.editSalary(id, newSalary);
                break;
            }
            case "4": {
                print("Enter the new bank account information of the employee");
                String newBankInfo = getValidBankAccount();
                success = serviceController.editBankInfo(id, newBankInfo);
                break;
            }
            case "5": {
                print("Enter the new contract of employment");
                String newContract = getValidContractOfEmployment();
                success = serviceController.editContract(id, newContract);
                break;
            }
        }
        if (!success)
            print("Something went wrong, the update was unsuccessful");
        else
            print("Your choice was updated successfully");
    }

    public void deleteEmployee(){
        print("Enter the id of the employee you want to delete");
        String id = getValidId();
        Response response = serviceController.removeEmployee(id);
        if(response.errorOccurred()){
            print(response.getErrorMessage());
        }
        else {
            print("Employee was deleted successfully");
        }
    }

    public void print(String s){
        System.out.println(s);
    }

    public String getUserInput(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("terminate")){
            System.exit(0);
        }
        if(input.equals("load data")) {
//            serviceController.loadPreMadeData();
            print("Data has loaded\nThe system is now loaded with several employees");
        }
        return input;
    }

    public void certify(){
        print("Enter the details of the employee being certified");
        String id = getValidId();
        String msg = "Enter the type of certification. it must be one the following:\n1.Human resources manager\n2.Shift manager\n3.Cashier\n4.Stock keeper\n5.Driver\n6.Merchandiser\n7.Logistics manager";
        String jobNum = getValidInput(msg , "",serviceController::isValidJobNumber);
        Response r;
        if(jobNum.equals("5")){
            String license = getValidInput("Enter the driver's license", "A valid license must be either C or C1", serviceController::isValidLicense);
            r = serviceController.certifyDriver(id, license);
        }
        else {
            r = serviceController.certifyEmployee(id, jobNum);
        }
        if (r.errorOccurred())
            print(r.getErrorMessage());
        else
            print("Employee is now certified");


    }
}

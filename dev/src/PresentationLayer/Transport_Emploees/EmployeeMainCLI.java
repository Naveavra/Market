package PresentationLayer.Transport_Emploees;

import ServiceLayer.Utility.Response;
import ServiceLayer.EmployeeService;
import java.util.Scanner;
import java.util.function.Function;

public class EmployeeMainCLI extends MainCLI {
    private final EmployeeService serviceController;

    public EmployeeMainCLI(){
        serviceController = new EmployeeService();
    }

    public void start(){
        print(serviceController.start());
        while (true) {
            while (!serviceController.isLoggedIn()){
                login();
            }
            print(serviceController.displayActions());
            String action = getUserInput();
            if (action.equals("goodbye")){
//                serviceController.closeDbConnection();
//                serviceController.handleLogOut(this);
                print("Goodbye");
                break;
            }
            Runnable ans = serviceController.doAction(action, this);
            ans.run();
        }
    }

    public void register(){
        print("Enter the following details of the employee you want to register:");
        String id = getValidId();
        String name = getValidName();
        String password = getValidPassword();
        String sSalary = getValidSalary();
        float salary = Float.parseFloat(sSalary);
        String bankAccount = getValidBankAccount();
        String contractOfEmployment = getValidContractOfEmployment();
        Response response = serviceController.register(id, name, password, salary, bankAccount, contractOfEmployment);
        if (response.errorOccurred()){
            print(response.getErrorMessage());
        }
        else{
            print("You have registered successfully");
        }

    }

    public String getValidInput(String msgToPrint, String msgInCaseOfErr , Function<String, Response> validationFunction){
        print(msgToPrint);
        String input = getUserInput();
        Response response = validationFunction.apply(input);
        while (response.errorOccurred()){
            print(response.getErrorMessage());
            if(!msgInCaseOfErr.equals(""))
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

    public String getValidPassword(){
        String msgToPrint = "Enter password:";
        String msgIfErr = "Enter a valid password:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidPassword);
    }

    public String getValidId(){
        String msgToPrint = "Enter id:";
        String msgIfErr = "Enter a valid id:";
        return getValidInput(msgToPrint, msgIfErr, serviceController::isValidID);
    }

    public boolean login() {
        print("Please log in to the system");
        String id = getValidId();
        String password = getValidPassword();
        Response response = serviceController.login(id, password);
        if(!response.errorOccurred()){
            print("You have logged in successfully");
            return true;
        }
        else{
            print(response.getErrorMessage());
            return false;
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
            //serviceController.loadPreMadeData();
            print("Data has loaded\nThe system is now loaded with several employees");
        }
        return input;
    }
}

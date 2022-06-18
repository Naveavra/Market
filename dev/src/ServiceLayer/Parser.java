package ServiceLayer;

import DomainLayer.Employees.JobType;
import DomainLayer.Employees.Time;
import ServiceLayer.Utility.Response;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
import java.util.*;

public class Parser {

    /**
     *
     * @param id id to be parsed
     * @return true if id is 9 characters long and made entirely of digits
     */
    public Response isValidID(String id) {
        try{
            Integer.parseInt(id);
            if(id.length() == 9){
                return new Response();
            }
            else {
                return new Response("Invalid id. A valid id has 9 digits");
            }
        }
        catch (NumberFormatException e){
            return new Response("All characters of an id must be numbers");
        }

    }

    public Action parseActionChoice(String input, boolean isManger) {
        try{
            int userChoice = Integer.parseInt(input);
            if (userChoice == 1){
                return Action.DISPLAY_SCHEDULE;
            }
            if (userChoice == 2){
                return Action.CHANGE_SCHEDULE;
            }
            if (userChoice == 3){
                return Action.START_SHIFT;
            }
            if (userChoice == 4){
                return Action.END_SHIFT;
            }
            if (userChoice == 5){
                return Action.MID_SHIFT_ACTIONS;
            }
            if (userChoice == 6){
                return Action.LOGOUT;
            }
            if (isManger){
                if (userChoice == 7){
                    return Action.REGISTER_EMPLOYEE;
                }
                if (userChoice == 8){
                    return Action.CERTIFY_EMPLOYEE;
                }
                if (userChoice == 9){
                    return Action.CREATE_SHIFT;
                }
                if (userChoice == 10){
                    return Action.VIEW_EMPLOYEE_DETAILS;
                }
                if (userChoice == 11)
                    return Action.EDIT_EMPLOYEE_DETAILS;
                if (userChoice == 12)
                    return Action.DELETE_EMPLOYEE;
                if (userChoice == 13)
                    return Action.VIEW_SHIFT;
                if (userChoice == 14)
                    return Action.DELETE_SHIFT;
            }
            else{
                return Action.ILLEGAL_ACTION;
            }
        }
        catch (NumberFormatException e){
            return Action.ILLEGAL_ACTION;
        }
        return Action.ILLEGAL_ACTION;
    }

    public Response isValidShiftInput(String input) {
        String[] split = input.split(" ");
        if (split.length != 2)
            return new Response("Invalid input. Please enter the date and time of the shift\n" + "example:19/10/2022 evening");
        String[] nums = split[0].split("/");
        if (nums.length != 3)
            return new Response("Invalid input. Please enter the date and time of the shift\n" + "example:19/10/2022 evening");
        try{
            int day = Integer.parseInt(nums[0]);
            int month = Integer.parseInt(nums[1]);
            int year = Integer.parseInt(nums[2]);
            if(day > 31 || day < 0 || month > 12 || month < 0 || year < 0)
                return new Response("Invalid input. Make sure the date numbers are correct");

        } catch (NumberFormatException e) {
            return new Response("Invalid input. Please enter the date in the format: dd/mm/yyyy");
        }
        String time = split[1].toLowerCase(Locale.ROOT);
        if (!time.equals("morning") && !time.equals("evening")){
            return new Response("Enter a valid shift time");
        }
        return new Response();
    }

    /**
     *
     * @param name the name to be validated
     * @return true if the name has at least two components (first and last name)
     */
    public Response isValidName(String name) {
        String[] split = name.split(" ");
        boolean valid = split.length >= 2;
        if(!valid)
            return new Response("Name must contain at least first and last name");
        return new Response();
    }

    public Response isValidSalary(String sSalary) {
        try{
            float salary = Float.parseFloat(sSalary);
            if (salary <= 0){
                return new Response("Salary must be greater than 0");
            }
            return new Response();
        } catch (NumberFormatException e) {
            return new Response("Salary must contain only numbers");
        }
    }

    public Response isValidBankAccount(String bankAccount) {
        String[] details = bankAccount.split(" ");
        if (details.length != 3){
            return new Response("Bank account details must contain 3 parts: bank name branch number account number\n" + "example:leumi 980 364573");
        }
        try{
            Integer.parseInt(details[1]);
        }
        catch (NumberFormatException e){
            return new Response("Branch number can only contain digits");
        }
        try{
            Integer.parseInt(details[2]);
        }
        catch (NumberFormatException e){
            return new Response("Account number can only contain digits");
        }
        return new Response();
    }

    public ShiftPair getShiftPair(String input){
        String[] split = input.split(" ");
        String[] date = split[0].split("/");
        String day = date[0];
        String month = date[1];
        String year = date[2];
        Time time;
        if (split[1].equals("morning")){
            time = Time.MORNING;
        }
        else{
            time = Time.EVENING;
        }
        return new ShiftPair(new ShiftDate(day, month, year), time);
    }

    public Response isValidJobNumber(String jobNum) {
        if(!(jobNum.equals("1")|| jobNum.equals("2")||jobNum.equals("3")||jobNum.equals("4")
                ||jobNum.equals("5")||jobNum.equals("6") || jobNum.equals("7") || jobNum.equals("8")
                ||jobNum.equals("9"))){
            return new Response("Input does not match any of the valid options");
        }
        return new Response();
    }

    public JobType parseJobType(String jobNum) {
        //HR_MANAGER, SHIFT_MANAGER ,CASHIER, STOCK_KEEPER, DRIVER, MERCHANDISER, LOGISTICS_MANAGER,
        // TRANSPORT_MANAGER, STORE_MANAGER
         switch (jobNum) {
            case "1" : return JobType.HR_MANAGER;
            case "2" : return JobType.SHIFT_MANAGER;
             case "3" : return JobType.CASHIER;
            case "4" : return JobType.STOCK_KEEPER;
            case "5" : return JobType.DRIVER;
            case "6" : return JobType.MERCHANDISER;
            case "7" : return JobType.LOGISTICS_MANAGER;
             case "8" : return JobType.TRANSPORT_MANAGER;
             case "9" : return JobType.STORE_MANAGER;
            default : return null;
        }
    }

    public Response isValidIdList(String input) {
        String[] ids = input.split(",");
        for (String s: ids) {
            if (s.length() != 9)
                return new Response("Invalid id. A valid id has 9 digits");
        }
        return new Response();
    }

    /**
     * @param a start of choice range
     * @param b end of choice range
     * @param sChoice user choice
     * @return non-error response if choice is in [a,b]
     */
    public Response isValidChoice(int a, int b, String sChoice){
        try{
            int choice = Integer.parseInt(sChoice);
            if (choice < a || choice > b)
                return new Response("Your choice must be a number between " + a + " and " + b);
            return new Response();
        }
        catch (NumberFormatException e){
            return new Response("Your must choose a number");
        }
    }

    public Response assertNoIntersection(String[]... arrays){
        Set<String> ids = new HashSet<>();
        for (String[] idArr : arrays){
            ids.retainAll(new HashSet<>(Arrays.asList(idArr)));
        }
        if (!ids.isEmpty())
            return new Response(ids.stream().findAny().get() + " appears twice. Each id must be selected once");
        return new Response();
    }

    public Response isValidLicense(String s) {
        if (!s.toLowerCase(Locale.ROOT).equals("c") && !s.toLowerCase(Locale.ROOT).equals("c1"))
            return new Response("Invalid license");
        return new Response();
    }

    public String displaySubMenu(String action, boolean hrManager) {
        if (hrManager){
            switch (action){
                case "1": return "1.View schedule\n2.Add available time slot\n3.Remove available time slot\n4.Reset schedule";
                case "2": return "1.Start shift\n2.End shift\n3.Add employee to current shift\n4.Remove employee from current shift";
                case "3": return "1.Register employee\n2.Delete employee\n3.Certify employee\n4.Manage employee Details";
                case "4": return "1.Create shift\n2.View shift\n3.Delete shift";
                case "5": return "Logging out...";
                default: return "Invalid choice";
            }
        }
        else{
            switch (action){
                case "1": return "1.View schedule\n2.Add available time slot\n3.Remove available time slot\n4.Reset schedule";
                case "2": return "1.Start shift\n2.End shift\n3.Add employee to current shift\n4.Remove employee from current shift";
                case "3": return "Logging out...";
                default: return "Invalid choice";
            }
        }
    }

    public Action parseManageSchedule(String choice) {
        if (choice.equals("0")){
            return Action.GO_BACK;
        }
        else if (choice.equals("1")){
            return Action.DISPLAY_SCHEDULE;
        }
        else if (choice.equals("2")){
            return Action.CHANGE_SCHEDULE;
        }
        else {
            return Action.ILLEGAL_ACTION;
        }
    }

    public Action parseManageEmployees(String choice) {
        if (choice.equals("0")){
            return Action.GO_BACK;
        }
        else if (choice.equals("1")){
            return Action.REGISTER_EMPLOYEE;
        }
        else if (choice.equals("2")){
            return Action.DELETE_EMPLOYEE;
        }
        else if (choice.equals("3")){
            return Action.CERTIFY_EMPLOYEE;
        }
        else if (choice.equals("4")){
            return Action.MANAGE_DETAILS;
        }
        else{
            return Action.ILLEGAL_ACTION;
        }
    }

    public Action parseManageDetails(String choice) {
        if (choice.equals("0")){
            return Action.GO_BACK;
        }
        else if (choice.equals("1")){
            return Action.VIEW_EMPLOYEE_DETAILS;
        }
        else if (choice.equals("2")){
            return Action.EDIT_EMPLOYEE_DETAILS;
        }
        else{
            return Action.ILLEGAL_ACTION;
        }
    }

    public Action parseManageShifts(String choice) {
        if (choice.equals("0")){
            return Action.GO_BACK;
        }
        else if (choice.equals("1")){
            return Action.CREATE_SHIFT;
        }
        else if (choice.equals("2")){
            return Action.VIEW_SHIFT;
        }
        else if (choice.equals("3")){
            return Action.DELETE_SHIFT;
        }
        else{
            return Action.ILLEGAL_ACTION;
        }
    }
}

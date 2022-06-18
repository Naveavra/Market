package ServiceLayer;

public class HRManagerService extends EmployeeServiceGeneric {
    public HRManagerService(String id) {
        super(id);
    }

    @Override
    public String displayActions() {
        /*return super.displayActions() + "\n7. Register a new employee\n8. Certify employee to a role\n9. Create shift"+
                "\n10. View employee's details\n11. Edit employee details\n12. Delete employee\n" +
                "13. View shift\n14. Delete shift";*/
        return "0.Exit\n1.Manage availability schedule\n2.Manage current shift\n3.Manage employees\n" +
                "4.Manage shifts\n5.Logout";
    }
}

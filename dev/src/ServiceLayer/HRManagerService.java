package ServiceLayer;

public class HRManagerService extends EmployeeService{
    public HRManagerService(String id) {
        super(id);
    }

    @Override
    public String displayActions() {
        return super.displayActions() + "\n7. Register a new employee\n8. Certify employee to a role\n9. Create shift"+
                "\n10. View employee's details\n11. Edit employee details\n12. Delete employee\n";
    }
}

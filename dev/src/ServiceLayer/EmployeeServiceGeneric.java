package ServiceLayer;
public class EmployeeServiceGeneric {
    private String id;

    public EmployeeServiceGeneric(String id){
        this.id = id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String displayActions() {
//        return "Choose an action:\n1. See my availability schedule\n2. Change my availability schedule\n3. Start" +
//                " shift\n4. End shift\n5. Mid-shift actions\n6. Logout";
        return "0.Exit\n1.Manage availability schedule\n2.Manage current shift\n3.Logout";
    }

    public String getID() {
        return id;
    }
}

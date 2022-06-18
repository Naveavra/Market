package DAL;


//import SharedSpace.DBConnector;
import DomainLayer.Transport.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DriverDAO {
    private Connect conn = Connect.getInstance();
    private static HashMap<String, Driver> identityMap = new HashMap<>();
//    private final static DriverDAO INSTANCE = new DriverDAO();
//    public static DriverDAO getInstance(){
//        return INSTANCE;
//    }

//    public void setAvailability(Driver driver, String date, String time,boolean param) {
//        String driverID = driver.getId();
//        String avail;
//        if(param){
//            avail = "#t";
//        }
//        else{avail = "#f";}
//        String query = "UPDATE DriverAvailability SET available = "+"'"+avail+"'"+ " WHERE id = "+"'"+driverID+"'"+" AND date = "+"'"+date+"'"+" AND time = "+"'"+time+"'";
//        try {
//            conn.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    //FROM HERE

    public void setAvailability(Driver driver, String date, String time,boolean param) {
        String driverID = driver.getId();
        String avail;
        if(param){
            avail = "#t";
        }
        else{avail = "#f";}
        String query = "INSERT INTO DriverAvailability(id,date,time,available) VALUES(?,?,?,?)";
        String query2 = "DELETE FROM DriverAvailability WHERE id = ? AND date = ? AND time = ?";
        try {
            conn.executeUpdate(query2,driverID,date,time);
            conn.executeUpdate(query,driverID,date,time,avail);
        } catch (SQLException e) {

        }

    }

    public boolean getAvailability(String id,String date,String time){
        if(containsDriver(id)){
            String query = "SELECT available FROM DriverAvailability WHERE id = ? AND date = ? AND time = ?";
            try {
                List<HashMap<String, Object>> rs = conn.executeQuery(query,id,date,time);
                if(Objects.equals((String)rs.get(0).get("available"), "#t")){
                    return true;
                }
                else {return false;}
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    public void RemoveFromIdentityMap(String id){
        if (identityMap.containsKey(id)){
            identityMap.remove(id);
        }
    }

    public Driver getDriver(String driverID){
        if(identityMap.containsKey(driverID)){
            return identityMap.get(driverID);
        }
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery("SELECT * FROM Drivers WHERE id = "+"'"+driverID+"'");
            String name = (String)rs.get(0).get("name");
            String id = (String)rs.get(0).get("id");
            String license = (String)rs.get(0).get("license");
            Driver d = new Driver(name,id,license);
            identityMap.put(id,d);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Driver> getDrivers(String date,String time){
        ArrayList<Driver> rval = new ArrayList<>();
        List<HashMap<String, Object>> rs;
        try {
            rs = conn.executeQuery("Select * From DriverAvailability WHERE date = "
                    +"'"+date+"'"+ " AND time = "+"'"+time+"'");
            for (int i = 0 ; i< rs.size(); i++){
                if(Objects.equals((String)rs.get(i).get("available"), "#t")) {
                    rval.add(getDriver((String)rs.get(i).get("id")));
                }
            }

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            System.out.println("Cannot connect to the DB");
        }
        return rval;
    }

    public boolean containsDriver(String driverID){
        try {
            return identityMap.containsKey(driverID) || conn.executeQuery("SELECT id FROM Drivers WHERE id = " + "'"+driverID+"'").size() > 0;
        } catch (Exception e) {
            return false;
        }
    }


    public void addDriver(String name,String id, String license) {
        String query = "INSERT OR IGNORE INTO Drivers(name,id,license) VALUES(?,?,?)";
        try {
            conn.executeUpdate(query,name,id,license);
//            addAvailability(d.getId());
        }catch (SQLException se){
//            System.out.println("Cannot Insert a Driver,Something is wrong with the db");
        }
    }
    public void addDriver(Driver d) {
        String query = "INSERT OR IGNORE INTO Drivers(name,id,license) VALUES(?,?,?)";
        try {
            conn.executeUpdate(query,d.getName(),d.getId(),d.getLicense());
//            addAvailability(d.getId());
        }catch (SQLException se){
//            System.out.println("Cannot Insert a Driver,Something is wrong with the db");
        }
    }

    public boolean removeDriver(String id) {
        try {
            if(Objects.equals(conn.deleteRecordFromTableSTR("Drivers", "id", id), "Success")){
                identityMap.remove(id);
                return true;
            }
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}

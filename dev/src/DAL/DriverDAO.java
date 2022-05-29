package DAL;


//import SharedSpace.DBConnector;
import DomainLayer.Transport.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class DriverDAO {
    private Connect conn = Connect.getInstance();
    private final static DriverDAO INSTANCE = new DriverDAO();
    private HashMap<String, Driver> identityMap = new HashMap<>();
    public static DriverDAO getInstance(){
        return INSTANCE;
    }

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
            e.printStackTrace();
        }

    }

    public boolean getAvailability(String id,String date,String time){
        if(containsDriver(id)){
            String query = "SELECT available FROM DriverAvailability WHERE id = ? AND date = ? AND time = ?";
            try {
                ResultSet rs = conn.executeQuery(query,id,date,time);
                if(Objects.equals(rs.getString("available"), "#t")){
                    return true;
                }
                else {return false;}
            } catch (SQLException e) {
//                e.printStackTrace();
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
            ResultSet rs = conn.executeQuery("SELECT * FROM Drivers WHERE id = "+"'"+driverID+"'");
            String name = rs.getString("name");
            String id = rs.getString("id");
            String license = rs.getString("license");
            Driver d = new Driver(name,id,license);
            identityMap.put(id,d);
            return d;
        } catch (SQLException e) {
//            System.out.println("Unable to execute query getDriver");
        }
        return null;
    }

    public ArrayList<Driver> getDrivers(String date,String time){
        ArrayList<Driver> rval = new ArrayList<>();
        ResultSet rs;
        try {
            rs = conn.executeQuery("Select * From DriverAvailability WHERE date = "
                    +"'"+date+"'"+ " AND time = "+"'"+time+"'");
            while(rs.next()){
                if(Objects.equals(rs.getString("available"), "#t")) {
                    rval.add(getDriver(rs.getString("id")));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Cannot connect to the DB");
        }
        return rval;
    }

    public boolean containsDriver(String driverID){
        try {
            return identityMap.containsKey(driverID) || conn.executeQuery("SELECT id FROM Drivers WHERE id = " + "'"+driverID+"'").next();
        } catch (SQLException e) {
            return false;
        }
    }
    public void addAvailability(String driverID){
        String query = "INSERT INTO DriverAvailability(id,date,time,available) VALUES(?,?,?,?)";
        try {
            int day=19;
            int month=5;
            String year = "/2022";
            String res;
            for(int i = 0; i<30;i++){
                if(day==30){
                    day = 1;
                    month++;
                }
                if(day>10){
                    res = String.valueOf(day)+"/0"+String.valueOf(month)+year;
                    conn.executeUpdate(query, driverID,res, "MORNING","#t");
                    conn.executeUpdate(query, driverID, res, "EVENING", "#t");
                }
                else{
                    res = "0"+String.valueOf(day)+"/0"+String.valueOf(month)+year;
                    conn.executeUpdate(query, driverID,res, "MORNING","#t");
                    conn.executeUpdate(query, driverID, res, "EVENING", "#t");
                }
                day++;
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void addDriver(String name,String id, String license) {
        String query = "INSERT OR IGNORE INTO Drivers(name,id,license) VALUES(?,?,?)";
        try {
            conn.executeUpdate(query,name,id,license);
//            addAvailability(d.getId());
        }catch (SQLException se){
            System.out.println("Cannot Insert a Driver,Something is wrong with the db");
        }
    }
    public void addDriver(Driver d) {
        String query = "INSERT OR IGNORE INTO Drivers(name,id,license) VALUES(?,?,?)";
        try {
            conn.executeUpdate(query,d.getName(),d.getId(),d.getLicense());
            addAvailability(d.getId());
        }catch (SQLException se){
            System.out.println("Cannot Insert a Driver,Something is wrong with the db");
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
        } catch (SQLException e) {
            return false;
        }
    }
}

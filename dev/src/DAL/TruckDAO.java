package DAL;


import DomainLayer.Transport.Truck;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TruckDAO {
    private Connect conn = Connect.getInstance();
    private HashMap<String, Truck> identityMap = new HashMap<>();
    private final static TruckDAO INSTANCE = new TruckDAO();
    public static TruckDAO getInstance(){
        return INSTANCE;
    }

    public String addTruck(String type, String licensePlate, double maxWeight, double initialWeight){
        String query = "INSERT INTO Trucks(licensePlate,type, maxWeight,initialWeight) VALUES(?,?,?,?)";
        try {
            conn.executeUpdate(query,licensePlate,type,maxWeight,initialWeight);
            addAvailability(licensePlate);
            return "Success";
        }catch (SQLException se){
            System.out.println("Cannot Insert a Truck,Something is wrong with the db");
        }
        return "Failed to add truck";
    }

    public void setAvailability(Truck truck, String  date, String time, boolean param){
        String licenseplate = truck.getLicensePlate();
        String avail;
        if(param){
            avail = "#t";
        }
        else {avail = "#f";}
//        String query = "UPDATE TrucksAvailability SET available = "+"'"+avail+"'"+ " WHERE licenseplate = "+"'"+licenseplate+"'"+" AND date = "+"'"+date+"'"+" AND time = "+"'"+time+"'";
        String query = "UPDATE TrucksAvailability SET available = ? WHERE licenseplate = ? AND date = ? AND time = ?";
        try {
            conn.executeUpdate(query,avail,licenseplate,date,time);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getAvailability(String id,String date){
        if(containsTruck(id)){
            String query = "SELECT available FROM TrucksAvailability WHERE licenseplate = ? AND date = ?";
            try {
                ResultSet rs = conn.executeQuery(query,id,date);
                if(Objects.equals(rs.getString("available"), "#t")){
                    return true;
                }
                else {return false;}
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public String removeTruck(String licensePlate){
        try {
            if(Objects.equals(conn.deleteRecordFromTableSTR("Trucks", "licenseplate", licensePlate), "Success")){
                identityMap.remove(licensePlate);
                if (RemoveTruckAvailability(licensePlate)){
                    return "Success";
                }
                else {
                    return "Couldn't remove from trucks availability";
                }
            }
            else
                return "Failure to remove Truck";
        } catch (SQLException e) {
            return "Couldn't remove from trucks availability";
        }
    }

    public boolean RemoveTruckAvailability(String licensePlate){
        try {
            return Objects.equals(conn.deleteRecordFromTableSTR("TrucksAvailability", "licenseplate", licensePlate), "Success");
        } catch (SQLException e) {
            return false;
        }
    }

    public void addAvailability(String licenseplate){
        String query = "INSERT INTO TrucksAvailability(licenseplate,date,time,available) VALUES(?,?,?,?)";
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
                    conn.executeUpdate(query, licenseplate,res, "MORNING","#t");
                    conn.executeUpdate(query, licenseplate, res, "EVENING", "#t");
                }
                else{
                    res = "0"+String.valueOf(day)+"/0"+String.valueOf(month)+year;
                    conn.executeUpdate(query, licenseplate,res, "MORNING","#t");
                    conn.executeUpdate(query, licenseplate, res, "EVENING", "#t");
                }
                day++;
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        }

    public String updateTruckAvailability(String licensePlate,String date, boolean available){
        throw new NotImplementedException();
    }
    public double getMaxWeight(String licensePlate){
        throw new NotImplementedException();
    }
    public Truck getTruck(String licensePlate){
        if(identityMap.containsKey(licensePlate)){
            return identityMap.get(licensePlate);
        }
        try {
            ResultSet rs = conn.executeQuery("SELECT * FROM Trucks Where licensePlate = "+"'"+licensePlate+"'");
            String type = rs.getString("type");
            String license = rs.getString("licensePlate");
            double maxWeight = rs.getDouble("maxWeight");
            double initialWeight = rs.getDouble("initialWeight");
            Truck t = new Truck(type,license,maxWeight,initialWeight);
            identityMap.put(license,t);
            return t;

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            System.out.println("Unable to execute query getTruck");
        }
        return null;
    }
    public ArrayList<Truck> getTrucks(String date, String time, String licenseType){
        ArrayList<Truck> rval = new ArrayList<>();
        ResultSet rs1;
        ResultSet rs2;
        try {
            rs1 = conn.executeQuery("SELECT licenseplate from TrucksAvailability WHERE date = "+ "'"+date+"'"+" AND time = "+"'"+time+"'"+" AND available = #f");
            rs2 = conn.executeQuery("SELECT licensePlate From TRUCKS");
            ArrayList<String> temp = new ArrayList<>();
            while(rs1.next()){
                temp.add(rs1.getString("licensePlate"));
            }
            while(rs2.next()){
                if(!temp.contains(rs2.getString("licensePlate"))) {
                    rval.add(getTruck(rs2.getString("licensePlate")));
                }
            }

        } catch (SQLException e) {
            System.out.println("Cannot connect to the DB");
        }
        return rval;
    }
    public String showTrucks(String date,String type){
        throw new NotImplementedException();
    }
    public boolean containsTruck(String licensePlate){
        try {
            return identityMap.containsKey(licensePlate) || conn.executeQuery("SELECT licensePlate FROM Trucks WHERE licensePlate = " + "'"+licensePlate+"'").next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void RemoveFromIdentityMap(String licenseplate) {
        identityMap.remove(licenseplate);
    }
}

package DAL;


import DomainLayer.Transport.Truck;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TruckDAO {
    private Connect conn = Connect.getInstance();
    private static HashMap<String, Truck> identityMap = new HashMap<>();
//    private final static TruckDAO INSTANCE = new TruckDAO();
//    public static TruckDAO getInstance(){
//        return INSTANCE;
//    }

    public String addTruck(String type, String licensePlate, double maxWeight, double initialWeight){
        String query = "INSERT INTO Trucks(licensePlate,type, maxWeight,initialWeight) VALUES(?,?,?,?)";
        try {
            conn.executeUpdate(query,licensePlate,type,maxWeight,initialWeight);
            if (addAvailability(licensePlate))
                return "Success";
        }catch (SQLException se){
            return "Failed to add truck";
        }
        return "Failed to add truck";
    }

    public boolean setAvailability(Truck truck, String  date, String time, boolean param){
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
            return false;
        }
        return true;
    }
    public boolean getAvailability(String id,String date){
        if(containsTruck(id)){
            String query = "SELECT available FROM TrucksAvailability WHERE licenseplate = ? AND date = ?";
            try {
                List<HashMap<String, Object>> rs = conn.executeQuery(query,id,date);
                if((rs.size() == 0) ||  Objects.equals((String)rs.get(0).get("available"), "#t")){
                    return true;
                }
                else {
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("problem with getAvailability");
                return false;
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

    public boolean addAvailability(String licenseplate){
        String query = "INSERT INTO TrucksAvailability(licenseplate,date,time,available) VALUES(?,?,?,?)";
        try {
            int day=1;
            int month=6;
            String year = "/2022";
            String res;
            for(int i = 0; i<30;i++){
                if(day==30){
                    day = 1;
                    month++;
                }
                if(day>=10){
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
            return false;
        }
        return true;
    }
//
//    public String updateTruckAvailability(String licensePlate,String date, boolean available){
//        throw new NotImplementedException();
//    }
//    public double getMaxWeight(String licensePlate){
//        throw new NotImplementedException();
//    }

    public Truck getTruck(String licensePlate){
        if(identityMap.containsKey(licensePlate)){
            return identityMap.get(licensePlate);
        }
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery("SELECT * FROM Trucks Where licensePlate = "+"'"+licensePlate+"'");
            String type = (String)rs.get(0).get("type");
            String license = (String)rs.get(0).get("licensePlate");
            double maxWeight = Double.parseDouble(String.valueOf(rs.get(0).get("maxWeight")));
//            double maxWeight = (double)rs.get(0).get("maxWeight");
            double initialWeight = Double.parseDouble(String.valueOf(rs.get(0).get("initialWeight")));
            Truck t = new Truck(type,license,maxWeight,initialWeight);
            identityMap.put(license,t);
            return t;

        } catch (SQLException e) {
            return null;
        }
    }
    public ArrayList<Truck> getTrucks(String date, String time, String licenseType){
        ArrayList<Truck> rval = new ArrayList<>();
        List<HashMap<String, Object>> rs1;
        List<HashMap<String, Object>> rs2;
        try {
            rs1 = conn.executeQuery("SELECT licenseplate from TrucksAvailability WHERE date = "+ "'"+date+"'"+" AND time = "+"'"+time+"'"+" AND available = #f");
            rs2 = conn.executeQuery("SELECT licensePlate From TRUCKS WHERE type = "+ "'"+licenseType+"'");
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < rs1.size(); i++){
                temp.add((String)rs1.get(i).get("licensePlate"));
            }
            for (int i = 0; i < rs2.size(); i++){
                if(!temp.contains((String)rs2.get(i).get("licensePlate"))) {
                    rval.add(getTruck((String)rs2.get(i).get("licensePlate")));
                }
            }

        } catch (SQLException e) {
            return null;
        }
        return rval;
    }
//    public String showTrucks(String date,String type){
//        throw new NotImplementedException();
//    }
    public boolean containsTruck(String licensePlate){
        try {
            return identityMap.containsKey(licensePlate) ||
                    conn.executeQuery("SELECT licensePlate FROM Trucks WHERE licensePlate = " + "'"+licensePlate+"'").size() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void RemoveFromIdentityMap(String licenseplate) {
        identityMap.remove(licenseplate);
    }
}

package DAL;


//import SharedSpace.DBConnector;
import DomainLayer.Transport.DriverDoc;
import DomainLayer.Transport.OrderDoc;
import DomainLayer.Transport.Supply;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DriverDocDAO {
    private Connect conn = Connect.getInstance();
    private final static DriverDocDAO INSTANCE = new DriverDocDAO();
    private HashMap<Integer, DriverDoc> identityMap = new HashMap<>();
    public static DriverDocDAO getInstance(){
        return INSTANCE;
    }
    public String addDriverDocs(ArrayList<DriverDoc> docs){
        throw new NotImplementedException();
    }
    public String removeDriverDoc(String dDocID){
        throw new NotImplementedException();
    }
    public ArrayList<DriverDoc> showDriverDocs(String oDocID){
        ArrayList<DriverDoc> docs = new ArrayList<>();
        String query = "SELECT * FROM DriverDocs WHERE orderDocID = "+"'"+oDocID+"'";
        try {
            ResultSet rs = conn.executeQuery(query);
            while(rs.next()){
                if(identityMap.containsKey(rs.getInt("id"))){
                    docs.add(identityMap.get(rs.getInt("id")));
                    continue;
                }
                String query2 ="SELECT supply,quantity FROM order4Dest WHERE orderDocID = "+"'"+oDocID+"'"+" AND siteID = "+ "'"+rs.getString("siteID")+"'";
                ResultSet rs2 = conn.executeQuery(query2);
                ConcurrentHashMap<Supply,Integer> orders = new ConcurrentHashMap<>();
                while (rs2.next()){
                    orders.put(SuppliesDAO.getInstance().getSupply(rs2.getString("supply")),rs2.getInt("quantity"));
                }
                docs.add(new DriverDoc(DriverDAO.getInstance().getDriver(rs.getString("driverID")),rs.getInt("id"),orders,
                        SiteDAO.getInstance().getSite(rs.getString("siteID")),rs.getString("orderDocID")));
            }
        } catch (SQLException e) {
//            System.out.println("Unable to show driverDocs,Something went wrong in the DB");
        }
        return docs;
    }
    public String removeDriverDocbyODOC(String oDocID,String storeID){
        String query = "SELECT id FROM DriverDocs WHERE orderDocID = "+"'"+oDocID+"'"+" AND siteID = "+"'"+storeID+"'";
        try {
            ResultSet rs = conn.executeQuery(query);
            conn.deleteRecordFromTableSTR("DriverDocs","id",String.valueOf(rs.getInt("id")));
            if(identityMap.containsKey(rs.getInt("id"))){
                identityMap.remove(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Unable to remove driverdoc");
            return "Failed to remove driverdoc";
        }
        return "Success";
    }
    public String updateDriverDoc(String oDocID,String storeID, ConcurrentHashMap<Supply,Integer> supplies){
        throw new NotImplementedException(); //TODO DELETE IF NOT NEEDED
    }

    public String addDriverDoc(DriverDoc newDoc) {
        String query = "INSERT INTO DriverDocs(id,siteID,driverID,orderDocID) VALUES(?,?,?,?)";
        try {
            conn.executeUpdate(query,newDoc.getID(),newDoc.getStore().getId(),newDoc.getDriverID(),newDoc.getOrderDocID());
            return "Success";
        } catch (SQLException e) {
            System.out.println("Unable to execute query addDriverDoc");
            return "Fail";
        }
    }
    public String addDriverDoc(int id,String driverID ,String docID,String storeID){
        String query = "INSERT INTO DriverDocs(id,siteID,driverID,orderDocID) VALUES(?,?,?,?)";
        try {
            conn.executeUpdate(query,id,storeID,driverID,docID);
            return "Success";
        } catch (SQLException e) {
            System.out.println("Unable to execute query addDriverDoc");
            return "Fail";
        }
    }
}

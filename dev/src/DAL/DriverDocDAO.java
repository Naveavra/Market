package DAL;


//import SharedSpace.DBConnector;
import DomainLayer.Transport.DriverDocument;
import DomainLayer.Transport.Supply;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DriverDocDAO {
    private Connect conn = Connect.getInstance();
    private static HashMap<Integer, DriverDocument> identityMap = new HashMap<>();
    private SiteDAO sitesDAO = new SiteDAO();
    private SuppliesDAO suppliesDAO = new SuppliesDAO();
    private DriverDAO driverDAO = new DriverDAO();

//    private final static DriverDocDAO INSTANCE = new DriverDocDAO();
//    public static DriverDocDAO getInstance(){
//        return INSTANCE;
//    }
//    public String addDriverDocs(ArrayList<DriverDocument> docs){
//        throw new NotImplementedException();
//    }
//    public String removeDriverDoc(String dDocID){
//        throw new NotImplementedException();
//    }


    //FROM HERE
    public ArrayList<DriverDocument> showDriverDocs(String oDocID){
        ArrayList<DriverDocument> docs = new ArrayList<>();
        String query = "SELECT * FROM DriverDocs WHERE orderDocID = "+"'"+oDocID+"'";
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery(query);
            for (int i = 0; i < rs.size(); i++){
                if(identityMap.containsKey((int)rs.get(i).get("id"))){
                    docs.add(identityMap.get((int)rs.get(i).get("id")));
                    continue;
                }
                String query2 ="SELECT supply,quantity FROM order4Dest WHERE orderDocID = "+"'"+oDocID+"'"+" AND siteID = "+ "'"+rs.get(i).get("siteID")+"'";
                List<HashMap<String, Object>> rs2 = conn.executeQuery(query2);
                ConcurrentHashMap<Supply,Integer> orders = new ConcurrentHashMap<>();
                for (int j = 0; j < rs.size(); j++){

                    orders.put(suppliesDAO.getSupply((String)rs2.get(j).get("supply")),(int)rs2.get(j).get("quantity"));
                }
                docs.add(new DriverDocument(driverDAO.getDriver((String)rs.get(i).get("driverID")),(int)rs.get(i).get("id"),orders,
                        sitesDAO.getSite((String)rs.get(i).get("siteID")),(String)rs.get(i).get("orderDocID")));
            }
        } catch (SQLException e) {
//            System.out.println("Unable to show driverDocs,Something went wrong in the DB");
        }
        return docs;
    }
    public String removeDriverDocByODOC(String oDocID, String storeID){
        String query = "SELECT id FROM DriverDocs WHERE orderDocID = "+"'"+oDocID+"'"+" AND siteID = "+"'"+storeID+"'";
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery(query);
            conn.deleteRecordFromTableSTR("DriverDocs","id",String.valueOf(rs.get(0).get("id")));
            if(identityMap.containsKey((int)rs.get(0).get("id"))){
                identityMap.remove((int)rs.get(0).get("id"));
            }
        } catch (SQLException e) {
//            System.out.println("Unable to remove driverdoc");
            return "Failed to remove driverdoc";
        }
        return "Success";
    }
//    public String updateDriverDoc(String oDocID,String storeID, ConcurrentHashMap<Supply,Integer> supplies){
//        throw new NotImplementedException(); //TODO DELETE IF NOT NEEDED
//    }

    public String addDriverDoc(DriverDocument newDoc) {
        String query = "INSERT INTO DriverDocs(id,siteID,driverID,orderDocID) VALUES(?,?,?,?)";
        try {
            conn.executeUpdate(query,newDoc.getID(),newDoc.getStore().getId(),newDoc.getDriverID(),newDoc.getOrderDocID());
            return "Success";
        } catch (SQLException e) {
//            System.out.println("Unable to execute query addDriverDoc");
            return "Fail";
        }
    }
    public String addDriverDoc(int id,String driverID ,String docID,String storeID){
        String query = "INSERT INTO DriverDocs(id,siteID,driverID,orderDocID) VALUES(?,?,?,?)";
        try {
            conn.executeUpdate(query,id,storeID,driverID,docID);
            return "Success";
        } catch (SQLException e) {
//            System.out.println("Unable to execute query addDriverDoc");
            return "Fail";
        }
    }
}

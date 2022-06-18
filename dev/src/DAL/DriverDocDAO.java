package DAL;


//import SharedSpace.DBConnector;
import DomainLayer.Transport.DriverDocument;
import DomainLayer.Transport.Supply;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DriverDocDAO {
    private Connect conn = Connect.getInstance();
    private static HashMap<Integer, DriverDocument> identityMap = new HashMap<>();
    private StoreDAO sitesDAO = new StoreDAO();
//    private ProductDAO suppliesDAO = new ProductDAO();
    private DriverDAO driverDAO = new DriverDAO();
    private ProductDAO productDAO = new ProductDAO();

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
                    docs.add(identityMap.get(Integer.parseInt((String)rs.get(i).get("id"))));
                    continue;
                }
                String query2 ="SELECT supply,quantity FROM order4Dest WHERE orderDocID = "+"'"+oDocID+"'"+" AND siteID = "+ "'"+rs.get(i).get("siteID")+"'";
                List<HashMap<String, Object>> rs2 = conn.executeQuery(query2);
                ConcurrentHashMap<String,Integer> orders = new ConcurrentHashMap<>();
                for (int j = 0; j < rs2.size(); j++){
                    orders.put(String.valueOf(productDAO.get(Integer.parseInt((String) rs2.get(j).get("supply"))).getId()),(int)rs2.get(j).get("quantity"));
                }
                DriverDocument d = new DriverDocument(driverDAO.getDriver((String)rs.get(i).get("driverID")),(int)rs.get(i).get("id"),orders,sitesDAO.getSite((String)rs.get(i).get("siteID")),(String)rs.get(i).get("orderDocID"));
                docs.add(d);
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
            int key = (int)rs.get(0).get("id");
            if(identityMap.containsKey(key)){
                identityMap.remove(Integer.parseInt((String)rs.get(0).get("id")));
            }
        } catch (SQLException e) {
            System.out.println("Unable to remove driverdoc");
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


    public HashMap<Integer, Integer> getProductsFromOrderDoc(int orderDocId) throws SQLException {
        String query = "SELECT supply, quantity FROM order4Dest WHERE " +
                String.format("orderDocID=\"%s\"", orderDocId);
        HashMap<Integer, Integer> ans =new HashMap<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while(!rs.isClosed()) {
                ans.put(Integer.parseInt(rs.getString("supply")), rs.getInt("quantity"));
                rs.next();
            }
        } catch (SQLException ignored) {
        } finally {
        conn.closeConnect();
    }
        return ans;
    }

    public int getLastId() {
        String query = "SELECT(*) as count FROM driverDocs";
        String ans = "";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while(!rs.isClosed()){
//                ans = Integer.parseInt(rs.getString("id"));
                ans = rs.getString("id");
                rs.next();
            }
        }
        catch (Exception e){
            return 1;
        }
        finally {
            try {
                conn.closeConnect();
            } catch (SQLException ignored) {
            }
        }
        if (ans.isEmpty()){
            return 1;
        }
        return Integer.parseInt(ans) + 1;
    }
}

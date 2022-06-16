package DAL;


import DomainLayer.Transport.Store;
import DomainLayer.Transport.Contact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StoreDAO {
    private Connect conn = Connect.getInstance();
    private static HashMap<String, Store> identityMap = new HashMap<>();
//    private final static SiteDAO INSTANCE = new SiteDAO();
//    public static SiteDAO getInstance(){
//        return INSTANCE;
//    }

    //FROM HERE
    public String addSite(String id,int area,int type,String contactAddr, String contactName,String contactPNumber){
        String query = "INSERT OR IGNORE INTO Stores(id,type,ShippingArea,contactPNumber,contactName,contactAddress) VALUES(?,?,?,?,?,?)";
        try {
            conn.executeUpdate(query,id,type,area,contactPNumber,contactName,contactAddr);
            return "Success";
        }catch (SQLException se) {
        }
        return "Failed to add Site";
    }
    public String removeSite(String id){
        try {
            if(Objects.equals(conn.deleteRecordFromTableSTR("Stores", "id", id), "Success")){
                identityMap.remove(id);
                return "Success";
            }
            else
                return "Failure to remove Site";
        } catch (SQLException e) {
            return "Failure to remove Site";
        }
    }
    //get id's of sites in area
    public ArrayList<String> showSites(int areaCode, int type){
        String query = "SELECT * FROM Stores WHERE shippingArea = ? AND type = ?";
        ArrayList<String> retVal = new ArrayList<>();
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery(query,areaCode,type);
            for(int i = 0; i < rs.size(); i++){
                retVal.add((String)rs.get(i).get("id"));
            }
            return retVal;
        } catch (Exception e) {
        }
        return null;
    }
    public Store getSite(String id){
        if(identityMap.containsKey(id)){
            return identityMap.get(id);
        }
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery("SELECT * FROM Stores Where id = "+"'"+id+"'");
            String siteid = (String)rs.get(0).get("id");
            int type = (int)rs.get(0).get("type");
            Store.ShippingArea sArea;
            int shippingArea = (int)rs.get(0).get("shippingArea");
            if(shippingArea == 0){sArea = Store.ShippingArea.North;}
            else if(shippingArea == 1){ sArea=Store.ShippingArea.Center;}
            else{sArea=Store.ShippingArea.South;};
            String contactPNumber = (String)rs.get(0).get("contactPNumber");
            String contactName = (String)rs.get(0).get("contactName");
            String contactAddress = (String)rs.get(0).get("contactAddress");
            Store s = new Store(id,new Contact(contactAddress,contactName,contactPNumber),sArea,type);
            identityMap.put(id,s);
            return s;
//id,type,ShippingArea,contactPNumber,contactName,contactAddress
        } catch (SQLException e) {
            //System.out.println("Unable to execute query getSite");
        }finally {
//            conn.startConnection();
        }
        return null;     }

    public boolean contains(String id) {
        try {
            return identityMap.containsKey(id) || (Objects.equals((String)conn.executeQuery("SELECT id FROM Stores WHERE id = " + "'" + id + "'").get(0).get("id"), id));
        } catch (Exception e) {
            return false;
        }  finally {
//            conn.startConnection();
        }
    }
}

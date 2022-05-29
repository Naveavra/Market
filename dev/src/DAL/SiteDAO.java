package DAL;


import DomainLayer.Transport.Site;
import DomainLayer.Transport.Contact;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SiteDAO {
    private Connect conn = Connect.getInstance();
    private HashMap<String, Site> identityMap = new HashMap<>();
    private final static SiteDAO INSTANCE = new SiteDAO();
    public static SiteDAO getInstance(){
        return INSTANCE;
    }
    public String addSite(String id,int area,int type,String contactAddr, String contactName,String contactPNumber){
        String query = "INSERT INTO Sites(id,type,ShippingArea,contactPNumber,contactName,contactAddress) VALUES(?,?,?,?,?,?)";
        try {
            conn.executeUpdate(query,id,type,area,contactPNumber,contactName,contactAddr);
            return "Success";
        }catch (SQLException se){
            System.out.println(se.getMessage());
            System.out.println("Cannot Insert a Site,Something is wrong with the db");
        }finally {
//            conn.startConnection();
        }
        return "Failed to add Site";
    }
    public String removeSite(String id){
        try {
            if(Objects.equals(conn.deleteRecordFromTableSTR("Sites", "id", id), "Success")){
                identityMap.remove(id);
                return "Success";
            }
            else
                return "Failure to remove Site";
        } catch (SQLException e) {
            return "Failure to remove Site";
        }
    }
    public ArrayList<String> showSites(int areaCode, int type){
        String query = "SELECT * FROM Sites WHERE shippingArea = ? AND type = ?";
        ArrayList<String> retVal = new ArrayList<>();
        try {
            ResultSet rs = conn.executeQuery(query,areaCode,type);
            while(rs.next()){
                retVal.add(rs.getString("id"));
            }
            return retVal;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Unable to fetch sites");
        }finally {
//            conn.startConnection();
        }
        return null;
    }
    public Site getSite(String id){
        if(identityMap.containsKey(id)){
            return identityMap.get(id);
        }
        try {
            ResultSet rs = conn.executeQuery("SELECT * FROM Sites Where id = "+"'"+id+"'");
            String siteid = rs.getString("id");
            int type = rs.getInt("type");
            Site.ShippingArea sArea;
            int shippingArea = rs.getInt("ShippingArea");
            if(shippingArea == 0){sArea = Site.ShippingArea.North;}
            else if(shippingArea == 1){ sArea=Site.ShippingArea.Center;}
            else{sArea=Site.ShippingArea.South;};
            String contactPNumber = rs.getString("contactPNumber");
            String contactName = rs.getString("contactName");
            String contactAddress = rs.getString("contactAddress");
            Site s = new Site(id,new Contact(contactAddress,contactName,contactPNumber),sArea,type);
            identityMap.put(id,s);
            return s;
//id,type,ShippingArea,contactPNumber,contactName,contactAddress
        } catch (SQLException e) {
            System.out.println("Unable to execute query getSite");
        }finally {
//            conn.startConnection();
        }
        return null;     }

    public boolean contains(String id) {
        try {
            return identityMap.containsKey(id) || (Objects.equals(conn.executeQuery("SELECT id FROM Sites WHERE id = " + "'" + id + "'").getString("id"), id));
        } catch (SQLException e) {
            return false;
        }  finally {
//            conn.startConnection();
        }
    }
}

package DAL;

import DomainLayer.Transport.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class OrderDocDAO {
    private Connect conn = Connect.getInstance();
    private HashMap<String, OrderDocument> identityMap = new HashMap<>();
    private final static OrderDocDAO INSTANCE = new OrderDocDAO();
    public static OrderDocDAO getInstance(){
        return INSTANCE;
    }
    public String addDoc(int ID,String originID,String creationDate,boolean complete,String truckPlate,String driverID,double weight,ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> orders, String supplier){
        throw new NotImplementedException();
    }
    public String addDoc(OrderDocument doc){
        String query = "INSERT INTO OrderDocs(id,driverID,licensePlate,origin,date,time,weight,finished) VALUES(?,?,?,?,?,?,?,?)";
        try {
            conn.executeUpdate(query,doc.getId(),doc.getDriver().getId(),doc.getTruck(),doc.getOrigin(),doc.getDate().toString(),doc.getTime(),0,"#f");
            DriverDAO.getInstance().setAvailability(doc.getDriver(),doc.getDate().toString(),doc.getTime(),false);
            TruckDAO.getInstance().setAvailability(TruckDAO.getInstance().getTruck(doc.getTruck()),doc.getDate().toString(),doc.getTime(),false);
            for(Site dest: doc.getDestinations().keySet()){
                query= "INSERT INTO Destinations(siteID,orderDocID) VALUES(?,?)";
                conn.executeUpdate(query,dest.getId(),doc.getId());
                for(Supply supp: doc.getDestinations().get(dest).keySet()){
                    query = "INSERT INTO order4Dest(siteID,orderDocID,supply,quantity) VALUES(?,?,?,?)";
                    conn.executeUpdate(query,dest.getId(),doc.getId(),supp.getName(),doc.getDestinations().get(dest).get(supp));
                }

            }
            identityMap.put(doc.getId(),doc);
            return "Success";
        }catch (SQLException se){
            System.out.println(se.getMessage());
            System.out.println("Cannot Insert a OrderDoc,Something is wrong with the db");
        }
        return "Failed to add OrderDoc";
    }
    public String removeDoc(String ID) {
        if(containsDoc(ID)){
            try {
                if (Objects.equals(conn.deleteRecordFromTableSTR("OrderDocs", "id", ID), "Success")) {
                    conn.deleteRecordFromTableSTR("DriverDocs","orderDocID",ID);
                    conn.deleteRecordFromTableSTR("order4Dest","orderDocID",ID);
                    identityMap.remove(ID);
                    return "Success";
                }
            } catch (SQLException e) {
                return "fail";
            }
        }
        return "Unable to remove order document, ID: "+ID;
    }
//    docid,storeid,destination
    public String removeDest(String docID, String siteID, String field){
        String query = "DELETE FROM "+"'"+field+"'"+" WHERE siteID = "+"'"+siteID+"'"+" AND orderDocID = "+ "'"+docID+"'";
        try {
            conn.executeUpdate(query);
            return "Success";
        } catch (SQLException e) {
            System.out.println("Unable to delete orderDOC");
            return "Failure to remove dest from doc";
        }
    }
    public String updateDocWeight(String docID,String value,String field){
        String query = "UPDATE OrderDocs SET weight = "+"'"+value+"'"+" WHERE id = "+"'"+docID+"'";
        try {
            conn.executeUpdate(query);
            return "Success";
        } catch (SQLException e) {
            System.out.println("Unable to execute query updateDoc");
        }
        return "Fail";
    }
    public String replaceStore(String docID, String id2Replace, String newID, ConcurrentHashMap<String,Integer> supplies){
        removeDest(docID,id2Replace,"order4Dest");
//        DriverDocDAO.getInstance().removeDriverDocbyODOC(docID,id2Replace);
        for(String s : supplies.keySet()) {
            String query = "INSERT INTO order4Dest(siteID,orderDocID,supply,quantity) VALUES(?,?,?,?)";
            String query2 = "INSERT INTO Destinations(siteID,orderDocID) VALUES(?,?)";
            try {
                conn.executeUpdate(query, newID, docID, s, supplies.get(s));
                conn.executeUpdate(query2,newID,docID);
                return "Success";
            } catch (SQLException e) {
                System.out.println("Unable to update Order");
                return "Unable to update Order";
            }
        }return "Failed";

    }
    public String updateOrder(String docID, String storeID, ArrayList<String> names,ArrayList<Integer> quantities){
        removeDest(docID,storeID,"order4Dest");
        for(int i = 0; i<names.size();i++) {
            String query = "INSERT INTO order4Dest(siteID,orderDocID,supply,quantity) VALUES(?,?,?,?)";
            String query2 = "INSERT INTO Destinations(siteID,orderDocID) VALUES(?,?)";
            try {
                conn.executeUpdate(query, storeID, docID, names.get(i), quantities.get(i));
                conn.executeUpdate(query2,storeID,docID);
            } catch (SQLException e) {
                System.out.println("Unable to update Order");
                return "Unable to update Order";
            }
        }
        return "Success";
    }
    public OrderDocument getOrderDoc(String docID){
        if(identityMap.containsKey(docID)){
            return identityMap.get(docID);
        }
        ResultSet rs;
        String query = "SELECT * FROM OrderDocs WHERE id = "+"'"+docID+"'";
        try {
            rs = conn.executeQuery(query);
            String id = rs.getString("id");
            String driverID = rs.getString("driverID");
            String licensePlate = rs.getString("licensePlate");
            String originID = rs.getString("origin");
            String date = rs.getString("date");
            String time = rs.getString("time");
            double weight = rs.getDouble("weight");
            boolean finished;
            if(Objects.equals(rs.getString("finished"), "#t")){
                finished=true;
            }
            else{finished = false;}
            Date da = createDate(date);
            Driver d = DriverDAO.getInstance().getDriver(driverID);
            Truck t = TruckDAO.getInstance().getTruck(licensePlate);
            Site s = SiteDAO.getInstance().getSite(originID);
            ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> supplies =new ConcurrentHashMap<>();
            query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'";
            rs = conn.executeQuery(query);
            while(rs.next()){
                Site store = SiteDAO.getInstance().getSite(rs.getString("siteID"));
                supplies.put(store,showSupplies(docID,store.getId()));
            }
            OrderDocument doc = new OrderDocument(id,s,supplies,da,time);
            doc.setTruckandDriver(t,d);
            doc.setWeight(weight);
            doc.setFinished(finished);
            identityMap.put(docID,doc);
            return doc;
        } catch (SQLException e) {
            return null;
        }


    }
    public ArrayList<OrderDocument> showDocs(String date){
        ArrayList<OrderDocument> orderdocs= new ArrayList<>();
        String query = "SELECT * FROM OrderDocs WHERE date = "+"'"+date+"'";
        ResultSet rs = null;
        try {
            rs = conn.executeQuery(query);
            while(rs.next()){
                OrderDocument doc = getOrderDoc(rs.getString("orderDocID"));
                orderdocs.add(doc);
            }
            return orderdocs;

        } catch (SQLException e) {
            System.out.println("Unable to retrieve orderDocs");
            return null;
        }
    }
    public boolean containsStore(String docID,String storeID){
        try {
            if(identityMap.containsKey(docID)){
                Site s = SiteDAO.getInstance().getSite(storeID);
                OrderDocument doc = identityMap.get(docID);
                return identityMap.get(docID).containsStore(SiteDAO.getInstance().getSite(storeID).getId());
            }
            String res = conn.executeQuery("SELECT siteID FROM order4Dest WHERE siteID = " + "'" + storeID + "'"+ "AND orderDocID = "+"'"+docID+"'").getString("siteID");
            return Objects.equals(res, storeID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ConcurrentHashMap<Supply,Integer> showSupplies(String docID,String storeID){
        ConcurrentHashMap<Supply,Integer> supplies = new ConcurrentHashMap<>();
        String query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'"+" AND siteID = "+"'"+storeID+"'";
        try {
            ResultSet rs = conn.executeQuery(query);
            while(rs.next()){
                Supply supp = SuppliesDAO.getInstance().getSupply(rs.getString("supply"));
                int quantity = rs.getInt("quantity");
                supplies.put(supp,quantity);
            }
            return supplies;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String get(String docID,String field){
        throw new NotImplementedException();
    }
    public ArrayList<String> showStores(String docID){
        ArrayList<String> stores = new ArrayList<>();
        String query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'";
        try {
            ResultSet rs = conn.executeQuery(query);
            while (rs.next()){
                if(!stores.contains(rs.getString("siteID"))) {
                    stores.add(rs.getString("siteID"));
                }
            }
            return stores;
        } catch (SQLException e) {
            System.out.println("Unable to show stores");
        }
        return null;

    }
    public boolean set(String orderDocID,String field,String param){
        String query = "UPDATE OrderDocs SET "+field+" = ? WHERE id = ?";
        try {
            conn.executeUpdate(query,param,orderDocID);
            return true;
        } catch (SQLException e) {
            System.out.println("Unable to set weight");
        }
        return false;
    }

    public boolean containsDoc(String docID){
        try {
            return identityMap.containsKey(docID) || (Objects.equals(conn.executeQuery("SELECT id FROM OrderDocs WHERE id = " + "'" + docID + "'").getString("id"), docID));
        } catch (SQLException e) {
            return false;
        }
    }

    //TODO DISCUSS WHERE TO MOVE THIS METHOD
    public Date createDate(String date){
        String[] temp = date.split("/");
        return new Date(temp[0], temp[1], temp[2]);

    }
}

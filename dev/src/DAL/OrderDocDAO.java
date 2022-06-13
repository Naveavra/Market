package DAL;

import DomainLayer.Storage.Product;
import DomainLayer.Suppliers.Supplier;
import DomainLayer.Transport.*;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class OrderDocDAO {
    private Connect conn = Connect.getInstance();
    private HashMap<String, OrderDocument> identityMap = new HashMap<>();
    private StoreDAO storesDAO = new StoreDAO();
    private DriverDAO driverDAO = new DriverDAO();
    private SuppliersDAO suppliersDAO = new SuppliersDAO();
    private ProductDAO productDAO = new ProductDAO();
//    private final static OrderDocDAO INSTANCE = new OrderDocDAO();
//    public static OrderDocDAO getInstance(){
//        return INSTANCE;
//    }
//    public String addDoc(int ID,String originID,String creationDate,boolean complete,String truckPlate,String driverID,double weight,ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> orders, String supplier){
//        throw new NotImplementedException();
//    }


    //FROM HERE
    public String addDoc(OrderDocument doc){
        String query = "INSERT INTO OrderDocs(id,driverID,licensePlate,origin,date,time,weight,finished) VALUES(?,?,?,?,?,?,?,?)";
        try {
            conn.executeUpdate(query,doc.getId(),doc.getDriver().getId(),doc.getTruck(),doc.getOrigin(),doc.getDate().toString(),doc.getTime(),0,"#f");
            driverDAO.setAvailability(doc.getDriver(),doc.getDate().toString(),doc.getTime(),false);
            new TruckDAO().setAvailability(new TruckDAO().getTruck(doc.getTruck()),doc.getDate().toString(),doc.getTime(),false);
            for(Store dest: doc.getDestinations().keySet()){
                query= "INSERT INTO Destinations(siteID,orderDocID) VALUES(?,?)";
                conn.executeUpdate(query,dest.getId(),doc.getId());
                for(String supp: doc.getDestinations().get(dest).keySet()){
                    query = "INSERT INTO order4Dest(siteID,orderDocID,supply,quantity) VALUES(?,?,?,?)";
                    conn.executeUpdate(query,dest.getId(),doc.getId(),supp,doc.getDestinations().get(dest).get(supp));
                }

            }
            identityMap.put(doc.getId(),doc);
            return "Success";
        }catch (SQLException se){

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
            // System.out.println("Unable to delete orderDOC");
            return "Failure to remove dest from doc";
        }
    }
    public String updateDocWeight(String docID,String value,String field){
        String query = "UPDATE OrderDocs SET weight = "+"'"+value+"'"+" WHERE id = "+"'"+docID+"'";
        try {
            conn.executeUpdate(query);
            return "Success";
        } catch (SQLException e) {
            return "Fail";
        }

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
                return "Unable to update Order";
            }
        }
        return "Failed";

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
                return "Unable to update Order";
            }
        }
        return "Success";
    }

    public OrderDocument getOrderDoc(String docID){
        if(identityMap.containsKey(docID)){
            return identityMap.get(docID);
        }
        List<HashMap<String, Object>> rs;
        String query = "SELECT * FROM OrderDocs WHERE id = "+"'"+docID+"'";
        try {
            rs = conn.executeQuery(query);
            String id = (String)rs.get(0).get("id");
            String driverID = (String)rs.get(0).get("driverID");
            String licensePlate = (String)rs.get(0).get("licensePlate");
            String originID = (String)rs.get(0).get("origin");
            String date = (String)rs.get(0).get("date");
            String time = (String)rs.get(0).get("time");
            double weight = Double.parseDouble(String.valueOf(rs.get(0).get("weight")));
            boolean finished;
            if(Objects.equals((String)rs.get(0).get("finished"), "#t")){
                finished=true;
            }
            else{
                finished = false;
            }
            Date da = createDate(date);
            Driver d = driverDAO.getDriver(driverID);
            Truck t = new TruckDAO().getTruck(licensePlate);
            Supplier s = suppliersDAO.getSupplier(Integer.parseInt(originID));
            ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>> supplies =new ConcurrentHashMap<>();
            query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'";
            rs = conn.executeQuery(query);
            for (int i = 0; i < rs.size(); i++){
                Store store = storesDAO.getSite((String)rs.get(i).get("siteID"));
                int miki = Integer.parseInt((String)rs.get(i).get("supply"));
                Product p = productDAO.get(miki);
                if(!supplies.containsKey(store)){
                    supplies.put(store,new ConcurrentHashMap<>());
                }
                if(!supplies.get(store).containsKey(String.valueOf(p.getId()))){
                    supplies.get(store).put(String.valueOf(p.getId()),(int)rs.get(i).get("quantity"));
                }
//                supplies.put(store,showSupplies(docID,store.getId()));
            }
            OrderDocument doc = new OrderDocument(id,s.getSupplierNumber(),supplies,da,time);
            doc.setTruckandDriver(t,d);
            doc.setWeight(weight);
            doc.setFinished(finished);
            identityMap.put(docID,doc);
            return doc;
        } catch (SQLException e) {
            return null;
        }
    }
//    public OrderDocument getOrderDoc(String docID){
//        if(identityMap.containsKey(docID)){
//            return identityMap.get(docID);
//        }
//        List<HashMap<String, Object>> rs;
//        String query = "SELECT * FROM OrderDocs WHERE id = "+"'"+docID+"'";
//        try {
//            rs = conn.executeQuery(query);
//            String id = (String)rs.get(0).get("id");
//            String driverID = (String)rs.get(0).get("driverID");
//            String licensePlate = (String)rs.get(0).get("licensePlate");
//            String originID = (String)rs.get(0).get("origin");
//            String date = (String)rs.get(0).get("date");
//            String time = (String)rs.get(0).get("time");
//            double weight = Double.parseDouble(String.valueOf(rs.get(0).get("weight")));
//            boolean finished;
//            if(Objects.equals((String)rs.get(0).get("finished"), "#t")){
//                finished=true;
//            }
//            else{
//                finished = false;
//            }
//            Date da = createDate(date);
//            Driver d = driverDAO.getDriver(driverID);
//            Truck t = new TruckDAO().getTruck(licensePlate);
//            Supplier s = suppliersDAO.getSupplier(Integer.parseInt(originID));
//            ConcurrentHashMap<Store,ConcurrentHashMap<String,Integer>> supplies =new ConcurrentHashMap<>();
//            query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'";
//            rs = conn.executeQuery(query);
//            for (int i = 0; i < rs.size(); i++){
//                Store store = storesDAO.getSite((String)rs.get(i).get("siteID"));
//                Product p = productDAO.get((Integer.parseInt((String)rs.get(i).get("supply"))));
//                if(!supplies.containsKey(store)){
//                    supplies.put(store,new ConcurrentHashMap<>());
//                }
//                if(!supplies.get(store).containsKey(String.valueOf(p.getId()))){
//                    supplies.get(store).put(String.valueOf(p.getId()),(int)rs.get(i).get("quantity"));
//                }
////                supplies.put(store,showSupplies(docID,store.getId()));
//            }
//            OrderDocument doc = new OrderDocument(id,s.getSupplierNumber(),supplies,da,time);
//            doc.setTruckandDriver(t,d);
//            doc.setWeight(weight);
//            doc.setFinished(finished);
//            identityMap.put(docID,doc);
//            return doc;
//        } catch (SQLException e) {
//            return null;
//        }
//    }
    public ArrayList<OrderDocument> showDocs(String date){
        ArrayList<OrderDocument> orderdocs= new ArrayList<>();
        String query = "SELECT * FROM OrderDocs WHERE date = "+"'"+date+"'";
        List<HashMap<String, Object>> rs;
        try {
            rs = conn.executeQuery(query);
            for (int i = 0; i < rs.size(); i++){
                OrderDocument doc = getOrderDoc((String)rs.get(i).get("orderDocID"));
                orderdocs.add(doc);
            }
            return orderdocs;

        } catch (SQLException e) {
            return null;
        }
    }
    public boolean containsStore(String docID,String storeID){
        try {
            if(identityMap.containsKey(docID)){
                Store s = storesDAO.getSite(storeID);
                OrderDocument doc = identityMap.get(docID);
                return identityMap.get(docID).containsStore(storesDAO.getSite(storeID).getId());
            }
            String res = (String) conn.executeQuery("SELECT siteID FROM order4Dest WHERE siteID = " + "'" + storeID + "'"+ "AND orderDocID = "+"'"+docID+"'").get(0).get("siteID");
            return Objects.equals(res, storeID);
        } catch (SQLException e) {
            return false;
        }
    }
    public ConcurrentHashMap<String,Integer> showSupplies(String docID,String storeID){
        ConcurrentHashMap<String,Integer> supplies = new ConcurrentHashMap<>();
        String query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'"+" AND siteID = "+"'"+storeID+"'";
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery(query);
            for (int i = 0; i < rs.size(); i++){
                Product supp = productDAO.get((int)rs.get(i).get("supply"));
                int quantity = (int)rs.get(i).get("quantity");
                supplies.put(supp.getName(),quantity);
            }
            return supplies;

        } catch (SQLException e) {
            return null;
        }

    }

//    public String get(String docID,String field){
//        throw new NotImplementedException();
//    }

    public ArrayList<String> showStores(String docID){
        ArrayList<String> stores = new ArrayList<>();
        String query = "SELECT * FROM order4Dest WHERE orderDocID = "+"'"+docID+"'";
        try {
            List<HashMap<String, Object>> rs = conn.executeQuery(query);
            for (int i = 0; i < rs.size(); i++){
                if(!stores.contains((String)rs.get(i).get("siteID"))) {
                    stores.add((String)rs.get(i).get("siteID"));
                }
            }
            return stores;
        } catch (SQLException e) {
            return null;
        }
    }
    public boolean set(String orderDocID,String field,String param){
        String query = "UPDATE OrderDocs SET "+field+" = ? WHERE id = ?";
        try {
            conn.executeUpdate(query,param,orderDocID);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean containsDoc(String docID){
        try {
            return identityMap.containsKey(docID) || (Objects.equals((String)conn.executeQuery("SELECT id FROM OrderDocs WHERE id = " + "'" + docID + "'").get(0).get("id"), docID));
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

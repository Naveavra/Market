//package DAL;
//
//import DomainLayer.Transport.Supply;
////import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class SuppliesDAO {
//    private Connect conn = Connect.getInstance();
//    private static HashMap<String, Supply> identityMap = new HashMap<>();
////    private final static SuppliesDAO INSTANCE = new SuppliesDAO();
////    public static SuppliesDAO getInstance(){
////        return INSTANCE;
////    }
//
//
//    //FROM HERE
//    public String addSupply(String name, double weight){
//        String query = "INSERT INTO Supplies(name,weight) VALUES(?,?)";
//        try {
//            conn.executeUpdate(query,name,weight);
//            return "Success";
//        }catch (SQLException se){
//            return ("Cannot Insert a Supply,Something is wrong with the db");
//        }
//    }
//    public String removeSupply(String name){
//        try {
//            if(Objects.equals(conn.deleteRecordFromTableSTR("Supplies", "name", name), "Success")){
//                identityMap.remove(name);
//                return "Success";
//            }
//            else
//                return "Failure to remove Supply";
//        } catch (SQLException e) {
//            return "Failure to remove Supply";
//        }
//    }
//    public Supply getSupply(String name){
//        if(identityMap.containsKey(name)){
//            return identityMap.get(name);
//        }
//        try {
//            List<HashMap<String, Object>> rs = conn.executeQuery("SELECT * FROM Supplies Where name = "+"'"+name+"'");
//            String suppName = (String)rs.get(0).get("name");
//            double weight = (double)rs.get(0).get("weight");
//            Supply s = new Supply(suppName,weight, "1");
//            identityMap.put(name,s);
//            return s;
//
//        } catch (SQLException e) {
//            return null;
//        }
//    }
//
////    public double getSupplyWeight(String name){
////        throw new NotImplementedException();
////    }
//
//    public ConcurrentHashMap<Supply,Integer> showSupplies(){
//        ArrayList<Supply> supp = getSupplies();
//        ConcurrentHashMap<Supply,Integer> retVal = new ConcurrentHashMap<>();
//        for(Supply s : supp){
//            retVal.put(s,0);// 0 is a default value
//        }
//        return retVal;
//    }
//
//    public boolean contains(String name) {
//        try {
//            return identityMap.containsKey(name) ||
//                    (Objects.equals(conn.executeQuery("SELECT name FROM Supplies WHERE name = " + "'" + name + "'").get(0).get("name"), name));
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public ArrayList<Supply> getSupplies() {
//        ArrayList<Supply> rval = new ArrayList<>();
//        List<HashMap<String, Object>> rs;
//        try {
//            rs = conn.executeQuery("Select name From Supplies");
//            for (int i = 0; i < rs.size(); i++){
//                rval.add(getSupply((String) rs.get(i).get("name")));
//            }
//
//        } catch (SQLException e) {
//            return null;
//        }
//        return rval;
//    }
//}

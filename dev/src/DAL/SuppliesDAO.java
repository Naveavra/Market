package DAL;

import DomainLayer.Transport.Supply;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SuppliesDAO {
    private Connect conn = Connect.getInstance();
    private HashMap<String, Supply> identityMap = new HashMap<>();
    private final static SuppliesDAO INSTANCE = new SuppliesDAO();
    public static SuppliesDAO getInstance(){
        return INSTANCE;
    }
    public String addSupply(String name, double weight){
        String query = "INSERT INTO Supplies(name,weight) VALUES(?,?)";
        try {
            conn.executeUpdate(query,name,weight);
            return "Success";
        }catch (SQLException se){
            System.out.println(se.getMessage());
            System.out.println("Cannot Insert a Supply,Something is wrong with the db");
        }
        return "Failed to add Supply";    }
    public String removeSupply(String name){
        try {
            if(Objects.equals(conn.deleteRecordFromTableSTR("Supplies", "name", name), "Success")){
                identityMap.remove(name);
                return "Success";
            }
            else
                return "Failure to remove Supply";
        } catch (SQLException e) {
            return "Failure to remove Supply";
        }
    }
    public Supply getSupply(String name){
        if(identityMap.containsKey(name)){
            return identityMap.get(name);
        }
        try {
            ResultSet rs = conn.executeQuery("SELECT * FROM Supplies Where name = "+"'"+name+"'");
            String suppName = rs.getString("name");
            double weight = rs.getDouble("weight");
            Supply s = new Supply(suppName,weight);
            identityMap.put(name,s);
            return s;

        } catch (SQLException e) {
            System.out.println("Unable to execute query getSupply");
        }
        return null;    }
    public double getSupplyWeight(String name){
        throw new NotImplementedException();
    }
    public ConcurrentHashMap<Supply,Integer> showSupplies(){
        ArrayList<Supply> supp = getSupplies();
        ConcurrentHashMap<Supply,Integer> retVal = new ConcurrentHashMap<>();
        for(Supply s : supp){
            retVal.put(s,0);// 0 is a default value
        }
        return retVal;
    }

    public boolean contains(String name) {
        try {
            return identityMap.containsKey(name) || (Objects.equals(conn.executeQuery("SELECT name FROM Supplies WHERE name = " + "'" + name + "'").getString("name"), name));
        } catch (SQLException e) {
            return false;
        }    }

    public ArrayList<Supply> getSupplies() {
        ArrayList<Supply> rval = new ArrayList<>();
        ResultSet rs;
        try {
            rs = conn.executeQuery("Select name From Supplies");
            while(rs.next()){
                rval.add(getSupply(rs.getString("name")));
            }

        } catch (SQLException e) {
            System.out.println("Cannot connect to the DB");
        }
        return rval;    }
}

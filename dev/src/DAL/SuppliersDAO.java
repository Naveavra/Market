package DAL;

import DomainLayer.ProductSupplier;
import DomainLayer.Supplier;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SuppliersDAO {
    private Connect connect;
    private static HashMap<Integer, Supplier> IMSuppliers;//key: supplierId

    /**
     * constructor
     */
    public SuppliersDAO(){
        connect = Connect.getInstance();
        IMSuppliers = new HashMap<>();
    }

    public void updateSupplier(Supplier s) throws SQLException {
        String query = String.format("UPDATE Suppliers SET name = \"%s\" and bankAccount = %d and active = %d and isDeliver = %d " +
                        "WHERE supplierNumber = %d",
                s.getName(),s.getBankAccount(),s.getActive(), s.getIsDeliver(),
                s.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            IMSuppliers.put(s.getSupplierNumber(),s);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public void updateSupplierContacts(Supplier s) throws SQLException {
        String query = String.format("DELETE from ContactsSupplier WHERE supplierNumber = %d",
                s.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            for(Map.Entry<String,String> k:s.getContacts().entrySet()){
                query = String.format("INSERT INTO ContactsSupplier (supplierNumber,name,email)"+ "VALUES" +
                        String.format("(%d,'%s','%s')",s.getSupplierNumber(),k.getKey(),k.getValue()));
                stmt.execute(query);
            }
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public void updateSupplierDiscounts(Supplier s) throws SQLException {
        String query = String.format("DELETE from DiscountSupplier WHERE supplierNumber = %d",
                s.getSupplierNumber());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            for(Map.Entry<Integer,Double> k:s.getDiscounts().entrySet()){
                query = String.format("INSERT INTO DiscountSupplier (supplierNumber,quantity,discount)"+ "VALUES" +
                        String.format("(%d,%d,%f)",s.getSupplierNumber(),k.getKey(),k.getValue()));
                stmt.execute(query);
            }
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    public void insertSupplier(Supplier s) throws SQLException {
        String query =String.format("INSERT INTO Suppliers (supplierNumber,name,bankAccount,active,isDeliver) " +
                        "VALUES (%d,'%s',%d,%b,%b)", s.getSupplierNumber(),s.getName(),s.getBankAccount(),s.getActive(),s.getIsDeliver());
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            IMSuppliers.put(s.getSupplierNumber(), s);
            updateSupplierContacts(s);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }


    public Supplier getSupplier(int supplierNumber) throws SQLException {
        if(IMSuppliers.containsKey(supplierNumber)){
            return IMSuppliers.get(supplierNumber);
        }
        String query =String.format("SELECT * from Suppliers WHERE supplierNumber =%d", supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return null;
            System.out.println(rs.getInt("isDeliver"));
            System.out.println(rs.getBoolean("isDeliver"));
            Supplier supplier =new Supplier(supplierNumber, rs.getNString("name"),rs.getInt("bankAccount"),getContacts(supplierNumber),
                    Boolean.parseBoolean(rs.getInt("isDeliver")+""),rs.getBoolean("active"));
            return supplier;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    private Map<String, String> getContacts(int supplierNumber) throws SQLException {
        String query =String.format("SELECT * from ContactsSupplier WHERE supplierNumber =%d", supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            Map<String,String> contacts =new HashMap<>();
            while(rs.next())  {
                contacts.put(rs.getString("name"),rs.getString("email"));
            }
            return contacts;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
}

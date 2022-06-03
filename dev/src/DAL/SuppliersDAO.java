package DAL;

import DomainLayer.Suppliers.Contact;
import DomainLayer.Suppliers.Discount;
import DomainLayer.Suppliers.Supplier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

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

    public static void reset() {
        IMSuppliers = new HashMap<>();
    }

    public void updateSupplier(Supplier s) throws SQLException {
        Integer isDeliver = s.getIsDeliver() ? 1 : 0;
        Integer isActive = s.getActive() ? 1 : 0;
        String query = String.format("UPDATE Suppliers SET name = '%s' and bankAccount = %d and active = %d and isDeliver = %d WHERE supplierNumber = %d",
                s.getName(),s.getBankAccount(),isActive, isDeliver,
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
            for(Contact c:s.getContacts()){
                query = String.format("INSERT INTO ContactsSupplier (supplierNumber,name,email,telephone)"+ "VALUES" +
                        String.format("(%d,'%s','%s','%s')",s.getSupplierNumber(),c.getName(),c.getEmail(),c.getTelephone()));
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
            for(Discount k:s.getDiscounts()){
                query = String.format("INSERT INTO DiscountSupplier (supplierNumber,quantity,discount)"+ "VALUES" +
                        "(%d,%d,%f)",s.getSupplierNumber(),k.getAmount(),k.getDiscount());
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
        Integer isDeliver = s.getIsDeliver() ? 1 : 0;
        Integer isActive = s.getActive() ? 1:0;
        String query =String.format("INSERT INTO Suppliers (supplierNumber,name,bankAccount,active,isDeliver) " +
                "VALUES (%d,'%s',%d,%d,%d)", s.getSupplierNumber(),s.getName(),s.getBankAccount(),isActive,isDeliver);
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
    public void insertDiscountOnAmount(Supplier s,int count,double discount) throws SQLException{
        String query =String.format("INSERT INTO DiscountSupplier (supplierNumber,quantity,discount) " +
                "VALUES (%d,%d,%f)", s.getSupplierNumber(),count,discount);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
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
        LinkedList<Contact> contacts=getContacts(supplierNumber);
        String query =String.format("SELECT * from Suppliers WHERE supplierNumber =%d", supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return null;
            Supplier supplier =new Supplier(supplierNumber, rs.getString("name"),rs.getInt("bankAccount"),contacts,
                    parseIntToBool(rs.getInt("isDeliver")),parseIntToBool(rs.getInt("active")));
            supplier.addDiscounts(getDiscountsSupplier(supplierNumber));
            return supplier;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
    private boolean parseIntToBool(int i){
        return i == 1;
    }
    private LinkedList<Contact> getContacts(int supplierNumber) throws SQLException {
        String query =String.format("SELECT * from ContactsSupplier WHERE supplierNumber =%d", supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            LinkedList<Contact> contacts=new LinkedList<>();
            while(rs.next())  {
                contacts.add(new Contact(rs.getString("name"),rs.getString("email"),rs.getString("telephone")));
            }
            return contacts;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }


    public LinkedList<Discount> getDiscountsSupplier(int supplierNumber) throws SQLException {
        String query =String.format("SELECT * from DiscountSupplier WHERE supplierNumber =%d", supplierNumber);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            LinkedList<Discount> discounts=new LinkedList<>();
            while(rs.next())  {
                discounts.add(new Discount(rs.getInt("quantity"),rs.getDouble("discount")));
            }
            return discounts;
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void removeDiscountOnAmount(Supplier supplier, int count) throws SQLException {
        String query =String.format("DELETE from DiscountSupplier WHERE supplierNumber = %d and quantity = %d ",
                supplier.getSupplierNumber(),count);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void addContact(Supplier supplier, String name, String email, String telephone) throws SQLException {
        String query =String.format("INSERT INTO ContactsSupplier (supplierNumber,name,email,telephone) " +
                "VALUES (%d,'%s','%s','%s')", supplier.getSupplierNumber(),name,email,telephone);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

    public void closeAccount(Supplier s) throws SQLException {
        String query = String.format("UPDATE Suppliers SET active = %d WHERE supplierNumber = %d",
                0,s.getSupplierNumber());
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
}

package DAL;

import DomainLayer.ProductSupplier;

import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */
public class Connect {
    /**
     * Connect to a sample database
     */
    public Connection conn = null;
    public final String url = "jdbc:sqlite:superli.db";
    private static Connect instance;
    public static Connect getInstance(){
        if(instance==null){
            instance=new Connect();
        }
        return instance;
    }
    private Connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:superli.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            createTables();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connect connect =new Connect();
        ProductSupplierDAO productSupplierMapper=new ProductSupplierDAO();
        ProductSupplier p1=new ProductSupplier(16,100,5);
        p1.addDiscount(10,0.5);
        productSupplierMapper.insert(p1);
        ProductSupplier p = productSupplierMapper.getProduct(14, 5);
        System.out.println(p.getPrice());
        ProductSupplier p2 = productSupplierMapper.getProduct(14, 5);
        System.out.println(p2.getPrice());
    }
    public void createTables() throws SQLException {
        try (Statement stmt = createStatement()) {
            //table product supplier
            String query = "CREATE TABLE IF NOT EXISTS \"ProductSupplier\"  (\n" +
                    "\t\"productId\"\tInteger,\n" +
                    "\t\"supplierNumber\"\tInteger,\n" +
                    "\t\"catalogNumber\"\tINTEGER,\n" +
                    "\t\"price\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"productId\")\n"+ ")";
            stmt.execute(query);
            //table DiscountProductSupplier
            query = "CREATE TABLE IF NOT EXISTS \"DiscountProductSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"quantity\"\tInteger,\n" +
                    "\t\"discount\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"productId\",\"quantity\")\n"+ ")";
            stmt.execute(query);

            //table suppliers
            query = "CREATE TABLE IF NOT EXISTS \"Suppliers\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT ,\n" +
                    "\t\"banlNumber\"\tInteger,\n" +
                    "\t\"active\"\tInteger,\n"+
                    "\t\"isDeliver\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\")\n"+ ")";
            stmt.execute(query);
            //add table contacts


            //table discount
            query = "CREATE TABLE IF NOT EXISTS \"DiscountSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"quantity\"\tInteger,\n" +
                    "\t\"discount\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"quantity\")\n"+ ")";
            stmt.execute(query);
            //table orderFromSupplier
            query = "CREATE TABLE IF NOT EXISTS \"OrderFromSupplier\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"date\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"orderId\")\n"+ ")";
            stmt.execute(query);

            //table deliveryTerms
            query = "CREATE TABLE IF NOT EXISTS \"DeliveryTerms\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"daysToDeliver\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"orderId\",\"daysToDeliver\")\n"+ ")";
            stmt.execute(query);

            //table pastOrder
            query = "CREATE TABLE IF NOT EXISTS \"PastOrdersSupplier\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"finishDate\"\tTEXT,\n" +
                    "\t\"totalPrice\"\tREAL,\n" +
                    "\tPRIMARY KEY(\"orderId\", \"finishDate\")\n"+ ")";
            stmt.execute(query);

        } catch (SQLException e) {
            throw e;
        }
        finally {
            closeConnect();
        }
    }

    public Statement createStatement() throws SQLException {
        conn = DriverManager.getConnection(url);
        return conn.createStatement();
    }
    public void closeConnect() throws SQLException {
        conn.close();
    }
}
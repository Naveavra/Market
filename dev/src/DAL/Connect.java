package DAL;

import java.io.File;
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
    public final String url = "jdbc:sqlite:../dev/superli.db";
    private static Connect instance;
    public static Connect getInstance(){
        if(instance==null){
            instance = new Connect();
        }
        return instance;
    }
    private Connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:../dev/superli.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            createTables();

        } catch (SQLException ignored) {
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    public void createTables() throws SQLException {
        try (Statement stmt = createStatement()) {
            //table products
            String query = "CREATE TABLE IF NOT EXISTS \"Products\"  (\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT,\n" +
                    "\t\"description\"\tTEXT,\n"+
                    "\t\"maker\"\tTEXT,\n"+
                    "\t\"storageAmount\"\tINTEGER,\n"+
                    "\t\"storeAmount\"\tINTEGER,\n"+
                    "\t\"timesBought\"\tINTEGER,\n"+
                    "\t\"amountNeededForRefill\"\tINTEGER,\n"+
                    "\t\"price\"\tInteger,\n"+
                    "\t\"discount\"\tInteger,\n"+
                    "\t\"dayAdded\"\tTEXT,\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"subCategoryName\"\tTEXT,\n"+
                    "\t\"subSubCategoryName\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"productId\")\n"+ ")";
            stmt.execute(query);
            //table category discount
            query = "CREATE TABLE IF NOT EXISTS \"CategoryDiscount\"  (\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"discount\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"categoryName\")\n"+ ")";
            stmt.execute(query);
            //table category subCategory
            query = "CREATE TABLE IF NOT EXISTS \"SubCategories\"  (\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"subCategoryName\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"categoryName\", \"subCategoryName\")\n"+ ")";
            stmt.execute(query);
            //table subCategory subSubCategory
            query = "CREATE TABLE IF NOT EXISTS \"SubSubCategories\"  (\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"subCategoryName\"\tTEXT,\n" +
                    "\t\"subSubCategoryName\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"categoryName\", \"subCategoryName\", \"subSubCategoryName\")\n"+ ")";
            stmt.execute(query);
            //table items
            query = "CREATE TABLE IF NOT EXISTS \"Items\"  (\n" +
                    "\t\"itemId\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"expirationDate\"\tTEXT,\n"+
                    "\t\"place\"\tTEXT,\n"+
                    "\t\"shelf\"\tINTEGER,\n"+
                    "\t\"isDamaged\"\tTEXT,\n"+
                    "\t\"defectiveDescription\"\tTEXT)";
            stmt.execute(query);

            //table product supplier
            query = "CREATE TABLE IF NOT EXISTS \"ProductSupplier\"  (\n" +
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
                    "\t\"discount\"\tREAL ,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"productId\",\"quantity\")\n"+ ")";
            // amount discount on specific product to supplier
            stmt.execute(query);

            //table suppliers
            query = "CREATE TABLE IF NOT EXISTS \"Suppliers\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT ,\n" +
                    "\t\"bankAccount\"\tInteger,\n" +
                    "\t\"active\"\tINTEGER ,\n"+
                    "\t\"isDeliver\"\tINTEGER,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\")\n"+ ")";
            stmt.execute(query);

            //add table contacts
            query = "CREATE TABLE IF NOT EXISTS \"ContactsSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT,\n" +
                    "\t\"email\"\tTEXT,\n"+
                    "\t\"telephone\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"name\")\n"+ ")";
            stmt.execute(query);

            //table discount
            query = "CREATE TABLE IF NOT EXISTS \"DiscountSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"quantity\"\tInteger,\n" +
                    "\t\"discount\"\tDOUBLE ,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"quantity\")\n"+ ")";
            //amount discount on sum of products in order

            stmt.execute(query);
            //table orderFromSupplier
            query = "CREATE TABLE IF NOT EXISTS \"OrdersFromSupplier\"  (\n" +
                    "\t\"orderId\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"date\"\tTEXT,\n" +
                    "\t\"supplierNumber\"\tTEXT)";
            stmt.execute(query);

            //table productsInOrder
            query = "CREATE TABLE IF NOT EXISTS \"ProductsInOrder\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"count\"\tINTEGER,\n" +
                    "\tPRIMARY KEY(\"orderId\" , \"productId\")\n"+ ")";
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
                    "\t\"supplierNumber\"\tINTEGER,\n" +
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
    public void deleteRecordsOfTables() throws SQLException {
        try (Statement stmt = createStatement()) {
            //table products
            String query = "DELETE from Products";
            stmt.execute(query);
            ProductDAO.reset();
            //table category discount
            query = "DELETE from CategoryDiscount";
            stmt.execute(query);
            CategoryDAO.reset();
            OrdersFromSupplierDAO.reset();
            ProductsSupplierDAO.reset();
            SuppliersDAO.reset();
            //table category subCategory
            query = "DELETE from SubCategories";
            stmt.execute(query);
            //table subCategory subSubCategory
            query = "DELETE from SubSubCategories";
            stmt.execute(query);
            //table items
            query = "DELETE from Items";
            stmt.execute(query);

            //table product supplier
            query = "DELETE from ProductSupplier";
            stmt.execute(query);
            //table DiscountProductSupplier
            query = "DELETE from DiscountProductSupplier";
            // amount discount on specific product to supplier
            stmt.execute(query);

            //table suppliers
            query = "DELETE from Suppliers";
            stmt.execute(query);

            //add table contacts
            query = "DELETE from ContactsSupplier";
            stmt.execute(query);

            //table discount
            query = "DELETE from DiscountSupplier";
            //amount discount on sum of products in order

            stmt.execute(query);
            //table orderFromSupplier
            query = "DELETE from OrdersFromSupplier";
            stmt.execute(query);

            //table productsInOrder
            query = "DELETE from ProductsInOrder";
            stmt.execute(query);
            //table deliveryTerms
            query = "DELETE from DeliveryTerms";
            stmt.execute(query);

            //table pastOrder
            query = "DELETE from PastOrdersSupplier";
            stmt.execute(query);



        } catch (SQLException e) {
            throw e;
        }
        finally {
            closeConnect();
        }

    }



}
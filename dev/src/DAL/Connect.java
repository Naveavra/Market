package DAL;

import ServiceLayer.Utility.Response;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author sqlitetutorial.net
 */
public class Connect {
    /**
     * Connect to a sample database
     */
    public Connection conn = null;
    public final String url = "jdbc:sqlite:./superli.db";
    private static Connect instance;
    public static Connect getInstance(){
        if(instance==null){
            instance = new Connect();
        }
        return instance;
    }
    private Connect() {
        try {
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
        try (Statement statement = createStatement()) {
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
            statement.execute(query);
            //table category discount
            query = "CREATE TABLE IF NOT EXISTS \"CategoryDiscount\"  (\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"discount\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"categoryName\")\n"+ ")";
            statement.execute(query);
            //table category subCategory
            query = "CREATE TABLE IF NOT EXISTS \"SubCategories\"  (\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"subCategoryName\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"categoryName\", \"subCategoryName\")\n"+ ")";
            statement.execute(query);
            //table subCategory subSubCategory
            query = "CREATE TABLE IF NOT EXISTS \"SubSubCategories\"  (\n" +
                    "\t\"categoryName\"\tTEXT,\n" +
                    "\t\"subCategoryName\"\tTEXT,\n" +
                    "\t\"subSubCategoryName\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"categoryName\", \"subCategoryName\", \"subSubCategoryName\")\n"+ ")";
            statement.execute(query);
            //table items
            query = "CREATE TABLE IF NOT EXISTS \"Items\"  (\n" +
                    "\t\"itemId\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"expirationDate\"\tTEXT,\n"+
                    "\t\"place\"\tTEXT,\n"+
                    "\t\"shelf\"\tINTEGER,\n"+
                    "\t\"isDamaged\"\tTEXT,\n"+
                    "\t\"defectiveDescription\"\tTEXT)";
            statement.execute(query);

            //table product supplier
            query = "CREATE TABLE IF NOT EXISTS \"ProductSupplier\"  (\n" +
                    "\t\"productId\"\tInteger,\n" +
                    "\t\"supplierNumber\"\tInteger,\n" +
                    "\t\"catalogNumber\"\tINTEGER,\n" +
                    "\t\"daysUntilExpiration\"\tINTEGER,\n" +
                    "\t\"price\"\tInteger,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"productId\")\n"+ ")";
            statement.execute(query);
            //table DiscountProductSupplier
            query = "CREATE TABLE IF NOT EXISTS \"DiscountProductSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"quantity\"\tInteger,\n" +
                    "\t\"discount\"\tREAL ,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"productId\",\"quantity\")\n"+ ")";
            // amount discount on specific product to supplier
            statement.execute(query);

            //table suppliers
            query = "CREATE TABLE IF NOT EXISTS \"Suppliers\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT ,\n" +
                    "\t\"bankAccount\"\tInteger,\n" +
                    "\t\"active\"\tINTEGER ,\n"+
                    "\t\"area\"\tINTEGER,\n"+
                    "\t\"deliveryDays\"\tINTEGER,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\")\n"+ ")";
            statement.execute(query);

            //add table contacts
            query = "CREATE TABLE IF NOT EXISTS \"ContactsSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT,\n" +
                    "\t\"email\"\tTEXT,\n"+
                    "\t\"telephone\"\tTEXT,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"name\")\n"+ ")";
            statement.execute(query);

            //table discount
            query = "CREATE TABLE IF NOT EXISTS \"DiscountSupplier\"  (\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\t\"quantity\"\tInteger,\n" +
                    "\t\"discount\"\tDOUBLE ,\n"+
                    "\tPRIMARY KEY(\"supplierNumber\",\"quantity\")\n"+ ")";
            //amount discount on sum of products in order

            statement.execute(query);
            //table orderFromSupplier
            query = "CREATE TABLE IF NOT EXISTS \"OrdersFromSupplier\"  (\n" +
                    "\t\"orderId\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"date\"\tTEXT,\n" +
                    "\t\"supplierNumber\"\tTEXT)";
            statement.execute(query);

            //table productsInOrder
            query = "CREATE TABLE IF NOT EXISTS \"ProductsInOrder\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"productId\"\tINTEGER,\n" +
                    "\t\"count\"\tINTEGER,\n" +
                    "\tPRIMARY KEY(\"orderId\" , \"productId\")\n"+ ")";
            statement.execute(query);
            //table deliveryTerms
            query = "CREATE TABLE IF NOT EXISTS \"DeliveryTerms\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"daysToDeliver\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"orderId\",\"daysToDeliver\")\n"+ ")";
            statement.execute(query);

            //table pastOrder
            query = "CREATE TABLE IF NOT EXISTS \"PastOrdersSupplier\"  (\n" +
                    "\t\"orderId\"\tINTEGER,\n" +
                    "\t\"finishDate\"\tTEXT,\n" +
                    "\t\"totalPrice\"\tREAL,\n" +
                    "\t\"supplierNumber\"\tINTEGER,\n" +
                    "\tPRIMARY KEY(\"orderId\", \"finishDate\")\n"+ ")";
            statement.execute(query);

            //transport - Employees tabels
            query = "CREATE TABLE IF NOT EXISTS Employees (\n" +
                    "id VARCHAR(9),\n" +
                    "name VARCHAR(200),\n" +
                    "password VARCHAR(200),\n" +
                    "salary DECIMAL,\n" +
                    "contract VARCHAR(255),\n" +
                    "bankAccount VARCHAR(50),\n" +
                    "monthlyHours DECIMAL,\n" +
                    "dateOfEmployment DATE,\n" +
                    "PRIMARY KEY (id)\n" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Roles (\n" +
                    "id VARCHAR(9),\n" +
                    "jobType VARCHAR(30),\n" +
                    "PRIMARY KEY (id, jobType),\n" +
                    "FOREIGN KEY (id) REFERENCES Employees(id) ON DELETE CASCADE" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Shifts (" +
                    "timeOfDay VARCHAR(8)," + //MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "start VARCHAR(6)," +
                    "end VARCHAR(6)," +
                    "PRIMARY KEY (timeOfDay, day, month, year)" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS EmployeesInShift(" +
                    "timeOfDay VARCHAR(8)," + // MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "employeeID VARCHAR(9)," +
                    "job VARCHAR(30)," +
                    "PRIMARY KEY (employeeID ,timeOfDay, day, month, year)," +
                    "FOREIGN KEY (timeOfDay, day, month, year) REFERENCES Shifts(timeOfDay, day, month, year)" +
                    " ON DELETE CASCADE" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Schedules(" +
                    "id VARCHAR(9)," +
                    "timeOfDay VARCHAR(8)," + // MORNING/EVENING
                    "day VARCHAR(2)," +
                    "month VARCHAR(2)," +
                    "year VARCHAR(4)," +
                    "PRIMARY KEY (id, day, timeOfDay)," +
                    "FOREIGN KEY (id) REFERENCES Employees(id) ON DELETE CASCADE" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Messages(" +
                    "messageID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "message VARCHAR(500)," +
                    "read VARCHAR(5)" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS DriversLicenses (" +
                    "id VARCHAR(9)," +
                    "license VARCHAR(5)," +
                    "PRIMARY KEY (id, license)," +
                    "FOREIGN KEY (id) REFERENCES Drivers(id) ON DELETE CASCADE" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS DriverAvailability(" +
                    "id VARCHAR(9)," +
                    "date VARCHAR(10)," +
                    "time VARCHAR(10)," +
                    "available VARCHAR(2)" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Trucks(" +
                    "licensePlate varchar(20)," +
                    "type varchar (5)," +
                    "maxWeight DECIMAL," +
                    "initialWeight DECIMAL," +
                    "PRIMARY KEY (licensePlate)" +
                    ")";
            statement.execute(query);
            query = "CREATE Table IF NOT EXISTS Drivers(" +
                    "name varchar(15)," +
                    "id varchar(20)," +
                    "license varchar(10)," +
                    "PRIMARY KEY (id)" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS TrucksAvailability(" +
                    "licenseplate varchar(30)," +
                    "date varchar(30)," +
                    "time varchar (10)," +
                    "available varchar(3)," +
                    "PRIMARY KEY (licenseplate,date,time)" +
                    ")";
            statement.execute(query);
            query = "CREATE Table IF NOT EXISTS OrderDocs(" +
                    "id varchar(20)," +
                    "driverID varchar(9)," +
                    "licensePlate varchar(8)," +
                    "origin varchar (50)," +
                    "date varchar (20)," +
                    "time varchar (20)," +
                    "weight DECIMAL," +
                    "finished varchar(3),"+
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (driverID) REFERENCES Employees(id)" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Destinations(" +
                    "siteID VARCHAR(50)," +
                    "orderDocID VARCHAR(20)," +
                    "PRIMARY KEY (siteID, orderDocID)," +
                    "FOREIGN KEY (orderDocID) REFERENCES OrderDocs(id) ON DELETE CASCADE" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS order4Dest(" +
                    "siteID varchar (50)," +
                    "orderDocID varchar (20)," +
                    "supply varchar (50)," +
                    "quantity integer," +
                    "PRIMARY KEY (siteID,orderDocID,supply)," +
                    "FOREIGN KEY (orderDocID) REFERENCES OrderDocs(id) ON DELETE CASCADE" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS DriverDocs(" +
                    "id INTEGER ," +
                    "siteID VARCHAR(50)," +
                    "driverID VARCHAR(9)," +
                    "orderDocID VARCHAR (30)," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (driverID) REFERENCES Drivers(id)" +
                    ")";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Supplies(" +
                    "name VARCHAR(100)," +
                    "weight DECIMAL, " +
                    "PRIMARY KEY (name)" +
                    ")";
            statement.execute(query);

            query = "CREATE TABLE IF NOT EXISTS Stores(" +
                    "id varchar (20)," +
                    "type INTEGER," +
                    "shippingArea INTEGER," +
                    "contactPNumber varchar (20)," +
                    "contactName varchar (100)," +
                    "contactAddress varchar (100)," +
                    "primary KEY (id)" +
                    ")";
            statement.execute(query);

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


    public List<HashMap<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String,Object>> list = new ArrayList<>();

        while (rs.next()) {
            HashMap<String,Object> row = new HashMap<>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    public List<HashMap<String,Object>> executeQuery(String query, Object... params) throws SQLException {
        try  {
            createStatement();
            PreparedStatement statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                statement.setObject(i+1, params[i]);
            ResultSet rs = statement.executeQuery();
            return convertResultSetToList((rs));
        } catch (SQLException throwable) {
            throw throwable;
        }
        finally {
            closeConnect();
        }
    }


    public int executeUpdate(String query,Object... params) throws SQLException {
        try  {
            createStatement();
            PreparedStatement statement = conn.prepareStatement("PRAGMA foreign_keys = ON");
            statement.executeUpdate();
            statement = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                statement.setObject(i+1, params[i]);

            int res = statement.executeUpdate();
            return res;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            closeConnect();
        }
    }

    public Response updateRecordInTable(String tableName, String columnName, String recordID, Object idValue, Object newValue) throws SQLException {
        try {
            Statement statement = createStatement();
            String query = "UPDATE " + tableName + " SET " + columnName + " = " + newValue +
                    " WHERE " + recordID + " = " + idValue;
            statement.execute(query);
            return new Response();
        } catch (SQLException se) {
            return new Response("Failed to connect to DB");
        }
        finally {
            closeConnect();
        }
    }


    public String deleteRecordFromTableSTR(String tableName, String columnName, String recordID) throws SQLException {
        try  {
            createStatement();
            Statement statement = conn.createStatement();
            String query = "DELETE FROM " + tableName +
                    " WHERE " + columnName + " = " + "'"+ recordID + "'";
            statement.execute(query);
            return "Success";
        } catch (SQLException throwable) {
            return "Failed to connect to DB";
        } finally {
            closeConnect();
        }
    }

    public void deleteRecordsOfTables() throws SQLException {
        try (Statement stmt = createStatement()) {
            //table products
            String query = "DROP TABLE IF EXISTS Products";
            stmt.execute(query);
            ProductDAO.reset();
            //table category discount
            query = "DROP TABLE IF EXISTS CategoryDiscount";
            stmt.execute(query);
            CategoryDAO.reset();
            OrdersFromSupplierDAO.reset();
            ProductsSupplierDAO.reset();
            SuppliersDAO.reset();
            //table category subCategory
            query = "DROP TABLE IF EXISTS SubCategories";
            stmt.execute(query);
            //table subCategory subSubCategory
            query = "DROP TABLE IF EXISTS SubSubCategories";
            stmt.execute(query);
            //table items
            query = "DROP TABLE IF EXISTS Items";
            stmt.execute(query);

            //table product supplier
            query = "DROP TABLE IF EXISTS ProductSupplier";
            stmt.execute(query);
            //table DiscountProductSupplier
            query = "DROP TABLE IF EXISTS DiscountProductSupplier";
            // amount discount on specific product to supplier
            stmt.execute(query);

            //table suppliers
            query = "DROP TABLE IF EXISTS Suppliers";
            stmt.execute(query);

            //add table contacts
            query = "DROP TABLE IF EXISTS ContactsSupplier";
            stmt.execute(query);

            //table discount
            query = "DROP TABLE IF EXISTS DiscountSupplier";
            //amount discount on sum of products in order

            stmt.execute(query);
            //table orderFromSupplier
            query = "DROP TABLE IF EXISTS OrdersFromSupplier";
            stmt.execute(query);

            //table productsInOrder
            query = "DROP TABLE IF EXISTS ProductsInOrder";
            stmt.execute(query);
            //table deliveryTerms
            query = "DROP TABLE IF EXISTS DeliveryTerms";
            stmt.execute(query);

            //table pastOrder
            query = "DROP TABLE IF EXISTS PastOrdersSupplier";
            stmt.execute(query);

            query = "DROP TABLE IF EXISTS Employees";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS EmployeesInShift";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Roles";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Schedules";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Shifts";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Drivers";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS DriverAvailability";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Destinations";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS DriverDocs";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS DriversLicenses";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Messages";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS OrderDocs";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Stores";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Supplies";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS Trucks";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS TrucksAvailability";
            stmt.execute(query);
            query = "DROP TABLE IF EXISTS order4Dest";
            stmt.execute(query);
            createTables();





        } catch (SQLException e) {
            throw e;
        }
        finally {
            closeConnect();
        }

    }

}
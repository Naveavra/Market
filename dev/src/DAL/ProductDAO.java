package DAL;

import DomainLayer.Storage.Product;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductDAO {
    private Connect connect;
    private static HashMap<Integer, Product> IMProducts;
    //private static HashMap<Pair<Pair<String, String>, String>, List<Product>> IMProductByCategory;

    /**
     * constructor
     * the connect is singleton
     */
    public ProductDAO() {
        connect = Connect.getInstance();
        IMProducts = new HashMap<>();
        IMProductByCategory = new HashMap<>();
    }

    public boolean insert(Product p, String catName, String subCatName, String subSubCatName) throws SQLException {
        if (get(p.getId()) == null) {
            String query = "INSERT INTO Products (productId,name,description,maker,storageAmount,storeAmount,timesBought,price,discount,dayAdded, needsRefill, categoryName,subCategoryName,subSubCategoryName)" +
                    " VALUES " + String.format("(%d,\"%s\",\"%s\",\"%s\",%d,%d,%d,%f,%f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")", p.getId(), p.getName(), p.getDescription(), p.getMaker(),
                    p.getStorageAmount(), p.getStoreAmount(), p.getTimesBought(), p.getPrice(), p.getDiscount(),p.getDayAdded(), p.getNeedsRefill()+"", catName, subCatName, subSubCatName);
            try (Statement stmt = connect.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                return false;
            } finally {
                IMProducts.put(p.getId(), p);
                Pair<Pair<String, String>, String> key=new Pair<>(new Pair<>(catName, subCatName), subSubCatName);
                if(!IMProductByCategory.containsKey(key))
                    IMProductByCategory.put(key, new LinkedList<>());
                IMProductByCategory.get(key).add(p);
                connect.closeConnect();
            }
            return true;
        }
        return false;
    }

    public Product get(int productId) throws SQLException {
        int key = productId;
        if (IMProducts.containsKey(key)) {
            return IMProducts.get(key);
        }
        String query = "SELECT * FROM Products WHERE " +
                String.format("productId=%d", productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (!rs.isClosed()) {
                Product p = new Product(rs.getInt("productId"), rs.getString("name"),
                        rs.getString("description"), rs.getInt("price"), rs.getString("maker"));
                p.setTimesBought(rs.getInt("timesBought"));
                p.setStorageAmount(rs.getInt("storageAmount"));
                p.setStoreAmount(rs.getInt("storeAmount"));
                p.setDayAdded(rs.getString("dayAdded"));
                String needsRefill=rs.getString("needsRefill");
                p.setNeedsRefill(!needsRefill.equals("false"));
                IMProducts.put(p.getId(), p);
                return p;
            }
            return null;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }


    public Product get(String productName) throws SQLException {
        String query = "SELECT * FROM Products WHERE " +
                String.format("name=\"%s\"", productName);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (!rs.isClosed()) {
                Product p = new Product(rs.getInt("productId"), rs.getString("name"),
                        rs.getString("description"), rs.getInt("price"), rs.getString("maker"));
                p.setTimesBought(rs.getInt("timesBought"));
                p.setStorageAmount(rs.getInt("storageAmount"));
                p.setStoreAmount(rs.getInt("storeAmount"));
                p.setDayAdded(rs.getString("dayAdded"));
                String needsRefill=rs.getString("needsRefill");
                p.setNeedsRefill(!needsRefill.equals("false"));
                IMProducts.put(p.getId(), p);
                return p;
            }
            return null;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }


    public List<Product> getAllProducts() throws SQLException {
        String query = "SELECT * FROM Products";
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            List<Product> ans=new LinkedList<>();
                while(!rs.isClosed()) {
                    Product p = new Product(rs.getInt("productId"), rs.getString("name"),
                            rs.getString("description"), rs.getInt("price"), rs.getString("maker"));
                    p.setTimesBought(rs.getInt("timesBought"));
                    p.setStorageAmount(rs.getInt("storageAmount"));
                    p.setStoreAmount(rs.getInt("storeAmount"));
                    p.setDayAdded(rs.getString("dayAdded"));
                    String needsRefill = rs.getString("needsRefill");
                    p.setNeedsRefill(!needsRefill.equals("false"));
                    ans.add(p);
                    IMProducts.put(p.getId(), p);
                    rs.next();
                }
            return ans;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }

    //functions to get products of categories
    public List<Product> getAllByCategory(String category) throws SQLException {
        List<Product> ans=new LinkedList<>();
        String query = "SELECT * FROM Products WHERE " +
                String.format("categoryName=\"%s\"", category);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                while (!rs.isClosed()) {
                    Product p = new Product(rs.getInt("productId"), rs.getString("name"),
                            rs.getString("description"), rs.getInt("price"), rs.getString("maker"));
                    p.setTimesBought(rs.getInt("timesBought"));
                    p.setStorageAmount(rs.getInt("storageAmount"));
                    p.setStoreAmount(rs.getInt("storeAmount"));
                    p.setDayAdded(rs.getString("dayAdded"));
                    String needsRefill=rs.getString("needsRefill");
                    p.setNeedsRefill(!needsRefill.equals("false"));
                    if (!IMProducts.containsKey(p.getId()))
                        IMProducts.put(p.getId(), p);
                    ans.add(p);
                    rs.next();
                }
            }
            return ans;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }


    public List<Product> getAllBySubCategory(String category, String subCategory) throws SQLException {
        List<Product> ans=new LinkedList<>();
        String query = "SELECT * FROM Products WHERE " +
                String.format("categoryName=\"%s\" AND subCategoryName=\"%s\"", category, subCategory);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                while (!rs.isClosed()) {
                    Product p = new Product(rs.getInt("productId"), rs.getString("name"),
                            rs.getString("description"), rs.getInt("price"), rs.getString("maker"));
                    p.setTimesBought(rs.getInt("timesBought"));
                    p.setStorageAmount(rs.getInt("storageAmount"));
                    p.setStoreAmount(rs.getInt("storeAmount"));
                    p.setDayAdded(rs.getString("dayAdded"));
                    String needsRefill=rs.getString("needsRefill");
                    p.setNeedsRefill(!needsRefill.equals("false"));
                    if (!IMProducts.containsKey(p.getId()))
                        IMProducts.put(p.getId(), p);
                    ans.add(p);
                    rs.next();
                }
            }
            return ans;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }
    public List<Product> getAllBySubSubCategory(String category, String subCategory, String subSubCategory) throws SQLException {
        Pair<Pair<String , String>, String> key = new Pair<>(new Pair<>(category, subCategory), subSubCategory);
        if (IMProductByCategory.containsKey(key)) {
            return IMProductByCategory.get(key);
        }
        String query = "SELECT * FROM Products WHERE " +
                String.format("categoryName=\"%s\" AND subCategoryName=\"%s\" AND subSubCategoryName=\"%s\"", category, subCategory
                ,subSubCategory);
        List<Product> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                IMProductByCategory.put(key, new LinkedList<>());
                while (!rs.isClosed()) {
                    Product p = new Product(rs.getInt("productId"), rs.getString("name"),
                            rs.getString("description"), rs.getInt("price"), rs.getString("maker"));
                    p.setTimesBought(rs.getInt("timesBought"));
                    p.setStorageAmount(rs.getInt("storageAmount"));
                    p.setStoreAmount(rs.getInt("storeAmount"));
                    p.setDayAdded(rs.getString("dayAdded"));
                    String needsRefill=rs.getString("needsRefill");
                    p.setNeedsRefill(!needsRefill.equals("false"));
                    if (!IMProducts.containsKey(p.getId()))
                        IMProducts.put(p.getId(), p);
                    IMProductByCategory.get(key).add(p);

                    ans.add(p);
                    rs.next();
                }
            }
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
        return ans;
    }


    public void updateProduct(Product p) throws SQLException {
        int key=p.getId();
        String query = "UPDATE Products" +
                String.format(" SET name=\"%s\", description=\"%s\", maker=\"%s\",storageAmount=%d, storeAmount=%d,timesBought=%d, price=%f, discount=%f, dayAdded=\"%s\", needsRefill=\"%s\"",
                        p.getName(), p.getDescription(), p.getMaker(), p.getStorageAmount(), p.getStoreAmount(), p.getTimesBought(),
                        p.getPrice(), p.getDiscount(), p.getDayAdded(), p.getNeedsRefill()+"") +
                String.format(" WHERE productId=%d", key);
        try (Statement stmt = connect.createStatement()) {
            stmt.executeQuery(query);
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }

    public void updateCategoriesForProduct(int productId, String catName, String subCatName, String subSubCatName) throws SQLException {
        String query = "UPDATE Products" +
                String.format(" SET categoryName=\"%s\", subCategoryName=\"%s\", subSubCategoryName=\"%s\"", catName, subCatName, subSubCatName)+
                String.format(" WHERE productId=%d", productId);
        try (Statement stmt = connect.createStatement()) {
            stmt.executeQuery(query);
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }

    public void removeProduct(int productId) throws SQLException {
        String query = String.format("DELETE FROM Products WHERE productId=%d",
                productId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }
}

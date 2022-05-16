package DAL;

import DomainLayer.Storage.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO {

    private Connect connect;
    private static HashMap<String, Category> IMCategories;
    private static HashMap<String[], Category> IMSubCategories;
    private static HashMap<String[], Category> IMSubSubCategories;

    /**
     * constructor
     * the connect is singleton
     */
    public CategoryDAO() {
        connect = Connect.getInstance();
        IMCategories=new HashMap<>();
        IMSubCategories=new HashMap<>();
        IMSubSubCategories=new HashMap<>();
    }

    public static void reset() {
        IMCategories=new HashMap<>();
        IMSubCategories=new HashMap<>();
        IMSubSubCategories=new HashMap<>();
    }

    public boolean insertIntoDiscount(String category, double discount) throws SQLException {
            String query = "INSERT INTO CategoryDiscount (categoryName,discount)" +
                    " VALUES " + String.format("(\"%s\",%f)", category, discount);
            try (Statement stmt = connect.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                return false;
            } finally {
                Category c=new Category(category);
                c.setDiscount(discount);
                IMCategories.put(category, c);
                connect.closeConnect();
            }
            return true;
    }

    public void updateDiscount(String category, double discount) throws SQLException {
        String query = "UPDATE CategoryDiscount" +
                String.format(" SET discount=%f", discount)+
                String.format(" WHERE categoryName=\"%s\"", category);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            if(IMCategories.containsKey(category))
                IMCategories.get(category).setDiscount(discount);
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }


    public boolean hasCategory(String category) throws SQLException {
        if(IMCategories.containsKey(category))
            return true;
        String query = "SELECT * FROM CategoryDiscount WHERE " +
                String.format("categoryName=\"%s\"", category);
        boolean ans=false;
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            ans=!rs.isClosed();
        } catch (SQLException throwable) {
            ans=false;
        } finally {
            connect.closeConnect();
        }
        return ans;
    }

    public List<Category> getCategories() throws SQLException {
        String query = "SELECT * FROM CategoryDiscount";
        List<Category> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while (!rs.isClosed()){
                Category c=new Category(rs.getString("categoryName"));
                c.setDiscount(rs.getDouble("discount"));
                ans.add(c);
                rs.next();
            }
            return ans;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }

    public void removeCategory(String category) throws SQLException {
        String query = String.format("DELETE FROM CategoryDiscount WHERE categoryName=\"%s\"",
                category);
        String query2 = String.format("DELETE FROM SubCategories WHERE categoryName=\"%s\"",
                category);
        String query3 = String.format("DELETE FROM SubSubCategories WHERE categoryName=\"%s\"",
                category);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            stmt.execute(query2);
            stmt.execute(query3);
            IMCategories.remove(category);

            List<String[]> sub=new LinkedList<>();
            for(String[] key : IMSubCategories.keySet())
                if(key[0].equals(category))
                    sub.add(key);
            for(String[] key : sub)
                IMSubCategories.remove(key);

            List<String[]>subSub=new LinkedList<>();
            for(String[] key : IMSubSubCategories.keySet())
                if(key[0].equals(category))
                    subSub.add(key);
            for(String[] key : subSub)
                IMSubSubCategories.remove(key);

        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }

    }


    //sub table
    public boolean insertIntoSubCategory(String category, String subCategory) throws SQLException {
        if(hasCategory(category)) {
            String query = "INSERT INTO SubCategories (categoryName,subCategoryName)" +
                    " VALUES " + String.format("(\"%s\",\"%s\")", category, subCategory);
            try (Statement stmt = connect.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                return false;
            } finally {
                String[] sub=new String[2];
                sub[0]=category;
                sub[1]=subCategory;
                IMSubCategories.put(sub, new Category(subCategory));
                connect.closeConnect();
            }
            return true;
        }
        return false;
    }

    public boolean hasSubCategory(String category, String subCategory) throws SQLException {
        for(String[] key : IMSubCategories.keySet())
            if(key[0].equals(category) && key[1].equals(subCategory))
                return true;
            String query = "SELECT * FROM SubCategories WHERE " +
                    String.format("categoryName=\"%s\" AND subCategoryName=\"%s\"", category, subCategory);
            boolean ans;
            try (Statement stmt = connect.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                if(!rs.isClosed()){
                    ans=true;
                    String[] sub=new String[2];
                    sub[0]=category;
                    sub[1]=subCategory;
                    IMSubCategories.put(sub, new Category(subCategory));
                }
                else
                    ans=false;
            } catch (SQLException throwable) {
                ans = false;
            } finally {
                connect.closeConnect();
            }
            return ans;
    }

    public List<Category> getSubCategories(String category) throws SQLException {
        String query = "SELECT * FROM SubCategories WHERE " +
                String.format("categoryName=\"%s\"", category);
        List<Category> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while (!rs.isClosed()){
                Category c=new Category(rs.getString("subCategoryName"));
                ans.add(c);
                rs.next();
            }
            return ans;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }

    public void removeSubCategory(String category, String subCategory) throws SQLException {
        String query = String.format("DELETE FROM SubCategories WHERE categoryName=\"%s\" AND subCategoryName=\"%s\"",
                category, subCategory);
        String query2 = String.format("DELETE FROM SubSubCategories WHERE categoryName=\"%s\" AND subCategoryName=\"%s\"",
                category, subCategory);

        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            stmt.execute(query2);

            List<String[]> sub=new LinkedList<>();
            for(String[] key : IMSubCategories.keySet())
                if(key[0].equals(category) && key[1].equals(subCategory))
                    sub.add(key);
            for(String[] key : sub)
                IMSubCategories.remove(key);

            List<String[]>subSub=new LinkedList<>();
            for(String[] key : IMSubSubCategories.keySet())
                if(key[0].equals(category) && key[1].equals(subCategory))
                    subSub.add(key);
            for(String[] key : subSub)
                IMSubSubCategories.remove(key);

        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }



    //sub sub table
    public boolean insertIntoSubSubCategory(String category, String subCategory, String subSubCategory) throws SQLException {
        if (hasSubCategory(category, subCategory)) {
            String query = "INSERT INTO SubSubCategories (categoryName, subCategoryName,subSubCategoryName)" +
                    " VALUES " + String.format("(\"%s\",\"%s\",\"%s\")", category, subCategory, subSubCategory);
            try (Statement stmt = connect.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                return false;
            } finally {
                String[] sub=new String[3];
                sub[0]=category;
                sub[1]=subCategory;
                sub[2]=subSubCategory;
                IMSubSubCategories.put(sub, new Category(subSubCategory));
                connect.closeConnect();
            }
            return true;
        }
        return false;
    }


    public boolean hasSubSubCategory(String category, String subCategory, String subSubCategory) throws SQLException {
        for(String[] key : IMSubSubCategories.keySet())
            if(key[0].equals(category) && key[1].equals(subCategory) && key[2].equals(subSubCategory))
                return true;
            String query = "SELECT * FROM SubSubCategories WHERE " +
                    String.format("categoryName=\"%s\" AND subCategoryName=\"%s\" AND subSubCategoryName=\"%s\"", category, subCategory, subSubCategory);
            boolean ans;
            try (Statement stmt = connect.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                ans = !rs.isClosed();
            } catch (SQLException throwable) {
                ans = false;
            } finally {
                connect.closeConnect();
            }
            return ans;
    }

    public List<Category> getSubSubCategories(String category, String subCategory) throws SQLException {
        String query = "SELECT * FROM SubSubCategories WHERE " +
                String.format("categoryName=\"%s\" AND subCategoryName=\"%s\"", category, subCategory);
        List<Category> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while (!rs.isClosed()){
                Category c=new Category(rs.getString("subSubCategoryName"));
                ans.add(c);
                rs.next();
            }
            return ans;
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
    }


    public void removeSubSubCategory(String category, String subCategory, String subSubCategory) throws SQLException {
        String query = String.format("DELETE FROM SubSubCategories WHERE categoryName=\"%s\" AND subCategoryName=\"%s\" AND subSubCategoryName=\"%s\"",
                category, subCategory, subSubCategory);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);

            List<String[]>subSub=new LinkedList<>();
            for(String[] key : IMSubSubCategories.keySet())
                if(key[0].equals(category) && key[1].equals(subCategory) && key[2].equals(subSubCategory))
                    subSub.add(key);
            for(String[] key : subSub)
                IMSubSubCategories.remove(key);

        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

}

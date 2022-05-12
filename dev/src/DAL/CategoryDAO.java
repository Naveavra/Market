package DAL;

import DomainLayer.Storage.Category;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO {

    private Connect connect;
    private static HashMap<String, Category> IMCategories;
    private static HashMap<Pair<String, String>, Category> IMSubCategories;
    private static HashMap<Pair<Pair<String, String>, String>, Category> IMSubSubCategories;

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
            stmt.executeQuery(query);
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

            List<Pair<String, String >> sub=new LinkedList<>();
            for(Pair<String, String> key : IMSubCategories.keySet())
                if(key.getKey().equals(category))
                    sub.add(key);
            for(Pair<String, String> key : sub)
                IMSubCategories.remove(key);

            List<Pair<Pair<String, String>, String>>subSub=new LinkedList<>();
            for(Pair<Pair<String, String>, String> key : IMSubSubCategories.keySet())
                if(key.getKey().getKey().equals(category))
                    subSub.add(key);
            for(Pair<Pair<String, String>, String> key : subSub)
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
                IMSubCategories.put(new Pair<>(category, subCategory), new Category(subCategory));
                connect.closeConnect();
            }
            return true;
        }
        return false;
    }

    public boolean hasSubCategory(String category, String subCategory) throws SQLException {
        for(Pair<String, String> key : IMSubCategories.keySet())
            if(key.getKey().equals(category) && key.getValue().equals(subCategory))
                return true;
            String query = "SELECT * FROM SubCategories WHERE " +
                    String.format("categoryName=\"%s\" AND subCategoryName=\"%s\"", category, subCategory);
            boolean ans;
            try (Statement stmt = connect.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                if(!rs.isClosed()){
                    ans=true;
                    IMSubCategories.put(new Pair<>(category, subCategory), new Category(subCategory));
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

    public List<String> getSubCategories(String category) throws SQLException {
        String query = "SELECT * FROM SubCategories WHERE " +
                String.format("categoryName=\"%s\"", category);
        List<String> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while (!rs.isClosed()){
                ans.add(rs.getString("subCategoryName"));
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

            List<Pair<String, String >> sub=new LinkedList<>();
            for(Pair<String, String> key : IMSubCategories.keySet())
                if(key.getKey().equals(category) && key.getValue().equals(subCategory))
                    sub.add(key);
            for(Pair<String, String> key : sub)
                IMSubCategories.remove(key);

            List<Pair<Pair<String, String>, String>>subSub=new LinkedList<>();
            for(Pair<Pair<String, String>, String> key : IMSubSubCategories.keySet())
                if(key.getKey().getKey().equals(category) && key.getKey().getValue().equals(subCategory))
                    subSub.add(key);
            for(Pair<Pair<String, String>, String> key : subSub)
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
                IMSubSubCategories.put(new Pair<>(new Pair<>(category, subCategory), subSubCategory), new Category(subSubCategory));
                connect.closeConnect();
            }
            return true;
        }
        return false;
    }


    public boolean hasSubSubCategory(String category, String subCategory, String subSubCategory) throws SQLException {
        for(Pair<Pair<String, String>, String> key : IMSubSubCategories.keySet())
            if(key.getKey().getKey().equals(category) && key.getKey().getValue().equals(subCategory) && key.getValue().equals(subSubCategory))
                return true;
            String query = "SELECT * FROM SubSubCategories WHERE " +
                    String.format("categoryName=\"%s\" AND subCategoryName=\"%s\" AND subSubCategoryName=\"%s\"", category, subCategory, subSubCategory);
            boolean ans = false;
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

    public List<String> getSubSubCategories(String category, String subCategory) throws SQLException {
        String query = "SELECT * FROM SubSubCategories WHERE " +
                String.format("categoryName=\"%s\" AND subCategoryName=\"%s\"", category, subCategory);
        List<String> ans=new LinkedList<>();
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while (!rs.isClosed()){
                ans.add(rs.getString("subSubCategoryName"));
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

            List<Pair<Pair<String, String>, String>>subSub=new LinkedList<>();
            for(Pair<Pair<String, String>, String> key : IMSubSubCategories.keySet())
                if(key.getKey().getKey().equals(category) && key.getKey().getValue().equals(subCategory) && key.getValue().equals(subSubCategory))
                    subSub.add(key);
            for(Pair<Pair<String, String>, String> key : subSub)
                IMSubSubCategories.remove(key);

        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }
    }

}

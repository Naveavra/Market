package DAL;

import DomainLayer.Storage.Item;
import DomainLayer.Storage.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ItemDAO {

    private Connect connect;


    /**
     * constructor
     * the connect is singleton
     */
    public ItemDAO() {
        connect = Connect.getInstance();
    }

    public boolean insert(Item item, int productId) throws SQLException {
        if (productExists(productId)) {
            String query = "INSERT INTO Items (productId,expirationDate,place,shelf,isDamaged, defectiveDescription)" +
                    " VALUES " + String.format("(%d,\"%s\",\"%s\",%d,\"%s\",\"%s\")", productId, item.getExpDate(),
                    item.getLoc().getPlace().toString(), item.getLoc().getShelf(), item.getIsDamaged(), item.getDefectiveDescription());
            try (Statement stmt = connect.createStatement()) {
                stmt.execute(query);
            } catch (SQLException e) {
                return false;
            } finally {
                connect.closeConnect();
            }
            return true;
        }
        return false;
    }

    public boolean setItemDamaged(int productId, String expirationDate, String place, int shelf, String damageDescription) throws SQLException {
        String query = "SELECT itemId FROM Items WHERE " +
                String.format("productId=%d AND expirationDate=\"%s\" AND place=\"%s\" AND shelf=%d AND isDamaged=\"%s\"",
                        productId, expirationDate, place, shelf, "false");
        int itemId=-1;
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (!rs.isClosed()) {
                itemId=rs.getInt("itemId");
                query="UPDATE Items" +
                        String.format(" SET isDamaged=\"%s\", defectiveDescription=\"%s\"", "true", damageDescription)+
                        String.format(" WHERE itemId=%d", itemId);
                stmt.execute(query);
                return true;
            }
        } catch (SQLException throwable) {
            return false;
        } finally {
            connect.closeConnect();
        }
        return false;
    }


    public List<Item> getAllItemsOfProduct(Product product){
        int key = product.getId();
        String query = "SELECT * FROM Items WHERE " +
                String.format("productId=%d AND isDamaged=\"%s\"", key, "false");
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                List<Item> ans=new LinkedList<>();
                while (!rs.isClosed()) {
                    Item i = new Item(product.getName(), rs.getString("place"), rs.getInt("shelf"), rs.getString("expirationDate"));
                    String isDamaged=rs.getString("isDamaged");
                    i.setDefectiveDescription(rs.getString("defectiveDescription"));
                    i.setDamaged(!isDamaged.equals(false+""));
                    ans.add(i);
                    rs.next();
                }
                return ans;
            }
        } catch (SQLException throwable) {
            return null;
        } finally {
            try {
                connect.closeConnect();
            } catch (SQLException ignored) {

            }
        }
        return null;
    }


    public Item getItem(Product p, String place, int shelf, String expirationDate){
        int key = p.getId();
        String query = "SELECT * FROM Items WHERE " +
                String.format("productId=%d AND place=\"%s\" AND shelf=%d AND expirationDate=\"%s\" AND isDamaged=\"%s\"", key,
                        place, shelf, expirationDate, "false");
        Item i=null;
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                i = new Item(p.getName(), rs.getString("place"), rs.getInt("shelf"), rs.getString("expirationDate"));
                String isDamaged=rs.getString("isDamaged");
                i.setDefectiveDescription(rs.getString("defectiveDescription"));
                i.setDamaged(!isDamaged.equals(false+""));
                }
                return i;
    } catch (SQLException throwable) {
            return null;
        } finally {
            try {
                connect.closeConnect();
            } catch (SQLException ignored) {
            }
        }
    }


    public void removeItem(int productId, Item i) throws SQLException {
        //get item id - take 1 and remove
        int key = productId;
        String query = "SELECT itemId FROM Items WHERE " +
                String.format("productId=%d AND place=\"%s\" AND shelf=%d AND expirationDate=\"%s\" AND isDamaged=\"%s\"", key,
                        i.getLoc().getPlace().toString(), i.getLoc().getShelf(), i.getExpDate(), i.getIsDamaged());
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                int id=rs.getInt("itemId");
                query = String.format("DELETE FROM Items WHERE itemId=%d",
                        id);
                stmt.execute(query);
            }
        } catch (SQLException e) {
            throw e;
        }
        finally {
            connect.closeConnect();
        }

    }

    public List<Item> getDamagedItemsOfProduct(Product product){
        int key = product.getId();
        String query = "SELECT * FROM Items WHERE " +
                String.format("productId=%d AND isDamaged=\"%s\" AND defectiveDescription !=\"%s\"", key, "true", "expired date");
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                List<Item> ans=new LinkedList<>();
                while (!rs.isClosed()) {
                    Item i = new Item(product.getName(), rs.getString("place"), rs.getInt("shelf"), rs.getString("expirationDate"));
                    String isDamaged=rs.getString("isDamaged");
                    i.setDefectiveDescription(rs.getString("defectiveDescription"));
                    i.setDamaged(!isDamaged.equals(false+""));
                    ans.add(i);
                    rs.next();
                }
                return ans;
            }
        } catch (SQLException throwable) {
            return new LinkedList<>();
        } finally {
            try {
                connect.closeConnect();
            } catch (SQLException ignored) {

            }
        }
        return new LinkedList<>();
    }


    public List<Item> getExpiredItemsOfProduct(Product product){
        int key = product.getId();
        String query = "SELECT * FROM Items WHERE " +
                String.format("productId=%d AND isDamaged=\"%s\" AND defectiveDescription =\"%s\"", key, "true", "expired date");
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if(!rs.isClosed()) {
                List<Item> ans=new LinkedList<>();
                while (!rs.isClosed()) {
                    Item i = new Item(product.getName(), rs.getString("place"), rs.getInt("shelf"), rs.getString("expirationDate"));
                    String isDamaged=rs.getString("isDamaged");
                    i.setDefectiveDescription(rs.getString("defectiveDescription"));
                    i.setDamaged(!isDamaged.equals(false+""));
                    ans.add(i);
                    rs.next();
                }
                return ans;
            }
        } catch (SQLException throwable) {
            return new LinkedList<>();
        } finally {
            try {
                connect.closeConnect();
            } catch (SQLException ignored) {

            }
        }
        return new LinkedList<>();
    }

    public void moveToPlace(Product product, Item item, String place, int shelf) throws SQLException {
            String query = "SELECT itemId FROM Items WHERE " +
                    String.format("productId=%d AND place=\"%s\" AND shelf=%d AND expirationDate=\"%s\" AND isDamaged=\"%s\"",
                            product.getId(), item.getLoc().getPlace().toString(), item.getLoc().getShelf(), item.getExpDate(),
                            item.getIsDamaged());
            int itemId = -1;
            try (Statement stmt = connect.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                        itemId = rs.getInt("itemId");
                        query = "UPDATE Items" +
                                String.format(" SET place=\"%s\", shelf=%d", place, shelf) +
                                String.format(" WHERE itemId=%d", itemId);
                        stmt.execute(query);
            } catch (SQLException throwable) {
                throw throwable;
            } finally {
                connect.closeConnect();
            }
    }

    public int moveSection(Product product, String place, int amount) throws SQLException {
        String query = "SELECT itemId FROM Items WHERE " +
                String.format("productId=%d AND isDamaged=\"%s\"",product.getId(), "false");
        int count=0;
        int itemId = -1;
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            for (int i = 0; i < amount && !rs.isClosed(); i++) {
                count++;
                itemId = rs.getInt("itemId");
                query = "UPDATE Items" +
                        String.format(" SET place=\"%s\"", place) +
                        String.format(" WHERE itemId=%d", itemId);
                stmt.execute(query);
            }
        } catch (SQLException throwable) {
            return count;
        } finally {
            connect.closeConnect();
        }
        return count;
    }

    private int getLastId(){
        int ans=0;
        String query = "SELECT itemId FROM Items";
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            while(!rs.isClosed()) {
                ans++;
                rs.next();
            }
        } catch (SQLException throwable) {
            return 1;
        } finally {

            try {
                connect.closeConnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ans+1;
    }

    private boolean productExists(int productId) throws SQLException {
        String query = "SELECT * FROM Products WHERE " +
                String.format("productId=%d", productId);
        try (Statement stmt = connect.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (!rs.isClosed()) {
                return true;
            }
        } catch (SQLException throwable) {
            throw throwable;
        } finally {
            connect.closeConnect();
        }
        return false;
    }

    public void removeAllItems(int productId) throws SQLException {
        String query = String.format("DELETE FROM Items WHERE productId=%d",productId);
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(query);
            }
        catch (SQLException ignored) {
        }
        finally {
            connect.closeConnect();
        }
    }
}

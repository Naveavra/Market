package DomainLayer.Storage;

import DAL.ItemDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Product
{
    private int productId;
    private String name;
    private String description;
    private String maker;// brand of the product
    private int storeAmount;
    private int storageAmount;
    private transient int timesBought;
    private int amountNeededForRefill;
    private String dayAdded;
    private double price;
    private double discount;
    private transient ItemDAO itemsDAO;
    /**
     *
     * @param productId
     * @param pName
     * @param desc
     * @param price
     * @param maker
     */
    public Product(int productId, String pName, String desc, double price, String maker)
    {
        this.productId = productId;
        this.name = pName;
        this.description=desc;
        this.price = price;
        this.storeAmount = 0;
        this.storageAmount = 0;
        this.timesBought=0;
        this.amountNeededForRefill=0;
        this.dayAdded= LocalDate.now().toString();
        this.discount=0;
        this.maker=maker;
        this.itemsDAO = new ItemDAO();
    }

    public double getPrice() {
        return price;
    }


    public int getCurAmount() {
        return storeAmount+storageAmount;
    }

    public int getRefill(){
        amountNeededForRefill=2*calcMinAmount()-getCurAmount();
        if(amountNeededForRefill<0)
            amountNeededForRefill=0;
        return amountNeededForRefill;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    public List<Item> getItems() {
        return itemsDAO.getAllItemsOfProduct(this);
    }

    public Item removeItem(Item i, boolean bought){
        if(i!=null) {
            if (i.getLoc().getPlace() == Location.Place.STORAGE)
                this.storageAmount--;
            else
                this.storeAmount--;
            try {
                this.itemsDAO.removeItem(productId, i);
            } catch (SQLException e) {
                if (i.getLoc().getPlace() == Location.Place.STORAGE)
                    this.storageAmount++;
                else
                    this.storeAmount++;
            }
            if(bought)
                timesBought++;
        }
        return i;

    }

    public void addItem(String  loc, int shelf, String ed){
        Location.Place add=stringToPlace(loc);
        if(add.equals(Location.Place.STORAGE))
            storageAmount++;
        else
            storeAmount++;
        try {
            itemsDAO.insert(new Item(name, new Location(add, shelf), ed), productId);
        } catch (SQLException e) {
            System.out.println("oh no");
            if(add.equals(Location.Place.STORAGE))
                storageAmount--;
            else
                storeAmount--;
        }
    }
    public int getId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public List<Item> getDamagedItems(){
        return itemsDAO.getDamagedItemsOfProduct(this);
    }

    public List<Item> getExpiredItems(){
        return itemsDAO.getExpiredItemsOfProduct(this);
    }


    public int calcMinAmount(){
        double calc=timesBought;
        return (int) (Math.ceil(calc/getDaysPassed()));
    }
    private List<Item> canBuy(int amount){
        List<Item> allItems=itemsDAO.getAllItemsOfProduct(this);
        List<Item> ans=new LinkedList<>();
        if(allItems.size()>=amount) {
            for (Item i : allItems) {
                if (i.validDate())
                    ans.add(i);
                else {
                    try {
                        itemsDAO.setItemDamaged(productId, i.getExpDate(), i.getLoc().getPlace().toString(), i.getLoc().getShelf(), "expired date");
                        if (i.getLoc().getPlace().equals(Location.Place.STORAGE))
                            storageAmount--;
                        else
                            storeAmount--;
                    } catch (SQLException ignored) {
                        if (i.getLoc().getPlace().equals(Location.Place.STORAGE))
                            storageAmount++;
                        else
                            storeAmount++;
                    }
                }
            }
        }
        return ans;
    }

    public double buyAmount(int amount){
        List<Item> inStock=canBuy(amount);
        if(inStock.size()>=amount) {
            double ans = 0;
            for (int i = 0; i < amount && i < inStock.size(); i++) {
                Item item=inStock.get(i);
                removeItem(item, true);
                ans += (price - (price * discount / 100));
            }
            return ans;
        }
        return -1;
    }

    public Item findItem(String ed, String curPlace, int curShelf){
        return itemsDAO.getItem(this, curPlace, curShelf, ed);
    }

    public void transferItem(String ed, String curPlace, int curShelf, String toPlace, int toShelf){
        Item i=findItem(ed, curPlace, curShelf);
        if(i!=null) {
            if(i.getLoc().getPlace().equals(Location.Place.STORAGE)) {
                storageAmount--;
                storeAmount++;
            }
            else {
                storeAmount--;
                storageAmount++;
            }
            try {
                itemsDAO.moveToPlace(this, i, toPlace, toShelf);
            } catch (SQLException e) {
                if(i.getLoc().getPlace().equals(Location.Place.STORAGE)) {
                    storageAmount++;
                    storeAmount--;
                }
                else {
                    storeAmount++;
                    storageAmount--;
                }
            }
            //update item in DAO
        }

    }

    public void addAllItems(int amount, String ed, int shelf) {
        for(int i=0;i<amount;i++){
            addItem("STORAGE", shelf, ed);
        }
        getRefill();
    }

    public void moveToStore(int amount){
        try {
            int count=itemsDAO.moveSection(this, "STORE", amount);
            while(count>0){
                storeAmount++;
                storageAmount--;
                count--;
            }
        } catch (SQLException ignored) {
        }
    }

    public boolean getNeedsRefill(){
        return getRefill()>0;
    }

    public double getDiscount(){
        return discount;
    }
    private static Location.Place stringToPlace(String s)
    {
        if(s.equals("STORAGE"))
            return Location.Place.STORAGE;
        else
            return Location.Place.STORE;
    }


    public String getDescription(){
        return description;
    }
    public String getMaker(){
        return maker;
    }

    public int getStoreAmount(){
        return storeAmount;
    }
    public int getStorageAmount(){
        return storageAmount;
    }

    public int getTimesBought(){
        return timesBought;
    }

    public void setTimesBought(int times){
        timesBought=times;
    }
    public void setStoreAmount(int storeAmount){this.storeAmount=storeAmount;}
    public void setStorageAmount(int storageAmount){this.storageAmount=storageAmount;}


    public void setItemDamaged(int productId, String expirationDate, String place, int shelf, String damageDescription){
        try {
            if(itemsDAO.setItemDamaged(productId, expirationDate, place, shelf, damageDescription)) {
                if (place.equals("STORAGE"))
                    storageAmount--;
                else
                    storeAmount--;
            }

        } catch (SQLException ignored) {
        }
    }

    private int getDaysPassed(){
        String curDate= LocalDate.now().toString();
        int curYear=Integer.parseInt(curDate.substring(0, 4));
        int curMonth=Integer.parseInt(curDate.substring(5, 7));
        int curDay=Integer.parseInt(curDate.substring(8, 10));
        int year=Integer.parseInt(dayAdded.substring(0, 4));
        int month=Integer.parseInt(dayAdded.substring(5, 7));
        int day=Integer.parseInt(dayAdded.substring(8, 10));
        return (curYear-year)*365+(curMonth-month)*30+(curDay-day)+1;
    }

    public String getDayAdded(){
        return dayAdded;
    }

    public void setDayAdded(String dayAdded){
        this.dayAdded=dayAdded;
    }

    public boolean removeAllItems() {
        try {
            itemsDAO.removeAllItems(productId);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


}

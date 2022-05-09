package DomainLayer.Storage;

import DAL.ItemDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Product
{
    private int productId;
    private String name;
    private String description;
    private String maker;
    private int storeAmount;
    private int storageAmount;
    private transient int timesBought;//remove
    private String dayAdded;//remove
    private int daysForResupply;
    private int minAmount;
    private int amountToRefill;
    private double price;
    private double priceSupplier;//remove
    private double discount;
    private transient boolean needsRefill;
    private transient ItemDAO items;
    private transient List<Integer> productSuppliers;//all the supplier that supplier the product

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
        this.priceSupplier = 0;
        this.price = price;
        this.storeAmount = 0;
        this.storageAmount = 0;
        this.daysForResupply=0;
        this.timesBought=0;
        this.dayAdded= LocalDate.now().toString();
        this.minAmount=0;
        this.amountToRefill=0;
        this.discount=0;
        this.maker=maker;
        this.items = new ItemDAO();
        this.needsRefill=false;
    }



    public double getPriceSupplier() {
        return priceSupplier;
    }

    public void setDaysForResupply(int change){
        daysForResupply=change;
    }

    public double getPrice() {
        return price;
    }

    public int getMinAmount() {
        return minAmount;
    }


    public boolean hasItem(String loc, int shelf, String ed){
        Item i =items.getItem(this, loc, shelf, ed);
        return i != null;
    }

    public int getCurAmount() {
        return storeAmount+storageAmount;
    }
    public int getRefill(){
        return amountToRefill;
    }

    public void setPriceSupplier(int priceSupplier) {
        this.priceSupplier = priceSupplier;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    public List<Item> getItems() {
        return items.getAllItemsOfProduct(this);
    }

    public Item removeItem(String loc, int shelf, String ed, boolean bought){
        Item i=findItem(ed, loc, shelf);
        if(i!=null) {
            if (i.getLoc().getPlace() == Location.Place.STORAGE)
                this.storageAmount--;
            else
                this.storeAmount--;
            try {
                this.items.removeItem(productId, i);
            } catch (SQLException e) {
                if (i.getLoc().getPlace() == Location.Place.STORAGE)
                    this.storageAmount++;
                else
                    this.storeAmount++;
            }
            if(bought)
                timesBought++;
        }
        toRefill();
        return i;

    }

    public void addItem(String  loc, int shelf, String ed){
        Location.Place add=stringToPlace(loc);
        if(add.equals(Location.Place.STORAGE))
            storageAmount++;

        else
            storeAmount++;
        try {
            items.insert(new Item(name, new Location(add, shelf), ed), productId);
        } catch (SQLException e) {
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
        return items.getDamagedItemsOfProduct(this);
    }
    public void calcMinAmount(){
        double calc=daysForResupply*timesBought;
        minAmount= (int) (Math.ceil(calc/getDaysPassed()));
    }
    public void toRefill(){
        calcMinAmount();
        amountToRefill=minAmount+timesBought/getDaysPassed()-getCurAmount();
        if(amountToRefill<0)
            amountToRefill=0;
    }

    public boolean canBuy(int amount){
        if(items.getAllItemsOfProduct(this).size()<amount)
            return false;
        else{
            int canBuy=0;
            for(Item i : items.getAllItemsOfProduct(this)) {
                if (i.validDate())
                    canBuy++;
                else {
                    try {
                        items.setItemDamaged(productId, i.getExpDate(), i.getLoc().getPlace().toString(), i.getLoc().getShelf(), "expired date");
                    } catch (SQLException ignored) {
                    }
                }

            }
            return canBuy>=amount;
        }
    }

    public double buyAmount(int amount){
        if(canBuy(amount)) {
            List<Item> inStock=items.getAllItemsOfProduct(this);
            for (int i = 0; i < amount&& i<inStock.size(); i++) {
                String check;
                if(inStock.get(i).getLoc().getPlace().equals(Location.Place.STORAGE))
                    check= "STORAGE";
                else
                    check= "STORE";
                removeItem(check, inStock.get(i).getLoc().getShelf(), inStock.get(i).getExpDate(), true);
            }
            return (price - (price * discount / 100)) * amount;
        }
        return -1;
    }

    public Item findItem(String ed, String curPlace, int curShelf){
        return items.getItem(this, curPlace, curShelf, ed);
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
            Location.Place check=stringToPlace(toPlace);
            i.transferPlace(check, toShelf);
        }

    }

    public void addAllItems(int amount, String ed, int shelf) {
        for(int i=0;i<amount;i++){
            addItem("STORAGE", shelf, ed);
        }
        toRefill();
    }

    public void moveToStore(int amount){
        try {
            items.moveSection(this, "STORE", amount);
        } catch (SQLException ignored) {
        }
    }

    public void setNeedsRefill(boolean ans){
        needsRefill=ans;
    }
    public boolean getNeedsRefill(){
        return needsRefill;
    }

    public double getDiscount(){
        return discount;
    }

    public double getDaysForResupply() {
        return this.daysForResupply;
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

    public void setPriceSupplier(double price){
        priceSupplier=price;
    }

    public void setTimesBought(int times){
        timesBought=times;
    }
    public void setStoreAmount(int storeAmount){this.storeAmount=storeAmount;}
    public void setStorageAmount(int storageAmount){this.storageAmount=storageAmount;}


    public void setItemDamaged(int productId, String expirationDate, String place, int shelf, String damageDescription){
        try {
            items.setItemDamaged(productId, expirationDate, place, shelf, damageDescription);
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

}

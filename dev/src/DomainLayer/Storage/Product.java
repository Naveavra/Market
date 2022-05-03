package DomainLayer.Storage;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Product
{
    private int id;
    private String name;
    private String description;
    private String maker;
    private int storeAmount;
    private int storageAmount;
    private transient int timesBought;
    private transient int daysPassed;
    private double daysForResupply;
    private int minAmount;
    private int amountToRefill;
    private double price;
    private double priceSupplier;
    private double discount;
    private transient boolean needsRefill;
    private List<main.java.DomainLayer.Storage.Item> items;
    private transient List<Item> damagedItems;


    public Product(int pId, String pName, String desc, int daysForResupply, double priceSupplier, double price, String maker)
    {
        this.id = pId;
        this.name = pName;
        this.description=desc;
        this.priceSupplier = priceSupplier;
        this.price = price;
        this.storeAmount = 0;
        this.storageAmount = 0;
        this.daysForResupply=daysForResupply;
        this.timesBought=0;
        this.daysPassed=1;
        this.minAmount=0;
        this.amountToRefill=0;
        this.discount=0;
        this.maker=maker;
        this.items = new LinkedList<>();
        this.damagedItems=new LinkedList<>();
        this.needsRefill=false;
    }



    public double getPriceSupplier() {
        return priceSupplier;
    }

    public void setDaysForResupply(double change){
        daysForResupply=change;
    }

    public double getPrice() {
        return price;
    }

    public int getMinAmount() {
        return minAmount;
    }


    public boolean hasItem(Location.Place loc, int shelf, String ed){
        for(Item i : this.items) {
            if (i.getLoc().getPlace().equals(loc) && shelf == i.getLoc().getShelf() && ed.equals(i.getExpDate())) {
                return true;
            }
        }
        return false;
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
        return items;
    }

    public Item removeItem(Location.Place loc, int shelf, String ed, boolean bought)
    {
        Item i=null;
        if(hasItem(loc, shelf, ed))
            i=findItem(ed, loc, shelf);
        if(i!=null) {
            if (i.getLoc().getPlace() == Location.Place.STORAGE)
                this.storageAmount--;
            else
                this.storeAmount--;
            this.items.remove(i);
            if(bought)
                timesBought++;
        }
        toRefill();
        return i;

    }

    public void addItem(Location.Place  loc, int shelf, String ed){
        if(loc.equals(Location.Place.STORAGE))
            storageAmount++;
        else
            storeAmount++;
        items.add(new Item(id, name, new Location(loc, shelf), ed));
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item> getDamagedItems(){
        return damagedItems;
    }
    public void removeDamagedItems(){
        damagedItems=new LinkedList<>();
    }
    public void calcMinAmount(){
        minAmount= (int) (Math.ceil(daysForResupply*timesBought/daysPassed));
    }
    public void toRefill(){
        calcMinAmount();
        amountToRefill=minAmount+timesBought/daysPassed-getCurAmount();
        if(amountToRefill<0)
            amountToRefill=0;
    }

    public boolean canBuy(int amount){
        if(items.size()<amount)
            return false;
        else{
            String date=LocalDate.now().toString();
            int canBuy=0;
            for(Item i : items) {
                if (i.validDate(date))
                    canBuy++;
                else
                    damagedItems.add(i);

            }

            for(int i=0;i<damagedItems.size();i++)
                items.remove(damagedItems.get(i));
            return canBuy>=amount;
        }
    }

    public double buyAmount(int amount){
        if(canBuy(amount)) {
            for (int i = 0; i < amount&& i<items.size(); i++)
                removeItem(items.get(i).getLoc().getPlace(), items.get(i).getLoc().getShelf(), items.get(i).getExpDate(), true);
            return (price - (price * discount / 100)) * amount;
        }
        return -1;
    }

    public Item findItem(String ed, Location.Place curPlace, int curShelf){
        for(Item i : items)
            if(i.getLoc().getPlace().equals(curPlace) && i.getLoc().getShelf()==curShelf && i.getExpDate().equals(ed))
                return i;
        return null;
    }

    public void transferItem(String ed, Location.Place curPlace, int curShelf, Location.Place toPlace, int toShelf){
        Item i=null;
        if(hasItem(curPlace, curShelf, ed))
            i=findItem(ed, curPlace, curShelf);
        if(i!=null) {
            if(!i.getLoc().getPlace().equals(toPlace)){
                if(i.getLoc().getPlace().equals(Location.Place.STORAGE)) {
                    storageAmount--;
                    storeAmount++;
                }
                else {
                    storeAmount--;
                    storageAmount++;
                }
            }
            i.transferPlace(toPlace, toShelf);
        }

    }

    public void addAllItems(int amount, String ed, int shelf){
        for(int i=0;i<amount;i++){
            addItem(Location.Place.STORAGE, shelf, ed);
        }
        toRefill();
    }

    public void moveToStore(int amount){
        for(int i=0;i<amount&&i<items.size();i++)
            if (items.get(i).getLoc().getPlace().equals(Location.Place.STORAGE)) {
                items.get(i).transferPlace(Location.Place.STORE, items.get(i).getLoc().getShelf());
                storeAmount++;
                storageAmount--;
            }
    }

    public void setNeedsRefill(boolean ans){
        needsRefill=ans;
    }
    public boolean getNeedsRefill(){
        return needsRefill;
    }
    public void setDaysPassed(int days){
        daysPassed=days;
    }

    public double getDiscount(){
        return discount;
    }

    public double getDaysForResupply() {
        return this.daysForResupply;
    }

}

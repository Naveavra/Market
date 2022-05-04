package DomainLayer.Storage;

import java.time.LocalDate;
import java.util.LinkedList;
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
    private transient int daysPassed;//remove
    private int daysForResupply;
    private int minAmount;
    private int amountToRefill;
    private double price;
    private double priceSupplier;//remove
    private double discount;
    private transient boolean needsRefill;
    private List<Item> items;
    private transient List<Item> damagedItems;
    private transient List<Product> productSuppliers;//all the supplier that supplier the product

    /**
     *
     * @param productId
     * @param pName
     * @param desc
     * @param daysForResupply
     * @param priceSupplier
     * @param price
     * @param maker
     */
    public Product(int productId, String pName, String desc, int daysForResupply, double priceSupplier, double price, String maker)
    {
        this.productId = productId;
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
        Location.Place check=stringToPlace(loc);
        for(Item i : this.items) {
            if (i.getLoc().getPlace().equals(check) && shelf == i.getLoc().getShelf() && ed.equals(i.getExpDate())) {
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

    public Item removeItem(String loc, int shelf, String ed, boolean bought)
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

    public void addItem(String  loc, int shelf, String ed){
        Location.Place add=stringToPlace(loc);
        if(add.equals(Location.Place.STORAGE))
            storageAmount++;

        else
            storeAmount++;

        items.add(new Item(name, new Location(add, shelf), ed));
    }
    public int getId() {
        return productId;
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
        double calc=daysForResupply*timesBought;
        minAmount= (int) (Math.ceil(calc/daysPassed));
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
            for (int i = 0; i < amount&& i<items.size(); i++) {
                String check;
                if(items.get(i).getLoc().getPlace().equals(Location.Place.STORAGE))
                    check= "Storage";
                else
                    check= "Store";
                removeItem(check, items.get(i).getLoc().getShelf(), items.get(i).getExpDate(), true);
            }
            return (price - (price * discount / 100)) * amount;
        }
        return -1;
    }

    public Item findItem(String ed, String curPlace, int curShelf){

        for(Item i : items)
            if(i.getLoc().getPlace().equals(stringToPlace(curPlace)) && i.getLoc().getShelf()==curShelf && i.getExpDate().equals(ed))
                return i;
        return null;
    }

    public void transferItem(String ed, String curPlace, int curShelf, String toPlace, int toShelf){
        Item i=null;

        if(hasItem(curPlace, curShelf, ed))
            i=findItem(ed, curPlace, curShelf);
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

    public void addAllItems(int amount, String ed, int shelf){
        for(int i=0;i<amount;i++){
            addItem("Storage", shelf, ed);
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


    public static Location.Place stringToPlace(String s)
    {
        if(s.equals("STORE"))
            return Location.Place.STORE;
        else
            return Location.Place.STORAGE;
    }

}

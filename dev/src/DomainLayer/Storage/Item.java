package DomainLayer.Storage;

import java.time.LocalDate;

public class Item {
    private String name;
    private Location loc;
    private String expirationDate;
    private String defectiveDescription;//if why damage
    private transient boolean isDamaged;

    public Item(String name, Location loc, String ed){
        this.name=name;
        this.loc=loc;
        this.expirationDate=ed;
        this.defectiveDescription = null;
        this.isDamaged=false;

    }

    public Item(String name, String place, int shelf, String ed){
        this.name=name;
        loc=new Location(stringToPlace(place), shelf);
        this.expirationDate=ed;
    }

    public String getName() {
        return name;
    }

    public Location getLoc()
    {
        return this.loc;
    }

    public String getExpDate()
    {
        return this.expirationDate;
    }

    public void setDefectiveDescription(String description)
    {
        this.defectiveDescription = description;
        isDamaged=true;
    }

    public void setDamaged(boolean set){
        isDamaged=set;
    }

    public boolean validDate(){
        String curDate= LocalDate.now().toString();
        int curYear=Integer.parseInt(curDate.substring(0, 4));
        int curMonth=Integer.parseInt(curDate.substring(5, 7));
        int curDay=Integer.parseInt(curDate.substring(8, 10));
        int year=Integer.parseInt(expirationDate.substring(0, 4));
        int month=Integer.parseInt(expirationDate.substring(5, 7));
        int day=Integer.parseInt(expirationDate.substring(8, 10));
        if(year<curYear)
            return false;
        else if(year==curYear && month<curMonth)
            return false;
        else if(year==curYear && month==curMonth && day<curDay)
            return false;
        return true;
    }
    public void transferPlace(Location.Place place, int shelf){
        Location location=new Location(place, shelf);
        loc=location;
    }

    public String getIsDamaged(){
        return isDamaged+"";
    }

    public String getDefectiveDescription(){
        return defectiveDescription;
    }


    private static Location.Place stringToPlace(String s)
    {
        if(s.equals("STORAGE"))
            return Location.Place.STORAGE;
        else
            return Location.Place.STORE;
    }


}

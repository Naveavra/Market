package main.java.DomainLayer.Storage;

public class Item {
    private int pId;
    private String name;
    private Location loc;
    private String expirationDate;
    private String defectiveDescription;

    public Item(int id, String name, Location loc, String ed){
        this.pId=id;
        this.name=name;
        this.loc=loc;
        this.expirationDate=ed;
        this.defectiveDescription = null;

    }

    public int getId() {
        return pId;
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
    }

    public boolean validDate(String curDate){
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


}

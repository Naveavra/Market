package DomainLayer.Storage;

public class Location
{
    public enum Place {STORE , STORAGE}

    private Place place;
    private int shelf;//num shelf

    public Location(Place place, int shelf)
    {
        this.place = place;
        this.shelf = shelf;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;
    }

    @Override
    public String toString(){
        return " place: "+place+" shelf: "+shelf;
    }
}
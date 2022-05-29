package DomainLayer.Transport;

public class Date {
    String year;
    String month;
    String day;

    public Date(String day, String month, String year){
        this.day = day;
        this.year = year;
        this.month = month;
    }

    public Date getDate(){
        return this;
    }
    public String toString(){
     return day+"/"+month+"/"+year;
    }
}

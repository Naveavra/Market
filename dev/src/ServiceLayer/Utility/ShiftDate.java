package ServiceLayer.Utility;

public class ShiftDate {
    private final String year;
    private final String month;
    private final String day;

    public ShiftDate(String day, String month, String year){
        this.day = day;
        this.year = year;
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public boolean equals(ShiftDate other){
        return this.day.equals(other.day) && this.month.equals(other.month) && this.year.equals(other.year);
    }

    public String toString(){
        return day+"/"+month+"/"+year;
    }
}

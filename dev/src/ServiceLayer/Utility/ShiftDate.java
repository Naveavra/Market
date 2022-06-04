package ServiceLayer.Utility;

import java.util.Calendar;

public class ShiftDate {
    private final String year;
    private final String month;
    private final String day;

    public ShiftDate(String day, String month, String year){
        this.day = day;
        this.year = year;
        this.month = month;
    }

    public ShiftDate(){
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (day.length() == 1)
            day = "0" + day;
        if (month.length() == 1)
            month = "0" + month;
        this.day = day;
        this.month = month;
        this.year = year;
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

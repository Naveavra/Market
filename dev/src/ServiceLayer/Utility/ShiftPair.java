package ServiceLayer.Utility;

import DomainLayer.Employees.Time;

import java.util.Locale;

public class ShiftPair{
    private final ShiftDate shiftDate;
    private final Time time;

    public ShiftPair(ShiftDate shiftDate, Time time){
        this.shiftDate = shiftDate;
        this.time = time;
    }

    public ShiftDate getDate() { return shiftDate; }

    public Time getTime() {
        return time;
    }

    public boolean equals(ShiftPair other){
        return this.time.compareTo(other.getTime()) == 0 && this.shiftDate.equals(other.getDate());
    }

    @Override
    public String toString() { return "Date: " + shiftDate + ", Time: " + time; }

    public String when() { return shiftDate.toString() + " " + time.toString().toLowerCase(Locale.ROOT); }
}

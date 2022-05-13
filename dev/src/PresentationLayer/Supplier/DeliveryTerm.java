package PresentationLayer.Supplier;
import DomainLayer.Supplier.*;
import java.util.HashMap;
import java.util.Map;

public class DeliveryTerm {
    public enum DaysInWeek {
        Sunday,
        monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday
    }
    Map<String, DaysInWeek> M  = new HashMap<String, DaysInWeek>() {{
        put("1", DaysInWeek.Sunday);
        put("2", DaysInWeek.monday);
        put("3", DaysInWeek.Tuesday);
        put("4", DaysInWeek.Wednesday);
        put("5", DaysInWeek.Thursday);
        put("6", DaysInWeek.Friday);
        put("7", DaysInWeek.Saturday);

    }};

    private DaysInWeek[] daysInWeeks;
    //private boolean isSupplierDeliver;

    public DeliveryTerm(DaysInWeek[] daysInWeek){

        this.daysInWeeks=new DaysInWeek[daysInWeek.length];
        System.arraycopy(daysInWeek, 0, this.daysInWeeks, 0, daysInWeek.length);
    }
    public String toString(){
        StringBuilder out= new StringBuilder();
        for(DaysInWeek a: daysInWeeks){
            out.append(a.toString()).append(",");
        }
        return out.substring(0, out.toString().length()-1);
    }

}

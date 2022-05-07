package DomainLayer;

import java.util.HashMap;
import java.util.List;
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

    public DeliveryTerm(List<String> days){
        daysInWeeks = new DaysInWeek[days.size()];
        for(int i=0; i< days.size(); i++){
            daysInWeeks[i] = fromStringToDays(days.get(i));
        }
    }

    /**
     *
     * @param days
     * @return
     */
    public boolean updateFixedDeliveryDays(String[] days){
        daysInWeeks = new DaysInWeek[days.length];
        for(int i=0; i< days.length; i++){
            daysInWeeks[i] = fromStringToDays(days[i]);
        }
        return true;
    }

    private DaysInWeek fromStringToDays(String day) {
        return M.get(day);
    }

    public boolean stopFixedDeliveryDays(){
        daysInWeeks = null;
        return true;
    }
    public boolean isEmpty()
    {
        return this.daysInWeeks.length==0;
    }
    public String toString(){
        StringBuilder out= new StringBuilder();
        for(DaysInWeek a: daysInWeeks){
            out.append(a.toString()).append(",");
        }
        return out.substring(0, out.toString().length()-1);
    }
}

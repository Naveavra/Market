package DomainLayer.Supplier;

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
    public static Map<String, DaysInWeek> intToEnum  = new HashMap<String, DaysInWeek>() {{
        put("1", DaysInWeek.Sunday);
        put("2", DaysInWeek.monday);
        put("3", DaysInWeek.Tuesday);
        put("4", DaysInWeek.Wednesday);
        put("5", DaysInWeek.Thursday);
        put("6", DaysInWeek.Friday);
        put("7", DaysInWeek.Saturday);

    }};


    private DaysInWeek[] daysInWeeks;

    public  DaysInWeek[] getDaysInWeeks(){
        return daysInWeeks;
    }
    public DeliveryTerm(DaysInWeek[] daysInWeek) {
        if (daysInWeek != null) {
            this.daysInWeeks = new DaysInWeek[daysInWeek.length];
            System.arraycopy(daysInWeek, 0, this.daysInWeeks, 0, daysInWeek.length);
        }
        else
            this.daysInWeeks=new DaysInWeek[0];
    }
    public DeliveryTerm (String days)  {
        if(days.length()!=0) {
            this.daysInWeeks = new DaysInWeek[days.length()];
            char[] arr = days.toCharArray();
            int i = 0;
            for (char c : arr) {
                daysInWeeks[i] = fromStringToDays(String.valueOf(c));
                i++;
            }
        }
        else
            this.daysInWeeks=new DaysInWeek[0];
    }

    public DeliveryTerm(List<String> days){
        if(days!=null) {
            this.daysInWeeks = new DaysInWeek[days.size()];
            for (int i = 0; i < days.size(); i++) {
                daysInWeeks[i] = fromStringToDays(days.get(i));
            }
        }
        else
            this.daysInWeeks=new DaysInWeek[0];
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
        return intToEnum.get(day);
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
    public int getDayValue(DaysInWeek d){
        return daysToInt.get(d);
    }
    public Map<DaysInWeek,Integer> daysToInt = new HashMap<DaysInWeek,Integer>() {{
        put(DaysInWeek.Sunday,7);
        put(DaysInWeek.monday,1);
        put(DaysInWeek.Tuesday,2);
        put(DaysInWeek.Wednesday,3);
        put(DaysInWeek.Thursday,4);
        put(DaysInWeek.Friday,5);
        put(DaysInWeek.Saturday,6);

    }};
}

package DomainLayer.Suppliers;

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
        Saturday,
        NoDays
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
            DaysInWeek[] dd = new DaysInWeek[days.length()];
            char[] arr = days.toCharArray();
            int i = 0;
            for (char c : arr) {
                String s= String.valueOf(c);
                dd[i]=intToEnum.get(s);
                i++;
            }
            this.daysInWeeks=dd;
        }
        else {
            DaysInWeek[] dd = new DaysInWeek[1];
             dd[0] = DaysInWeek.NoDays;
             this.daysInWeeks = dd;
        }
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
    public static Map<DaysInWeek,Integer> daysToInt = new HashMap<DaysInWeek,Integer>() {{
        put(DaysInWeek.Sunday,1);
        put(DaysInWeek.monday,2);
        put(DaysInWeek.Tuesday,3);
        put(DaysInWeek.Wednesday,4);
        put(DaysInWeek.Thursday,5);
        put(DaysInWeek.Friday,6);
        put(DaysInWeek.Saturday,7);
        put(DaysInWeek.NoDays,8);
    }};

    public static Integer getDay(DaysInWeek d){
        return daysToInt.get(d);
    }
    public static String getDayAsString(DaysInWeek d){
        return String.valueOf(daysToInt.get(d));
    }
}

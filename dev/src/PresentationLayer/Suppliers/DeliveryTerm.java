package PresentationLayer.Suppliers;
import java.util.HashMap;
import java.util.Map;

public class DeliveryTerm {
    public DeliveryTerm(DomainLayer.Suppliers.DeliveryTerm.DaysInWeek[] daysInWeeks) {
        DaysInWeek[] day = new DaysInWeek[daysInWeeks.length];
        int i=0;
        for(DomainLayer.Suppliers.DeliveryTerm.DaysInWeek d : daysInWeeks){
            day[i] =fromIntToDay.get(DomainLayer.Suppliers.DeliveryTerm.getDay(d));
        }
        this.daysInWeeks=day;

    }

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
    public Map<String, DaysInWeek> intToEnum = new HashMap<String, DaysInWeek>() {{
        put("1", DaysInWeek.Sunday);
        put("2", DaysInWeek.monday);
        put("3", DaysInWeek.Tuesday);
        put("4", DaysInWeek.Wednesday);
        put("5", DaysInWeek.Thursday);
        put("6", DaysInWeek.Friday);
        put("7", DaysInWeek.Saturday);
        put("8",DaysInWeek.NoDays);
    }};
    public Map<DaysInWeek,String> fromDayToString  = new HashMap<DaysInWeek,String>() {{
        put(DaysInWeek.Sunday,"1");
        put(DaysInWeek.monday,"2");
        put(DaysInWeek.Tuesday,"3");
        put(DaysInWeek.Wednesday,"4");
        put(DaysInWeek.Thursday,"5");
        put(DaysInWeek.Friday,"6");
        put(DaysInWeek.Saturday,"7");
        put(DaysInWeek.NoDays,"no days");

    }};
    public Map<Integer, DaysInWeek> fromIntToDay  = new HashMap<Integer, DaysInWeek>() {{
        put(1, DaysInWeek.Sunday);
        put(2, DaysInWeek.monday);
        put(3, DaysInWeek.Tuesday);
        put(4, DaysInWeek.Wednesday);
        put(5, DaysInWeek.Thursday);
        put(6, DaysInWeek.Friday);
        put(7, DaysInWeek.Saturday);
        put(8,DaysInWeek.NoDays);
    }};

    private DaysInWeek[] daysInWeeks;
    //private boolean isSupplierDeliver;

    public DeliveryTerm(DaysInWeek[] daysInWeek){

        this.daysInWeeks=new DaysInWeek[daysInWeek.length];
        System.arraycopy(daysInWeek, 0, this.daysInWeeks, 0, daysInWeek.length);
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
    public String toString(){
        StringBuilder out= new StringBuilder();
        for(DaysInWeek a: daysInWeeks){
            out.append(getString(a)).append(",");
        }
        return out.substring(0, out.toString().length()-1);
    }
public String getString(DaysInWeek a){
        return fromDayToString.get(a);
}
}

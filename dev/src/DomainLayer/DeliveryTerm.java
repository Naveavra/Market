package DomainLayer;

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

    private DaysInWeek[] daysInWeeks;
    //private boolean isSupplierDeliver;
    
    public boolean updateFixedDeliveryDays(String[] days){
        daysInWeeks = new DaysInWeek[days.length];
        for(int i=0; i< days.length; i++){
            daysInWeeks[i] = fromStringToDays(days[i]);
        }
        return true;
    }

    private DaysInWeek fromStringToDays(String day) {
        return DaysInWeek.valueOf(day);
    }

    public boolean stopFixedDeliveryDays(){
        daysInWeeks = null;
        return true;
    }

}

package PresentationLayer.Supplier;

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
    Map<String, DomainLayer.DeliveryTerm.DaysInWeek> M  = new HashMap<String, DomainLayer.DeliveryTerm.DaysInWeek>() {{
        put("1", DomainLayer.DeliveryTerm.DaysInWeek.Sunday);
        put("2", DomainLayer.DeliveryTerm.DaysInWeek.monday);
        put("3", DomainLayer.DeliveryTerm.DaysInWeek.Tuesday);
        put("4", DomainLayer.DeliveryTerm.DaysInWeek.Wednesday);
        put("5", DomainLayer.DeliveryTerm.DaysInWeek.Thursday);
        put("6", DomainLayer.DeliveryTerm.DaysInWeek.Friday);
        put("7", DomainLayer.DeliveryTerm.DaysInWeek.Saturday);

    }};

    private DomainLayer.DeliveryTerm.DaysInWeek[] daysInWeeks;
    //private boolean isSupplierDeliver;

    public DeliveryTerm(DomainLayer.DeliveryTerm.DaysInWeek[] daysInWeek){

        this.daysInWeeks=new DomainLayer.DeliveryTerm.DaysInWeek[daysInWeek.length];
        System.arraycopy(daysInWeek, 0, this.daysInWeeks, 0, daysInWeek.length);
    }
}

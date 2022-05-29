package DomainLayer.Transport;

import java.util.concurrent.ConcurrentHashMap;

public class Driver  {

    private String id;
    private String license; //
    private String name;
    private boolean available;
    private final ConcurrentHashMap<String, Boolean> dates; //date - available

    public Driver(String name, String id, String license){
        this.id = id;
        this.name = name;
        this.license = license;
        this.available = true;
        dates = new ConcurrentHashMap<>();
    }

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getId(){return id;}

    public void setId(String id){this.id = id;}

    public String getLicense(){return license;}

    public void setLicense(String license) {
        this.license = license;
    }

    public void isAvailable(Date d) {
        dates.put(d.toString(),true);
    }
    public boolean isFree(Date d){
        return dates.getOrDefault(d.toString(),true);
    }
    public void isoccupied(Date date){
        dates.put(date.toString(), false);
    }



    public String toString(){
        return "Name: "+name+" ,ID: "+id+ " ,License: "+license + ", Available: "+ available;
    }


    public void finished(Date date) {
        dates.put(date.toString(),true);
    }
}
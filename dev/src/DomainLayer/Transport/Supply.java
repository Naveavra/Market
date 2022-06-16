package DomainLayer.Transport;

public class Supply {
    String name;
    double weight;
    String id;

    public Supply(String name, double weight, String id){
        this.name = name;
        this.weight = weight;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
    public String getId(){return id;}
    
    public String toString(){
        return "Name: "+ name +" ,Weight: "+ weight;
    }
}

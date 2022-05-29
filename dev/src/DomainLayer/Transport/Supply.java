package DomainLayer.Transport;

public class Supply {
    String name;
    double weight;

    public Supply(String name, double weight){
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
    
    public String toString(){
        return "Name: "+ name +" ,Weight: "+ weight;
    }
}

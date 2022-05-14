package ServiceLayer;

import DomainLayer.Facade;

import java.util.List;

public class ReportService {
    private Facade facade;

    public ReportService(){
        facade=new Facade();
    }


    public boolean makeReport(List<String> catNames){
        return facade.makeReport(catNames);
    }

    public boolean makeDamagedReport(){
        return facade.makeDamagedReport();
    }


    public boolean makeRefillReport(){
        return facade.makeRefillReport();
    }

    public boolean makeProductReport(int id){
        return facade.makeProductReport(id);
    }
}



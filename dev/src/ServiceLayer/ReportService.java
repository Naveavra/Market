package ServiceLayer;

import DomainLayer.FacadeSupplier_Storage;

import java.util.List;

public class ReportService {
    private FacadeSupplier_Storage facadeSupplier;

    public ReportService(){
        facadeSupplier =new FacadeSupplier_Storage();
    }


    public boolean makeCatReport(List<String> catNames){
        return facadeSupplier.makeCatReport(catNames);
    }

    public boolean makeDamagedReport(){
        return facadeSupplier.makeDamagedReport();
    }


    public boolean makeRefillReport(){
        return facadeSupplier.makeRefillReport();
    }

    public boolean makeProductReport(int id){
        return facadeSupplier.makeProductReport(id);
    }
}



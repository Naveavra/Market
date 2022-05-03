package main.java.ServiceLayer.Storage;

import main.java.DomainLayer.Storage.ReportController;

import java.io.IOException;
import java.util.List;

public class ReportService {
    private ReportController reportCon;

    public ReportService(ReportController reportCon){
        this.reportCon=reportCon;
    }

    public void makeReport(List<String> catNames){
        reportCon.makeReport(catNames);
    }

    public void makeDamagedReport() throws IOException {
        reportCon.makeDamagedReport();
    }


    public void makeRefillReport(){
        reportCon.makeRefillReport();
    }

    public void makeProductReport(int id){
        reportCon.makeProductReport(id);
    }


}

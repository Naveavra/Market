package ServiceLayer;

import DomainLayer.Storage.ReportController;

import java.io.IOException;
import java.util.List;

public class ReportService {
    private ReportController reportCon;

    //cons - empty?
    public ReportService(ReportController reportCon){
        this.reportCon=reportCon;
    }


    public void makeReport(List<String> catNames){
        reportCon.makeReport(catNames);
    }//service can not throw IOException

    public void makeDamagedReport() throws IOException {//service can not throw IOException
        reportCon.makeDamagedReport();
    }


    public void makeRefillReport(){
        reportCon.makeRefillReport();
    }//service can not throw IOException

    public void makeProductReport(int id){
        reportCon.makeProductReport(id);
    }//service can not throw IOException


}

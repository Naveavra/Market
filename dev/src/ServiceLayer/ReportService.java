package ServiceLayer;

import DomainLayer.Storage.ReportController;

import java.io.IOException;
import java.util.List;

public class ReportService {
    private ReportController reportCon;

    public ReportService(ReportController reportCon){
        this.reportCon=reportCon;
    }


    public boolean makeReport(List<String> catNames){
        return reportCon.makeReport(catNames);
    }

    public boolean makeDamagedReport() throws IOException {
        return reportCon.makeDamagedReport();
    }


    public boolean makeRefillReport(){
        return reportCon.makeRefillReport();
    }

    public boolean makeProductReport(int id){
        return reportCon.makeProductReport(id);
    }
}



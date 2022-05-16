package DomainLayer.Storage;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class ReportController {
    private CategoryController categoryCon;
    public ReportController(CategoryController cat){
        categoryCon=cat;
    }

        public boolean makeCatReport(List<String> catNames){
        File path= new File("./categoryReport.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(categoryCon.makeReport(catNames));
            out.write(jsonString);
            jsonString = gson.toJson(categoryCon.getDiscounts(catNames));
            out.write(jsonString);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public boolean makeDamagedReport(){
        File path= new File("./damagedItems.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            if(this.categoryCon.getDamagedItems().size()>0) {
                String jsonString = gson.toJson(this.categoryCon.getDamagedItems());
                out.write(jsonString);
            }
        } catch (Exception e) {
            return false;
        }
        File path2= new File("./expiredItems.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path2.getCanonicalPath()))) {
            Gson gson = new Gson();
            if(this.categoryCon.getExpiredItems().size()>0) {
                String jsonString = gson.toJson(this.categoryCon.getExpiredItems());
                out.write(jsonString);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean makeRefillReport(){
        File path= new File("./missingProducts.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            if( this.categoryCon.makeRefillReport().size()>0) {
                String jsonString = gson.toJson( this.categoryCon.makeRefillReport());
                out.write(jsonString);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean makeProductReport(int id){
        File path= new File("./productReport.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            if(this.categoryCon.getProductWithId(id)!=null) {
                String jsonString = gson.toJson(this.categoryCon.getProductWithId(id));
                out.write(jsonString);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

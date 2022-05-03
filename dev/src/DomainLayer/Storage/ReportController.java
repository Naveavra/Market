package main.java.DomainLayer.Storage;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReportController {
    private CategoryController categoryCon;
    private ProductController productCon;
    public ReportController(CategoryController cat, ProductController pro){
        categoryCon=cat;
        productCon=pro;
    }

    public void makeReport(List<String> catNames){
        File path= new File("./categoryReport.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(categoryCon.makeReport(catNames));
            out.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void makeDamagedReport() throws IOException {
        File path= new File("./damagedItems.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            if(this.productCon.getDamagedItems().size()>0) {
                String jsonString = gson.toJson(this.productCon.getDamagedItems());
                out.write(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeRefillReport(){
        File path= new File("./missingProducts.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            if( this.productCon.makeRefillReport().size()>0) {
                String jsonString = gson.toJson( this.productCon.makeRefillReport());
                out.write(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeProductReport(int id){
        File path= new File("./productReport.json");
        try (PrintWriter out = new PrintWriter(new FileWriter(path.getCanonicalPath()))) {
            Gson gson = new Gson();
            if(this.productCon.getProductWithId(id)!=null) {
                String jsonString = gson.toJson(this.productCon.getProductWithId(id));
                out.write(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}

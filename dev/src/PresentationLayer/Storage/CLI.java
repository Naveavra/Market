package main.java.PresentationLayer.Storage;

import main.java.DomainLayer.Storage.Location;
import main.java.DomainLayer.Storage.CategoryController;
import main.java.DomainLayer.Storage.ProductController;
import main.java.DomainLayer.Storage.ReportController;
import main.java.ServiceLayer.Storage.CategoryService;
import main.java.ServiceLayer.Storage.ProductService;
import main.java.ServiceLayer.Storage.ReportService;

import java.io.IOException;
import java.util.*;


public class CLI
{
    static Scanner in = new Scanner(System.in);
    public static void main(String[]args) throws IOException
    {
        ProductService pC=new ProductService(new ProductController());
        CategoryService cC=new CategoryService(new CategoryController(pC.getProductCon()));
        ReportService rC=new ReportService(new ReportController(cC.getCategoryCon(), pC.getProductCon()));
        String command;
        String detail;
        String line = "";
        System.out.println("Program Started");
        while(!line.equals("EXIT"))
        {
            System.out.println("insert commend number:");
            line = in.nextLine();
//            String[] str = line.split(")", 2);
            command = line;
            if(!command.equals("EXIT")) {
                switch (command) {
                    case ("1"): {
                        try {
                            System.out.println("enter the attributes of id");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = Integer.parseInt(fields[0]);
                            String name = fields[1];
                            String desc = fields[2];
                            int daysForResupply = Integer.parseInt(fields[3]);
                            double priceSupplier = Double.parseDouble(fields[4]);
                            double price = Double.parseDouble(fields[5]);
                            String maker = fields[6];
                            System.out.println("write the Category,subCategory,subSubCategory of the product in that format");
                            line = in.nextLine();
                            String[] cats = line.split(",");
                            if(cC.findCat(cats[0])!=null && cC.findCat(cats[0]).findSub(cats[1])!=null &&
                                    cC.findCat(cats[0]).findSub(cats[1]).findSubSub(cats[2])!=null) {
                                pC.addNewProduct(id, name, desc, daysForResupply, priceSupplier, price, maker);
                                cC.addProductToCat(id, cats[0], cats[1], cats[2]);
                            }
                            else
                                System.out.println("wrong input or one of the categories does not exists");
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }

                        break;
                    }
                    case ("2"): {
                        try {
                            System.out.println("enter category name");
                            detail = in.nextLine();
                            String cat = detail;
                            cC.addCategory(cat);
                        }
                        catch (Exception e) {
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("3"): {
                        try {
                            System.out.println("enter category name,sub category name");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            cC.addSubCat(fields[0], fields[1]);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("4"): {
                        try {
                            System.out.println("enter category name,sub category name, sub sub category name");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            cC.addSubSubCat(fields[0], fields[1], fields[2]);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("5"): {
                        try {
                            System.out.println("enter id/name,amount,exp date, shelf number");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            if (id != -1) {
                                int amount = Integer.parseInt(fields[1]);
                                String exp = fields[2];
                                int shelf = Integer.parseInt(fields[3]);
                                pC.addAllItems(id, amount, exp, shelf);
                                pC.needsRefill(id);
                            } else
                                System.out.println("no such product exists");
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }

                        break;
                    }
                    case ("6"): {
                        try {
                            System.out.println("enter id/name,amount");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            int amount = Integer.parseInt(fields[1]);
                            double finalPrice = pC.buyItems(id, amount);
                            if (finalPrice != -1)
                                System.out.println("the price is: " + finalPrice);
                            else
                                System.out.println("not enough in store");
                            if(pC.needsRefill(id))
                                System.out.println("the product "+id+":"+pC.getProductWithId(id).getName()+" need a refill. added to refill list");
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }

                        break;
                    }
                    case ("7"): {
                        try {
                            System.out.println("enter id/name,amount");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            int amount = Integer.parseInt(fields[1]);
                            pC.moveItemsToStore(id, amount);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }

                        break;
                    }
                    case ("8"): {
                        try {
                            System.out.println("enter the names of the categories");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            List<String> cats = new LinkedList<String>();
                            for (String s : fields)
                                cats.add(s);
                            rC.makeReport(cats);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }

                        break;
                    }
                    case ("9"): {
                        try {
                            rC.makeDamagedReport();
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("10"): {
                        try {
                            System.out.println("enter the id/name, damage description,place,shelf number, exp date");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            String description = fields[1];
                            Location.Place place = stringToPlace(fields[2]);
                            int shelf = Integer.parseInt(fields[3]);
                            String ed = fields[4];
                            pC.defineAsDamaged(id, description, place, shelf, ed);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("11"): {
                        try {
                            rC.makeRefillReport();
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("12"): {
                        try {
                            System.out.println("enter the id/name, category it belongs to, category to add to, sub category to add to, sub sub category to add to   ");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            String catRemove = fields[1];
                            String catAdd = fields[2];
                            String subAdd = fields[3];
                            String subSubAdd = fields[4];
                            cC.transferProduct(id, catRemove, catAdd, subAdd, subSubAdd);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("13"): {
                        try {
                            System.out.println("enter the id/name");
                            detail = in.nextLine();
                            int id = -1;
                            if (checkId(detail))
                                id = Integer.parseInt(detail);
                            else
                                id = pC.getProductIdWithName(detail);
                            if (id != -1)
                                cC.removeFromCatalog(id);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("14"): {
                        try {
                            System.out.println("enter the id/name, discount in %");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            double discount = Double.parseDouble(fields[1]);
                            if(id!=-1)
                                pC.setDiscountToOneItem(id, discount);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("15"): {
                        try {
                            System.out.println("enter the name of the category, discount in %");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            String name = fields[0];
                            double discount = Double.parseDouble(fields[1]);
                            cC.setDiscount(name, discount);
                            break;
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                    }
                    case ("16"): {
                        try {


                            System.out.println("enter the id/name, daysForResupply");
                            detail = in.nextLine();
                            String[] fields = detail.split(",");
                            int id = -1;
                            if (checkId(fields[0]))
                                id = Integer.parseInt(fields[0]);
                            else
                                id = pC.getProductIdWithName(fields[0]);
                            int days = Integer.parseInt(fields[1]);
                            if (id != -1)
                                pC.changeDaysForResupply(id, days);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }

                    case ("17"): {
                        try {

                            System.out.println("enter the id/name of product");
                            detail = in.nextLine();
                            int id = -1;
                            if (checkId(detail))
                                id = Integer.parseInt(detail);
                            else
                                id = pC.getProductIdWithName(detail);
                            rC.makeProductReport(id);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                    case ("18"): {
                        try {

                            System.out.println("creating a scenario");
                            cC.addCategory("first");
                            cC.addSubCat("first", "first1");
                            cC.addSubSubCat("first", "first1", "first11");
                            cC.addCategory("second");
                            cC.addSubCat("second", "second1");
                            cC.addSubSubCat("second", "second1", "second11");
                            pC.addNewProduct(1, "milk", "from cow", 2, 1, 3, "me");
                            pC.addNewProduct(2, "eggs", "from chicken", 3, 2, 5, "me");
                            cC.addProductToCat(1, "first", "first1", "first11");
                            cC.addProductToCat(2, "second", "second1", "second11");
                            pC.addAllItems(1, 7, "2022-06-01", 1);
                            pC.addAllItems(2, 3, "2019-06-01", 1);
                            pC.changeDaysPassed(1, 5);
                            pC.changeDaysPassed(2, 3);
                        }
                        catch (Exception e){
                            System.out.println("wrong input");
                        }
                        break;
                    }
                }
            }
        }
    }

    public static Location.Place stringToPlace(String s)
    {
        if(s.equals("STORE"))
            return Location.Place.STORE;
        else
            return Location.Place.STORAGE;
    }

    public static boolean checkId(String id){
        String nums="0123456789";
        for (int i=0;i<id.length();i++)
            if(!nums.contains(id.charAt(i)+""))
                return false;
        return true;
    }


}

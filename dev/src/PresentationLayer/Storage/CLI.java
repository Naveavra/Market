package PresentationLayer.Storage;

import DomainLayer.Employees.JobType;
import ServiceLayer.CategoryService;
import ServiceLayer.ProductSupplierService;
import ServiceLayer.ReportService;
import ServiceLayer.SupplierService;

import java.util.*;


public class CLI
{
    private Set<JobType> roles;
    static Scanner in = new Scanner(System.in);
    public void startStorageModel(Set<JobType> roles)
    {
        this.roles = roles;
        CategoryService cC=new CategoryService();
        ReportService rC=new ReportService();
        String command;
        String detail;
        String line = "";
        System.out.println("Program Started");
        while(!line.equals("EXIT"))
        {
            System.out.println("Instructions for the warehouse worker:");
            System.out.println("\t1) Buy product Menu");
            System.out.println("\t2) Add new Product Menu");
            System.out.println("\t3) Add new category Menu");
            System.out.println("\t4) Make report Menu");
            System.out.println("insert commend number:");
            line = in.nextLine();
            command = line;
            if(!command.equals("EXIT"))
            {
                switch (command)
                {
                    case ("1"):
                    {
                        if(!canUse(roles))
                        {
                            System.out.println("not authorized");
                            break;
                        }
                        while(!line.equals("0"))
                        {
                            System.out.println("BUY PRODUCT MENU:");
                            System.out.println("\t0)EXIT MENU");
                            System.out.println("\t1)buy Product");
                            System.out.println("\t2)define Item as damaged");
                            System.out.println("insert commend number:");
                            line = in.nextLine();
                            command = line;
                            switch (command)
                            {
                                case("1"):
                                {
                                    try {
                                        System.out.println("enter id/name,amount");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        int id = -1;
                                        if (checkId(fields[0]))
                                            id = Integer.parseInt(fields[0]);
                                        else
                                            id = cC.getProductIdWithName(fields[0]);
                                        int amount = Integer.parseInt(fields[1]);
                                        double finalPrice = cC.buyItems(id, amount);
                                        if (finalPrice != -1)
                                            System.out.println("the price is: " + finalPrice);
                                        else
                                            System.out.println("not enough in store");
                                        if(cC.needsRefill(id))
                                            System.out.println("the product "+id+":"+cC.getNameWithId(id)+" need a refill. added to refill list");
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }

                                    break;
                                }

                                case("2"):
                                {
                                    try {
                                        System.out.println("enter the id/name, damage description,place,shelf number, exp date");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        int id = -1;
                                        if (checkId(fields[0]))
                                            id = Integer.parseInt(fields[0]);
                                        else
                                            id = cC.getProductIdWithName(fields[0]);
                                        String description = fields[1];
                                        String place = fields[2];
                                        int shelf = Integer.parseInt(fields[3]);
                                        String ed = fields[4];
                                        cC.defineAsDamaged(id, description, place, shelf, ed);
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case ("2"):
                    {
                        if(!canUse(roles))
                        {
                            System.out.println("not authorized");
                            break;
                        }
                        while(!line.equals("0"))
                        {
                            System.out.println("ADD PRODUCT MENU:");
                            System.out.println("\t0)EXIT MENU");
                            System.out.println("\t1)add new Product");
                            System.out.println("\t2)add Items from transport");
                            System.out.println("\t3)print all Products");
                            System.out.println("\t4)set discount to Product");
                            System.out.println("\t5)remove Product from catalog");
                            System.out.println("insert commend number:");
                            line = in.nextLine();
                            command = line;
                            switch (command)
                            {
                                case("1"):
                                {
                                    try {
                                        System.out.println("enter the attributes of product");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        int id = Integer.parseInt(fields[0]);
                                        String name = fields[1];
                                        String desc = fields[2];
                                        double price = Double.parseDouble(fields[3]);
                                        String maker = fields[4];
                                        System.out.println("write the Category,subCategory,subSubCategory of the product in that format");
                                        line = in.nextLine();
                                        String[] cats = line.split(",");
                                        cC.addNewProduct(id, name, desc, price, maker, cats[0], cats[1], cats[2]);
                                    } catch (Exception e) {
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                                case("2"):
                                {
                                    try {
                                        System.out.println("enter id/name,amount,exp date, shelf number");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        int id = -1;
                                        if (checkId(fields[0]))
                                            id = Integer.parseInt(fields[0]);
                                        else
                                            id = cC.getProductIdWithName(fields[0]);
                                        if (id != -1) {
                                            int amount = Integer.parseInt(fields[1]);
                                            String exp = fields[2];
                                            int shelf = Integer.parseInt(fields[3]);
                                            cC.addAllItems(id, amount, exp, shelf);
                                        } else
                                            System.out.println("no such product exists");
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }

                                    break;
                                }

                                case ("3"):
                                {
                                    System.out.println(cC.printAllProducts());
                                    break;
                                }

                                case ("4"):
                                {
                                    try {
                                        System.out.println("enter the id/name, discount in %");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        int id = -1;
                                        if (checkId(fields[0]))
                                            id = Integer.parseInt(fields[0]);
                                        else
                                            id = cC.getProductIdWithName(fields[0]);
                                        double discount = Double.parseDouble(fields[1]);
                                        if(id!=-1)
                                            cC.setDiscountToOneItem(id, discount);
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }

                                case("5"):
                                {
                                    try {
                                        System.out.println("enter the id/name");
                                        detail = in.nextLine();
                                        int id = -1;
                                        if (checkId(detail))
                                            id = Integer.parseInt(detail);
                                        else
                                            id = cC.getProductIdWithName(detail);
                                        if (id != -1)
                                            cC.removeFromCatalog(id);
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }

                    case ("3"):
                    {
                        if(!canUse(roles))
                        {
                            System.out.println("not authorized");
                            break;
                        }
                        while(!line.equals("0"))
                        {
                            System.out.println("ADD CATEGORY MENU:");
                            System.out.println("\t0)EXIT MENU");
                            System.out.println("\t1)add new category");
                            System.out.println("\t2)add new sub category");
                            System.out.println("\t3)add new sub sub category");
                            System.out.println("\t4)transfer Product");
                            System.out.println("\t5)set discount to category");
                            System.out.println("insert commend number:");
                            line = in.nextLine();
                            command = line;
                            switch (command) {
                                case ("1"): {
                                    try {
                                        System.out.println("enter category name");
                                        detail = in.nextLine();
                                        String cat = detail;
                                        cC.addCategory(cat);
                                    } catch (Exception e) {
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                                case ("2"): {
                                    try {
                                        System.out.println("enter category name,sub category name");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        cC.addSubCat(fields[0], fields[1]);
                                    } catch (Exception e) {
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                                case ("3"): {
                                    try {
                                        System.out.println("enter category name,sub category name, sub sub category name");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        cC.addSubSubCat(fields[0], fields[1], fields[2]);
                                    } catch (Exception e) {
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                                case ("4"): {
                                    try {
                                        System.out.println("enter the id/name, category it belongs to, category to add to, sub category to add to, sub sub category to add to   ");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        int id = -1;
                                        if (checkId(fields[0]))
                                            id = Integer.parseInt(fields[0]);
                                        else
                                            id = cC.getProductIdWithName(fields[0]);
                                        String catRemove = fields[1];
                                        String catAdd = fields[2];
                                        String subAdd = fields[3];
                                        String subSubAdd = fields[4];
                                        cC.transferProduct(id, catAdd, subAdd, subSubAdd);
                                    } catch (Exception e) {
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }

                                case ("5"):
                                {
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
                            }
                        }
                        break;
                    }

                    case ("4"):
                    {
                        while(!line.equals("0"))
                        {
                            System.out.println("MAKE REPORT MENU:");
                            System.out.println("\t0)EXIT MENU");
                            System.out.println("\t1)make category report");
                            System.out.println("\t2)make refill report");
                            System.out.println("\t3)make Product report");
                            System.out.println("\t4)make damaged report");
                            System.out.println("insert commend number:");
                            line = in.nextLine();
                            command = line;
                            switch (command)
                            {
                                case("1"):
                                {
                                    try {
                                        System.out.println("enter the names of the categories");
                                        detail = in.nextLine();
                                        String[] fields = detail.split(",");
                                        List<String> cats = new LinkedList<String>();
                                        for (String s : fields)
                                            cats.add(s);
                                        if(rC.makeCatReport(cats))
                                            System.out.println("made category report");
                                        else
                                            System.out.println("failed while making report");
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                                case ("2"):
                                {
                                    try {
                                        if(rC.makeRefillReport())
                                            System.out.println("made refill report");
                                        else
                                            System.out.println("failed while making report");
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }

                                case ("3"):
                                {
                                    try {

                                        System.out.println("enter the id/name of product");
                                        detail = in.nextLine();
                                        int id = -1;
                                        if (checkId(detail))
                                            id = Integer.parseInt(detail);
                                        else
                                            id = cC.getProductIdWithName(detail);
                                        if(rC.makeProductReport(id))
                                            System.out.println("made product report");
                                        else
                                            System.out.println("failed while making report");
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }

                                case("4"):
                                {
                                    try
                                    {
                                        if(rC.makeDamagedReport())
                                            System.out.println("made damaged report");
                                        else
                                            System.out.println("failed while making report");
                                    }
                                    catch (Exception e){
                                        System.out.println("wrong input");
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean checkId(String id)
    {
        String nums="0123456789";
        for (int i=0;i<id.length();i++)
            if(!nums.contains(id.charAt(i)+""))
                return false;
        return true;
    }

    private boolean canUse(Set<JobType> roles) {
        return roles.contains(JobType.STOCK_KEEPER);
    }

}




//                    case ("1"):
//                    {
//                        try {
//                            System.out.println("enter category name");
//                            detail = in.nextLine();
//                            String cat = detail;
//                            cC.addCategory(cat);
//                        }
//                        catch (Exception e) {
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("3"):
//                    {
//                        try {
//                            System.out.println("enter category name,sub category name");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            cC.addSubCat(fields[0], fields[1]);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("4"): {
//                        try {
//                            System.out.println("enter category name,sub category name, sub sub category name");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            cC.addSubSubCat(fields[0], fields[1], fields[2]);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("5"):
//                    {
//                        try {
//                            System.out.println("enter id/name,amount,exp date, shelf number");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            int id = -1;
//                            if (checkId(fields[0]))
//                                id = Integer.parseInt(fields[0]);
//                            else
//                                id = cC.getProductIdWithName(fields[0]);
//                            if (id != -1) {
//                                int amount = Integer.parseInt(fields[1]);
//                                String exp = fields[2];
//                                int shelf = Integer.parseInt(fields[3]);
//                                cC.addAllItems(id, amount, exp, shelf);
//                            } else
//                                System.out.println("no such product exists");
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//
//                        break;
//                    }
//                    case ("6"):
//                    {
//                        try {
//                            System.out.println("enter id/name,amount");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            int id = -1;
//                            if (checkId(fields[0]))
//                                id = Integer.parseInt(fields[0]);
//                            else
//                                id = cC.getProductIdWithName(fields[0]);
//                            int amount = Integer.parseInt(fields[1]);
//                            double finalPrice = cC.buyItems(id, amount);
//                            if (finalPrice != -1)
//                                System.out.println("the price is: " + finalPrice);
//                            else
//                                System.out.println("not enough in store");
//                            if(cC.needsRefill(id))
//                                System.out.println("the product "+id+":"+cC.getNameWithId(id)+" need a refill. added to refill list");
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//
//                        break;
//                    }
//                    case ("7"): {
//                        try {
//                            System.out.println("enter id/name,amount");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            int id = -1;
//                            if (checkId(fields[0]))
//                                id = Integer.parseInt(fields[0]);
//                            else
//                                id = cC.getProductIdWithName(fields[0]);
//                            int amount = Integer.parseInt(fields[1]);
//                            cC.moveItemsToStore(id, amount);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//
//                        break;
//                    }
//                    case ("8"): {
//                        try {
//                            System.out.println("enter the names of the categories");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            List<String> cats = new LinkedList<String>();
//                            for (String s : fields)
//                                cats.add(s);
//                            if(rC.makeCatReport(cats))
//                                System.out.println("made category report");
//                            else
//                                System.out.println("failed while making report");
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//
//                        break;
//                    }
//                    case ("9"): {
//                        try {
//                            if(rC.makeDamagedReport())
//                                System.out.println("made damaged report");
//                            else
//                                System.out.println("failed while making report");
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("10"): {
//                        try {
//                            System.out.println("enter the id/name, damage description,place,shelf number, exp date");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            int id = -1;
//                            if (checkId(fields[0]))
//                                id = Integer.parseInt(fields[0]);
//                            else
//                                id = cC.getProductIdWithName(fields[0]);
//                            String description = fields[1];
//                            String place = fields[2];
//                            int shelf = Integer.parseInt(fields[3]);
//                            String ed = fields[4];
//                            cC.defineAsDamaged(id, description, place, shelf, ed);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("11"): {
//                        try {
//                            if(rC.makeRefillReport())
//                                System.out.println("made refill report");
//                            else
//                                System.out.println("failed while making report");
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("12"): {
//                        try {
//                            System.out.println("enter the id/name, category it belongs to, category to add to, sub category to add to, sub sub category to add to   ");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            int id = -1;
//                            if (checkId(fields[0]))
//                                id = Integer.parseInt(fields[0]);
//                            else
//                                id = cC.getProductIdWithName(fields[0]);
//                            String catRemove = fields[1];
//                            String catAdd = fields[2];
//                            String subAdd = fields[3];
//                            String subSubAdd = fields[4];
//                            cC.transferProduct(id, catAdd, subAdd, subSubAdd);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("13"): {
//                        try {
//                            System.out.println("enter the id/name");
//                            detail = in.nextLine();
//                            int id = -1;
//                            if (checkId(detail))
//                                id = Integer.parseInt(detail);
//                            else
//                                id = cC.getProductIdWithName(detail);
//                            if (id != -1)
//                                cC.removeFromCatalog(id);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("14"): {
//                        try {
//                            System.out.println("enter the id/name, discount in %");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            int id = -1;
//                            if (checkId(fields[0]))
//                                id = Integer.parseInt(fields[0]);
//                            else
//                                id = cC.getProductIdWithName(fields[0]);
//                            double discount = Double.parseDouble(fields[1]);
//                            if(id!=-1)
//                                cC.setDiscountToOneItem(id, discount);
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case ("15"): {
//                        try {
//                            System.out.println("enter the name of the category, discount in %");
//                            detail = in.nextLine();
//                            String[] fields = detail.split(",");
//                            String name = fields[0];
//                            double discount = Double.parseDouble(fields[1]);
//                            cC.setDiscount(name, discount);
//                            break;
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                    }
//                    case ("16"): {
//                        try {
//
//                            System.out.println("enter the id/name of product");
//                            detail = in.nextLine();
//                            int id = -1;
//                            if (checkId(detail))
//                                id = Integer.parseInt(detail);
//                            else
//                                id = cC.getProductIdWithName(detail);
//                            if(rC.makeProductReport(id))
//                                System.out.println("made product report");
//                            else
//                                System.out.println("failed while making report");
//                        }
//                        catch (Exception e){
//                            System.out.println("wrong input");
//                        }
//                        break;
//                    }
//                    case("17"):
//                    {
//                        System.out.println(cC.printAllProducts());
//                        break;
//                    }














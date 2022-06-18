package PresentationLayer.Suppliers;

import DomainLayer.Employees.JobType;
import PresentationLayer.Menu;
import ServiceLayer.ProductSupplierService;
import ServiceLayer.SupplierService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class ProductMenu {

    private Supplier supplier;
    private Scanner sc;
    private SupplierService ss;
    private ProductSupplierService ps;
    private SupplierMenu sm;
    private Set<JobType> roles;
    public ProductMenu(Supplier s) {
        sc=new Scanner(System.in);
        supplier = s;
        ss = new SupplierService();
        ps = new ProductSupplierService();
        roles=new HashSet<>();
        sm =new SupplierMenu();
    }
    public void setRole(Set<JobType> roles){
        this.roles=roles;
        sm.setRoles(roles);

    }

    public void manageProductsSupplierMenu() {
        int choice =0;
        while(choice != 7){
        System.out.println("Manage products of supplier: "+ supplier.getSupplierName());
        System.out.println("Choose what you want to do:");
        System.out.println("\t 1. add product to supplier");
        System.out.println("\t 2. remove product from supplier");
        System.out.println("\t 3. add discount on product to supplier");
        System.out.println("\t 4. remove discount on product to supplier");
        System.out.println("\t 5. watch all supplier products");
        System.out.println("\t 6. update product");
        System.out.println("\t 7. go back");
        String choiceStr = "";
        try{
            choiceStr = sc.next();
            choice=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        if(choice !=5 & choice!=7){
            if(!roles.contains(JobType.STOCK_KEEPER)){
                System.out.println("u dont have access to this area");
                return;
            }
        }
        switch (choice) {
            case 1:
                addProduct(supplier);
                break;
            case 2:
                removeProduct(supplier);
                break;
            case 3:
                addDiscountOnProduct(supplier);
                break;
            case 4:
                removeDiscountOnProduct(supplier);
                break;
            case 5:
                watchSupplierProducts(supplier);
                break;
            case 6:
                updateProduct(supplier);
                break;
            case 7:
                break;
            default:
                System.out.println("you must peek a number between 1 to 7");
        }
        }
    }



    private void addProduct(Supplier supplier) {
        System.out.println("Add new product:");
        System.out.println("Enter catalog number:");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Enter product id:");
        int productId=0;
        try{
            choiceStr = sc.next();
            productId=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Enter days until expiration for every new item of this product: ");
        int daysUntilExpiration =0;
        try{
            choiceStr = sc.next();
            daysUntilExpiration=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Enter price:");
        int price =0;
        try{
            choiceStr = sc.next();
            price=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        boolean added=ps.addProduct(supplier.getSupplierNumber(), catalogNum, daysUntilExpiration, price, productId);
        if(added) {
            System.out.println("product added");
        }
        else{
            System.out.println("The product is already exist or the price is invalid ");
        }
    }

    private void removeProduct(Supplier supplier) {
        System.out.println("Remove product:");
        System.out.println("Enter catalog number of the product:");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        boolean removed=ps.removeProduct(supplier.getSupplierNumber(), catalogNum);
        if(removed) {
            System.out.println("product remove");
        }
        else{
            System.out.println("product didn't exist");
        }
    }

    private void updateProduct(Supplier supplier) {
        System.out.println("Update product:");
        System.out.println("Enter catalog number of the product:");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Enter product id of the product:");
        int productId =0;
        try{
            choiceStr = sc.next();
            productId=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Enter new price for product:");
        choiceStr = "";
        int price =0;
        try{
            choiceStr = sc.next();
            price=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        boolean updated=ps.updateProduct(supplier.getSupplierNumber(),catalogNum, productId,price);
        if(updated) {
            System.out.println("product updated");
        }
        else{
            System.out.println("product didn't found or the price was invalid number");
        }
    }

    private void watchSupplierProducts(Supplier supplier) {
        String json = ps.getProductsOfSupplier(supplier.getSupplierNumber());
        Gson gson = new Gson();
        Map<Integer, LinkedTreeMap> products = new HashMap<>();
        products = gson.fromJson(json,products.getClass());
        int i = 1;
        if(products.size() == 0){
            System.out.println("There is no products to show");
            return;
        }
       // assert products != null;
        for (LinkedTreeMap p: products.values()){
            Product product = Menu.fromJson(p.toString(), Product.class);
            System.out.println(i + ". "+ product.toString());
            i++;
        }
    }
    private void addDiscountOnProduct(Supplier supplier){
        System.out.println("enter catalog number of product to add discount");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("enter amount of discount");
        choiceStr = "";
        double discount =0;
        try{
            choiceStr = sc.next();
            discount=Double.parseDouble(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Please write on how much product you want add discount?");
        choiceStr = "";
        int count=0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            return;
        }
        boolean added = ss.addDiscount(supplier.getSupplierNumber(),catalogNum ,count , discount);
        if (added){
            System.out.println("discount added to product");
        }
        else{
            System.out.println("invalid input");
        }
    }
    private void removeDiscountOnProduct(Supplier supplier) {
        System.out.println("enter catalog number of product to remove discount");
        String choiceStr = "";
        int catalogNum =0;
        try{
            choiceStr = sc.next();
            catalogNum=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only number");
            return;
        }
        System.out.println("Please write on how much product you want remove discount?");
        choiceStr = "";
        int count =0;
        try{
            choiceStr = sc.next();
            count=Integer.parseInt(choiceStr);
        }
        catch (Exception e){
            System.out.println("you must enter only digits number");
            return;
        }
        boolean removed = ss.removeDiscountOnProduct(supplier.getSupplierNumber(), catalogNum, count);
        if (removed){
            System.out.println("discount removed on product");
        }
        else{
            System.out.println("invalid input");
        }
    }
}

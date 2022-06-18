package DomainLayer;

import DomainLayer.Storage.CategoryController;
import DomainLayer.Storage.ReportController;
import DomainLayer.Suppliers.*;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.*;

public class FacadeSupplier_Storage {
    private OrdersController ordersController;
    private SupplierController supplierController;
    private CategoryController categoryController;
    private ReportController reportController;
    private Gson gson;
    private  FacadeEmployees_Transports facade;
    private static boolean needsUpdateOrders=true;


    public FacadeSupplier_Storage(){
        ordersController=new OrdersController();
        supplierController=new SupplierController();
        categoryController=new CategoryController();
        reportController=new ReportController(categoryController);
        gson=new Gson();
        facade=new FacadeEmployees_Transports();
        if(needsUpdateOrders){
            updateOrders();
            needsUpdateOrders=false;
        }

    }

    //Order service
    public String createOrder(int supplierNumber){
        return gson.toJson(ordersController.createOrder(supplierNumber));
    }
    public boolean addProductToOrder(int supplierNumber,int orderId , int catalogNUmber, int count){
        ProductSupplier p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        if(p==null){
            return false;
        }
        boolean ans = ordersController.getOrder(orderId).addProductToOrder(p, count);
        return ans;
    }

    public boolean updateProductInOrder(int supplierNumber,int orderId, int catalogNUmber, int newCount){
        ProductSupplier p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        boolean ans = ordersController.getOrder(orderId).updateProductToOrder(p, newCount);
        return ans;
    }

    public boolean deleteProductFromOrder(int supplierNumber,int orderId, int catalogNUmber){
        ProductSupplier p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        return ordersController.getOrder(orderId).removeProductFromOrder(p);
    }

    public Map<Integer, OrderFromSupplier> getActiveOrders(int supplierNumber){
        Map<Integer, OrderFromSupplier> orders = ordersController.getActiveOrders(supplierNumber);
        return orders;
    }
    public Map<Integer, OrderFromSupplier> getActiveOrders(){
        Map<Integer, OrderFromSupplier> orders = ordersController.getActiveOrders();
        return orders;
    }

    public Map<Integer, DeliveryTerm> getFixedDaysOrders(int supplierNumber){
        Map<Integer, OrderFromSupplier> orders = ordersController.getActiveOrders(supplierNumber);
        Map<Integer, DeliveryTerm> deliveryDays=new HashMap<>();
        for(Integer o: orders.keySet()){
            if(!orders.get(o).getDaysToDeliver().isEmpty()){
                deliveryDays.put(orders.get(o).getOrderId(),orders.get(o).getDaysToDeliver());
            }
        }
        return deliveryDays;
    }

    public boolean sendOrder(int supplierNumber, int orderId){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(ordersController.getOrder(orderId)==null){
            return false;
        }
        return ordersController.finishOrder(orderId);
    }
    public String getOrderTotalDetails(int orderId) {
        if(ordersController.getOrder(orderId)==null){
            return "fail";
        }
        return String.valueOf(ordersController.updateTotalIncludeDiscounts(orderId));
    }


    public String getOrder(int supplierNumber, int orderId) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        if(ordersController.getOrder(orderId)==null){
            return "fail";
        }
        OrderFromSupplier o = ordersController.getOrder(orderId);
        return gson.toJson(o);
    }

    public String getProductsInOrder(int supplierNumber, int orderID) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        if(ordersController.getOrder(orderID)==null){
            return "fail";
        }
        Map<ProductSupplier,Integer> products = ordersController.getOrder(orderID).getProducts();
        Map<String,Integer> prouductsJson = new HashMap<>();
        for (Map.Entry<ProductSupplier,Integer> e: products.entrySet()){
            ProductSupplier p = e.getKey();
            prouductsJson.put(gson.toJson(p),e.getValue());
        }
        return gson.toJson(prouductsJson);
    }

    //Delivery Service
    public boolean addFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(ordersController.getOrder(orderId)==null){
            return false;
        }
        return ordersController.getOrder(orderId).addDeliveryDays(daysInWeek);
    }

    public boolean updateFixedDeliveryDaysForOrder(int supplierNumber,int orderId, String[] daysInWeek){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(ordersController.getOrder(orderId)==null){
            return false;
        }
        return ordersController.getOrder(orderId).updateDeliveryDays(daysInWeek);
    }

    //Product supplier service
    public boolean addProductToSupplier(int supplierNumber, int catalogNumber, int daysUntilExpiration, int price, int productId){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(categoryController.getProductWithId(productId)==null){
            return false;
        }

        return supplierController.getSupplier(supplierNumber).addProduct(catalogNumber, daysUntilExpiration, price, productId);
    }
    public String getProductsOfSupplier(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        Map<Integer, ProductSupplier> products = supplierController.getSupplier(supplierNumber).getProducts();
        return gson.toJson(products);
    }

    public boolean updateProduct(int supplierNumber, int catalogNumber, int productId, int price){
        if(price<=0){
            return false;
        }
        if(productId<0){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isProductExist(productId)){
            return false;
        }
        supplierController.getSupplier(supplierNumber).updateProductPrice(catalogNumber,price);

        return true;
    }
    public boolean removeProduct(int supplierNumber, int catalogNumber){
        return supplierController.getSupplier(supplierNumber).removeProduct(catalogNumber);
    }

    public void updateOrders(){
        Map<Integer, Integer> productIds=categoryController.getProductIds();
        ordersController.updateOrders(productIds);
    }


    //SupplierService
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount,  String[] days,int area){
        if(supplierNumber<=0){
            return false;
        }
        if(area!= 0 & area!=1 & area!=2){
            return false;
        }
        return supplierController.openAccount(supplierNumber, supplierName,bankAccount, days, area);
    }
    public boolean closeAccount(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        return supplierController.closeAccount(supplierNumber);
    }
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(bankAccount<0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).updateAccount(supplierName,bankAccount);
    }
    //discount on amount of specific product
    public boolean addDiscount(int supplierNumber,int catalogNumber,int count,double discount){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(catalogNumber<0){
            return false;
        }
        if(supplierController.getSupplier(supplierNumber).getProduct(catalogNumber)==null){
            return false;
        }
        if(count<=0){
            return false;
        }
        if(discount<=0|discount>=1){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).addDiscount(count, discount);
    }
    //discount on sum of products in order
    public boolean addDiscount(int supplierNumber,int count,double discount){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }

        if(count<=0|discount<=0|discount>=1){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).addDiscount(count, discount);
    }
    public boolean removeDiscountOnProduct(int supplierNumber,int catalogNumber,int count){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }

        if(supplierController.getSupplier(supplierNumber).getProduct(catalogNumber)==null){
            return false;
        }
        if(count<=0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).removeDiscountOnProduct(count);
    }
    public boolean removeDiscountOnAmount(int supplierNumber,int count){
        if( supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(count<=0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).removeDiscountOnAmount(count);
    }
    public String getSupplier(int supplierNumber) {
        Supplier s = supplierController.getSupplier(supplierNumber);
        return gson.toJson(s);

    }
    public boolean updateDeliveration(int supplierNumber,String[] days) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).updateDeliveration(days);
    }
    public Map<Integer,PastOrderSupplier>  watchPastOrders(int supplierNumber) {
        Map<Integer,PastOrderSupplier> pastOrderList=new HashMap<>();
        pastOrderList=ordersController.getFinalOrders(supplierNumber);
        return pastOrderList;
    }


    //categoryService
    public void addCategory(String cName) {
        categoryController.addCategory(cName, 0);
    }

    public void addNewProduct(int pId, String pName, String desc, double price,
                              String maker, String cat, String sub, String subSub) {
        categoryController.addNewProduct(pId, pName, desc, price, maker, cat, sub, subSub);
    }
    public void addSubCat(String cName, String subName) {
        categoryController.addSubCategory(cName, subName);
    }

    public void addSubSubCat(String cName, String subName, String subsub) {
        categoryController.addSubSubCategory(cName, subName, subsub);
    }


    public void setDiscount(String cName, double discount) {
        categoryController.setDiscount(cName, discount);
    }

    public void transferProduct(int id, String catAdd, String subAdd, String subSubAdd) {
        categoryController.transferProduct(id, catAdd, subAdd, subSubAdd);
    }
    public boolean hasCategory(String cat){
        return categoryController.hasCategory(cat);
    }

    public void removeFromCatalog(int id) {
        categoryController.removeFromCatalog(id);
    }


    public void removeCat(String catName){
        categoryController.removeCat(catName);
    }


    public String getNameWithId(int id)
    {
        return categoryController.getProductWithId(id).getName();
    }

    public void setDiscountToOneItem(int id, double discount){
        categoryController.setDiscountToOneItem(id, discount);
    }

    public void defineAsDamaged(int id, String description,String place, int shelf, String ed)
    {
        categoryController.defineAsDamaged(id, description, place, shelf, ed);
    }


    public double buyItems(int id, int amount){
        double price=categoryController.buyItems(id, amount);
        if(categoryController.needsRefill(id)) {
            //find supplier with the lowest price and make an order
            ordersController.createOrderWithMinPrice(id, categoryController.getProductWithId(id).getRefill());
        }
        return price;
    }

    public void transferItem(int id, String ed, String curePlace, int curShelf, String toPlace, int toShelf){
        categoryController.transferItem(id, ed, curePlace, curShelf, toPlace, toShelf);
    }

    public boolean needsRefill(int productId){
        return categoryController.needsRefill(productId);
    }

    public HashMap<Integer, Integer> getItemsFromTransport(int id){
        HashMap<Integer, Integer> productsAndQuantity = facade.getProductsFromOrderDoc(id);
        int supplierNumber = getSupplierNumberFromOrderDoc(id);
        if(productsAndQuantity.size()>0) {
            for (int productId : productsAndQuantity.keySet()) {
                String expirationDate = getExpirationDate(supplierNumber, productId);
                categoryController.addAllItems(productId, productsAndQuantity.get(productId), expirationDate, 1);
            }
            facade.transportIsDone(id + "");
        }
        return productsAndQuantity;
    }

    private String getExpirationDate(int supplierNumber, int productId) {
        return ordersController.getExpirationDate(supplierNumber, productId);
    }

    private int getSupplierNumberFromOrderDoc(int orderDocId) {
        return ordersController.getSupplierNumberFromOrderDoc(orderDocId);
    }

    public void addAllItems(int productId, int quantity, String ed, int shelf){
        categoryController.addAllItems(productId, quantity, ed, shelf);
    }

    public int getProductIdWithName(String name){
        return categoryController.getProductIdWithName(name);
    }

    public void moveItemsToStore(int id, int amount){
        categoryController.moveItemsToStore(id, amount);
    }

    public String printAllProducts(){
        return categoryController.printAllProducts();
    }

    // report service
    public boolean makeCatReport(List<String> catNames){
        return reportController.makeCatReport(catNames);
    }

    public boolean makeDamagedReport(){
        return reportController.makeDamagedReport();
    }


    public boolean makeRefillReport(){
        return reportController.makeRefillReport();
    }

    public boolean makeProductReport(int id){
        return reportController.makeProductReport(id);
    }

    public boolean addContact(int supplierNumber,String name, String email, String telephone) {
        return supplierController.getSupplier(supplierNumber).addContact(name, email, telephone);
    }

    public boolean updateContact(int supplierNumber, String name, String email, String telephone) {
        return supplierController.getSupplier(supplierNumber).updateContact(name,email,telephone);
    }

    public Boolean cancelOrder(int orderId) {
        return ordersController.cancelOrder(orderId);
    }

    public String getAllOrderDocIDs() {
        return facade.getAllOrderDocIDs();
    }
}

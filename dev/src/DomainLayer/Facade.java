package DomainLayer;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade {
    private OrdersController ordersController;
    private SupplierController supplierController;
    private Gson gson;

    //Order service
    public String createOrder(int supplierNumber){
        return gson.toJson(ordersController.createOrder(supplierNumber));
    }
    public boolean addProductToOrder(int supplierNumber,int orderId , int catalogNUmber, int count){
        ProductSupplier p = supplierController.getSupplier(supplierNumber).getProduct(catalogNUmber);
        if(p==null){
            return false;
        }
        boolean ans = ordersController.getOrder(orderId).updateProductToOrder(p, count);
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

    public String getActiveOrders(int supplierNumber){
        Map<Integer, OrderFromSupplier> orders = ordersController.getActiveOrders(supplierNumber);
        List<OrderFromSupplier> orders1=new ArrayList<>(orders.values());
        return gson.toJson(orders);
    }

    public String getFixedDaysOrders(int supplierNumber){
        Map<Integer, OrderFromSupplier> orders = ordersController.getActiveOrders(supplierNumber);
        Map<Integer, DeliveryTerm> deliveryDays=new HashMap<>();
        for(Integer o: orders.keySet()){
            if(!orders.get(o).getDaysToDeliver().isEmpty()){
                deliveryDays.put(orders.get(o).getOrderId(),orders.get(o).getDaysToDeliver());
            }
        }
        return gson.toJson(deliveryDays);
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
    public boolean addProduct(int supplierNumber, int catalogNumber, String name, int price, int productId){
        return supplierController.getSupplier(supplierNumber).addProduct(catalogNumber, price, productId, supplierNumber);
    }
    public String getProductsOfSupplier(int supplierNumber){
        if(supplierController.getSupplier(supplierNumber)==null){
            return "fail";
        }
        Map<Integer, ProductSupplier> products = supplierController.getSupplier(supplierNumber).getProducts();
        return gson.toJson(products);
    }
    public boolean updateProduct(int supplierNumber, int catalogNumber, String name, int price){
        if(price<=0){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isProductExist(catalogNumber)){
            return false;
        }
        if(name.equals("")){
            return false;
        }
        supplierController.getSupplier(supplierNumber).getProduct(catalogNumber).setPrice(price);
        return true;
    }
    public boolean removeProduct(int supplierNumber, int catalogNumber){
        return supplierController.getSupplier(supplierNumber).removeProduct(catalogNumber);
    }

    //SupplierService
    public boolean openAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts,boolean isDeliver){
        if(supplierNumber<=0){
            return false;
        }
        return supplierController.openAccount(supplierNumber, supplierName,bankAccount,contacts,isDeliver);
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
    public boolean updateAccount(int supplierNumber, String supplierName, int bankAccount, Map<String,String> contacts){
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        if(bankAccount<0){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).updateAccount(supplierName,bankAccount,contacts);
    }
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
    public boolean updateDeliveration(int supplierNumber,boolean isDeliver) {
        if(supplierController.getSupplier(supplierNumber)==null){
            return false;
        }
        if(!supplierController.getSupplier(supplierNumber).isActive()){
            return false;
        }
        return supplierController.getSupplier(supplierNumber).updateDeliveration(isDeliver);
    }
    public String  watchPastOrders(int supplierNumber) {
        List<PastOrderSupplier> pastOrderList=new ArrayList<>();
        pastOrderList=supplierController.getSupplier(supplierNumber).getFinalOrders();
        return gson.toJson(pastOrderList);
    }

}

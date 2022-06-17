package Tests;

import DAL.*;
import DomainLayer.FacadeEmployees_Transports;
import DomainLayer.Employees.JobType;
import DomainLayer.Employees.Time;
import DomainLayer.Storage.Product;
import PresentationLayer.Transport_Emploees.MainCLI;
import ServiceLayer.Utility.ShiftDate;
import ServiceLayer.Utility.ShiftPair;
import DomainLayer.Transport.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class EmployeeTransportTests {
    //private FacadeEmployees_Transports OrderCtrl;



    @BeforeAll
    public static void setup() throws SQLException {
//        Facade.getInstance().loadPreMadeData();
//        orderCtrl.build();
        try {
            Connect.getInstance().deleteRecordsOfTables();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void reset(){
        try {
            Connect.getInstance().deleteRecordsOfTables();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

//    @After
//    static void finish(){
//        DBConnector.getInstance().resetDB();
//        System.out.println("Great Success");
//    }


    public void createShift(int choice){
        FacadeEmployees_Transports facade = new FacadeEmployees_Transports();
        if(choice == 1){
            facade.createEmployee("258369647", "mor shker", "123456", 1, "yahav", "bad");
            facade.createEmployee("100000001", "may term", "123456", 1, "yahav", "good");
            facade.createEmployee("011111110", "wendy th dog", "123456", 1, "yahav", "good");
            facade.createEmployee("122222221", "savta toa", "123456", 1, "yahav", "good");
            facade.createEmployee("033333330", "liron marinber", "123456", 1, "yahav", "good");
            facade.certifyEmployee(JobType.CASHIER, "100000001");
            facade.certifyDriver("258369647", "C");
            facade.certifyEmployee(JobType.MERCHANDISER, "011111110");
            facade.certifyEmployee(JobType.STOCK_KEEPER, "122222221");
            facade.certifyEmployee(JobType.SHIFT_MANAGER, "033333330");
//            facade.certifyEmployee(JobType.DRIVER, "033333330");
            facade.createShift(new ShiftPair(new ShiftDate("03", "06", "2022"), Time.MORNING), "033333330",
                    "100000001", "258369647", "011111110", "122222221");
        }else if (choice ==2) {
            facade.createEmployee("258314647", "r shker", "123456", 1, "yahav", "bad");
            facade.certifyDriver("258314647", "c");
            facade.createShift(new ShiftPair(new ShiftDate("09", "06", "2022"), Time.MORNING), "033333330",
                    "100000001", "258314647", "011111110", "122222221");
        }
        else if(choice ==3){
            facade.createEmployee("010101010", "r shker", "123456", 1, "yahav", "bad");
            facade.certifyDriver("010101010", "c");
            facade.createShift(new ShiftPair(new ShiftDate("05", "06", "2022"), Time.MORNING), "033333330",
                    "100000001", "010101010", "011111110", "122222221");
        }
        else if(choice == 4){
            facade.createEmployee("010161010", "r shker", "123456", 1, "yahav", "bad");
            facade.certifyDriver("010161010", "c");
            facade.createShift(new ShiftPair(new ShiftDate("19", "06", "2022"), Time.MORNING), "033333330",
                    "100000001", "010161010", "011111110", "122222221");
        }else if(choice == 5){
            facade.createEmployee("310161010", "r3 shker", "123456", 1, "yahav", "bad");
            facade.createEmployee("110161010", "r1 shker", "123456", 1, "yahav", "bad");
            facade.certifyDriver("310161010", "c");
            facade.certifyDriver("110161010", "c1");
            facade.createShift(new ShiftPair(new ShiftDate("30", "06", "2022"), Time.MORNING), "033333330",
                    "100000001", "310161010,110161010", "011111110", "122222221");
        }else if(choice == 6) {
            facade.createEmployee("010161011", "r shker", "123456", 1, "yahav", "bad");
            facade.certifyDriver("010161011", "c");
            facade.createShift(new ShiftPair(new ShiftDate("26", "06", "2022"), Time.EVENING), "033333330",
                    "100000001", "010161011", "011111110", "122222221");
        }
    }
    @Test
    public void checkDriverExists(){
        Driver d = new Driver("eyal","000000000","c");
        Assertions.assertNotNull("000000000");
        createShift(2);
        Assertions.assertNotNull("258314647");
    }
    @Test
    public void showDriversTest(){
        OrderController orderCtrl = new OrderController();
        Assertions.assertEquals("",orderCtrl.showDrivers("08/09/2022","MORNING"));
        Assertions.assertEquals("",orderCtrl.showDrivers("20/05/2022","MORNING"));
    }

    @Test
    public void createOrderTest(){
        reset();
        createShift(1);
        FacadeEmployees_Transports facade = new FacadeEmployees_Transports();
        Driver d = new Driver("eyal","258369647","c");
        Truck t = new Truck("c","nadia",10.5,5.5);
//      Truck t = TruckDAO.getTruck("nadia");
        Supply milk =  new Supply("milk", 5, "1");
        Supply eggs = new Supply("eggs", 2.13,"2");
//        Supply matza =  new Supply("matza", 20,"2");
        ConcurrentHashMap<String,Integer> supplst1 = new ConcurrentHashMap<>();
        supplst1.put(milk.getId(),15);
        supplst1.put(eggs.getId(),6);
//        supplst1.put(matza.getId(),20);
        ConcurrentHashMap<Store, ConcurrentHashMap<String, Integer>> des1 = new ConcurrentHashMap<>();
        Contact con2 = new Contact("eretzhakulim", "dan", "123333");
        Store eretz_hakulin = new Store("156",con2 , Store.ShippingArea.North, 1);
        des1.put(eretz_hakulin, supplst1);
        Date date1 = new Date("16","05","2022");
        OrderDocument doc1 = new OrderDocument("96",1, des1, date1,"MORNING");
        doc1.setTruckandDriver(t,d);
        OrderDocDAO orderDocDAO = new OrderDocDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderDocument doc = orderDocDAO.getOrderDoc("96");
        Assertions.assertNull(doc);
        TruckDAO trucks = new TruckDAO();
        trucks.addTruck(t.getType(), t.getLicensePlate(), t.getMaxWeight(), t.getInitialWeight());
//        new OrderDocDAO();
        orderDocDAO.addDoc(doc1);
        Assertions.assertNotNull(orderDocDAO.getOrderDoc("96"));
    }
    @Test
    public void availabilityWhenShiftNotCreatedForDriver(){
        createShift(1);
        DriverDAO driverDAO = new DriverDAO();
//        Driver driver = new Driver("010101010","05/06/2022","MORNING");
        Assertions.assertFalse(driverDAO.getAvailability("033333330", "05/06/2022","MORNING" ));
        createShift(3);
        Assertions.assertFalse(driverDAO.getAvailability("033333330","05/06/2022","MORNING"));
    }

    @Test
    public void CreateShiftWithoutDriver(){
        Driver d = new Driver("eyal","258369647","c");
        Truck t = new Truck("c","nadia",10.5,5.5);
//        Driver d = DriverDAO.getDriver("m");
//        Truck t = TruckDAO.getTruck("nadia");
        Supply milk =  new Supply("milk", 5, "1");
        Supply eggs = new Supply("eggs", 2.13,"2");
//        Supply matza =  new Supply("matza", 20,"2");
//        Supply milk =  new Supply("milk", 5);
//        Supply eggs = new Supply("eggs", 2.13);
//        Supply matza =  new Supply("matza", 20);
        ConcurrentHashMap<String,Integer> supplst1 = new ConcurrentHashMap<>();
        supplst1.put(milk.getId(),15);
        supplst1.put(eggs.getId(),6);
//        supplst1.put(matza.getId(),20);
        ConcurrentHashMap<Store, ConcurrentHashMap<String, Integer>> des1 = new ConcurrentHashMap<>();
        Contact con2 = new Contact("eretzhakulim", "dan", "123333");
        Store eretz_hakulin = new Store("156",con2 , Store.ShippingArea.North, 1);
        des1.put(eretz_hakulin, supplst1);
        Date date1 = new Date("14","05","2022");
        OrderDocument doc1 = new OrderDocument("97",1, des1, date1,"morning");
        //OrderDocument doc1 = new OrderDocument("96",122, eretz_hakulin, des1, date1, "morning" );
        doc1.setTruckandDriver(t,d);
        try {
            new OrderDocDAO();
        }catch (Exception e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void checkShowDrivers() {
        DriverDAO driverDAO = new DriverDAO();
        for (Driver d : driverDAO.getDrivers("19/06/2022", "MORNING")){
            if(Objects.equals(d.getId(), "010161010")){
                Assertions.fail();
            }
        }
        Assertions.assertTrue(true);
        createShift(4);
        boolean cond = false;
        for (Driver d : driverDAO.getDrivers("19/06/2022", "MORNING")){
            if(Objects.equals(d.getId(), "010161010")){
                cond = true;
            }
        }
        Assertions.assertTrue(cond);
    }


    @Test
    public void notAvailable(){
        DriverDAO driverDAO = new DriverDAO();
        Assertions.assertFalse(driverDAO.getAvailability("123","16/06/2022","MORNING"));
        Assertions.assertFalse(driverDAO.getAvailability("1234","17/06/2022","MORNING"));
        Assertions.assertFalse(driverDAO.getAvailability("258369647","16/05/2022","morning"));
    }
    @Test
    public void available(){
        DriverDAO driverDAO = new DriverDAO();
        Assertions.assertFalse(driverDAO.getAvailability("123","28/05/2022","MORNING"));
        Assertions.assertFalse(driverDAO.getAvailability("1234","28/05/2022","MORNING"));
        Assertions.assertTrue(driverDAO.getAvailability("258369647","03/06/2022","MORNING"));
    }
    @Test
    public void multipleDriversPresent(){
        DriverDAO driverDAO = new DriverDAO();
        Assertions.assertNull(driverDAO.getDriver("110161010"));
        Assertions.assertNull(driverDAO.getDriver("310161010"));
        createShift(5); //drivers created and assigned
        Assertions.assertNotNull(driverDAO.getDriver("110161010"));
        Assertions.assertNotNull(driverDAO.getDriver("310161010"));
    }
    @Test
    public void licenses(){
        DriverDAO driverDAO = new DriverDAO();
        try{
            Assertions.assertFalse(driverDAO.getDriver("010161011").getLicense().equalsIgnoreCase("C"));
        }catch (Exception e){
            Assertions.assertTrue(true);
        }
        createShift(6);
        Assertions.assertTrue(driverDAO.getDriver("010161011").getLicense().equalsIgnoreCase("C"));
    }


}
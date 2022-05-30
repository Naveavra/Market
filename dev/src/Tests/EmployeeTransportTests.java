//package Tests;
//
//import DomainLayer.Employees.Facade;
//import DomainLayer.Employees.JobType;
//import DomainLayer.Employees.Time;
//import PresentationLayer.Transport_Emploees.MainCLI;
//import ServiceLayer.Utility.ShiftDate;
//import ServiceLayer.Utility.ShiftPair;
//import DomainLayer.Transport.*;
//import DAL.DriverDAO;
//import DAL.OrderDocDAO;
//import DAL.TruckDAO;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeAll;
//
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class EmployeeTransportTests {
//
//
//    @BeforeAll
//    public static void setup() {
////        Facade.getInstance().loadPreMadeData();
//        orderCtrl.build();
//    }
//
////    @After
////    static void finish(){
////        DBConnector.getInstance().resetDB();
////        System.out.println("Great Success");
////    }
//
//
//    public void createShift(int choice){
//        Facade facade = Facade.getInstance();
//        if(choice == 1){
//            facade.createEmployee("258369647", "mor shker", "123456", 1, "yahav", "bad");
//            facade.createEmployee("100000001", "may term", "123456", 1, "yahav", "good");
//            facade.createEmployee("011111110", "wendy th dog", "123456", 1, "yahav", "good");
//            facade.createEmployee("122222221", "savta toa", "123456", 1, "yahav", "good");
//            facade.createEmployee("033333330", "liron marinber", "123456", 1, "yahav", "good");
//            facade.certifyEmployee(JobType.CASHIER, "100000001");
//            facade.certifyDriver("258369647", "c");
//            facade.certifyEmployee(JobType.MERCHANDISER, "011111110");
//            facade.certifyEmployee(JobType.STOCK_KEEPER, "122222221");
//            facade.certifyEmployee(JobType.SHIFT_MANAGER, "033333330");
//            facade.createShift(new ShiftPair(new ShiftDate("03", "06", "2022"), Time.MORNING), "033333330",
//                    "100000001", "258369647", "011111110", "122222221");
//        }else if (choice ==2) {
//            facade.createEmployee("258314647", "r shker", "123456", 1, "yahav", "bad");
//            facade.certifyDriver("258314647", "c");
//            facade.createShift(new ShiftPair(new ShiftDate("09", "06", "2022"), Time.MORNING), "033333330",
//                    "100000001", "258314647", "011111110", "122222221");
//        }
//        else if(choice ==3){
//            facade.createEmployee("010101010", "r shker", "123456", 1, "yahav", "bad");
//            facade.certifyDriver("010101010", "c");
//            facade.createShift(new ShiftPair(new ShiftDate("05", "06", "2022"), Time.MORNING), "033333330",
//                    "100000001", "010101010", "011111110", "122222221");
//        }
//        else if(choice == 4){
//            facade.createEmployee("010161010", "r shker", "123456", 1, "yahav", "bad");
//            facade.certifyDriver("010161010", "c");
//            facade.createShift(new ShiftPair(new ShiftDate("19", "06", "2022"), Time.MORNING), "033333330",
//                    "100000001", "010161010", "011111110", "122222221");
//        }else if(choice == 5){
//            facade.createEmployee("310161010", "r3 shker", "123456", 1, "yahav", "bad");
//            facade.createEmployee("110161010", "r1 shker", "123456", 1, "yahav", "bad");
//            facade.certifyDriver("310161010", "c");
//            facade.certifyDriver("110161010", "c1");
//            facade.createShift(new ShiftPair(new ShiftDate("30", "06", "2022"), Time.MORNING), "033333330",
//                    "100000001", "310161010,110161010", "011111110", "122222221");
//        }else if(choice == 6) {
//            facade.createEmployee("010161011", "r shker", "123456", 1, "yahav", "bad");
//            facade.certifyDriver("010161011", "c");
//            facade.createShift(new ShiftPair(new ShiftDate("26", "06", "2022"), Time.EVENING), "033333330",
//                    "100000001", "010161011", "011111110", "122222221");
//        }
//    }
//    @Test
//    void checkDriverExists(){
//        Assertions.assertNull(DriverDAO.getInstance().getDriver("258314647"));
//        createShift(2);
//        Assertions.assertNotNull(DriverDAO.getInstance().getDriver("258314647"));
//    }
//    @Test
//    void showDriversTest(){
//        Assertions.assertEquals("",OrderCtrl.getInstance().showDrivers("08/09/2022","MORNING"));
//        Assertions.assertNotEquals("",OrderCtrl
//                .getInstance().showDrivers("20/05/2022","MORNING"));
//    }
//
//    @Test
//    void createOrderTest(){
//        createShift(1);
//        Driver d = DriverDAO.getInstance().getDriver("258369647");
//        Truck t = TruckDAO.getInstance().getTruck("nadia");
//        Supply milk =  new Supply("milk", 5);
//        Supply eggs = new Supply("eggs", 2.13);
//        Supply matza =  new Supply("matza", 20);
//        ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
//        supplst1.put(milk,15);
//        supplst1.put(eggs,6);
//        supplst1.put(matza,20);
//        ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
//        Contact con2 = new Contact("eretzhakulim", "dan", "123333");
//        Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
//        des1.put(eretz_hakulin, supplst1);
//        Date date1 = new Date("16","05","2022");
//        OrderDoc doc1 = new OrderDoc("96", eretz_hakulin, des1, date1, "morning" );
//        doc1.setTruckandDriver(t,d);
//        Assertions.assertNull(OrderDocDAO.getInstance().getOrderDoc("96"));
//        OrderDocDAO.getInstance().addDoc(doc1);
//        Assertions.assertNotNull(OrderDocDAO.getInstance().getOrderDoc("96"));
//    }
//    @Test
//    void testAvailabilityWhenShiftNotCreatedForDriver(){
//        Assertions.assertFalse(DriverDAO.getInstance().getAvailability("010101010","05/06/2022","MORNING"));
//        createShift(3);
//        Assertions.assertTrue(DriverDAO.getInstance().getAvailability("010101010","05/06/2022","MORNING"));
//    }
//
//    @Test
//    void CreateShiftWithoutDriver(){
//        Driver d = DriverDAO.getInstance().getDriver("m");
//        Truck t = TruckDAO.getInstance().getTruck("nadia");
//        Supply milk =  new Supply("milk", 5);
//        Supply eggs = new Supply("eggs", 2.13);
//        Supply matza =  new Supply("matza", 20);
//        ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
//        supplst1.put(milk,15);
//        supplst1.put(eggs,6);
//        supplst1.put(matza,20);
//        ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
//        Contact con2 = new Contact("eretzhakulim", "dan", "123333");
//        Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
//        des1.put(eretz_hakulin, supplst1);
//        Date date1 = new Date("14","05","2022");
//        OrderDoc doc1 = new OrderDoc("96", eretz_hakulin, des1, date1, "morning" );
//        doc1.setTruckandDriver(t,d);
//        try {
//            OrderDocDAO.getInstance().addDoc(doc1);
//        }catch (Exception e){
//            Assertions.assertTrue(true);
//        }
//    }
//
//    @Test
//    void checkShowDrivers() {
//        for (Driver d : DriverDAO.getInstance().getDrivers("19/06/2022", "MORNING")){
//            if(Objects.equals(d.getId(), "010161010")){
//                Assertions.fail();
//            }
//        }
//        Assertions.assertTrue(true);
//        createShift(4);
//        boolean cond = false;
//        for (Driver d : DriverDAO.getInstance().getDrivers("19/06/2022", "MORNING")){
//            if(Objects.equals(d.getId(), "010161010")){
//                cond = true;
//            }
//        }
//        Assertions.assertTrue(cond);
//    }
//
//    @Test
//    void testNotAvailable(){
//        Assertions.assertFalse(DriverDAO.getInstance().getAvailability("123","16/06/2022","MORNING"));
//        Assertions.assertFalse(DriverDAO.getInstance().getAvailability("1234","17/06/2022","MORNING"));
//        Assertions.assertFalse(DriverDAO.getInstance().getAvailability("258369647","16/05/2022","MORNING"));
//    }
//    @Test
//    void testAvailable(){
//        Assertions.assertTrue(DriverDAO.getInstance().getAvailability("123","28/05/2022","MORNING"));
//        Assertions.assertTrue(DriverDAO.getInstance().getAvailability("1234","28/05/2022","MORNING"));
//        Assertions.assertTrue(DriverDAO.getInstance().getAvailability("258369647","03/06/2022","MORNING"));
//    }
//    @Test
//    void testMultipleDriversPresent(){
//        Assertions.assertNull(DriverDAO.getInstance().getDriver("110161010"));
//        Assertions.assertNull(DriverDAO.getInstance().getDriver("310161010"));
//        createShift(5); //drivers created and assigned
//        Assertions.assertNotNull(DriverDAO.getInstance().getDriver("110161010"));
//        Assertions.assertNotNull(DriverDAO.getInstance().getDriver("310161010"));
//    }
//    @Test
//    void testLicenses(){
//        try{
//            Assertions.assertFalse(DriverDAO.getInstance().getDriver("010161011").getLicense().equalsIgnoreCase("c"));
//        }catch (Exception e){
//            Assertions.assertTrue(true);
//        }
//        createShift(6);
//        Assertions.assertTrue(DriverDAO.getInstance().getDriver("010161011").getLicense().equalsIgnoreCase("c"));
//    }
//
//
//}

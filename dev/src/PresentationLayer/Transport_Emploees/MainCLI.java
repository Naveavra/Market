package PresentationLayer.Transport_Emploees;
import java.util.Scanner;

public class MainCLI {
    MainCLI moduleCLI;
    //DBConnector conn;
    public static String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static String reset = "\033[0m";
    public static String RED_BOLD = "\033[1;31m";
    public static String WHITE_BOLD = "\033[1;37m";  // WHITE
    public MainCLI(){
    }

    public void start(){
        System.out.println("Please wait a few second while our System loads up");
        //build();
        while (true) {
            print(GREEN_BOLD+"------- Hello! -------"+reset+"\n" +
                    "Enter your choice of system:\n\t" +
                    "1.Employees management system\n\t" +
                    "2.Transports management system\n\t" +
                    "3.SURPRISE\n");
            String choice = getUserInput();
            if(choice.equals("goodbye")){
                print("Goodbye!");
                return;
            }
            if (choice.equals("1"))
                moduleCLI = new EmployeeMainCLI();
            else if (choice.equals("2"))
                moduleCLI = new UserInterface();
            else if(choice.equals("3")){
                print(RED_BOLD+"\n\tConcurrentHashMayTeremBlockingLinkedDan\n"+reset);
                continue;
            }else if(choice.equals("42")){
                easterEgg();
            }
            if (moduleCLI != null)
                moduleCLI.start();
            else{
                print("I'm gonna give you another chance...");
            }
        }
    }

    public void print(String s){
        System.out.println(s);
    }
    public void build(){
        loadPreMadeDataEmployee();
        //OrderController.getInstance().build();
        loadPreMadeDataTransport();
    }

    public String getUserInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void easterEgg(){
        String answer = "are you sure?";
        String add = "for real?";
        int count = 0;
        System.out.println("Hello good traveler, I see you have stumbled upon my maze...\n" +
                "A glorious treasure awaits at the end.. just don't get eaten by an"+RED_BOLD+ "'SPL Assignment 2'"+reset+"\n" +
                "i heard its the most dangerous beast, it can smell your fear and prey on your tears and pain\n");
        while (true) {
            System.out.println(
                    "You have 2 options:\n" +
                    GREEN_BOLD + "1. Go left\n" + reset +
                    RED_BOLD + "2. Go right" + reset);
            String res = getUserInput();
            if(count ==40){
                break;
            }
            System.out.println(WHITE_BOLD+answer+reset);
            answer = answer.substring(0,answer.length()-1)+" "+ add;
            System.out.println("");
            count++;
        }
        System.out.println("YOU WIN NOTHING YOU ASSHOLE, NOW GET OUT OF HERE BEFORE I'LL MAKE YOU DO THIS AGAIN");
    }


    private void loadPreMadeDataTransport() {
        buildTransport();
        buildTranport2();
    }

    private void loadPreMadeDataEmployee() {
        loadPreMadeData();
    }


     public void loadPreMadeData() {
//        createEmployee("234567891", "gal halifa", "123456", 1000, "yahav", "good conditions");
//        createEmployee("123456789", "dan terem", "123456", 1000, "yahav", "good conditions");
//        createEmployee("345678912", "noa aviv", "123456", 1000, "yahav", "good conditions");
//        createEmployee("456789123", "nave avraham", "123456", 1000, "yahav", "good conditions");
//        createEmployee("789123456", "gili gershon", "123456", 1000, "yahav", "good conditions");
//        createEmployee("891234567", "amit halifa", "123456", 1000, "yahav", "good conditions");
//        createEmployee("012345678", "shachar bardugo", "123456", 1000, "yahav", "good conditions");
//        createEmployee("123456780", "nadia zlenko", "123456", 1000, "yahav", "good conditions");
//        createEmployee("234567801", "yossi gershon", "123456", 1000, "yahav", "good conditions");
//        createEmployee("345678012", "eti gershon", "123456", 1000, "yahav", "good conditions");
//        createEmployee("456780123", "amit sasson", "123456", 1000, "yahav", "good conditions");
//        createEmployee("567801234", "itamar shemesh", "123456", 1000, "yahav", "good conditions");
//        createEmployee("147258369", "dina agapov", "123456", 1, "yahav", "bad");
//        createEmployee("258369147", "mor shuker", "123456", 1, "yahav", "bad");
//        createEmployee("000000000", "may terem", "123456", 1, "yahav", "good");
//        createEmployee("111111111", "wendy the dog", "123456", 1, "yahav", "good");
//        createEmployee("222222222", "savta tova", "123456", 1, "yahav", "good");
//        createEmployee("333333333", "liron marinberg", "123456", 1, "yahav", "good");
//        certifyEmployee(JobType.CASHIER, "318856994");
//        certifyEmployee(JobType.CASHIER, "234567891");
//        certifyEmployee(JobType.CASHIER, "345678912");
//        certifyEmployee(JobType.CASHIER, "123456789");
//        certifyDriver("258369147", "c");
//        certifyDriver("123456789", "c1");
//        certifyDriver("456780123", "c");
//        certifyDriver("234567891", "c");
//        certifyDriver("012345678", "c1");
//        certifyEmployee(JobType.MERCHANDISER, "234567891");
//        certifyEmployee(JobType.MERCHANDISER, "123456789");
//        certifyEmployee(JobType.MERCHANDISER, "789123456");
//        certifyEmployee(JobType.MERCHANDISER, "234567891");
//        certifyEmployee(JobType.MERCHANDISER, "123456789");
//        certifyEmployee(JobType.STOCK_KEEPER, "123456780");
//        certifyEmployee(JobType.STOCK_KEEPER, "345678912");
//        certifyEmployee(JobType.STOCK_KEEPER, "456780123");
//        certifyEmployee(JobType.STOCK_KEEPER, "123456789");
//        certifyEmployee(JobType.SHIFT_MANAGER, "234567891");
//        certifyEmployee(JobType.SHIFT_MANAGER, "318856994");
//        certifyEmployee(JobType.SHIFT_MANAGER, "222222222");
//        certifyEmployee(JobType.STOCK_KEEPER, "111111111");
//        certifyDriver( "000000000", "c1");
//        certifyEmployee(JobType.SHIFT_MANAGER, "456789123");
//        certifyEmployee(JobType.CASHIER, "333333333");
//        certifyEmployee(JobType.MERCHANDISER, "567801234");
//        certifyDriver("123456780", "c");
////        employeeController.putBackAll();
//
//        Response r = createShift(new ShiftPair(new ShiftDate("01", "06", "2022"), Time.MORNING), "318856994",
//                "333333333,234567891", "123456780,000000000", "123456789", "111111111");
//        if (r.errorOccurred())
//            System.out.println("r1 = " + r.getErrorMessage());
//        r = createShift(new ShiftPair(new ShiftDate("02", "06", "2022"), Time.MORNING), "456789123",
//                "333333333,234567891", "258369147,000000000", "567801234", "345678912");
//        if (r.errorOccurred())
//            System.out.println("r2 = " + r.getErrorMessage());
//
    }

    public void buildTransport(){
//        Driver dan =new Driver("Dan", "11123", "C1");
//        Driver nave = new Driver("nave", "123", "C1");
//        Driver miki = new Driver("miki", "1234", "C");
//        Driver itay = new Driver("itay", "1111", "C");
//        drivers.addDriver(dan);
//        drivers.addDriver(nave);
//        drivers.addDriver(miki);
//        drivers.addDriver(itay);


/**
 * ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
 *         ConcurrentHashMap<Supply,Integer> supplst2 = new ConcurrentHashMap<>();
 *         Supply milk =  new Supply("milk", 5);
 *         Supply eggs = new Supply("eggs", 2.13);
 *         Supply matza =  new Supply("matza", 20);
 *         Supply computer = new Supply("computer", 1000);
 *         Supply bmw = new Supply("bmw", 5000);
 *         Supply volvo = new Supply("volvo", 10000);
 *         pool.addSupply(milk);
 *         pool.addSupply(eggs);
 *         pool.addSupply(matza);
 *         pool.addSupply(bmw);
 *         pool.addSupply(volvo);
 *         supplst1.put(milk,15);
 *         supplst1.put(eggs,6);
 *         supplst1.put(matza,20);
 *         supplst1.put(computer,1);
 *         supplst2.put(milk,1);
 *         supplst2.put(eggs,1);
 *         supplst2.put(matza,1);
 *         Driver nave = new Driver("nave", "123", "C1");
 *         Driver miki = new Driver("miki", "1234", "C");
 *         Driver itay = new Driver("itay", "1111", "C");
 *         Driver dan =new Driver("Dan", "11123", "C1");
 *         pool.addDriver(nave);
 *         pool.addDriver(miki);
 *         pool.addDriver(itay);
 *         pool.addDriver(dan);
 *         Truck nadia =  new Truck("C", "nadia", 123456, 120000);
 *         Truck shahar = new Truck("C1", "shahar", 12345, 12000);
 *         Truck optimusprime = new Truck("C", "optimusprime", 2000, 1000);
 *         Truck megatron =  new Truck("C1", "megatron", 1500, 1000);
 *         pool.addTruck(nadia);
 *         pool.addTruck(shahar);
 *         pool.addTruck(optimusprime);
 *         pool.addTruck(megatron);
 *         Contact con1 = new Contact("hakanaim 16", "liron", "123456789");
 *         Site hakanaim_16 = new Site("1567", con1,Site.ShippingArea.North, 0);
 *         Contact con2 = new Contact("eretzhakulim", "dan", "123333");
 *         Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
 *         Contact con3 = new Contact("eretz", "dannyboy", "1233713");
 *         Site eretz = new Site("136", con3, Site.ShippingArea.North, 1);
 *         Contact con4 = new Contact( "ereulim", "shuli", "124333");
 *         Site ereulim = new Site("153",con4, Site.ShippingArea.Center, 1);
 *         Contact con5 = new Contact("yotveta 6", "david", "050000");
 *         Site yotveats6 = new Site("111101", con5, Site.ShippingArea.Center, 0);
 *         Contact con6 =new Contact("rager 52", "stas", "000055");
 *         Site reager = new Site("11111",con6 , Site.ShippingArea.South, 1);
 *         Contact con7 = new Contact("jaja", "yuri", "0022");
 *         Site jaja = new Site("3333", con7, Site.ShippingArea.Center, 0 );
 *         pool.addSite(hakanaim_16);
 *         pool.addSite(eretz_hakulin);
 *         pool.addSite(eretz);
 *         pool.addSite(ereulim);
 *         pool.addSite(yotveats6);
 *         pool.addSite(reager);
 *         pool.addSite(jaja);
 *         Date date1 = new Date("12","02","2022");
 *         Date date2 = new Date("15","06","1256");
 *         nave.isoccupied(date1);
 *         shahar.isoccupied(date1);
 *         miki.isoccupied(date2);
 *         nadia.isoccupied(date2);
 *         ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
 *         des1.put(eretz_hakulin, supplst1);
 *         ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> des2=new ConcurrentHashMap<>();
 *         des2.put(ereulim,supplst1);
 *         des2.put(eretz_hakulin,supplst2);
 *         OrderDoc doc1 = new OrderDoc("100", hakanaim_16, des1, date1, "Morning" );
 *         doc1.setTruckandDriver(shahar,nave);
 *         OrderDoc doc2 = new OrderDoc("101",yotveats6,des2,date2, "Evening");
 *         doc2.setTruckandDriver(nadia,miki);
 *         pool.addDoc(doc1);
 *         pool.addDoc(doc2);
 *         doc1.setWeight(123400);
 *         doc2.setWeight(12300);
 *         os.createDriverDocs(doc1.getId());
 *         os.createDriverDocs(doc2.getId());
 *         System.out.println("Build finished");**/
    }

    public void buildTranport2(){
//        trucks.addTruck("C", "nadia", 123456, 120000);
//        trucks.addTruck("C1", "shahar", 12345, 12000);
//        trucks.addTruck("C", "optimusprime", 2000, 1000);
//        trucks.addTruck("C1", "megatron", 1500, 1000);
//        supplies.addSupply("milk", 5);
//        supplies.addSupply("eggs", 2.13);
//        supplies.addSupply("matza", 20);
//        supplies.addSupply("computer", 1000);
//        supplies.addSupply("bmw", 5000);
//        supplies.addSupply("volvo", 10000);
//        sites.addSite("1567", 0, 0, "hakanaim 16", "liron", "123456789");
//        sites.addSite("156", 0, 1, "eretzhakulim", "dan", "123333");
//        sites.addSite("136", 0, 1, "eretz", "dannyboy", "1233713");
//        sites.addSite("153", 1, 1, "ereulim", "shuli", "124333");
//        sites.addSite("111101", 1, 0,"yotveta 6", "david", "050000" );
//        sites.addSite("11111", 2, 1, "rager 52", "stas", "000055");
//        sites.addSite("3333", 1, 0,"jaja", "yuri", "0022" );
//        sites.addSite("77", 2, 0, "m", "ni", "043333"); //new
//        sites.addSite("76", 2, 0, "f", "df", "ds");
//        sites.addSite("75", 2, 1, "dww", "w", "545");
//        Driver dan =new Driver("Dan", "11123", "C1");
//        Driver nave = new Driver("nave", "123", "C1");
//        Driver miki = new Driver("miki", "1234", "C");
//        Driver itay = new Driver("itay", "1111", "C");
//        drivers.addDriver(dan);
//        drivers.addDriver(nave);
//        drivers.addDriver(miki);
//        drivers.addDriver(itay);
//        Contact con1 = new Contact("hakanaim 16", "liron", "123456789");
//        Site hakanaim_16 = new Site("1567", con1,Site.ShippingArea.North, 0);
//        Truck nadia =  new Truck("C", "nadia", 123456, 120000);
//        Truck shahar = new Truck("C1", "shahar", 12345, 12000);
//        Contact con5 = new Contact("yotveta 6", "david", "050000");
//        Site yotveats6 = new Site("111101", con5, Site.ShippingArea.Center, 0);
//        Contact con2 = new Contact("eretzhakulim", "dan", "123333");
//        Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
//        Supply milk =  new Supply("milk", 5);
//        Supply eggs = new Supply("eggs", 2.13);
//        Supply matza =  new Supply("matza", 20);
//        Supply computer = new Supply("computer", 1000);
//        Date date1 = new Date("16","06","2022");
//        Date date2 = new Date("17","06","2022");
//        ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
//        ConcurrentHashMap<Supply,Integer> supplst2 = new ConcurrentHashMap<>();
//        supplst1.put(milk,15);
//        supplst1.put(eggs,6);
//        supplst1.put(matza,20);
//        supplst1.put(computer,1);
//        supplst2.put(milk,1);
//        supplst2.put(eggs,1);
//        supplst2.put(matza,1);
//        ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
//        des1.put(eretz_hakulin, supplst1);
//        Contact con4 = new Contact( "ereulim", "shuli", "124333");
//        Site ereulim = new Site("153",con4, Site.ShippingArea.Center, 1);
//        ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> des2=new ConcurrentHashMap<>();
//        des2.put(ereulim,supplst1);
//        des2.put(eretz_hakulin,supplst2);
//        OrderDoc doc1 = new OrderDoc("100", hakanaim_16, des1, date1, "MORNING" );
//        doc1.setTruckandDriver(shahar,nave);
//        OrderDoc doc2 = new OrderDoc("101",yotveats6,des2,date2, "MORNING");
//        doc2.setTruckandDriver(nadia,miki);
//        orderDocs.addDoc(doc1);
//        orderDocs.addDoc(doc2);
//        createDriverDocs(doc1.getId());
//        createDriverDocs(doc2.getId());
    }

    /**        ConcurrentHashMap<Supply,Integer> supplst1 = new ConcurrentHashMap<>();
     ConcurrentHashMap<Supply,Integer> supplst2 = new ConcurrentHashMap<>();
     Supply milk =  new Supply("milk", 5);
     Supply eggs = new Supply("eggs", 2.13);
     Supply matza =  new Supply("matza", 20);
     Supply computer = new Supply("computer", 1000);
     Supply bmw = new Supply("bmw", 5000);
     Supply volvo = new Supply("volvo", 10000);
     pool.addSupply(milk);
     pool.addSupply(eggs);
     pool.addSupply(matza);
     pool.addSupply(bmw);
     pool.addSupply(volvo);
     supplst1.put(milk,15);
     supplst1.put(eggs,6);
     supplst1.put(matza,20);
     supplst1.put(computer,1);
     supplst2.put(milk,1);
     supplst2.put(eggs,1);
     supplst2.put(matza,1);
     Driver nave = new Driver("nave", "123", "C1");
     Driver miki = new Driver("miki", "1234", "C");
     Driver itay = new Driver("itay", "1111", "C");
     Driver dan =new Driver("Dan", "11123", "C1");
     pool.addDriver(nave);
     pool.addDriver(miki);
     pool.addDriver(itay);
     pool.addDriver(dan);
     Truck nadia =  new Truck("C", "nadia", 123456, 120000);
     Truck shahar = new Truck("C1", "shahar", 12345, 12000);
     Truck optimusprime = new Truck("C", "optimusprime", 2000, 1000);
     Truck megatron =  new Truck("C1", "megatron", 1500, 1000);
     pool.addTruck(nadia);
     pool.addTruck(shahar);
     pool.addTruck(optimusprime);
     pool.addTruck(megatron);
     Contact con1 = new Contact("hakanaim 16", "liron", "123456789");
     Site hakanaim_16 = new Site("1567", con1,Site.ShippingArea.North, 0);
     Contact con2 = new Contact("eretzhakulim", "dan", "123333");
     Site eretz_hakulin = new Site("156",con2 , Site.ShippingArea.North, 1);
     Contact con3 = new Contact("eretz", "dannyboy", "1233713");
     Site eretz = new Site("136", con3, Site.ShippingArea.North, 1);
     Contact con4 = new Contact( "ereulim", "shuli", "124333");
     Site ereulim = new Site("153",con4, Site.ShippingArea.Center, 1);
     Contact con5 = new Contact("yotveta 6", "david", "050000");
     Site yotveats6 = new Site("111101", con5, Site.ShippingArea.Center, 0);
     Contact con6 =new Contact("rager 52", "stas", "000055");
     Site reager = new Site("11111",con6 , Site.ShippingArea.South, 1);
     Contact con7 = new Contact("jaja", "yuri", "0022");
     Site jaja = new Site("3333", con7, Site.ShippingArea.Center, 0 );
     pool.addSite(hakanaim_16);
     pool.addSite(eretz_hakulin);
     pool.addSite(eretz);
     pool.addSite(ereulim);
     pool.addSite(yotveats6);
     pool.addSite(reager);
     pool.addSite(jaja);
     Date date1 = new Date("12","02","2022");
     Date date2 = new Date("15","06","1256");
     nave.isoccupied(date1);
     shahar.isoccupied(date1);
     miki.isoccupied(date2);
     nadia.isoccupied(date2);
     ConcurrentHashMap<Site, ConcurrentHashMap<Supply, Integer>> des1 = new ConcurrentHashMap<>();
     des1.put(eretz_hakulin, supplst1);
     ConcurrentHashMap<Site,ConcurrentHashMap<Supply,Integer>> des2=new ConcurrentHashMap<>();
     des2.put(ereulim,supplst1);
     des2.put(eretz_hakulin,supplst2);
     OrderDoc doc1 = new OrderDoc("100", hakanaim_16, des1, date1, "Morning" );
     doc1.setTruckandDriver(shahar,nave);
     OrderDoc doc2 = new OrderDoc("101",yotveats6,des2,date2, "Evening");
     doc2.setTruckandDriver(nadia,miki);
     pool.addDoc(doc1);
     pool.addDoc(doc2);
     doc1.setWeight(123400);
     doc2.setWeight(12300);
     os.createDriverDocs(doc1.getId());
     os.createDriverDocs(doc2.getId());**/

}

package PresentationLayer.Transport_Emploees;

import DomainLayer.Employees.Facade;
import DomainLayer.Transport.OrderCtrl;

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
            print(GREEN_BOLD+"------- Hello! -------"+reset+"\nEnter your choice of system:\n\t1.Employees management system\n\t2.Transports management system\n\t3.SURPRISE\n");
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
        Facade.getInstance().loadPreMadeData();
        OrderCtrl.getInstance().build();
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
}

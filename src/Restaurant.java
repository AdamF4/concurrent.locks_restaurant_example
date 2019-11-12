//
// Name:    Adam Fahey
// ID no:   17372646
//

//import native java packages
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Restaurant {
    static public Queue<String> orderQueue = new LinkedList<>();
    static public Queue<String> mealQueue = new LinkedList<>();
    static public int chefsComplete = 0; //stores the number of
    public static void main(String[] args) {
        readOrderFromFile();                   //reads orders from file and adds to queue in Chef
        System.out.println("\n\nLogs:\n");
        Chef john = new Chef("John");   //creates a new chef thread for John
        Chef mark = new Chef ("Mark");  //creates a new chef thread for mark
        john.start();                         //starts thread john
        mark.start();                         //starts thread mark

        Server katie = new Server ("Katie");    //creates a new server thread for katie
        Server andrew = new Server("Andrew");   //creates a new server thread for andrew
        Server emily = new Server ("Emily");    //creates a new server thread for emily
        katie.start();                                //starts thread katie
        andrew.start();                               //starts thread andrew
        emily.start();                                //starts thread emily

        while(!mealQueue.isEmpty() || chefsComplete<2){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n\nSummaries:\n");
        System.out.println(john);
        System.out.println(mark);
        System.out.println(katie);
        System.out.println(andrew);
        System.out.println(emily);
        System.exit(0);
    }

    private static void readOrderFromFile(){    //reads the order list from file
        try {                                   //try catch to catch file not found exception
            Scanner s = new Scanner(new File("orderList.txt")); //use scanner for the file orderList.txt
            while (s.hasNextLine()){            //while the file has lines
                orderQueue.add(s.nextLine());    //add the line to the queue in the chef class
            }
            s.close();                          //close the scanner
        } catch (FileNotFoundException e) {     //handles the file not found exception
            e.printStackTrace();
        }
    }

}

//
// Name:    Adam Fahey
// ID no:   17372646
//

//import native java packages
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {                                //server extends thread
    static private Queue<String> mealQueue = new LinkedList<>();    //meal queue where the chefs will add meals as they are prepared
    static private final ReentrantLock lock = new ReentrantLock();  //lock used to control access to the queue
    static private boolean ordersComplete = false;                  //used by the chefs to signify all orders have been completed

    static public void addMeal(String meal){    //function adds meal to the queue
        mealQueue.add(meal);                    //adds meal to the queue
    }
    static public void setOrdersComplete(){     //function to mark the orders complete
        ordersComplete=true;                    //sets orders complete boolean to true
    }
    public Server(String name) {    //constructor for server
        super(runnable, name);      //call super constructor
    }


    static private Runnable runnable = () -> {  //using lambda expression
        String currMeal;        //string to hold the current meal
        int totalMeals = 0;     //int to count total number of meals served
        int numBurgers = 0;     //int to count number of burgers served
        int numPizzas = 0;      //int to count number of pizzas served
        int numFish = 0;        //int to count number of fish and chips served

        while(mealQueue.peek()!=null || !ordersComplete) {      //while there are meals available or if the chefs haven't finished preparing orders
            if (mealQueue.peek()!=null) {
                lock.lock();    //apply the lock

                try {
                    currMeal = mealQueue.remove();              //set the current meal as the front of queue and remove from queue
                    totalMeals++;                               //increase total counter
                    if (currMeal.contains("Burger")) {          //if burger
                        numBurgers++;                           //increase burger count
                    } else if (currMeal.contains("Pizza")) {    //if pizza
                        numPizzas++;                            //increase pizza count
                    } else if (currMeal.contains("Fish")) {     //if fish
                        numFish++;                              //increase fish count
                    }
                    System.out.println("Server " + Thread.currentThread().getName() + " is serving " + currMeal); //log the current meal being served

                } finally {
                    lock.unlock();  //remove the lock
                }
            }
            try {   //try catch for interrupts
                Thread.sleep((int) (Math.random() * 1000)); //have the thread sleep for a random time 0-1 seconds

            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        try {   //try catch for interrupts
            Thread.sleep( 1500);    //have the thread sleep for a period of time, allowing chefs to printout summary first
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        String summary = "Server " + Thread.currentThread().getName();          //create the summary string with server name
        summary += " finished preparing " + totalMeals + " orders including ";  //add total meals prepared info
        summary += numBurgers + " burgers, ";                                   //add burgers prepared info
        summary += numPizzas + " pizzas and ";                                  //add pizzas prepared info
        summary += numFish + " fish n chips";                                   //add fish and chips prepared info
        System.out.println(summary);                                            //print out the summary
    };

}

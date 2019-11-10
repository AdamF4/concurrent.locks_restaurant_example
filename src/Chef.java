//
// Name:    Adam Fahey
// ID no:   17372646
//

//import native java packages
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread {                                  //chef extends thread
    static private Queue<String> orderQueue = new LinkedList<>();   //order queue where the restaurant will add orders as they are placed
    static private final ReentrantLock lock = new ReentrantLock();  //lock used to control access to the queue

    static public void addOrder(String order){  //function adds order to queue
        orderQueue.add(order);                  //adds order to the queue
    }
    public Chef(String name) {  //constructor for chef
        super(runnable, name);  //call super constructor
    }


    static private Runnable runnable = () -> {  //using lambda expression
        String currOrder;       //string to hold the current meal
        int totalOrders = 0;    //int to count total number of meals served
        int numBurgers = 0;     //int to count number of burgers served
        int numPizzas = 0;      //int to count number of pizzas served
        int numFish = 0;        //int to count number of fish and chips served

        while(orderQueue.peek()!=null) {    //while the order queue is not empty
            lock.lock();    //apply the lock

            try {
                currOrder=orderQueue.remove();              //set the current meal as the front of queue and remove from queue
                totalOrders++;                              //increase total counter
                if(currOrder.contains("Burger")){           //if burger
                    numBurgers++;                           //increase burger count
                } else if (currOrder.contains("Pizza")){    //if pizza
                    numPizzas++;                            //increase pizza count
                } else if (currOrder.contains("Fish")){     //if fish
                    numFish++;                              //increase fish count
                }
                System.out.println("Chef " + Thread.currentThread().getName() + " is preparing " + currOrder);  //log the current order being prepared

            } finally {
                lock.unlock();  //remove the lock
            }
            try {   //try catch for interrupts
                Thread.sleep((int) (Math.random() * 1000)); //have the thread sleep for a random time 0-1 seconds
                Server.addMeal(currOrder);                  //add the current order to the meal queue in server
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        try {   //try catch for interrupts
            Thread.sleep( 1500);   //have the thread sleep for a period of time, allowing servers to printout remaining meals first
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        String summary = "Chef " + Thread.currentThread().getName();            //create the summary string with server name
        summary += " finished preparing " + totalOrders + " orders including "; //add total meals prepared info
        summary += numBurgers + " burgers, ";                                   //add burgers prepared info
        summary += numPizzas + " pizzas and ";                                  //add pizzas prepared info
        summary += numFish + " fish n chips";                                   //add fish and chips prepared info
        System.out.println(summary);                                            //print out the summary
        Server.setOrdersComplete();                                             //set the orders complete function in server
    };

}

//
// Name:    Adam Fahey
// ID no:   17372646
//

//import native java packages
import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread {                                  //chef extends thread
    static private final ReentrantLock lock = new ReentrantLock();  //lock used to control access to the queue
    private String name;
    private int totalOrders = 0;    //int to count total number of meals served
    private int numBurgers = 0;     //int to count number of burgers served
    private int numPizzas = 0;      //int to count number of pizzas served
    private int numFish = 0;        //int to count number of fish and chips served


    public Chef(String name) {  //constructor for chef
        this.name=name;
    }


    @Override
    public void run(){  //using lambda expression
        String currOrder;       //string to hold the current meal

        while(!Restaurant.orderQueue.isEmpty()) {    //while the order queue is not empty
            lock.lock();    //apply the lock
            if(!Restaurant.orderQueue.isEmpty()) {
                try {

                    currOrder = Restaurant.orderQueue.remove();              //set the current meal as the front of queue and remove from queue
                    totalOrders++;                              //increase total counter
                    if (currOrder.contains("Burger")) {           //if burger
                        numBurgers++;                           //increase burger count
                    } else if (currOrder.contains("Pizza")) {    //if pizza
                        numPizzas++;                            //increase pizza count
                    } else if (currOrder.contains("Fish")) {     //if fish
                        numFish++;                              //increase fish count
                    }
                    System.out.println("Chef " + name + " is preparing " + currOrder);  //log the current order being prepared

                } finally {
                    lock.unlock();  //remove the lock
                }
                try {   //try catch for interrupts
                    Thread.sleep((int) (Math.random() * 1000)); //have the thread sleep for a random time 0-1 seconds
                    Restaurant.mealQueue.add(currOrder);                  //add the current order to the meal queue in server
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else{
                lock.unlock();
            }
        }
        Restaurant.chefsComplete++;
    }


    public String toString(){
        String summary = "Chef " + name;            //create the summary string with server name
        summary += " finished preparing " + totalOrders + " orders including "; //add total meals prepared info
        summary += numBurgers + " burgers, ";                                   //add burgers prepared info
        summary += numPizzas + " pizzas and ";                                  //add pizzas prepared info
        summary += numFish + " fish n chips";                                   //add fish and chips prepared info
        return summary;
    }
}

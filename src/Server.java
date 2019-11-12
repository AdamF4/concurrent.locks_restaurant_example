//
// Name:    Adam Fahey
// ID no:   17372646
//

//import native java packages

import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {                                //server extends thread
    static private final ReentrantLock lock = new ReentrantLock();  //lock used to control access to the queue
    private String name;
    private int totalMeals = 0;     //int to count total number of meals served
    private int numBurgers = 0;     //int to count number of burgers served
    private int numPizzas = 0;      //int to count number of pizzas served
    private int numFish = 0;        //int to count number of fish and chips served


    public Server(String name) {    //constructor for server
        this.name =name;
    }

    public void run(){  //using lambda expression
        String currMeal;        //string to hold the current meal
        while(!Restaurant.mealQueue.isEmpty() || Restaurant.chefsComplete<2) {      //while there are meals available or if the chefs haven't finished preparing orders

            lock.lock();    //apply the lock
            if (!Restaurant.mealQueue.isEmpty()) {
                try {

                    currMeal = Restaurant.mealQueue.remove();   //set the current meal as the front of queue and remove from queue
                    totalMeals++;                               //increase total counter
                    if (currMeal.contains("Burger")) {          //if burger
                        numBurgers++;                           //increase burger count
                    } else if (currMeal.contains("Pizza")) {    //if pizza
                        numPizzas++;                            //increase pizza count
                    } else if (currMeal.contains("Fish")) {     //if fish
                        numFish++;                              //increase fish count
                    }
                    System.out.println("Server " + name + " is serving " + currMeal); //log the current meal being served

                } finally {
                    lock.unlock();  //remove the lock
                }

                try {   //try catch for interrupts
                    Thread.sleep((int) (Math.random() * 1000)); //have the thread sleep for a random time 0-1 seconds

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else{
                lock.unlock();
            }
        }

    }


    public String toString(){
        String summary = "Server " + name;                                      //create the summary string with server name
        summary += " finished preparing " + totalMeals + " orders including ";  //add total meals prepared info
        summary += numBurgers + " burgers, ";                                   //add burgers prepared info
        summary += numPizzas + " pizzas and ";                                  //add pizzas prepared info
        summary += numFish + " fish n chips";                                   //add fish and chips prepared info
        return summary;
    }
}

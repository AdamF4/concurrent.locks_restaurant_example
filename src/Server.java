//
// Name:    Adam Fahey
// ID no:   17372646
//

public class Server extends Thread {    //server extends thread
    private String name;
    private int totalMeals = 0;         //int to count total number of meals served
    private int numBurgers = 0;         //int to count number of burgers served
    private int numPizzas = 0;          //int to count number of pizzas served
    private int numFish = 0;            //int to count number of fish and chips served


    public Server(String name) {        //constructor for server
        this.name = name;
    }

    @Override
    public void run() {
        String currMeal;  //stores current meal
        while (!Restaurant.mealQueue.isEmpty() || Restaurant.staffComplete < 2) {      //while there are meals available or if all staff haven't finished
            currMeal = null;                                //set currMeal to null
            try {                                           //try catch for interrupts
                Restaurant.mealLock.lock();                 //apply the lock
                while (Restaurant.mealQueue.isEmpty()&&Restaurant.staffComplete<2) {    //while meal queue is empty
                    Restaurant.mealEmpty.await();           //wait for not empty signal
                }
                currMeal = Restaurant.mealQueue.poll();      //set the current meal as the front of queue and remove from queue

            } catch (InterruptedException e) {
                e.printStackTrace();

            } finally {
                Restaurant.mealLock.unlock();               //remove the lock
            }
            if (currMeal != null) {
                totalMeals++;                               //increase total counter
                if (currMeal.contains("Burger")) {          //if burger
                    numBurgers++;                           //increase burger count
                } else if (currMeal.contains("Pizza")) {    //if pizza
                    numPizzas++;                            //increase pizza count
                } else if (currMeal.contains("Fish")) {     //if fish
                    numFish++;                              //increase fish count
                }
                System.out.println("Server " + name + " is serving " + currMeal); //log the current meal being served

                try {                                           //try catch for interrupts
                    Thread.sleep((int) (Math.random() * 1000));  //have the thread sleep for a random time 0-1 seconds

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
        System.out.println("server Done");
        try {
            Restaurant.mealLock.lock();                     //apply the lock
            Restaurant.staffComplete++;                     //increase number of staff members completed
            Restaurant.mealEmpty.signalAll();               //ensure servers are stuck waiting for meals
            Restaurant.staffCompleteSig.signalAll();        //this staff member has no more work assert signal to show this to other thread
        }finally {                                          //finally
            Restaurant.mealLock.unlock();                   //remove the lock
        }
    }


    public String toString() {
        String summary = "Server " + name;                                      //create the summary string with server name
        summary += " finished preparing " + totalMeals + " orders including ";  //add total meals prepared info
        summary += numBurgers + " burgers, ";                                   //add burgers prepared info
        summary += numPizzas + " pizzas and ";                                  //add pizzas prepared info
        summary += numFish + " fish n chips";                                   //add fish and chips prepared info
        return summary;
    }
}

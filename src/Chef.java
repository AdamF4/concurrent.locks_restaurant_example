//
// Name:    Adam Fahey
// ID no:   17372646
//

//import native java packages


public class Chef extends Thread {  //chef extends thread
    private String name;            //stores the chefs name
    private int totalOrders = 0;    //int to count total number of meals served
    private int numBurgers = 0;     //int to count number of burgers served
    private int numPizzas = 0;      //int to count number of pizzas served
    private int numFish = 0;        //int to count number of fish and chips served


    public Chef(String name) {  //constructor for chef
        this.name = name;       //initialise the chefs name
    }


    @Override
    public void run() {
        String currOrder;                                   //string to hold the current order
        while (!Restaurant.orderQueue.isEmpty()) {          //while the order queue is not empty
            try {
                Restaurant.orderLock.lock();                //apply the lock
                currOrder = Restaurant.orderQueue.poll();   //set the current meal as the front of queue and remove from queue
            } finally {                                     //finally
                Restaurant.orderLock.unlock();              //remove the lock;
            }

            if (currOrder != null) {                        //if the current order isn't empty
                totalOrders++;                              //increase total counter
                if (currOrder.contains("Burger")) {         //if burger
                    numBurgers++;                           //increase burger count
                } else if (currOrder.contains("Pizza")) {   //if pizza
                    numPizzas++;                            //increase pizza count
                } else if (currOrder.contains("Fish")) {    //if fish
                    numFish++;                              //increase fish count
                }
                System.out.println("Chef " + name + " is preparing " + currOrder);  //log the current order being prepared

                try {   //try catch for interrupts
                    Thread.sleep((int) (Math.random() * 1000));      //have the thread sleep for a random time 0-1 seconds
                    Restaurant.mealLock.lock();                     //apply the meal lock
                    Restaurant.mealQueue.add(currOrder);            //add the current order to the meal queue in server
                    Restaurant.mealEmpty.signalAll();               //signal that a meal has been added
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {                                         //finally
                    Restaurant.mealLock.unlock();                   //remove the lock
                }
            }
        }
        System.out.println("Chef Done");
        try {
            Restaurant.mealLock.lock();                     //apply the lock
            Restaurant.staffComplete++;                     //increase number of completed staff members
            Restaurant.staffCompleteSig.signalAll();        //this staff member has no more work assert signal to show this to other thread
        }finally {                                          //finally
            Restaurant.mealLock.unlock();                   //remove the lock
        }
    }


    public String toString() {
        String summary = "Chef " + name;                                        //create the summary string with server name
        summary += " finished preparing " + totalOrders + " orders including "; //add total meals prepared info
        summary += numBurgers + " burgers, ";                                   //add burgers prepared info
        summary += numPizzas + " pizzas and ";                                  //add pizzas prepared info
        summary += numFish + " fish n chips";                                   //add fish and chips prepared info
        return summary;
    }
}

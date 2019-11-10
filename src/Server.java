import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {
    static private Queue<String> mealQueue = new LinkedList<>();
    static private final ReentrantLock lock = new ReentrantLock();
    static private boolean ordersComplete = false;

    static public void addMeal(String meal){
        mealQueue.add(meal);
    }
    static public void setOrdersComplete(){
        ordersComplete=true;
    }
    public Server(String name) {
        super(runnable, name);
    }


    static private Runnable runnable = () -> {
        String currMeal;
        int totalMeals = 0;
        int numBurgers = 0;
        int numPizzas = 0;
        int numFish = 0;

        while(mealQueue.peek()!=null || !ordersComplete) {
            lock.lock();

            try {
                if (mealQueue.peek()!=null) {
                    currMeal = mealQueue.remove();
                    totalMeals++;
                    if (currMeal.contains("Burger")) {
                        numBurgers++;
                    } else if (currMeal.contains("Pizza")) {
                        numPizzas++;
                    } else if (currMeal.contains("Fish")) {
                        numFish++;
                    }
                    System.out.println("Server " + Thread.currentThread().getName() + " is preparing " + currMeal);
                }
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep((int) (Math.random() * 1000));

            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep( 1500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        String summary = "Server " + Thread.currentThread().getName();
        summary += " finished preparing " + totalMeals + " orders including ";
        summary += numBurgers + " burgers, ";
        summary += numPizzas + " pizzas and ";
        summary += numFish + " fish n chips";
        System.out.println(summary);
    };

}

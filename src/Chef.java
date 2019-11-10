import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread {
    static private Queue<String> orderQueue = new LinkedList<>();
    static private final ReentrantLock lock = new ReentrantLock();

    static public void addOrder(String order){
        orderQueue.add(order);
    }
    public Chef(String name) {
        super(runnable, name);
    }


    static private Runnable runnable = () -> {
        String currOrder;
        int totalOrders = 0;
        int numBurgers = 0;
        int numPizzas = 0;
        int numFish = 0;
        while(orderQueue.peek()!=null) {
            lock.lock();

            try {
                currOrder=orderQueue.remove();
                totalOrders++;
                if(currOrder.contains("Burger")){
                    numBurgers++;
                } else if (currOrder.contains("Pizza")){
                    numPizzas++;
                } else if (currOrder.contains("Fish")){
                    numFish++;
                }
                System.out.println("Chef " + Thread.currentThread().getName() + " is preparing " + currOrder);
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep((int) (Math.random() * 1000));
                Server.addMeal(currOrder);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep( 1500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        String summary = "Chef " + Thread.currentThread().getName();
        summary += " finished preparing " + totalOrders + " orders including ";
        summary += numBurgers + " burgers, ";
        summary += numPizzas + " pizzas and ";
        summary += numFish + " fish n chips";
        System.out.println(summary);
        Server.setOrdersComplete();
    };

}

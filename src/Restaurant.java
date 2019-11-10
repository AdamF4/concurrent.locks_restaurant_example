
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Restaurant {

    public static void main(String[] args) {
        readOrderFromFile();
        Chef john = new Chef("John");
        Chef mark = new Chef ("Mark");
        john.start();
        mark.start();

        Server katie = new Server ("Katie");
        Server andrew = new Server("Andrew");
        Server emily = new Server ("Emily");
        katie.start();
        andrew.start();
        emily.start();

    }

    private static void readOrderFromFile(){
        try {
            Scanner s = new Scanner(new File("orderList.txt"));

            while (s.hasNextLine()){
                Chef.addOrder(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

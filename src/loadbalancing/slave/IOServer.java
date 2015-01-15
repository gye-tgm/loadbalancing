package loadbalancing.slave;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author Gary Ye
 * @version 2014-01-09
 */
public class IOServer extends SlaveServer {
    /**
     * Initializes a new slave server object with a given port number.
     *
     * @param port the port number
     */
    public IOServer(int port) {
        super(port);
    }

    @Override
    public String call(String request) throws FileNotFoundException, UnsupportedEncodingException {
        for (int i = 0; i < 100; i++) {
            PrintWriter writer = new PrintWriter("testdir/no_virus_" + i + ".txt", "UTF-8");
            writer.println("The first line");
            writer.println("The second line");
            writer.close();
        }
        return "Success";
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("IOServer with port " + arg + " started");
            new IOServer(Integer.parseInt(arg)).start();
        }
    }
}

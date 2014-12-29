package loadbalancing.slave;

import loadbalancing.IServer;

/**
 * A slave server is the end node that will actually process the request.
 *
 * @author Gary Ye
 */
public class SlaveServer extends Thread implements IServer {
    private int port;

    /**
     * Initializes a new slave server object with a given port number.
     *
     * @param port the port number
     */
    public SlaveServer(int port) {
        this.port = port;
    }

    @Override
    public String call(String request) {
        return "You requested with " + request + " I am with port " + port;
    }

    public static void main(String[] args) {
        for (String arg : args) new SlaveServer(Integer.parseInt(arg)).start();
    }
}

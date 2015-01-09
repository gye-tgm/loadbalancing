package loadbalancing.slave;

/**
 * A simple server implementation that allocated a lot of RAM in {@link #call(String)}
 *
 * @author Elias Frantar
 * @version 2015-01-09
 */
public class RAMServer extends SlaveServer {
    private static final int MEMORY = 100000000; // the amount of bytes to allocate (currently 100 MB)
    private static final int SLEEP_TIME = 5000; // the amount of time two keep the RAM allocated

    /**
     * Initializes a new RAM-server object with a given port number.
     * @param port the port number
     */
    public RAMServer(int port) {
        super(port);
    }

    @Override
    public String call(String request) {
        byte[] ram = new byte[MEMORY]; // allocate RAM

        try {
            Thread.sleep(SLEEP_TIME);
        } catch (Exception e) {}

        return "RAM utilization finished (" + port + ")";
    }
}

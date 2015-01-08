package loadbalancing.loadbalancer;

import loadbalancing.client.Client;
import loadbalancing.slave.SlaveServer;

/**
 * Created by gary on 08/01/15.
 */
public class ApplicationTest {
    private static int SLEEP_TIME = 2000;

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    @org.junit.Test
    public void testMains() throws Exception {
        SlaveServer.main(new String[]{"9001", "9002"});
        sleep();

        LoadBalancer.main(new String[]{"5000", "lcf.xml"});
        sleep();

        Client client = new Client("Gary", "http://localhost:5000/RPC2");
        System.out.println(client.call("A quick brown fox jumps over a lazy dog"));
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (Exception e) {}
    }
}

package loadbalancing.loadbalancer.slave;

import loadbalancing.slave.IOServer;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * @author Gary Ye
 * @version 2014-01-09
 */
public class IOServerTest {
    @Test
    public void testIO() throws FileNotFoundException, UnsupportedEncodingException {
        IOServer ioServer = new IOServer(234);
        System.out.println(ioServer.call("x"));
    }
}

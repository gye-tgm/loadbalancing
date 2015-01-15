package loadbalancing.loadbalancer.slave;

import loadbalancing.slave.CPUServer;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Performance test for {@link loadbalancing.loadbalancer.slave.CPUServer}
 *
 * @author Gary Ye
 * @version 2014-01-09
 */
public class CPUServerTest {

    @Test
    @Ignore
    public void testCPU() throws Exception {
        CPUServer cpuServer = new CPUServer(100);

        String query = "";
        for (int i = 0; i < 1000; i++) {
            query += "a";
        }

        assertEquals(query.substring(1), cpuServer.call(query));
    }
}

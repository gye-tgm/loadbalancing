package loadbalancing.loadbalancer;

import loadbalancing.loadbalancer.strategies.lcf.LCF;
import loadbalancing.loadbalancer.strategies.lcf.LCFServerReference;
import loadbalancing.slave.SlaveServer;

/**
 * Created by gary on 12/12/14.
 */
public class LCFTest {
    private SlaveServer slaveServer1, slaveServer2;
    private LoadBalancer loadBalancer;

    @org.junit.Before
    public void setUp() throws Exception {
        slaveServer1 = new SlaveServer(5000);
        slaveServer2 = new SlaveServer(5001);
        slaveServer2.start();
        slaveServer1.start();
        loadBalancer = new LoadBalancer(new LCF());
        loadBalancer.register(new LCFServerReference("http://localhost:5000/RPC2"));
        loadBalancer.register(new LCFServerReference("http://localhost:5001/RPC2"));
        Thread.sleep(1000);
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testCall() throws Exception {
        System.out.println(loadBalancer.call("HEY"));
        System.out.println(loadBalancer.call("HEY"));
        System.out.println(loadBalancer.call("HEY"));
    }
}

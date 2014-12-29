package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.TestLoadBalancer;
import loadbalancing.loadbalancer.strategies.wrr.WRR;
import loadbalancing.loadbalancer.strategies.wrr.WeightedServerReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit-tests for {@link loadbalancing.loadbalancer.strategies.lcf.LCF}
 *
 * @author Elias Frantar
 * @version 2014-12-28
 */
public class LCFTest {

    private LoadBalancingStrategy lcf;

    LCFServerReference s1;
    LCFServerReference s2;
    LCFServerReference s3;

    @Before
    public void setUp() throws Exception {
        s1 = new LCFServerReference("S1");
        s2 = new LCFServerReference("S2");
        s3 = new LCFServerReference("S3");

        lcf = new TestLoadBalancer(new LCF());
        lcf.register(s1);
        lcf.register(s2);
        lcf.register(s3);
    }

    @After
    public void tearDown() throws Exception {
        lcf = null;
    }

    @Test
    public void simpleCall() {
        assertEquals(s1, lcf.getNext());
    }

    @Test
    public void testInsertionOrder() {
        /* standard order */
        assertEquals(s1, lcf.getNext());
        assertEquals(s2, lcf.getNext());

        /* add additional servers */
        WeightedServerReference s4 = new WeightedServerReference("S4", 4);
        WeightedServerReference s5 = new WeightedServerReference("S5", 5);
        lcf.register(s4);
        lcf.register(s5);

        assertEquals(s3, lcf.getNext());
        assertEquals(s4, lcf.getNext());
        assertEquals(s5, lcf.getNext());
    }

}

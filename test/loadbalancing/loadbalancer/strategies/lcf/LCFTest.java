package loadbalancing.loadbalancer.strategies.lcf;

import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.TestLoadBalancer;
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

    LCFServerReference s1, s2, s3, s4, s5;

    @Before
    public void setUp() throws Exception {
        s1 = new LCFServerReference("S1");
        s2 = new LCFServerReference("S2");
        s3 = new LCFServerReference("S3");
        s4 = new LCFServerReference("S4");
        s5 = new LCFServerReference("S5");

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
        lcf.register(s4);
        lcf.register(s5);

        assertEquals(s3, lcf.getNext());
        assertEquals(s4, lcf.getNext());
        assertEquals(s5, lcf.getNext());
    }

    @Test
    public void testLeastConnectionStandard() {
        LCFServerReference[] expecteds = {s1, s2, s3, s1, s2, s3, s1};

        for (LCFServerReference expected : expecteds)
            assertEquals(expected, lcf.getNext());
    }

    @Test
    public void testLeastConnectionOnce() {
        lcf.increment(s1);
        lcf.increment(s2);

        assertEquals(s3, lcf.getNext());
    }

    @Test
    public void testLeastConnectionWIncrementDecrement() {
        lcf.increment(s1);
        lcf.increment(s3);

        assertEquals(s2, lcf.getNext());

        /* decrement a server */
        lcf.decrement(s1);

        assertEquals(s1, lcf.getNext());
    }

    @Test
    public void testLeastConnectionWInsertRemove() {
        for (int i = 0; i < 2; i++) {
            lcf.increment(s1);
            lcf.increment(s3);
        }

        assertEquals(s2, lcf.getNext());

        /* add a server */
        lcf.register(s4);

        assertEquals(s4, lcf.getNext());

        /* remove a server */
        lcf.unregister(s2);

        assertEquals(s4, lcf.getNext());
    }

}

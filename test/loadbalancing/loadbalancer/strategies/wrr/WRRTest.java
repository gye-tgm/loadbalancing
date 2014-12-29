package loadbalancing.loadbalancer.strategies.wrr;

import loadbalancing.loadbalancer.strategies.LoadBalancingStrategy;
import loadbalancing.loadbalancer.strategies.TestLoadBalancer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit-tests for {@link WRR}
 *
 * @author Elias Frantar
 * @version 2014-12-28
 */
public class WRRTest {

    private LoadBalancingStrategy wrr;

    WeightedServerReference s1, s2, s3, s4, s5;

    @Before
    public void setUp() throws Exception {
        s1 = new WeightedServerReference("S1", 1);
        s2 = new WeightedServerReference("S2", 2);
        s3 = new WeightedServerReference("S3", 3);
        s4 = new WeightedServerReference("S4", 4);
        s5 = new WeightedServerReference("S5", 5);

        wrr = new TestLoadBalancer(new WRR());
        wrr.register(s1);
        wrr.register(s2);
        wrr.register(s3);
    }

    @After
    public void tearDown() throws Exception {
        wrr = null;
    }

    @Test
    public void simpleCall() {
        assertEquals(s1, wrr.getNext());
    }

    @Test
    public void testInsertionOrder() {
        /* standard order */
        assertEquals(s1, wrr.getNext());
        assertEquals(s2, wrr.getNext());

        /* add additional servers */
        wrr.register(s4);
        wrr.register(s5);

        assertEquals(s3, wrr.getNext());
        assertEquals(s4, wrr.getNext());
        assertEquals(s5, wrr.getNext());
    }

    @Test
    public void testWeightingSimple() {
        WeightedServerReference[] expecteds = {s1, s2, s3, s2, s3, s3, s1, s2, s3, s2, s3, s3};

        for (WeightedServerReference expected : expecteds)
            assertEquals(expected, wrr.getNext());
    }

    @Test
    public void testWeightingWInsertsRemoves() {
        WeightedServerReference[] expecteds = {s1, s2, s3, s2, s3, s4, s3, s4, s4, s1};

        /* standard order */
        for (int i = 0; i < 5; i++)
            assertEquals(expecteds[i], wrr.getNext());

        /* add a new server */
        wrr.register(s4);

        for (int i = 5; i < 9; i++)
            assertEquals(expecteds[i], wrr.getNext());

        /* remove the new server again */
        wrr.unregister(s4);

        for (int i = 9; i < expecteds.length; i++)
            assertEquals(expecteds[i], wrr.getNext());
    }

    @Test
    public void testWeightingWIncrementsAndDecrements() {
        WeightedServerReference[] expecteds = {s2, s3, s2, s3, s3, s3, s1};

        /* increment a server */
        wrr.increment(s1);

        for (int i = 0; i < 4; i++)
            assertEquals(expecteds[i], wrr.getNext());

        /* decrement a server */
        wrr.decrement(s3);

        for (int i = 4; i < expecteds.length; i++)
            assertEquals(expecteds[i], wrr.getNext());
    }

    @Test
    public void testNextEmpty() {
        wrr = new WRR();
        assertEquals(null, wrr.getNext());
    }
}

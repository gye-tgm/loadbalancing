package loadbalancing.loadbalancer;

import loadbalancing.loadbalancer.strategies.lcf.LCF;
import loadbalancing.loadbalancer.strategies.lcf.LCFServerReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by gary on 08/01/15.
 */
public class LoadBalancerXMLConfigTest {
    LoadBalancerConfigXMLReader reader;

    @Before
    public void setup() {
        reader = new LoadBalancerConfigXMLReader();
    }

    @After
    public void teardown() {
        reader = null;
    }

    @Test(expected = StrategyNotFoundException.class)
    public void testStrategyNotFoundException() throws StrategyNotFoundException, ParserConfigurationException,
            SAXException, IOException {
        reader.readXML(new File("test/loadbalancing/loadbalancer/res/strategy_not_found.xml"));
    }

    @Test
    public void testReadLCFStrategy() throws StrategyNotFoundException, ParserConfigurationException,
            SAXException, IOException {
        LoadBalancer loadBalancer = reader.readXML(new File("test/loadbalancing/loadbalancer/res/lcf.xml"));
        LoadBalancer expected = new LoadBalancer(new LCF());
        expected.register(new LCFServerReference("https://localhost:9001/RPC2"));
        expected.register(new LCFServerReference("https://localhost:9002/RPC2"));
        assertEquals(expected, loadBalancer);
    }

    @Test
    public void testReadWRRStrategy() throws StrategyNotFoundException, ParserConfigurationException,
            SAXException, IOException {
        LoadBalancer loadBalancer = reader.readXML(new File("test/loadbalancing/loadbalancer/res/wrr.xml"));
        LoadBalancer expected = new LoadBalancer(new LCF());
        expected.register(new LCFServerReference("https://localhost:9001/RPC2"));
        expected.register(new LCFServerReference("https://localhost:9002/RPC2"));
        assertEquals(expected, loadBalancer);
    }
}

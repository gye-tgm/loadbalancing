package loadbalancing.loadbalancer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

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
}

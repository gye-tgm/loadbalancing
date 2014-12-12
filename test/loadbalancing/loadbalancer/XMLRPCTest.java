package loadbalancing.loadbalancer;

import loadbalancing.slave.SlaveServer;

public class XMLRPCTest {
    private ServerReference serverReference;
    private SlaveServer slaveServer;

    @org.junit.Before
    public void setUp() throws Exception {
        serverReference = new ServerReference("http://localhost:5000/RPC2");
        slaveServer = new SlaveServer();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        slaveServer = null;
        serverReference = null;
    }

    @org.junit.Test
    public void testCall() throws Exception {
        slaveServer.start();
        System.out.println(serverReference.call("Hey!"));
    }
}
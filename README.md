Load-Balancing
=============

Load Balancing with Weighted Round Robin (WRR) and Least Connection First (LCF).


Running the example
==============

The ant-buildfile is just a demonstration on how the load-balancer can be used. Command-line parameters to the
individual targets must be configured manually in the build-file!

### Add More Servers:
To add more servers simply add some more entries <arg>s to the 'run-servers' target. These are the port numbers
to start the servers on.

    <arg value="9000"/> // port to start a server on
    <arg value="9001"/>
    <arg value="9002"/>

### Different Load-Balancer Config:
To change the load-balancer configuration best edit the 'config.xml'-file in the root directory.
Consult the example files 'examples/lcf.xml' and 'examples/wrr.xml' for how to configure the balancer for different
algorithms.

    <?xml version="1.0"?>
    <loadbalancing>
        <strategyname>LCF</strategyname>        // LCF or WRR
        <slave>
            <url>http://localhost:9000</url>    // exchange port number for different client
            <weight>30</weight>                 // add only if algorithm is WRR
        </slave>
        <slave>
            <url>http://localhost:9001</url>
            <weight>50</weight>                 // add only if algorithm is WRR
        </slave>
    </loadbalancing>

### Different Client Config:
The client can also be configured in the build-file. Simply change the <arg> values in the 'run-client' target.

    <arg value="Gary"/>                                         // Client name; important for session persistence
    <arg value="http://localhost:5000/RPC2"/>                   // URL of load-balancer
    <arg value="A quick brown fox jumps over the lazy dog"/>    // request to send

### Different Server Types:
If you want to use CPU-, RAM- or IOServers instead of standard SlaveServers the easiest way is to edit the <java>
tag in the 'run-servers' target. Port configuration works exactly the same.

    // classname = "loadbalancing.slave.SlaveServer|IOServer|RAMServer|CPUServer
    <java classname="loadbalancing.slave.SlaveServer" ...

### Example Run:

    /* run the balancer with default configuration */
    > Terminal1:    ant run-servers
    > Terminal2:    ant run-loadbalancer
    > Terminal3:    ant run-client
    > Terminal4:    ant run-client
    ...

Note that requests to SlaveServers (default) need 30s for better load-balancing demonstration!

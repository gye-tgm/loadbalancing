package loadbalancing.slave;

import org.apache.log4j.Logger;

/**
 * The CPU server receives the string and searches with an O(n^4) naive algorithm
 * for the longest substring that occurs twice in the string. If there are many,
 * then the lexicographical smallest will be chosen.
 *
 * @author Gary Ye
 * @version 2014-01-09
 */
public class CPUServer extends SlaveServer {
    private static Logger log = Logger.getLogger(SlaveServer.class.getName());

    /**
     * Initializes a new slave server object with a given port number.
     *
     * @param port the port number
     */
    public CPUServer(int port) {
        super(port);
    }

    @Override
    public String call(String request) throws Exception {
        int n = request.length();
        String ret = "";
        for (int len = 0; len < n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                for (int j = i + 1; j + len - 1 < n; j++) {
                    if (request.substring(i, i + len).equals(request.substring(j, j + len))) {
                        // check whether our current result has a shorter length or is lexicographically greater
                        if (ret.length() < len || (ret.length() == len && ret.compareTo(request.substring(i, i + len)) >
                                0)) {
                            ret = request.substring(i, i + len);
                        }
                    }
                }
            }
        }
        return ret;
    }
}

package pl.adamsiedlecki.OTM.tools.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class Ping {

    private static final short TIMEOUT = 1000;
    private static final Logger log = LoggerFactory.getLogger(Ping.class);

    public boolean isReachable(String address) {
        try {
            InetAddress inetAddress = InetAddress.getByName(address);
            return inetAddress.isReachable(TIMEOUT);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}

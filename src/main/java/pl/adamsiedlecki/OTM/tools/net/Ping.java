package pl.adamsiedlecki.OTM.tools.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Service
public class Ping {

    private static final short TIMEOUT = 1000;
    private static final Logger log = LoggerFactory.getLogger(Ping.class);

    public boolean isReachable(String address, short port) {
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(address, port), TIMEOUT);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}

package pl.adamsiedlecki.otm.tools.net;

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
    private static final Socket socket = new Socket();

    public boolean isReachable(String address, short port) {
        try {
            try (socket) {
                socket.connect(new InetSocketAddress(address, port), TIMEOUT);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}

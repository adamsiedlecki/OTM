package pl.adamsiedlecki.otm.tools.net;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Service
@Slf4j
public class Ping {

    private static final short TIMEOUT = 1200;
    private static final Socket socket = new Socket();

    public boolean isReachable(String address, short port) {
        try (socket) {
            socket.connect(new InetSocketAddress(address, port), TIMEOUT);
            return true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
}

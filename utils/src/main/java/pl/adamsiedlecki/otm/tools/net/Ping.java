package pl.adamsiedlecki.otm.tools.net;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Service
@Slf4j
public class Ping {

    private static final short TIMEOUT = 1500;
    private static final Socket socket = new Socket();

    public boolean isReachable(String address, int port) {
        try (socket) {
            address = address.replace("http://", "").replace("https://", "");
            socket.connect(new InetSocketAddress(address, port), TIMEOUT);
            return true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }
}

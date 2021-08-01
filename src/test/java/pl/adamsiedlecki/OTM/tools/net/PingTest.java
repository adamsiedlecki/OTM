package pl.adamsiedlecki.OTM.tools.net;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PingTest {

    @Autowired
    private Ping ping;
    private final short PORT = 443;

    @Test
    public void localhostTest() {
        Assert.assertTrue(ping.isReachable("localhost", PORT));
    }

    @Test
    public void facebookTest() {
        Assert.assertTrue(ping.isReachable("facebook.com", PORT));
    }

    @Test
    public void notExistingAddressTest() {
        Assert.assertFalse(ping.isReachable("asdasdasjhfgashfvbuysgfyuahydgwyagdyagdygaw.pl", PORT));
    }
}
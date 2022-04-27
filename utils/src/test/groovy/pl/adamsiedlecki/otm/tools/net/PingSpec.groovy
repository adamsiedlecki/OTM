package pl.adamsiedlecki.otm.tools.net

import org.assertj.core.api.Assertions
import org.testng.annotations.Ignore
import spock.lang.Specification

class PingSpec extends Specification {

    @Ignore // Tested class is trivial, so I don't want to waste time starting a mock server here
    def "should be able to reach localhost"() {
        given:
            def ping = new Ping()

        when:
            def result = ping.isReachable("http://10.0.0.72", 80)

        then:
            Assertions.assertThat(result).isTrue();
    }

    def "should not be able to reach unreal address"() {
        given:
            def ping = new Ping();

        when:
            def result = ping.isReachable("1270.0.0.1", 80)

        then:
            Assertions.assertThat(result).isFalse();
    }
}

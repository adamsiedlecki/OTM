package pl.adamsiedlecki.otm.schedule

import pl.adamsiedlecki.otm.config.OtmConfigProperties
import pl.adamsiedlecki.otm.tools.net.Ping
import spock.lang.Specification

class PingScheduleSpec extends Specification {

    def "should ping and return true"() {
        given:
            def otmProperties = Mock(OtmConfigProperties)
            otmProperties.getGen1ApiAddress() >> "gen1apiAddress"
            otmProperties.getGen2ApiAddress() >> "gen2apiAddress"
            def ping = Mock(Ping)
            1 * ping.isReachable("gen1apiAddress" , 80) >> true
            1 * ping.isReachable("gen2apiAddress" , 80) >> true
            def pingSchedule = new PingSchedule(ping, otmProperties)

        when:
            def result = pingSchedule.areHttpDevicesAvailable()

        then:
            result
    }

    def "should ping and return false"() {
        given:
            def otmProperties = Mock(OtmConfigProperties)
            otmProperties.getGen1ApiAddress() >> "gen1apiAddress"
            otmProperties.getGen2ApiAddress() >> "gen2apiAddress"
            def ping = Mock(Ping)
            1 * ping.isReachable("gen1apiAddress" , 80) >> true
            5 * ping.isReachable("gen2apiAddress" , 80) >> false
            def pingSchedule = new PingSchedule(ping, otmProperties)

        when:
            def result = pingSchedule.areHttpDevicesAvailable()

        then:
            !result
    }
}

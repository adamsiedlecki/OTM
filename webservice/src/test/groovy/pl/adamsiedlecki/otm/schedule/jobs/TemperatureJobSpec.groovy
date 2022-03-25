package pl.adamsiedlecki.otm.schedule.jobs

import pl.adamsiedlecki.otm.data.fetcher.TemperatureDataFetcher
import pl.adamsiedlecki.otm.db.temperature.TemperatureData
import pl.adamsiedlecki.otm.external.services.facebook.FacebookManager
import spock.lang.Specification

import java.time.LocalDateTime

class TemperatureJobSpec extends Specification {

    def dataFetcher = Mock(TemperatureDataFetcher)
    def facebookManager = Mock(FacebookManager)

    TemperatureJob sut

    void setup() {
        sut = new TemperatureJob(dataFetcher, facebookManager)
    }

    def "should not post data on facebook because temperature is above zero"() {
        given:
        def td1 = TemperatureData.builder().temperatureCelsius(new BigDecimal(3.12)).build()
        def td2 = TemperatureData.builder().temperatureCelsius(new BigDecimal(40)).build()
        1 * dataFetcher.fetchAndSaveAllTemperatures() >> [td1, td2]

        when:
        sut.fetchSaveAndConditionallyPostOnFacebook()

        then:
        0 * facebookManager._
    }

    def "should not post data on facebook because there is no data"() {
        given:
        1 * dataFetcher.fetchAndSaveAllTemperatures() >> []

        when:
        sut.fetchSaveAndConditionallyPostOnFacebook()

        then:
        0 * facebookManager._
    }

    def "should not post data on facebook because there is no information about temperature"() {
        given:
        def td1 = new TemperatureData()

        1 * dataFetcher.fetchAndSaveAllTemperatures() >> [td1]

        when:
        sut.fetchSaveAndConditionallyPostOnFacebook()

        then:
        0 * facebookManager._
    }

    def "should post data on facebook because temperature is below zero"() {
        given:
        def td1 = TemperatureData.builder()
                .date(LocalDateTime.of(2022, 3, 25, 23, 25))
                .transmitterName("station no 1")
                .temperatureCelsius(new BigDecimal(-3))
                .build()
        def td2 = TemperatureData.builder()
                .date(LocalDateTime.of(2022, 3, 25, 23, 25))
                .transmitterName("station no 2")
                .temperatureCelsius(new BigDecimal(40))
                .build()
        1 * dataFetcher.fetchAndSaveAllTemperatures() >> [td1, td2]

        when:
        sut.fetchSaveAndConditionallyPostOnFacebook()

        then:
        1 * facebookManager.postMessage('❄ Odnotowano temperaturę < 0  \n  [ 25.3.2022 23:25 ]  \n  station no 1 : -3 °C  \n  station no 2 : 40 °C ')
        0 * facebookManager._
    }

    def "should post data on facebook in form of post and the next one should be a comment"() {
        given:
        def td1 = TemperatureData.builder()
                .date(LocalDateTime.of(2022, 3, 25, 23, 25))
                .transmitterName("station no 1")
                .temperatureCelsius(new BigDecimal(-3))
                .build()
        def td2 = TemperatureData.builder()
                .date(LocalDateTime.of(2022, 3, 25, 23, 25))
                .transmitterName("station no 2")
                .temperatureCelsius(new BigDecimal(40))
                .build()
        1 * dataFetcher.fetchAndSaveAllTemperatures() >> [td1, td2]
        1 * dataFetcher.fetchAndSaveAllTemperatures() >> [td1]
        1 * facebookManager.postMessage('❄ Odnotowano temperaturę < 0  \n  [ 25.3.2022 23:25 ]  \n  station no 1 : -3 °C  \n  station no 2 : 40 °C ') >> 'last post id'

        when:
        sut.fetchSaveAndConditionallyPostOnFacebook()
        and:
        sut.fetchSaveAndConditionallyPostOnFacebook()

        then:
        1 * facebookManager.postComment('last post id', '❄ Odnotowano temperaturę < 0  \n  [ 25.3.2022 23:25 ]  \n  station no 1 : -3 °C ')
    }
}

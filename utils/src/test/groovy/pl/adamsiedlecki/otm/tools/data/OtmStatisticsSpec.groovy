package pl.adamsiedlecki.otm.tools.data

import pl.adamsiedlecki.otm.db.temperature.TemperatureData
import spock.lang.Specification

class OtmStatisticsSpec extends Specification {

    def "should be able get lowest temperature"() {
        given:
            def temperatureData1 = TemperatureData.builder().temperatureCelsius(new BigDecimal("2.88")).build()
            def temperatureData2 = TemperatureData.builder().temperatureCelsius(BigDecimal.TEN).build()
            def temperatureData3 = TemperatureData.builder()
                    .temperatureCelsius(BigDecimal.ZERO)
                    .transmitterName("stationX").build()
            def list = [temperatureData1, temperatureData2, temperatureData3]

        when:
            def statistics = new OtmStatistics(list)

        then:
            statistics.lowestTemperature.isPresent() == true
            statistics.lowestTemperature.get().transmitterName == "stationX"
    }

    def "should be able to get highest temperature"() {
        given:
            def temperatureData1 = TemperatureData.builder().temperatureCelsius(new BigDecimal("2.88")).build()
            def temperatureData2 = TemperatureData.builder()
                    .temperatureCelsius(BigDecimal.TEN)
                    .transmitterName("stationY")
                    .build()
            def temperatureData3 = TemperatureData.builder().temperatureCelsius(BigDecimal.ZERO).build()
            def list = [temperatureData1, temperatureData2, temperatureData3]

        when:
            def statistics = new OtmStatistics(list)

        then:
            statistics.highestTemperature.isPresent() == true
            statistics.highestTemperature.get().transmitterName == "stationY"
    }
}

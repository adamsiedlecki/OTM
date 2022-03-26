package pl.adamsiedlecki.otm.tools.data

import pl.adamsiedlecki.otm.db.temperature.TemperatureData
import spock.lang.Specification

class ChartDataUtilsSpec extends Specification {

    def "should return below zero"() {
        when:
        def td1 = TemperatureData.builder().temperatureCelsius(new BigDecimal(temperature1)).build()
        def td2 = TemperatureData.builder().temperatureCelsius(new BigDecimal(temperature2)).build()
        def td3 = TemperatureData.builder().temperatureCelsius(new BigDecimal(temperature3)).build()
        def temperatureList = [td1, td2, td3]

        then:
        boolean result = ChartDataUtils.isAnyBelowZero(temperatureList)

        then:
        result == true

        where:
        temperature1 | temperature2 | temperature3
        1            | 0            | -4
        -1           | 3            | 4
        1            | -3           | 4
    }

    def "should not return below zero"() {
        when:
        def td1 = TemperatureData.builder().temperatureCelsius(new BigDecimal(temperature1)).build()
        def td2 = TemperatureData.builder().temperatureCelsius(new BigDecimal(temperature2)).build()
        def td3 = TemperatureData.builder().temperatureCelsius(new BigDecimal(temperature3)).build()
        def temperatureList = [td1, td2, td3]

        then:
        boolean result = ChartDataUtils.isAnyBelowZero(temperatureList)

        then:
        result == false

        where:
        temperature1 | temperature2 | temperature3
        1            | 3            | 0
        0            | 3            | 4
    }
}

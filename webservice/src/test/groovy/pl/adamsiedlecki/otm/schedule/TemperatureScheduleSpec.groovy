package pl.adamsiedlecki.otm.schedule


import pl.adamsiedlecki.otm.schedule.jobs.TemperatureJob
import spock.lang.Specification

class TemperatureScheduleSpec extends Specification {

    def temperatureJob = Mock(TemperatureJob)

    TemperatureSchedule sut

    void setup() {
        sut = new TemperatureSchedule(temperatureJob)
    }

    def "should run job every hour"() {
        when:
            sut.checkTemperaturesHourly()

        then:
            1 * temperatureJob.fetchSaveAndConditionallyPostOnFacebook()
    }

    def "should run job every half hour"() {
        when:
            sut.checkTemperaturesHalfHourAtNight()

        then:
            1 * temperatureJob.fetchSaveAndConditionallyPostOnFacebook()
    }
}

package pl.adamsiedlecki.otm.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.db.statistics.ExistingStatistics;
import pl.adamsiedlecki.otm.db.statistics.StatisticsService;

@Aspect
@Component
public class EspCommunicationAspects {

    private final StatisticsService statService;
    private final Logger log = LoggerFactory.getLogger(EspCommunicationAspects.class);

    @Autowired
    public EspCommunicationAspects(StatisticsService statService) {
        this.statService = statService;
    }

    @After("execution(* pl.adamsiedlecki.otm.EspApiTool.getHtml(..))")
    private void requestsToEsp() {
        statService.increment(ExistingStatistics.REQUESTS_TO_ESP_COUNT);
        log.info("esp request aspect worked");
    }

    @After("execution(* pl.adamsiedlecki.otm.EspApiTool.espNoResponseStrategy(..))")
    private void espNoResponse() {
        statService.increment(ExistingStatistics.ESP_NO_RESPONSE_COUNT);
        log.info("esp no response aspect worked");
    }

    @After("execution(* pl.adamsiedlecki.otm.EspApiTool.sendRestartCommand(..))")
    private void espRestart() {
        statService.increment(ExistingStatistics.ESP_RESTART_COUNT);
        log.info("esp restart aspect worked");
    }
}

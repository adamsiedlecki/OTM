package pl.adamsiedlecki.OTM.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.statistics.ExistingStatistics;
import pl.adamsiedlecki.OTM.db.statistics.StatisticsService;

@Aspect
@Component
public class FacebookAspects {

    private final StatisticsService statService;
    private final Logger log = LoggerFactory.getLogger(FacebookAspects.class);

    @Autowired
    public FacebookAspects(StatisticsService statService) {
        this.statService = statService;
    }

    @After("execution(* pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager.postChart(..))")
    private void postChart() {
        statService.increment(ExistingStatistics.FACEBOOK_POST_CHART);
        log.info("facebook post chart aspect worked");
    }

    @After("execution(* pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager.postMessage(..))")
    private void postMessage() {
        statService.increment(ExistingStatistics.FACEBOOK_POST_MESSAGE);
        log.info("facebook post chart aspect worked");
    }

    @After("execution(* pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager.postComment(..))")
    private void postComment() {
        statService.increment(ExistingStatistics.FACEBOOK_POST_COMMENT);
        log.info("facebook post chart aspect worked");
    }
}

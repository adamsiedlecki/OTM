package pl.adamsiedlecki.otm.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.db.statistics.ExistingStatistics;
import pl.adamsiedlecki.otm.db.statistics.StatisticsService;

@Aspect
@Component
@Slf4j
public class FacebookAspects {

    private final StatisticsService statService;

    @Autowired
    public FacebookAspects(final StatisticsService statService) {
        this.statService = statService;
    }

    @After("execution(* pl.adamsiedlecki.otm.external.services.facebook.FacebookManager.postChart(..))")
    private void postChart() {
        statService.increment(ExistingStatistics.FACEBOOK_POST_CHART);
        log.info("Facebook post chart aspect worked");
    }

    @After("execution(* pl.adamsiedlecki.otm.external.services.facebook.FacebookManager.postMessage(..))")
    private void postMessage() {
        statService.increment(ExistingStatistics.FACEBOOK_POST_MESSAGE);
        log.info("Facebook post message aspect worked");
    }

    @After("execution(* pl.adamsiedlecki.otm.external.services.facebook.FacebookManager.postComment(..))")
    private void postComment() {
        statService.increment(ExistingStatistics.FACEBOOK_POST_COMMENT);
        log.info("Facebook post comment aspect worked");
    }
}

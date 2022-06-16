package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class OtherApiProperties {

    @Value("${open.weather.api.key:defaultOpenWeatherApiKey}")
    private String openWeatherApiKey;

    @Value("${sms.api.key:defaultSmsApiKey}")
    private String smsApiKey;
    @Value("${sms.api.password:defaultSmsApiPassword}")
    private String smsApiPassword;
    @Value("${sms.api.enabled:false}")
    private boolean isSmsApiEnabled;
    @Value("${sms.api.max.daily.amount:10}")
    private int smsApiMaxDailySmsAmount;

}

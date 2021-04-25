package pl.adamsiedlecki.OTM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.adamsiedlecki.OTM.dataFetcher.DataFetcher;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;

import java.util.List;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class OtmApplication {

	private final static Logger log = LoggerFactory.getLogger(OtmApplication.class);

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Europe/Warsaw");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
		ConfigurableApplicationContext run = SpringApplication.run(OtmApplication.class, args);
		log.info("APPLICATION LAUNCHED");

		DataFetcher dataFetcher = run.getBean(DataFetcher.class);
		List<TemperatureData> list = dataFetcher.fetch();
		for (TemperatureData data : list) {
			log.info(data.toString());
		}

	}

}

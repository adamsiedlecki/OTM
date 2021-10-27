package pl.adamsiedlecki.OTM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.adamsiedlecki.OTM.config.OtmConfigProperties;
import pl.adamsiedlecki.OTM.dataFetcher.DataFetcher;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.user.User;
import pl.adamsiedlecki.OTM.db.user.UserDs;
import pl.adamsiedlecki.OTM.db.user.userRole.UserAuthority;

import java.util.List;
import java.util.TimeZone;

@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties(OtmConfigProperties.class)
@SpringBootApplication
public class OtmApplication {

	private final static Logger log = LoggerFactory.getLogger(OtmApplication.class);

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Europe/Warsaw");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
		ConfigurableApplicationContext ctx = SpringApplication.run(OtmApplication.class, args);
		log.info("APPLICATION LAUNCHED");

		DataFetcher dataFetcher = ctx.getBean(DataFetcher.class);
		List<TemperatureData> tdList = dataFetcher.fetch();

//		MapsImageCreator mapsCreator = ctx.getBean(MapsImageCreator.class);
//		mapsCreator.get(tdList);

		// create admin if do not exist
		UserDs userDs = ctx.getBean(UserDs.class);
		if (userDs.getUserByUsername("admin").isEmpty()) {
			User admin = User.UserBuilder
					.anUser()
					.withUsername("admin")
					.withPassword("admin")
					.withRoles(List.of(new UserAuthority("ADMIN")))
					.build();
			userDs.saveUser(admin);
		}
	}
}

package pl.adamsiedlecki.otm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.adamsiedlecki.otm.db.user.User;
import pl.adamsiedlecki.otm.db.user.UserDs;
import pl.adamsiedlecki.otm.db.user.userRole.UserAuthority;

import java.util.List;
import java.util.TimeZone;

@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@SpringBootApplication
public class OtmApplication {

	private static final Logger log = LoggerFactory.getLogger(OtmApplication.class);

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Europe/Warsaw");
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
		ConfigurableApplicationContext ctx = SpringApplication.run(OtmApplication.class, args);
		log.info("APPLICATION LAUNCHED");

		createAdminUserIfNotExists(ctx);
	}

	private static void createAdminUserIfNotExists(ApplicationContext ctx) {
		String admin = "admin";
		UserDs userDs = ctx.getBean(UserDs.class);
		if (userDs.getUserByUsername(admin).isEmpty()) {
			User user = User.UserBuilder
					.anUser()
					.withUsername(admin)
					.withPassword(admin)
					.withRoles(List.of(new UserAuthority("ADMIN")))
					.build();
			userDs.saveUser(user);
		}
	}
}

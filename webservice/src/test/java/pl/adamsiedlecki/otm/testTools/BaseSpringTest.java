package pl.adamsiedlecki.otm.testTools;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testcontainers.containers.MySQLContainer;
import pl.adamsiedlecki.otm.OtmApplication;

import javax.sql.DataSource;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration(classes = {OtmApplication.class, BaseSpringTest.class})
public class BaseSpringTest extends AbstractTestNGSpringContextTests {

    private static final String MYSQL_DOCKER_IMAGE = "mysql:8.0.27";

    @Bean
    public MySQLContainer getMySQLContainer() {
        MySQLContainer mySQLContainer = new MySQLContainer(MYSQL_DOCKER_IMAGE);
        mySQLContainer.withDatabaseName("otmdb")
                .withUsername("testUsername")
                .withPassword("testPassword")
                .start();
        return mySQLContainer;
    }

    @Bean
    public DataSource getDataSource(MySQLContainer mySQLContainer) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url(mySQLContainer.getJdbcUrl());
        dataSourceBuilder.username(mySQLContainer.getUsername());
        dataSourceBuilder.password(mySQLContainer.getPassword());
        return dataSourceBuilder.build();
    }

}

package founders.easyRouteAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "founders.easyRouteAssistant")
public class EasyRouteAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyRouteAssistantApplication.class, args);
    }

}

package founders.EasyRouteAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

public class EasyRouteAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyRouteAssistantApplication.class, args);
	}

}

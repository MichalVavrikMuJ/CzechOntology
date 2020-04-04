package michal.vavrik.diplomathesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class })
@SpringBootApplication
@ComponentScan(value = "michal.vavrik.diplomathesis")
public class WikiCorpusNeuralNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(WikiCorpusNeuralNetworkApplication.class, args);
	}

}

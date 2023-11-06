package efub.toy2.papers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PapersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PapersApplication.class, args);
	}

}

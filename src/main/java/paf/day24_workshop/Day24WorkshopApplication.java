package paf.day24_workshop;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.scheduling.annotation.EnableAsync;

import paf.day24_workshop.repository.RedisRepository;

@SpringBootApplication
@EnableAsync
public class Day24WorkshopApplication implements CommandLineRunner {

	@Autowired
    private RedisRepository redisRepo;

	@Autowired
	private MessagePoller messagePoller;

    private static final Logger logger = Logger.getLogger(Day24WorkshopApplication.class.getName());

    @Value("${spring.application.name}")
    private String appName;

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Day24WorkshopApplication.class);
        application.addListeners(event -> {
            if (event instanceof ApplicationEnvironmentPreparedEvent) {
                String[] appArgs = ((ApplicationEnvironmentPreparedEvent) event).getArgs();
                if (appArgs.length > 0) {
                    System.setProperty("custom.application.name", appArgs[0]);
                }
            }
        });
        application.run(args);
	}

	@Override
    public void run(String... args) throws Exception {
        redisRepo.insertName(appName);
        logger.info("[CLR] Inserted app name: " + appName);
		messagePoller.start();
    }

}

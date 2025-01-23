package paf.day24_workshop;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessagePoller {
    
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.application.name}")
    private String appName;

    @Async
    public void start() {
        Runnable run = () -> {
            while(true) {
                String message = redisTemplate.opsForList().rightPop(appName, Duration.ofSeconds(5));
                if(message != null){
                    System.out.println("[Poller] Message received: " + message);
                    redisTemplate.convertAndSend(appName, message);
                }
            }
        };
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        executor.submit(run);
    }
}

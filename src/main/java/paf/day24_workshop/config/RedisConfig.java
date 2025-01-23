package paf.day24_workshop.config;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import paf.day24_workshop.OrderSubscribe;

@Configuration
public class RedisConfig {
    
    private static final Logger logger = Logger.getLogger(RedisConfig.class.getName());

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private OrderSubscribe orderSubscribe;

    private JedisConnectionFactory createFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(redisDatabase);

        if(!redisUsername.trim().equals("")) {
            logger.info("Setting redis username and password");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        return jedisFac;
    }

    @Bean("listener-container")
    public RedisMessageListenerContainer createListenerContainer() {
        JedisConnectionFactory jedisFac = createFactory();
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(jedisFac);
        listenerContainer.addMessageListener(orderSubscribe, ChannelTopic.of(appName));
        logger.info("[RedisConfig] App Name: " + appName);

        return listenerContainer;
    }

    @Bean("redis-0")
    public RedisTemplate<String, String> createRedisTemplate() {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(createFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }
    
}


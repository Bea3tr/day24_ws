package paf.day24_workshop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class RedisRepository {

    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    @SuppressWarnings("null")
    public void insertName(String applicationName) {
        if(template.hasKey("registrations")) {
            if(!template.opsForList().range("registrations", 0, -1).contains(applicationName))
                template.opsForList().leftPush("registrations", applicationName);
        } else {
            template.opsForList().leftPush("registrations", applicationName);
        }
    }

    public List<String> getRegistrations() {
        return template.opsForList().range("registrations", 0, -1);
    }

    public void insertJson(String name, JsonObject obj) {
        template.opsForList().leftPush(name, obj.toString());
    }
    
}

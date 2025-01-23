package paf.day24_workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import paf.day24_workshop.repository.RedisRepository;

@Service
public class RedisService {

    @Autowired
    private RedisRepository redisRepo;
    
    public List<String> getRegistrations() {
        return redisRepo.getRegistrations();
    }

    public void insertJson(String name, JsonObject obj) {
        redisRepo.insertJson(name, obj);
    }
    
}

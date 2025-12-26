package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.entity.UssdSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SessionService {

    @Autowired
    private RedisTemplate<String,Object> redis;
    private final long ttl = 300;

    public UssdSession getOrCreate(String sessionId, String msisdn) {
        UssdSession s = (UssdSession) redis.opsForValue().get(sessionId);
        return s != null ? s :
                new UssdSession(sessionId, msisdn, "WELCOME", false, null);
    }

    public void save(UssdSession s) {
        redis.opsForValue().set(s.getSessionId(), s, Duration.ofSeconds(ttl));
    }

    public void delete(String sessionId) {
        redis.delete(sessionId);
    }
}


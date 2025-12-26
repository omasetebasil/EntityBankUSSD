package com.entitybank.digital.ussd.service.framework;

import com.entitybank.digital.ussd.entity.UssdSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;

@Service
public class SessionService {

    @Autowired
    private RedisTemplate<String, Object> redis;

    private final long ttl = 300;

    public UssdSession getOrCreate(String sessionId, String msisdn) {

        UssdSession s = (UssdSession) redis.opsForValue().get(sessionId);

        if (s == null) {
            s = new UssdSession();
            s.setSessionId(sessionId);
            s.setMsisdn(msisdn);
            s.setCurrentMenu("WELCOME");
            s.setAuthenticated(false);
            s.setAttributes(new HashMap<>());
            return s;
        }

        // 🔥 CRITICAL FIX
        if (s.getAttributes() == null) {
            s.setAttributes(new HashMap<>());
        }

        return s;
    }

    public void save(UssdSession s) {

        // 🔒 Safety
        if (s.getAttributes() == null) {
            s.setAttributes(new HashMap<>());
        }

        redis.opsForValue().set(
                s.getSessionId(),
                s,
                Duration.ofSeconds(ttl)
        );
    }

    public void delete(String sessionId) {
        redis.delete(sessionId);
    }
}



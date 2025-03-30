package com.chatApplication.Authentication.repo;

import com.chatApplication.Authentication.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class userRedisRepository {

    @Autowired
    private RedisTemplate<String, Object>redisTemplate;

    private static final long CACHE_EXPIRATION = 30;

    public void saveUserToRedis(Users user) {
        redisTemplate.opsForValue().set("user:" + user.getUsername(), user, CACHE_EXPIRATION, TimeUnit.MINUTES);
    }

    public Users getUserFromRedis(String username) {
        return (Users) redisTemplate.opsForValue().get("user:" + username);
    }

    public void deleteUserFromRedis(String username) {
        redisTemplate.delete("user:" + username);
    }

    public void saveSessionToRedis(String username, String sessionId) {
        redisTemplate.opsForValue().set("session:" + username, sessionId, CACHE_EXPIRATION, TimeUnit.MINUTES);
    }

    public String getSessionFromRedis(String username) {
        return (String) redisTemplate.opsForValue().get("session:" + username);
    }

    public void deleteSessionFromRedis(String username) {
        redisTemplate.delete("session:" + username);
    }

    public void blacklistToken(String token, long expirationTime) {
        redisTemplate.opsForValue().set("blacklist:" + token, "BLACKLISTED", expirationTime, TimeUnit.MILLISECONDS);
    }


    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}

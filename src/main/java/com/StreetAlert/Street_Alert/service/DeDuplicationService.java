package com.StreetAlert.Street_Alert.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
@Slf4j
public class DeDuplicationService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration DEDUP_TTL = Duration.ofHours(24);
    private static final String KEY_PREFIX = "news:seen:";

    public DeDuplicationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDuplicate(String url) {
        String key = KEY_PREFIX + DigestUtils.md5DigestAsHex(url.getBytes(StandardCharsets.UTF_8));
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (RedisConnectionFailureException ex) {
            log.warn("Redis unavailable; skipping de-dup check.");
            return false;
        }
    }

    public void markAsSeen(String url) {
        String key = KEY_PREFIX + DigestUtils.md5DigestAsHex(url.getBytes(StandardCharsets.UTF_8));
        // because urls can be 100s of characters long, md5 converts it into 30 char string to store in redis
        try {
            redisTemplate.opsForValue().set(key, "1", DEDUP_TTL);
        } catch (RedisConnectionFailureException ex) {
            log.warn("Redis unavailable; skipping de-dup mark.");
        }
    }
}
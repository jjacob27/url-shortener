package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unused")
public class CacheServiceImpl implements CacheService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void storeInCache(String shortUrl, String longUrl) {
        redisTemplate.opsForValue().set(shortUrl,longUrl);
    }

    @Override
    public String getLongUrlFromCache(String shortUrl) {
        return redisTemplate.opsForValue().get(shortUrl);
    }
}

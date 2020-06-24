package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void storeInCache(String shortUrl, String longUrl) {
        redisTemplate.opsForHash().put("URL",shortUrl,longUrl);
    }

    @Override
    public String getLongUrlFromCache(String shortUrl) {
        Object o = redisTemplate.opsForHash().get("URL",shortUrl);
        if(o==null)
            return null;
        else
            return (String)o;
    }
}

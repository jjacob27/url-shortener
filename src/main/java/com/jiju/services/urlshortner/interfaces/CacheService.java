package com.jiju.services.urlshortner.interfaces;

public interface CacheService {
    void storeInCache(String shortUrl, String longUrl);
    String getLongUrlFromCache(String shortUrl);
}

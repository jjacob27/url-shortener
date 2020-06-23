package com.jiju.services.urlshortner.interfaces;

public interface UrlPersistenceService {
    void storeUrls(String shortUrl, String longUrl);
    String getLongUrl(String shortUrl);
    String getShortUrl(String longUrl);
}

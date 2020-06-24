package com.jiju.services.urlshortner.interfaces;

public interface UrlShortenerService {
    String getShortUrl(String longUrl);
    String getLongUrl(String shortUrl);
}

package com.jiju.services.urlshortner.interfaces;

public interface UrlShortnerService {
    String getShortUrl(String longUrl);
    String getLongUrl(String shortUrl);
}
